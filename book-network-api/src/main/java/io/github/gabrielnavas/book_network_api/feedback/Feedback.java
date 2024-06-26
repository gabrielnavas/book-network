package io.github.gabrielnavas.book_network_api.feedback;

import io.github.gabrielnavas.book_network_api.book.Book;
import io.github.gabrielnavas.book_network_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity {
    @Column(nullable = false)
    private Double note; // 1-5 star

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
