package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
