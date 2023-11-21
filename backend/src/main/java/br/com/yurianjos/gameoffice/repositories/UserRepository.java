package br.com.yurianjos.gameoffice.repositories;

import br.com.yurianjos.gameoffice.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u " +
            "WHERE u.name LIKE %?1% " +
            "AND u.role LIKE 'USER'")
    List<User> searchNonAdminUsers(String search, PageRequest pageRequest);
}
