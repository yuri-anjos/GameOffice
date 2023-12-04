package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.dtos.ComboDTO;
import br.com.yurianjos.gameoffice.dtos.GameRequestDTO;
import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/search")
    public ResponseEntity<Page<GameResponseDTO>> searchGames(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "6") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long console,
            @RequestParam(required = false) List<Long> genres,
            @RequestParam(required = false) Integer year) {
        var response = gameService.searchGames(page, size, search, console, genres, year);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/combos")
    public ResponseEntity<List<ComboDTO>> searchGamesCombo(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        var response = gameService.searchGamesCombo(page, size, search);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDTO> getGame(@PathVariable Long gameId) throws CustomException {
        var response = gameService.getGame(gameId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{gameId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long gameId) throws CustomException {
        var cover = gameService.getImage(gameId);

        if (cover == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(cover.getContentType()))
                .body(cover.getData());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Long> createGame(@RequestBody @Valid GameRequestDTO dto) {
        var game = gameService.createGame(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(game.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{gameId}")
    public ResponseEntity<Void> updateGame(@PathVariable Long gameId, @RequestBody @Valid GameRequestDTO dto) throws CustomException {
        gameService.updateGame(gameId, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{gameId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveImage(@PathVariable Long gameId, @RequestParam("file") MultipartFile file) throws CustomException, IOException {
        gameService.saveImage(gameId, file);
        return ResponseEntity.ok().build();
    }
}
