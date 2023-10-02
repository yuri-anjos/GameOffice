package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Genre;
import br.com.yurianjos.gameoffice.repositories.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }
}
