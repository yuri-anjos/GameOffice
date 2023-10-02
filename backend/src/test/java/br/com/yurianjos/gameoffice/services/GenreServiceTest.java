package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Genre;
import br.com.yurianjos.gameoffice.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    private GenreService underTest;

    @Mock
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        underTest = new GenreService(genreRepository);
    }

    @Test
    void getGenres() {
        //given
        var expected = Collections.singletonList(new Genre(1L, "genre"));

        //when
        when(genreRepository.findAll()).thenReturn(expected);
        var result = underTest.getGenres();

        //then
        verify(genreRepository).findAll();
        assertThat(result).isEqualTo(expected);
    }
}