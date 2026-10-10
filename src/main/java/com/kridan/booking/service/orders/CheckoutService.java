package com.kridan.booking.service.orders;

import com.kridan.booking.entity.Order;
import com.kridan.booking.service.payment.PaymentIntent;
import com.kridan.booking.service.payment.PaymentProvider;
import com.kridan.booking.service.payment.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderTransactions orderTransactions;
    private final PaymentProvider paymentProvider;

    @Value("${booking.base-url}")
    private String baseUrl;

    public CheckoutResponse checkout(String orderId, String buyerEmail) {
        com.kridan.booking.service.orders.CheckoutContext checkoutContext = orderTransactions.createOrder(orderId, buyerEmail);

        PaymentIntent intent = paymentProvider.create(
                new PaymentRequest(
                    checkoutContext.orderId(),
                    checkoutContext.amount(),
                    checkoutContext.customerEmail(),
                    checkoutContext.lines(),
                    baseUrl + "/orders/" + checkoutContext.orderId().toString(),
                    checkoutContext.idempotencyKey()
                )
        );

        return orderTransactions.attachPayment(orderId, intent);
    }
}
