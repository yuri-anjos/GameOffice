package br.com.yurianjos.gameoffice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Table
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, precision = 6, scale = 2)
    private Double price;

    private Integer totalUnits;

    private Integer availableUnits;

    @Column(nullable = false)
    private Integer hoursLength;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_genre",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"game_id", "genre_id"})
            },
            joinColumns = {
                    @JoinColumn(name = "game_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "genre_id")})
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_console",
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"game_id", "console_id"})
            },
            joinColumns = {
                    @JoinColumn(name = "game_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "console_id")})
    private Set<Console> consoles;

    @OneToOne(fetch = FetchType.LAZY)
    private GameImage cover;
}
