package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.domain.Game;
import br.com.yurianjos.gameoffice.domain.GameImage;
import br.com.yurianjos.gameoffice.domain.Genre;
import br.com.yurianjos.gameoffice.dtos.GameRequestDTO;
import br.com.yurianjos.gameoffice.dtos.GameResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;

    public Page<GameResponseDTO> searchGames(int page, int size, String search, Long console, List<Long> genres, Integer year) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name");

        var result = gameRepository.searchGames(!StringUtils.hasText(search) ? null : search, console, CollectionUtils.isEmpty(genres) ? null : genres, year, pageRequest);

        return new PageImpl<>(
                result.getContent().stream().map(GameResponseDTO::new).toList(),
                pageRequest,
                result.getTotalElements()
        );
    }

    public GameResponseDTO getGame(Long gameId) throws CustomException {
        var game = findById(gameId);
        return new GameResponseDTO(game);
    }

    public GameImage getImage(Long gameId) throws CustomException {
        var game = findById(gameId);
        return game.getCover();
    }

    public Game createGame(GameRequestDTO dto) {
        var game = Game.builder()
                .name(dto.name())
                .price(dto.price())
                .hoursLength(dto.hoursLength())
                .totalUnits(dto.totalUnits())
                .availableUnits(dto.totalUnits())
                .year(dto.year())
                .console(new Console(dto.console()))
                .genres(Arrays.stream(dto.genres()).map(Genre::new).collect(Collectors.toSet()))
                .build();

        return gameRepository.save(game);
    }

    public void updateGame(Long gameId, GameRequestDTO dto) throws CustomException {
        var game = findById(gameId);

        var diff = game.getTotalUnits() - dto.totalUnits();
        var newAvailableUnits = game.getAvailableUnits() - diff;
        if (newAvailableUnits < 0) {
            throw new CustomException("Não há unidades disponíveis para remover!", HttpStatus.BAD_REQUEST.value());
        }

        game.setTotalUnits(dto.totalUnits());
        game.setAvailableUnits(newAvailableUnits);
        game.setName(dto.name());
        game.setYear(dto.year());
        game.setPrice(dto.price());
        game.setHoursLength(dto.hoursLength());
        game.setConsole(new Console(dto.console()));
        game.setGenres(Arrays.stream(dto.genres()).map(Genre::new).collect(Collectors.toSet()));

        gameRepository.save(game);
    }

    public void saveImage(Long gameId, MultipartFile file) throws CustomException, IOException {
        var game = findById(gameId);

        var cover = game.getCover() == null ? GameImage.builder().build() : game.getCover();
        cover.setData(file.getBytes());
        cover.setFileName(file.getOriginalFilename());
        cover.setContentType(file.getContentType());
        game.setCover(cover);

        gameRepository.save(game);
    }

    public Game findById(Long id) throws CustomException {
        return gameRepository.findById(id).orElseThrow(() -> new CustomException("Game não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
