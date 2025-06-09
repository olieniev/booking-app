package com.olieniev.bookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "payments")
@Getter
@Setter
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@ToString
@EqualsAndHashCode
public class Payment {
    @Id
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;
    @Column(nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private BigDecimal amountToPay;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        PENDING,
        PAID
    }
}
