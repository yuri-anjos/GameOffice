package br.com.yurianjos.gameoffice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer year;

    private Double price;

    private Integer hoursLength;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_genre",
            joinColumns = {
                    @JoinColumn(name = "game_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "genre_id")})
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_console",
            joinColumns = {
                    @JoinColumn(name = "game_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "console_id")})
    private Set<Console> consoles;
}
