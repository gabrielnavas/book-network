package io.github.gabrielnavas.book_network_api.book;

import io.github.gabrielnavas.book_network_api.common.BaseEntity;
import io.github.gabrielnavas.book_network_api.feedback.Feedback;
import io.github.gabrielnavas.book_network_api.history.BookTransactionHistory;
import io.github.gabrielnavas.book_network_api.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Table(name = "books")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String synopsis;

    @Column(nullable = false)
    private String bookCover;

    @Column(nullable = false)
    private boolean archived;

    @Column(nullable = false)
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;
}
