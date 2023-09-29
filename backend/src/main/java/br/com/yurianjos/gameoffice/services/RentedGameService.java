package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.RentedGame;
import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.ReturnRentedGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.RentedGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RentedGameService {

    @Autowired
    private RentedGameRepository rentedGameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    public Long rentGame(Long userId, Long gameID) throws CustomException {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var user = this.userService.findById(userId);
        var game = this.gameService.findById(gameID);

        if (Integer.valueOf(0).equals(game.getAvailableUnits())) {
            throw new CustomException("Não há cópias de '" + game.getName() + "' disponíveis!", HttpStatus.BAD_REQUEST.value());
        }

        game.setAvailableUnits(game.getAvailableUnits() - 1);

        var rentedGame = RentedGame.builder()
                .game(game)
                .user(user)
                .admin(admin)
                .guaranty(game.getPrice())
                .build();

        return this.rentedGameRepository.save(rentedGame).getId();
    }

    public ReturnRentedGameResponseDTO returnRentedGame(Long rentedGameId) throws CustomException {
        var rentedGame = this.findById(rentedGameId);
        var game = rentedGame.getGame();

        if (!rentedGame.isActive()) {
            throw new CustomException("O jogo '" + game.getName() + "' já foi devolvido!", HttpStatus.BAD_REQUEST.value());
        }

        game.setAvailableUnits(game.getAvailableUnits() + 1);

        rentedGame.setPaymentDate(LocalDateTime.now());
        rentedGame.setActive(Boolean.FALSE);

        var daysRented = rentedGame.calculateDaysRented();
        var pricePerDay = rentedGame.calculatePricePerDay();
        var totalPrice = rentedGame.calculateTotalPrice(daysRented, pricePerDay);
        rentedGame.setPaid(totalPrice);

        this.rentedGameRepository.save(rentedGame);

        return new ReturnRentedGameResponseDTO(daysRented, pricePerDay, totalPrice);
    }

    public RentedGame findById(Long id) throws CustomException {
        return this.rentedGameRepository.findById(id).orElseThrow(() -> new CustomException("Aluguel de jogo não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
