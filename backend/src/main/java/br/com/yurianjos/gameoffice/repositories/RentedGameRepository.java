package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.RentedGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentedGameRepository extends JpaRepository<RentedGame, Long> {

}
