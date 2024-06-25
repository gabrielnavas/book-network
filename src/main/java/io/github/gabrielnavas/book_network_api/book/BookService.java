package io.github.gabrielnavas.book_network_api.book;

import io.github.gabrielnavas.book_network_api.common.PageResponse;
import io.github.gabrielnavas.book_network_api.exception.OperationNotPermittedException;
import io.github.gabrielnavas.book_network_api.history.BookTransactionHistory;
import io.github.gabrielnavas.book_network_api.history.BookTransactionHistoryRepository;
import io.github.gabrielnavas.book_network_api.history.BorrowedBooksResponse;
import io.github.gabrielnavas.book_network_api.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;

    public Integer saveBook(BookRequest request, Authentication connectedUser) {
        bookRepository.findByTitle(request.title()).ifPresent(book -> {
            final String message = String.format("already exists a book with name: %s", request.title());
            throw new OperationNotPermittedException(message);
        });

        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        book = bookRepository.save(book);
        return book.getId();
    }

    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with id: %d", bookId))
                );
    }

    public PageResponse<BookResponse> findAllBooks(
            int page,
            int size,
            Authentication connectedUser
    ) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                Integer.parseInt(String.format("%d", books.getTotalElements())),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(
            int page,
            int size,
            Authentication connectedUser
    ) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );
        Integer userId = user.getId();
        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(userId), pageable);
//        Page<Book> books = bookRepository.findAllBooksByOwner(pageable, userId);

        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                Integer.parseInt(String.format("%d", books.getTotalElements())),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBooksResponse> findAllBorrowedBooks(
            int page,
            int size,
            Authentication connectedUser
    ) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<BookTransactionHistory> allBorrowedBooks =
                bookTransactionHistoryRepository.findAllBorrowedBooks(
                        pageable,
                        user.getId()
                );
        List<BorrowedBooksResponse> borrowedBooksResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBooksResponse)
                .toList();

        return new PageResponse<>(
                borrowedBooksResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                Integer.parseInt(String.format("%d", allBorrowedBooks.getTotalElements())),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBooksResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<BookTransactionHistory> allBorrowedBooks =
                bookTransactionHistoryRepository.findAllReturnedBooks(
                        pageable,
                        user.getId()
                );
        List<BorrowedBooksResponse> borrowedBooksResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBooksResponse)
                .toList();

        return new PageResponse<>(
                borrowedBooksResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                Integer.parseInt(String.format("%d", allBorrowedBooks.getTotalElements())),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with the ID: %d", bookId))
                );
        User user = ((User) connectedUser.getPrincipal());

        boolean isBookOwnerCorrect = Objects.equals(book.getOwner().getId(), user.getId());
        if (!isBookOwnerCorrect) {
            throw new OperationNotPermittedException("You cannot update others books shareable status");
        }

        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with the ID: %d", bookId))
                );
        User user = ((User) connectedUser.getPrincipal());

        boolean isBookOwnerCorrect = Objects.equals(book.getOwner().getId(), user.getId());
        if (!isBookOwnerCorrect) {
            throw new OperationNotPermittedException("You cannot update others books archived status");
        }

        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with the ID: %d", bookId))
                );
        User user = ((User) connectedUser.getPrincipal());

        boolean isBookOwnerCorrect = Objects.equals(book.getOwner().getId(), user.getId());
        if (isBookOwnerCorrect) {
            throw new OperationNotPermittedException("The request book cannot be borrowed your own book");
        }

        boolean alreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if (alreadyBorrowed) {
            throw new OperationNotPermittedException(
                    "The request book is already borrowed by own book"
            );
        }

        boolean bookIsArchived = book.isArchived();
        if (bookIsArchived) {
            throw new OperationNotPermittedException("The request book cannot be borrowed because it is archived");
        }
        boolean bookIsNotShareable = !book.isShareable();
        if (bookIsNotShareable) {
            throw new OperationNotPermittedException("The request book cannot be borrowed because it is not shareable");
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .book(book)
                .user(user)
                .returned(false)
                .returnedApproved(false)
                .build();

        bookTransactionHistory = bookTransactionHistoryRepository.save(bookTransactionHistory);
        return bookTransactionHistory.getId();
    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with the ID: %d", bookId))
                );
        boolean bookIsArchived = book.isArchived();
        if (bookIsArchived) {
            throw new OperationNotPermittedException("The request book cannot be borrowed because it is archived");
        }
        boolean bookIsNotShareable = !book.isShareable();
        if (bookIsNotShareable) {
            throw new OperationNotPermittedException("The request book cannot be borrowed because it is not shareable");
        }

        User user = ((User) connectedUser.getPrincipal());
        boolean isBookOwnerCorrect = Objects.equals(book.getOwner().getId(), user.getId());
        if (isBookOwnerCorrect) {
            throw new OperationNotPermittedException("The request book cannot be borrowed or return your own book");
        }

        BookTransactionHistory history = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(
                        () -> new OperationNotPermittedException("You did not borrow this book")
                );
        history.setReturned(true);
        bookTransactionHistoryRepository.save(history);

        return history.getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with the ID: %d", bookId))
                );
        boolean bookIsArchived = book.isArchived();
        if (bookIsArchived) {
            throw new OperationNotPermittedException("The request book cannot be borrowed because it is archived");
        }
        boolean bookIsNotShareable = !book.isShareable();
        if (bookIsNotShareable) {
            throw new OperationNotPermittedException("The request book cannot be borrowed because it is not shareable");
        }

        User user = ((User) connectedUser.getPrincipal());
        boolean isNotBookOwnerCorrect = !Objects.equals(book.getOwner().getId(), user.getId());
        if (isNotBookOwnerCorrect) {
            throw new OperationNotPermittedException("The request book cannot be borrowed or return your own book");
        }

        BookTransactionHistory history = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(
                        () -> new OperationNotPermittedException(
                                "The book has not been returned yet, or the transaction was not found. You cannot approve the return of the book"
                        )
                );
        history.setReturnedApproved(true);
        bookTransactionHistoryRepository.save(history);

        return history.getId();
    }
}
