package br.com.yurianjos.gameoffice.dtos;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.Genre;

import java.util.Set;

public record GameResponseDTO(
        Long id,
        String name,
        Integer year,
        Integer hoursLength,
        Double price,
        Integer totalUnits,
        Integer availableUnits,
        Set<Genre> genres,
        Console console) {

    public GameResponseDTO(Game game) {
        this(
                game.getId(),
                game.getName(),
                game.getYear(),
                game.getHoursLength(),
                game.getPrice(),
                game.getTotalUnits(),
                game.getAvailableUnits(),
                game.getGenres(),
                game.getConsole());
    }
}
