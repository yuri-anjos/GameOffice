package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    int deleteByGameIdAndUserId(Long wishId, Long userId);

    List<Wish> findByUserId(Long id);

    @Query("SELECT w.game.id FROM Wish w " +
            "WHERE w.user.id = ?1")
    List<Long> findGameIdByUserId(Long id);

}
