package br.com.yurianjos.gameoffice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Table
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RentedGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 6, scale = 2)
    private Double guaranty;

    @Column(precision = 5, scale = 2)
    private Double paid;

    private LocalDateTime paymentDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Game game;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User rentAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    private User returnAdmin;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    private LocalDateTime created;

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.paid = 0.0;
    }

    public Long calculateDaysRented() {
        return ChronoUnit.DAYS.between(this.getCreated().toLocalDate(), this.getPaymentDate().toLocalDate());
    }

    public Double calculatePricePerDay() {
        return this.getGuaranty() / 4 / 30;
    }

    public Double calculateTotalPrice(Long daysRented, Double pricePerDay) {
        return (this.getGuaranty() * 0.1) + (daysRented * pricePerDay);
    }
}
