package io.github.gabrielnavas.book_network_api.feedback;

import io.github.gabrielnavas.book_network_api.book.Book;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false) // Not required and has not impacted :: just top satisfy lombok
                        .shareable(false) // Not required and has not impacted :: just top satisfy lombok
                        .build())
                .build();
    }
}
