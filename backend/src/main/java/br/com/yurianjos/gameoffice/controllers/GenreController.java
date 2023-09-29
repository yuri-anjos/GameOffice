package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.domain.Genre;
import br.com.yurianjos.gameoffice.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreService consoleService;

    @GetMapping
    public ResponseEntity<List<Genre>> getGenres() {
        var response = this.consoleService.getGenres();
        return ResponseEntity.ok().body(response);
    }
}
