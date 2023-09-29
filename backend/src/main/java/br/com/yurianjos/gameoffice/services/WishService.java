package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.domain.Wish;
import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;

    public void wishGame(Long gameId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var wish = Wish.builder()
                .game(Game.builder().id(gameId).build())
                .user(user).build();

        this.wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long gameId) throws CustomException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var removed = this.wishRepository.deleteByGameIdAndUserId(gameId, user.getId());
        if (removed == 0) {
            throw new CustomException("Erro ao remover Game da lista de desejos!", HttpStatus.BAD_REQUEST.value());
        }
    }

    public List<GameResponseDTO> getWishedGames() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var wishes = this.wishRepository.findByUserId(user.getId());
        return wishes.stream().map(
                        wish -> new GameResponseDTO(wish.getGame()))
                .collect(Collectors.toList());
    }

    public List<Long> getWishedGamesIds(Long userId) {
        return this.wishRepository.findGameIdByUserId(userId);
    }
}
