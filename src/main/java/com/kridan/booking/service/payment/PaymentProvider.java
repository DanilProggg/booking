package com.kridan.booking.service.payment;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentProvider {

    /**
     * Создаёт платёж в эквайринге.
     * Идемпотентен по request.idempotencyKey(): повтор с тем же ключом
     * вернёт уже созданный платёж, а не спишет деньги дважды.
     *
     * @throws PaymentProviderUnavailableException сеть или 5xx — результат неизвестен,
     *         заказ нельзя считать неоплаченным, нужен повтор с тем же ключом
     * @throws PaymentRejectedException эквайринг отклонил сам запрос
     *         (неверная сумма, некорректный чек) — повтор бессмыслен
     */
    PaymentIntent create(PaymentRequest request);

    /** Актуальный статус. Источник истины при расхождениях и для reconciler'а. */
    PaymentState fetchState(String externalPaymentId);

    /**
     * Разбирает сырое тело уведомления и подтверждает статус запросом к API.
     * @return null, если уведомление не относится к платежам этого приложения
     */
    PaymentEvent parseNotification(String rawBody);

    /** Компенсация: деньги списались, а выдать билет невозможно. */
    void refund(String externalPaymentId, BigDecimal amount, UUID idempotencyKey);
}
