package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity searchGames(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long console,
            @RequestParam(required = false) Collection<Long> genres,
            @RequestParam(required = false) Integer year) {
        var response = this.gameService.searchGames(page, size, search, console, genres, year);
        return ResponseEntity.ok().body(response);
    }
}
