package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(
            value = "SELECT g FROM Game g " +
                    "LEFT JOIN FETCH g.genres ge " +
                    "WHERE (?1 IS NULL OR g.name LIKE %?1%) " +
                    "AND (?2 IS NULL OR g.console.id = ?2) " +
                    "AND (?3 IS NULL OR ge.id in ?3) " +
                    "AND (?4 IS NULL OR g.year = ?4) ",
            countQuery = "SELECT count(distinct g.id) FROM Game g " +
                    "LEFT JOIN g.genres ge " +
                    "WHERE (?1 IS NULL OR g.name LIKE %?1%) " +
                    "AND (?2 IS NULL OR g.console.id = ?2) " +
                    "AND (?3 IS NULL OR ge.id in ?3) " +
                    "AND (?4 IS NULL OR g.year = ?4) ")
    Page<Game> searchGames(
            String search,
            Long console,
            Collection<Long> genres,
            Integer year,
            PageRequest pageRequest);

    @Query("SELECT g FROM Game g " +
            "WHERE g.name LIKE %?1%")
    List<Game> searchGamesCombo(String search, PageRequest pageRequest);
}
