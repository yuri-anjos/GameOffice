package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Genre;
import br.com.yurianjos.gameoffice.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getGenres() {
        return this.genreRepository.findAll();
    }
}
