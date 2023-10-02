package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.GameImage;
import br.com.yurianjos.gameoffice.domain.Genre;
import br.com.yurianjos.gameoffice.dtos.GameRequestDTO;
import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    private GameService underTest;

    @Mock
    private GameRepository gameRepository;

    @BeforeEach
    void setUp() {
        underTest = new GameService(gameRepository);
    }

    @Test
    void searchGames() {
        //given
        var page = 0;
        var size = 5;
        var search = "search";
        var console = 1L;
        var genres = List.of(new Long[]{1L});
        var year = 2000;
        var pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name");

        var game = new Game();
        var gameList = Collections.singletonList(game);

        var expectedGameList = Collections.singletonList(new GameResponseDTO(game));
        var expected = new PageImpl<>(expectedGameList, pageRequest, expectedGameList.size());

        //when
        when(gameRepository.searchGames(search, console, genres, year, pageRequest)).thenReturn(new PageImpl<>(gameList, pageRequest, gameList.size()));
        var result = underTest.searchGames(page, size, search, console, genres, year);

        //then
        verify(gameRepository).searchGames(search, console, genres, year, pageRequest);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void searchGamesNullFilters() {
        //given
        var page = 0;
        var size = 5;
        var search = "";
        var console = 1L;
        var genres = List.of(new Long[]{});
        var year = 2000;
        var pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name");


        var game = new Game();
        var gameList = Collections.singletonList(game);

        var expectedGameList = Collections.singletonList(new GameResponseDTO(game));
        var expected = new PageImpl<>(expectedGameList, pageRequest, expectedGameList.size());

        //when
        when(gameRepository.searchGames(null, console, null, year, pageRequest)).thenReturn(new PageImpl<>(gameList, pageRequest, gameList.size()));
        var result = underTest.searchGames(page, size, search, console, genres, year);

        //then
        verify(gameRepository).searchGames(null, console, null, year, pageRequest);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getGame() throws CustomException {
        //given
        var gameId = 1L;

        var game = Game.builder()
                .id(gameId)
                .build();

        var expected = new GameResponseDTO(game);

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        var result = underTest.getGame(gameId);

        //then
        verify(gameRepository).findById(gameId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getImage() throws CustomException {
        //given
        var gameId = 1L;

        var cover = new GameImage();
        var game = Game.builder()
                .id(gameId)
                .cover(cover)
                .build();

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
        var result = underTest.getImage(gameId);

        //then
        verify(gameRepository).findById(gameId);
        assertThat(result).isEqualTo(cover);
    }

    @Test
    void createGame() {
        //given
        var dto = new GameRequestDTO("name", 2000, 100.0, 5, 20, new Long[]{1L}, new Long[]{1L});
        var expected = Game.builder()
                .name(dto.name())
                .price(dto.price())
                .hoursLength(dto.hoursLength())
                .totalUnits(dto.totalUnits())
                .availableUnits(dto.totalUnits())
                .year(dto.year())
                .consoles(Arrays.stream(dto.consoles()).map(Console::new).collect(Collectors.toSet()))
                .genres(Arrays.stream(dto.genres()).map(Genre::new).collect(Collectors.toSet()))
                .build();

        //when
        when(gameRepository.save(expected)).thenReturn(expected);
        var result = underTest.createGame(dto);

        //then
        verify(gameRepository).save(expected);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void updateGameSuccess() throws CustomException {
        //given
        var gameId = 1L;
        var dto = new GameRequestDTO("name", 2000, 100.0, 5, 20, new Long[]{1L}, new Long[]{1L});
        var game = Game.builder()
                .totalUnits(8)
                .availableUnits(5)
                .build();

        var expected = Game.builder()
                .name(dto.name())
                .year(dto.year())
                .price(dto.price())
                .hoursLength(dto.hoursLength())
                .consoles(Arrays.stream(dto.consoles()).map(Console::new).collect(Collectors.toSet()))
                .genres(Arrays.stream(dto.genres()).map(Genre::new).collect(Collectors.toSet()))
                .totalUnits(5)
                .availableUnits(2)
                .build();

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
        underTest.updateGame(gameId, dto);

        //then
        ArgumentCaptor<Game> argumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).findById(gameId);
        verify(gameRepository).save(argumentCaptor.capture());

        var captured = argumentCaptor.getValue();
        assertThat(captured).isEqualTo(expected);
        assertThat(captured.getTotalUnits()).isEqualTo(expected.getTotalUnits());
        assertThat(captured.getAvailableUnits()).isEqualTo(expected.getAvailableUnits());
    }

    @Test
    void updateGameThrowError() {
        //given
        var gameId = 1L;
        var dto = new GameRequestDTO("name", 2000, 100.0, 1, 20, new Long[]{1L}, new Long[]{1L});
        var game = Game.builder()
                .totalUnits(5)
                .availableUnits(2)
                .build();

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));

        //then
        assertThatThrownBy(() -> underTest.updateGame(gameId, dto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Não há unidades disponíveis para remover!");
        verify(gameRepository).findById(gameId);
        verify(gameRepository, never()).save(any());
    }

    @Test
    void saveImageCoverIsNull() throws CustomException, IOException {
        //given
        var gameId = 1L;
        var game = Game.builder()
                .build();

        var file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        var expected = GameImage.builder()
                .data(file.getBytes())
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
        underTest.saveImage(gameId, file);

        //then
        ArgumentCaptor<Game> argumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).findById(gameId);
        verify(gameRepository).save(argumentCaptor.capture());
        var captured = argumentCaptor.getValue();
        assertThat(captured.getCover()).isEqualTo(expected);
    }

    @Test
    void saveImageCoverAlreadyExists() throws CustomException, IOException {
        //given
        var gameId = 1L;
        var game = Game.builder()
                .cover(new GameImage())
                .build();

        var file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );

        var expected = GameImage.builder()
                .data(file.getBytes())
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
        underTest.saveImage(gameId, file);

        //then
        ArgumentCaptor<Game> argumentCaptor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).findById(gameId);
        verify(gameRepository).save(argumentCaptor.capture());
        var captured = argumentCaptor.getValue();
        assertThat(captured.getCover()).isEqualTo(expected);
    }

    @Test
    void findByIdSuccess() throws CustomException {
        //given
        var gameId = 1L;
        var game = Game.builder().id(gameId).build();

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(game));
        var result = underTest.findById(gameId);

        //then
        verify(gameRepository).findById(gameId);
        assertThat(result).isEqualTo(game);
    }

    @Test
    void findByIdThrowError() {
        //given
        var gameId = 1L;

        //when
        when(gameRepository.findById(gameId)).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> underTest.findById(gameId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Game não encontrado!");
        verify(gameRepository).findById(gameId);
    }
}