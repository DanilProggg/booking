package com.kridan.booking.service.payment;

import com.kridan.booking.entity.Order;
import com.kridan.booking.repository.OrderRepository;
import com.kridan.booking.repository.TicketRepository;
import com.kridan.booking.service.orders.OrderNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Применяет к заказу подтверждённое событие платежа.
 * Один и тот же путь и для webhook'а эквайринга, и для dev-ручек заглушки.
 * Идемпотентен: эквайринг может прислать одно уведомление несколько раз.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCallbackService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void handle(PaymentEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new OrderNotFoundException("Заказ не найден: " + event.orderId()));

        if (!event.externalPaymentId().equals(order.getExternalPaymentId())) {
            log.warn("Платёж {} не привязан к заказу {} (ожидался {})",
                    event.externalPaymentId(), order.getId(), order.getExternalPaymentId());
            return;
        }

        // Заказ уже в финальном состоянии — повторное уведомление
        if (!"PENDING".equals(order.getStatus())) {
            if (event.state() == PaymentState.SUCCEEDED && "CANCELED".equals(order.getStatus())) {
                // Деньги пришли после отмены заказа: места уже отданы, нужен возврат
                order.markNeedsRefund();
            }
            return;
        }

        switch (event.state()) {
            case SUCCEEDED -> {
                if (ticketRepository.markSold(order.getId()) == 0) {
                    order.markNeedsRefund();
                } else {
                    order.markPaid();
                }
            }
            case CANCELED -> {
                ticketRepository.releasePending(order.getId());
                order.markCanceled();
            }
            case PENDING -> { }
        }
    }
}
