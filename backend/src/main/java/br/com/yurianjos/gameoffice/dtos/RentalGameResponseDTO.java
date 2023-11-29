package br.com.yurianjos.gameoffice.dtos;

import br.com.yurianjos.gameoffice.domain.RentalGame;

import java.time.LocalDateTime;

public record RentalGameResponseDTO(Long id,
                                    Double rent,
                                    Double payment,
                                    GameResponseDTO game,
                                    ComboDTO rentAdmin,
                                    ComboDTO returnAdmin,
                                    boolean active,
                                    LocalDateTime created,
                                    LocalDateTime returnedDate) {

    public RentalGameResponseDTO(RentalGame rentalGame) {
        this(
                rentalGame.getId(),
                rentalGame.getRent(),
                rentalGame.getPayment(),
                new GameResponseDTO(rentalGame.getGame()),
                new ComboDTO(rentalGame.getRentAdmin()),
                rentalGame.getReturnAdmin() == null ? null : new ComboDTO(rentalGame.getReturnAdmin()),
                rentalGame.isActive(), rentalGame.getCreated(),
                rentalGame.getReturnedDate()
        );
    }
}
