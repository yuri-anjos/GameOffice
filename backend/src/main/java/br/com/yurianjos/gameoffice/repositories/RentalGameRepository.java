package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.RentalGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalGameRepository extends JpaRepository<RentalGame, Long> {

    List<RentalGame> findByUserIdAndActive(Long userId, boolean isActive, Sort sort);

    Page<RentalGame> findByUserId(Long userId, PageRequest pageRequest);
}
