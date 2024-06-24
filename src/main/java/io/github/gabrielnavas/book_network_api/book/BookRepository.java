package io.github.gabrielnavas.book_network_api.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitle(String title);

    @Query("""
                        SELECT book 
                        FROM Book book
                        WHERE book.archived = false
                        AND book.shareable = true
                        AND book.owner.id != :userId    
               
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);
}
