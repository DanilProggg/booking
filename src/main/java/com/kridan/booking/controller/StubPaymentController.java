package com.kridan.booking.controller;

import com.kridan.booking.service.payment.PaymentCallbackService;
import com.kridan.booking.service.payment.PaymentEvent;
import com.kridan.booking.service.payment.PaymentState;
import com.kridan.booking.service.payment.dev.StubPaymentProvider;
import com.kridan.booking.service.payment.exceptions.PaymentProviderUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Имитация действий пользователя на странице эквайринга (в заглушке её нет).
 * Ручки собирают то же тело уведомления, что и webhook, и прогоняют его
 * через parseNotification — чтобы в dev работал тот же путь, что и в проде.
 */
@RestController
@Profile("dev")
@RequestMapping("/dev/payments")
@RequiredArgsConstructor
public class StubPaymentController {

    private final StubPaymentProvider provider;
    private final PaymentCallbackService callbackService;

    @GetMapping("/{paymentId}")
    public PaymentState get(@PathVariable String paymentId) {
        return provider.fetchState(paymentId);
    }

    @PostMapping("/{paymentId}/confirm")
    public ResponseEntity<PaymentEvent> confirm(@PathVariable String paymentId) {
        return notify(paymentId, PaymentState.SUCCEEDED);
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentEvent> cancel(@PathVariable String paymentId) {
        return notify(paymentId, PaymentState.CANCELED);
    }

    private ResponseEntity<PaymentEvent> notify(String paymentId, PaymentState state) {
        PaymentEvent event = provider.parseNotification(paymentId + ":" + state);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        callbackService.handle(event);
        return ResponseEntity.ok(event);
    }

    @ExceptionHandler(PaymentProviderUnavailableException.class)
    public ResponseEntity<Void> unknownPayment() {
        return ResponseEntity.notFound().build();
    }
}
