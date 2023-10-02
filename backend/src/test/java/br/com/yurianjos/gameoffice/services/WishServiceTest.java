package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.domain.Wish;
import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.WishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    private WishService underTest;

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ContextService contextService;

    @BeforeEach
    void setUp() {
        underTest = new WishService(wishRepository, contextService);
    }

    @Test
    void wishGame() {
        //given
        var user = User.builder().build();
        var gameId = 1L;
        var expected = Wish.builder()
                .game(Game.builder().id(gameId).build())
                .user(user)
                .build();
        //when
        when(contextService.getContextUser()).thenReturn(user);
        underTest.wishGame(gameId);

        //then
        ArgumentCaptor<Wish> wishArgumentCaptor = ArgumentCaptor.forClass(Wish.class);
        verify(contextService).getContextUser();
        verify(wishRepository).save(wishArgumentCaptor.capture());

        var wish = wishArgumentCaptor.getValue();
        assertThat(wish).isEqualTo(expected);
    }

    @Test
    void deleteWishThrowError() {
        //given
        var gameId = 1L;
        var user = User.builder()
                .id(1L)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(wishRepository.deleteByGameIdAndUserId(gameId, user.getId())).thenReturn(0);

        //then
        assertThatThrownBy(() -> underTest.deleteWish(gameId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Erro ao remover Game da lista de desejos!");
        verify(contextService).getContextUser();
        verify(wishRepository).deleteByGameIdAndUserId(any(), any());
    }

    @Test
    void deleteWishSuccess() throws CustomException {
        //given
        var gameId = 1L;
        var user = User.builder()
                .id(1L)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(wishRepository.deleteByGameIdAndUserId(gameId, user.getId())).thenReturn(1);
        underTest.deleteWish(gameId);

        //then
        verify(contextService).getContextUser();
        verify(wishRepository).deleteByGameIdAndUserId(any(), any());
    }

    @Test
    void getWishedGames() {
        //given
        var user = User.builder()
                .id(1L)
                .build();

        var game = new Game();

        List<Wish> wishList = Collections.singletonList(
                Wish.builder()
                        .game(game)
                        .build()
        );

        var expected = List.of(new GameResponseDTO(game));

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(wishRepository.findByUserId(user.getId())).thenReturn(wishList);
        var result = underTest.getWishedGames();

        //then
        verify(contextService).getContextUser();
        verify(wishRepository).findByUserId(any());
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getWishedGamesIds() {
        //given
        var userId = 1L;
        var expected = Collections.singletonList(anyLong());

        //when
        when(wishRepository.findGameIdByUserId(userId)).thenReturn(expected);
        var result = underTest.getWishedGamesIds(userId);

        //then
        verify(wishRepository).findGameIdByUserId(any());
        assertThat(result).isEqualTo(expected);
    }
}