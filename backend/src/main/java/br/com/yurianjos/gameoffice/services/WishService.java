package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.Wish;
import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.WishRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WishService {

    private final WishRepository wishRepository;

    private final ContextService contextService;

    public void wishGame(Long gameId) {
        var user = contextService.getContextUser();
        var wish = Wish.builder()
                .game(Game.builder().id(gameId).build())
                .user(user)
                .build();

        wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long gameId) throws CustomException {
        var user = contextService.getContextUser();
        var removed = wishRepository.deleteByGameIdAndUserId(gameId, user.getId());
        if (removed == 0) {
            throw new CustomException("Erro ao remover Game da lista de desejos!", HttpStatus.BAD_REQUEST.value());
        }
    }

    public List<GameResponseDTO> getWishedGames() {
        var user = contextService.getContextUser();
        var wishes = wishRepository.findByUserId(user.getId());
        return wishes.stream()
                .map(wish -> new GameResponseDTO(wish.getGame()))
                .collect(Collectors.toList());
    }

    public List<Long> getWishedGamesIds(Long userId) {
        return wishRepository.findGameIdByUserId(userId);
    }
}
