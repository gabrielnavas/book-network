package io.github.gabrielnavas.book_network_api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query("""
            SELECT t 
            FROM Token t
            WHERE t.user.email=:email
            """)
    Optional<Token> findByUserEmail(String email);
}
