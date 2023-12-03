package br.com.yurianjos.gameoffice.domain;

import br.com.yurianjos.gameoffice.utils.Utils;
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

@Table
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RentalGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 2)
    private Double rent;

    @Column(precision = 2)
    private Double payment;

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
    private LocalDateTime rentDate;

    private LocalDateTime returnDate;

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.payment = 0.0;
    }

    public Double calculateRentalPayment() {
        var daysRented = Utils.calculateDaysBetween(this.rentDate, this.returnDate);
        var pricePerDay = (rent / 4 / 30); //25% on a month
        return (rent * 0.1) + (daysRented * pricePerDay); //10% initially
    }
}
