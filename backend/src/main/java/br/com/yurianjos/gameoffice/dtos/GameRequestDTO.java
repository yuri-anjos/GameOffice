package br.com.yurianjos.gameoffice.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Objects;

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
        @NotEmpty
        Long[] consoles
) {

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                GameRequestDTO that = (GameRequestDTO) o;
                return Objects.equals(name, that.name) && Objects.equals(year, that.year) && Objects.equals(price, that.price) && Objects.equals(totalUnits, that.totalUnits) && Objects.equals(hoursLength, that.hoursLength) && Arrays.equals(genres, that.genres) && Arrays.equals(consoles, that.consoles);
        }

        @Override
        public int hashCode() {
                int result = Objects.hash(name, year, price, totalUnits, hoursLength);
                result = 31 * result + Arrays.hashCode(genres);
                result = 31 * result + Arrays.hashCode(consoles);
                return result;
        }

        @Override
        public String toString() {
                return "GameRequestDTO{" +
                        "name='" + name + '\'' +
                        ", year=" + year +
                        ", price=" + price +
                        ", totalUnits=" + totalUnits +
                        ", hoursLength=" + hoursLength +
                        ", genres=" + Arrays.toString(genres) +
                        ", consoles=" + Arrays.toString(consoles) +
                        '}';
        }
}
