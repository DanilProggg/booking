package com.kridan.booking.service.payment.dev;

import com.kridan.booking.service.payment.*;
import com.kridan.booking.service.payment.exceptions.PaymentNotificationException;
import com.kridan.booking.service.payment.exceptions.PaymentProviderUnavailableException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Полностью in-memory реализация без похода в сеть: никакого реального эквайринга,
 * состояние платежей живёт в мапах внутри бина. Этого достаточно и для unit-тестов
 * (создаём объект через `new`), и для интеграционных (бин поднимается вместе с
 * контекстом в профиле dev — он же профиль по умолчанию).
 */
@Component
@Profile("dev")
public class StubPaymentProvider implements PaymentProvider {

    private final Map<String, PaymentState> states = new ConcurrentHashMap<>();
    private final Map<String, UUID> orderIdsByPaymentId = new ConcurrentHashMap<>();
    private final Map<UUID, String> paymentIdsByIdempotencyKey = new ConcurrentHashMap<>();

    @Override
    public PaymentIntent create(PaymentRequest request) {
        String existingPaymentId = paymentIdsByIdempotencyKey.get(request.idempotencyKey());
        if (existingPaymentId != null) {
            return toIntent(existingPaymentId, request.returnUrl());
        }

        String externalPaymentId = "stub-" + UUID.randomUUID();
        states.put(externalPaymentId, PaymentState.PENDING);
        orderIdsByPaymentId.put(externalPaymentId, request.orderId());
        paymentIdsByIdempotencyKey.put(request.idempotencyKey(), externalPaymentId);

        return toIntent(externalPaymentId, request.returnUrl());
    }

    @Override
    public PaymentState fetchState(String externalPaymentId) {
        PaymentState state = states.get(externalPaymentId);
        if (state == null) {
            throw new PaymentProviderUnavailableException("Unknown externalPaymentId: " + externalPaymentId, null);
        }
        return state;
    }

    /**
     * Формат тела уведомления в заглушке: "{externalPaymentId}:{state}",
     * например "stub-3e5...:SUCCEEDED". Никакого JSON — эту же строку
     * собирают тесты, изображающие webhook от эквайринга.
     */
    @Override
    public PaymentEvent parseNotification(String rawBody) {
        String[] parts = rawBody.strip().split(":", 2);
        if (parts.length != 2) {
            throw new PaymentNotificationException("Malformed stub notification body: " + rawBody, null);
        }

        String externalPaymentId = parts[0];
        PaymentState state;
        try {
            state = PaymentState.valueOf(parts[1]);
        } catch (IllegalArgumentException e) {
            throw new PaymentNotificationException("Unknown state in stub notification body: " + rawBody, e);
        }

        UUID orderId = orderIdsByPaymentId.get(externalPaymentId);
        if (orderId == null) {
            return null;
        }

        states.put(externalPaymentId, state);
        return new PaymentEvent(externalPaymentId, orderId, state);
    }

    @Override
    public void refund(String externalPaymentId, BigDecimal amount, UUID idempotencyKey) {
        fetchState(externalPaymentId);
        states.put(externalPaymentId, PaymentState.CANCELED);
    }

    /**
     * Тестовый хук, которого нет в реальном эквайринге: напрямую двигает статус
     * платежа, чтобы в тестах не собирать вручную JSON для parseNotification.
     */
    public void forceState(String externalPaymentId, PaymentState state) {
        fetchState(externalPaymentId);
        states.put(externalPaymentId, state);
    }

    private PaymentIntent toIntent(String externalPaymentId, String returnUrl) {
        return new PaymentIntent(externalPaymentId, PaymentIntent.ConfirmationType.REDIRECT_URL, returnUrl);
    }
}
