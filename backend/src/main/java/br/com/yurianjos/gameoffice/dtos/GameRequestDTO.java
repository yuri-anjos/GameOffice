package br.com.yurianjos.gameoffice.dtos;


public record GameRequestDTO(
        String name,
        Integer year,
        Double price,
        Integer totalUnits,
        Integer hoursLength,
        Long[] genres,
        Long[] consoles
) {

}
