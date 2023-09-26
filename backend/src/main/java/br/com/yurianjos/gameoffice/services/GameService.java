package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Page<Game> searchGames(int page, int size, String search, Long console, Collection<Long> genres, Integer year) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name");

        return this.gameRepository.searchGames(search, console, CollectionUtils.isEmpty(genres) ? null : genres, year, pageRequest);
    }

    public Game findById(Long id, boolean throwError) throws CustomException {
        var game = this.gameRepository.findById(id).orElse(null);
        if (throwError && game == null) {
            throw new CustomException("Game não encontrado!", HttpStatus.NOT_FOUND.value());
        } else {
            return game;
        }
    }
}
