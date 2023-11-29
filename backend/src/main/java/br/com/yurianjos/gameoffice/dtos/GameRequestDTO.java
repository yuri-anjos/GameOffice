package br.com.yurianjos.gameoffice.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GameRequestDTO(
        @NotBlank
        String name,
        @NotNull
        Integer year,
        @NotNull
        Double price,
        @NotNull
        Integer totalUnits,
        @NotNull
        Integer hoursLength,
        @NotEmpty
        Long[] genres,
        @NotNull
        Long console
) {

}
