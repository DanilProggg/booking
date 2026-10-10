package com.kridan.booking.service.orders;

import com.kridan.booking.config.HoldProperties;
import com.kridan.booking.entity.Order;
import com.kridan.booking.repository.OrderRepository;
import com.kridan.booking.repository.TicketRepository;
import com.kridan.booking.service.payment.PaymentIntent;
import com.kridan.booking.service.payment.PaymentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderTransactions {
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final HoldProperties holdProperties;


    @Transactional
    public CheckoutContext createOrder(String orderId, String buyerEmail) {
        UUID orderUUID = UUID.fromString(orderId);
        Order order = orderRepository.findById(orderUUID)
                .orElseThrow(() -> new OrderNotFoundException("Заказ не найден"));

        //If status CREATED - skip checks
        if (!order.getStatus().equals("CREATED")) {
            if (!order.getStatus().equals("HELD")) {
                throw new OrderStateException("Заказ уже оформляется или завершён");
            }
            if (order.isExpired(new Date())) {
                throw new HoldExpiredException("Время брони истекло");
            }

            int total = ticketRepository.totalCostByOrderId(orderUUID);
            if (total <= 0) {
                throw new OrderStateException("В заказе нет мест");
            }

            // Move order to CREATED
            order.prepareForPayment(buyerEmail, total);
        }

        BigDecimal amount = BigDecimal.valueOf(order.getTotalCost());
        String description = "Билеты: " + order.getEvent().getName();

        return new CheckoutContext(
                order.getId(),
                amount,
                order.getBuyerEmail(),
                order.getIdempotencyKey(),
                order.getEvent().getName(),
                List.of(new PaymentRequest.ReceiptItem(description, 1, amount)));
    }


    @Transactional
    public CheckoutResponse attachPayment(String orderId, PaymentIntent intent) {
        UUID orderUUID = UUID.fromString(orderId);
        Order order = orderRepository.findById(orderUUID)
                .orElseThrow(() -> new OrderNotFoundException("Заказ не найден"));

        if (order.getStatus().equals("PENDING")) {
            return new CheckoutResponse(orderUUID, intent.value(), order.getExpiresAt());
        }
        if (!order.getStatus().equals("CREATED")) {
            throw new OrderStateException("Заказ в состоянии " + order.getStatus());
        }

        if (ticketRepository.moveToPending(orderUUID) == 0) {
            throw new HoldExpiredException("Места освободились");
        }

        Date expiresAt = Date.from(Instant.now().plus(holdProperties.paymentTtl()));
        order.startPayment(intent.externalPaymentId(), expiresAt);

        return new CheckoutResponse(orderUUID, intent.value(), expiresAt);
    }


}
