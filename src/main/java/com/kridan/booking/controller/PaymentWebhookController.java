package com.kridan.booking.controller;

import com.kridan.booking.service.payment.PaymentCallbackService;
import com.kridan.booking.service.payment.PaymentEvent;
import com.kridan.booking.service.payment.PaymentProvider;
import com.kridan.booking.service.payment.exceptions.PaymentNotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Приёмник уведомлений от эквайринга. Работает через PaymentProvider,
 * поэтому не меняется при замене заглушки на реальный провайдер.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentWebhookController {

    private final PaymentProvider paymentProvider;
    private final PaymentCallbackService callbackService;

    @PostMapping("/notifications")
    public ResponseEntity<Void> notification(@RequestBody String rawBody) {
        PaymentEvent event = paymentProvider.parseNotification(rawBody);
        if (event != null) {
            callbackService.handle(event);
        }
        // 200 и для чужих уведомлений — иначе эквайринг будет слать их повторно
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(PaymentNotificationException.class)
    public ResponseEntity<Void> malformed(PaymentNotificationException e) {
        log.warn("Некорректное уведомление о платеже: {}", e.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
