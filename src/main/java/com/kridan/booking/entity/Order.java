package com.kridan.booking.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    private UUID id = UUID.randomUUID();

    private String holdKey;

    private String status;

    @Column(nullable = false)
    private Date expiresAt;

    private String buyerEmail;

    @Column(nullable = false)
    private int totalCost;

    private UUID idempotencyKey;

    private String externalPaymentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_id")
    private User user;

    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();

    private Date paidAt;

    public Order(Event event, Date expiresAt, User user) {
        this.id = UUID.randomUUID();
        this.event = event;
        this.expiresAt = expiresAt;
        this.user = user;
        this.status = "HELD";
        this.totalCost = 0;
    }

    public void prepareForPayment(String buyerEmail, int totalCost) {
        this.buyerEmail = buyerEmail;
        this.totalCost = totalCost;
        this.idempotencyKey = UUID.randomUUID();
        this.status = "CREATED";
    }

    public void startPayment(String externalPaymentId, Date newExpiresAt) {
        this.externalPaymentId = externalPaymentId;
        this.expiresAt = newExpiresAt;
        this.status = "PENDING";
    }

    public void markPaid() {
        this.status = "PAID";
        this.paidAt = new Date();
    }

    public void markCanceled() {
        this.status = "CANCELED";
    }

    public void markNeedsRefund() {
        this.status = "NEEDS_REFUND";
    }

    public boolean isExpired(Date now) {
        return expiresAt.before(now);
    }

}
