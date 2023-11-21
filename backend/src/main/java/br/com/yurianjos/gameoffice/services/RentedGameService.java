package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.RentedGame;
import br.com.yurianjos.gameoffice.dtos.ReturnRentedGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.RentedGameRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class RentedGameService {

    private final RentedGameRepository rentedGameRepository;

    private final GameService gameService;

    private final UserService userService;

    private final ContextService contextService;

    public RentedGame rentGame(Long userId, Long gameID) throws CustomException {
        var admin = contextService.getContextUser();

        var user = userService.findById(userId);
        var game = gameService.findById(gameID);

        if (Integer.valueOf(0).equals(game.getAvailableUnits())) {
            throw new CustomException("Não há cópias de '" + game.getName() + "' disponíveis!", HttpStatus.BAD_REQUEST.value());
        }

        game.setAvailableUnits(game.getAvailableUnits() - 1);

        var rentedGame = RentedGame.builder()
                .game(game)
                .user(user)
                .rentAdmin(admin)
                .guaranty(game.getPrice())
                .build();

        return rentedGameRepository.save(rentedGame);
    }

    public ReturnRentedGameResponseDTO returnRentedGame(Long rentedGameId) throws CustomException {
        var admin = contextService.getContextUser();

        var rentedGame = findById(rentedGameId);
        var game = rentedGame.getGame();

        if (!rentedGame.isActive()) {
            throw new CustomException("O jogo '" + game.getName() + "' já foi devolvido!", HttpStatus.BAD_REQUEST.value());
        }

        game.setAvailableUnits(game.getAvailableUnits() + 1);
        rentedGame.setPaymentDate(LocalDateTime.now());
        rentedGame.setActive(Boolean.FALSE);
        rentedGame.setReturnAdmin(admin);

        var daysRented = rentedGame.calculateDaysRented();
        var pricePerDay = rentedGame.calculatePricePerDay();
        var totalPrice = rentedGame.calculateTotalPrice(daysRented, pricePerDay);
        rentedGame.setPaid(totalPrice > rentedGame.getGuaranty() ? rentedGame.getGuaranty() : totalPrice);

        rentedGameRepository.save(rentedGame);

        return new ReturnRentedGameResponseDTO(daysRented, pricePerDay, totalPrice);
    }

    public List<RentedGame> getRentedGames(Long userId) {
        return rentedGameRepository.findByUserId(userId);
    }

    public RentedGame findById(Long id) throws CustomException {
        return rentedGameRepository.findById(id)
                .orElseThrow(() -> new CustomException("Aluguel de jogo não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
