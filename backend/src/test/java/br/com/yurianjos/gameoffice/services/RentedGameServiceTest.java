package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.RentedGame;
import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.ReturnRentedGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.RentedGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentedGameServiceTest {

    private RentedGameService underTest;

    @Mock
    private RentedGameRepository rentedGameRepository;

    @Mock
    private GameService gameService;

    @Mock
    private UserService userService;

    @Mock
    private ContextService contextService;

    @BeforeEach
    void setUp() {
        underTest = new RentedGameService(rentedGameRepository, gameService, userService, contextService);
    }

    @Test
    void rentGameSuccess() throws CustomException {
        //given
        var price = 100.0;
        var userId = 1L;
        var gameId = 2L;
        var user = User.builder()
                .id(userId)
                .build();
        var game = Game.builder()
                .id(gameId)
                .availableUnits(1)
                .totalUnits(1)
                .price(price)
                .build();
        var admin = User.builder()
                .id(3L)
                .build();
        var rentedGame = RentedGame.builder()
                .game(game)
                .user(user)
                .rentAdmin(admin)
                .build();
        var expected = RentedGame.builder()
                .id(1L)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(admin);
        when(userService.findById(userId)).thenReturn(user);
        when(gameService.findById(gameId)).thenReturn(game);
        when(rentedGameRepository.save(rentedGame)).thenReturn(expected);
        var result = underTest.rentGame(userId, gameId);

        //then
        ArgumentCaptor<RentedGame> argumentCaptor = ArgumentCaptor.forClass(RentedGame.class);
        verify(contextService).getContextUser();
        verify(userService).findById(userId);
        verify(gameService).findById(gameId);
        verify(rentedGameRepository).save(argumentCaptor.capture());
        var capturedRentedGame = argumentCaptor.getValue();

        assertThat(result).isEqualTo(expected);
        assertThat(capturedRentedGame.getGuaranty()).isEqualTo(price);
        assertThat(capturedRentedGame.getGame().getAvailableUnits()).isZero();
    }

    @Test
    void rentGameThrowError() throws CustomException {
        //given
        var price = 100.0;
        var userId = 1L;
        var gameId = 2L;
        var user = User.builder()
                .id(userId)
                .build();
        var game = Game.builder()
                .id(gameId)
                .availableUnits(0)
                .totalUnits(1)
                .price(price)
                .build();
        var admin = User.builder()
                .id(3L)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(admin);
        when(userService.findById(userId)).thenReturn(user);
        when(gameService.findById(gameId)).thenReturn(game);

        //then
        assertThatThrownBy(() -> underTest.rentGame(userId, gameId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Não há cópias de '" + game.getName() + "' disponíveis!");
        verify(contextService).getContextUser();
        verify(userService).findById(userId);
        verify(gameService).findById(gameId);
        verify(rentedGameRepository, never()).save(ArgumentMatchers.any(RentedGame.class));
    }

    @Test
    void returnRentedGameSuccess() throws CustomException {
        //given
        var rentedGameId = 1L;
        var rentedGame = RentedGame.builder()
                .id(rentedGameId)
                .game(Game.builder().availableUnits(1).build())
                .active(Boolean.TRUE)
                .guaranty(100.0)
                .created(LocalDateTime.now().minusDays(7))
                .build();

        var admin = User.builder()
                .id(3L)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(admin);
        when(rentedGameRepository.findById(rentedGameId)).thenReturn(Optional.of(rentedGame));
        var result = underTest.returnRentedGame(rentedGameId);

        //then
        ArgumentCaptor<RentedGame> argumentCaptor = ArgumentCaptor.forClass(RentedGame.class);
        verify(contextService).getContextUser();
        verify(rentedGameRepository).findById(rentedGameId);
        verify(rentedGameRepository).save(argumentCaptor.capture());
        var captured = argumentCaptor.getValue();

        assertThat(captured.getReturnAdmin()).isEqualTo(admin);
        assertFalse(captured.isActive());
        assertThat(captured.getPaymentDate().toLocalDate()).isEqualTo(LocalDate.now());
        assertThat(captured.getGame().getAvailableUnits()).isEqualTo(2);

        var daysRented = captured.calculateDaysRented();
        var pricePerDay = captured.calculatePricePerDay();
        var totalPrice = captured.calculateTotalPrice(daysRented, pricePerDay);
        assertThat(result).isEqualTo(new ReturnRentedGameResponseDTO(daysRented, pricePerDay, totalPrice));
    }

    @Test
    void returnRentedGameThrowError() {
        //given
        var rentedGameId = 1L;
        var rentedGame = RentedGame.builder()
                .id(rentedGameId)
                .game(Game.builder().availableUnits(1).build())
                .active(Boolean.FALSE)
                .guaranty(100.0)
                .created(LocalDateTime.now().minusDays(7))
                .build();

        var admin = User.builder()
                .id(3L)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(admin);
        when(rentedGameRepository.findById(rentedGameId)).thenReturn(Optional.of(rentedGame));

        //then
        assertThatThrownBy(() -> underTest.returnRentedGame(rentedGameId))
                .isInstanceOf(CustomException.class)
                .hasMessage("O jogo '" + rentedGame.getGame().getName() + "' já foi devolvido!");
        verify(contextService).getContextUser();
        verify(rentedGameRepository).findById(rentedGameId);
    }

    @Test
    void findByIdSuccess() throws CustomException {
        //given
        var rentedGameId = 1L;
        var expected = new RentedGame();

        //when
        when(rentedGameRepository.findById(rentedGameId)).thenReturn(Optional.of(expected));
        var result = underTest.findById(rentedGameId);

        //then
        verify(rentedGameRepository).findById(rentedGameId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findByIdThrowError() {
        //given
        var rentedGameId = 1L;

        //when
        when(rentedGameRepository.findById(rentedGameId)).thenReturn(Optional.ofNullable(null));

        //then
        assertThatThrownBy(() -> underTest.findById(rentedGameId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Aluguel de jogo não encontrado!");
        verify(rentedGameRepository).findById(rentedGameId);
    }
}