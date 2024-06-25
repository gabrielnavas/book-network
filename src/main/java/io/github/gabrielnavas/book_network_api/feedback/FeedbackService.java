package io.github.gabrielnavas.book_network_api.feedback;

import io.github.gabrielnavas.book_network_api.book.Book;
import io.github.gabrielnavas.book_network_api.book.BookRepository;
import io.github.gabrielnavas.book_network_api.common.PageResponse;
import io.github.gabrielnavas.book_network_api.exception.OperationNotPermittedException;
import io.github.gabrielnavas.book_network_api.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer saveFeedback(FeedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("No book found with the ID: %d", request.bookId()))
                );
        boolean bookIsArchived = book.isArchived();
        if (bookIsArchived) {
            throw new OperationNotPermittedException("You cannot give a feedback for an archived book");
        }
        boolean bookIsNotShareable = !book.isShareable();
        if (bookIsNotShareable) {
            throw new OperationNotPermittedException("You cannot give a feedback for a not shareable book");
        }

        User user = ((User) connectedUser.getPrincipal());
        boolean isBookOwnerCorrect = Objects.equals(book.getOwner().getId(), user.getId());
        if (isBookOwnerCorrect) {
            throw new OperationNotPermittedException("You cannot give a feedback to your own book");
        }

        Feedback feedback = feedbackMapper.toFeedback(request);
        feedback = feedbackRepository.save(feedback);
        return feedback.getId();

    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(
            Integer bookId,
            int page,
            int size,
            Authentication connectedUser
    ) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());

        Page<Feedback> pages = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = pages.map(
                feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId())
        ).toList();

        return new PageResponse<>(
                feedbackResponses,
                pages.getNumber(),
                pages.getSize(),
                Long.valueOf(pages.getTotalElements()).intValue(),
                pages.getTotalPages(),
                pages.isFirst(),
                pages.isLast()
        );
    }
}
