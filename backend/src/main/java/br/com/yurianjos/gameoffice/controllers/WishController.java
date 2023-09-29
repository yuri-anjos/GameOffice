package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wish")
public class WishController {

    @Autowired
    private WishService wishService;

    @PostMapping("/{gameId}")
    public ResponseEntity<Void> wishGame(@PathVariable Long gameId) {
        this.wishService.wishGame(gameId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long gameId) throws CustomException {
        this.wishService.deleteWish(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<List<GameResponseDTO>> getWishedGames() {
        var result = this.wishService.getWishedGames();
        return ResponseEntity.ok().body(result);
    }
}
