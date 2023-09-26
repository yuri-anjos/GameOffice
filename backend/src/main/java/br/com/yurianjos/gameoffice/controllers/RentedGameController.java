package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.RentedGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rented-game")
public class RentedGameController {

    @Autowired
    private RentedGameService rentedGameService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/{userId}/rent/{gameId}")
    public ResponseEntity rentGame(@PathVariable Long userId, @PathVariable Long gameId) throws CustomException {
        this.rentedGameService.rentGame(userId, gameId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{rentedGameId}/return")
    public ResponseEntity returnRentedGame(@PathVariable Long rentedGameId) throws CustomException {
        var response = this.rentedGameService.returnRentedGame(rentedGameId);
        return ResponseEntity.ok().body(response);
    }
}
