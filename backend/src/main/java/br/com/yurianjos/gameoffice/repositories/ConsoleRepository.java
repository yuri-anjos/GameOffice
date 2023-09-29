package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.Console;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsoleRepository extends JpaRepository<Console, Long> {
}
