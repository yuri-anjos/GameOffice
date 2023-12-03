package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.RentalGame;
import br.com.yurianjos.gameoffice.dtos.RentalGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.ReturnRentalGameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.RentalGameRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class RentalGameService {

    private final RentalGameRepository rentalGameRepository;

    private final GameService gameService;

    private final UserService userService;

    private final ContextService contextService;

    public RentalGame rentGame(Long userId, Long gameID) throws CustomException {
        var admin = contextService.getContextUser();

        var user = userService.findById(userId);
        var game = gameService.findById(gameID);

        if (game.getAvailableUnits() == 0) {
            throw new CustomException("Não há cópias de '" + game.getName() + "' disponíveis!", HttpStatus.BAD_REQUEST.value());
        }

        game.setAvailableUnits(game.getAvailableUnits() - 1);

        var rentalGame = RentalGame.builder()
                .game(game)
                .user(user)
                .rentAdmin(admin)
                .rent(game.getPrice())
                .build();

        return rentalGameRepository.save(rentalGame);
    }

    public ReturnRentalGameResponseDTO returnRentalGame(Long rentedGameId) throws CustomException {
        var admin = contextService.getContextUser();

        var rentedGame = findById(rentedGameId);
        var game = rentedGame.getGame();

        if (!rentedGame.isActive()) {
            throw new CustomException("O jogo '" + game.getName() + "' já foi devolvido!", HttpStatus.BAD_REQUEST.value());
        }

        game.setAvailableUnits(game.getAvailableUnits() + 1);
        rentedGame.setReturnDate(LocalDateTime.now());
        rentedGame.setReturnAdmin(admin);
        rentedGame.setActive(Boolean.FALSE);

        var totalPrice = rentedGame.calculateRentalPayment();
        rentedGame.setPayment(totalPrice > rentedGame.getRent() ? rentedGame.getRent() : totalPrice);

        rentalGameRepository.save(rentedGame);

        return new ReturnRentalGameResponseDTO(rentedGame.getRent(), rentedGame.getPayment(), rentedGame.getRent() - rentedGame.getPayment());
    }

    public List<RentalGameResponseDTO> getActiveRentalGames(Long userId) {
        Sort sort = Sort.by("rentDate").descending();

        var result = rentalGameRepository.findByUserIdAndActive(userId, Boolean.TRUE, sort);

        return result.stream().map(RentalGameResponseDTO::new).toList();
    }

    public Page<RentalGameResponseDTO> getRentalGames(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.DESC,
                "rentDate");

        var result = rentalGameRepository.findByUserId(userId, pageRequest);

        return new PageImpl<>(
                result.getContent().stream().map(RentalGameResponseDTO::new).toList(),
                pageRequest,
                result.getTotalElements()
        );
    }

    public RentalGame findById(Long id) throws CustomException {
        return rentalGameRepository.findById(id)
                .orElseThrow(() -> new CustomException("Aluguel de jogo não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
