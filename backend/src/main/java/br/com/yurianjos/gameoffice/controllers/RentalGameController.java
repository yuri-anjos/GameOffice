package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.dtos.RentalGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.ReturnRentalGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.RentalGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rental-game")
public class RentalGameController {

    @Autowired
    private RentalGameService rentalGameService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/{userId}/rent/{gameId}")
    public ResponseEntity<Void> rentGame(@PathVariable Long userId, @PathVariable Long gameId) throws CustomException {
        var rentalGame = rentalGameService.rentGame(userId, gameId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{rentalGameId}/return")
    public ResponseEntity<ReturnRentalGameResponseDTO> returnRentalGame(@PathVariable Long rentalGameId) throws CustomException {
        var response = rentalGameService.returnRentalGame(rentalGameId);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active/user/{userId}")
    public ResponseEntity<List<RentalGameResponseDTO>> getActiveRentalGames(@PathVariable Long userId) {
        var response = rentalGameService.getActiveRentalGames(userId);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<RentalGameResponseDTO>> getRentedGames(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size) {
        var response = rentalGameService.getRentalGames(userId, page, size);
        return ResponseEntity.ok().body(response);
    }
}
