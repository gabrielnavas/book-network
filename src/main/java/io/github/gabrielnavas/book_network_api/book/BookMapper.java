package io.github.gabrielnavas.book_network_api.book;

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
}
