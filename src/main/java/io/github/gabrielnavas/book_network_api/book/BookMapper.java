package io.github.gabrielnavas.book_network_api.book;

import io.github.gabrielnavas.book_network_api.history.BookTransactionHistory;
import io.github.gabrielnavas.book_network_api.history.BorrowedBooksResponse;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.id())
                .title(request.title())
                .isbn(request.isbn())
                .authorName(request.authorName())
                .synopsis(request.synopsis())
                .archived(false)
                .shareable(request.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authorName(book.getAuthorName())
                .synopsis(book.getSynopsis())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .rate(book.getRate())
//                .cover() TODO: implements this later
                .build();
    }

    public BorrowedBooksResponse toBorrowedBooksResponse(BookTransactionHistory history) {
        return BorrowedBooksResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnedApproved())
                .build();
    }
}
