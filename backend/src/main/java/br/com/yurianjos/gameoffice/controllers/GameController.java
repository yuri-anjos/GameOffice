package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.dtos.CreatedResponseDTO;
import br.com.yurianjos.gameoffice.dtos.GameRequestDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.GameService;
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
import java.util.Collection;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public ResponseEntity<Page<Game>> searchGames(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long console,
            @RequestParam(required = false) Collection<Long> genres,
            @RequestParam(required = false) Integer year) {
        var response = this.gameService.searchGames(page, size, search, console, genres, year);
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CreatedResponseDTO> createGame(@RequestBody GameRequestDTO dto) {
        var id = this.gameService.createGame(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponseDTO(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{gameId}")
    public ResponseEntity<Void> updateGame(@PathVariable Long gameId, @RequestBody GameRequestDTO dto) throws CustomException {
        this.gameService.updateGame(gameId, dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{gameId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveImage(@PathVariable Long gameId, @RequestParam("file") MultipartFile file) throws CustomException, IOException {
        this.gameService.saveImage(gameId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{gameId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long gameId) throws CustomException {
        var cover = this.gameService.getImage(gameId);
        if(cover == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(cover.getContentType()))
                .body(cover.getData());
    }
}
