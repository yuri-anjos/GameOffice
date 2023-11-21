package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.RentedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentedGameRepository extends JpaRepository<RentedGame, Long> {

    List<RentedGame> findByUserId(Long userId);
}
