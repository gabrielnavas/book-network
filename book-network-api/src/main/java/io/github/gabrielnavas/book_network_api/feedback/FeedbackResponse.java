package io.github.gabrielnavas.book_network_api.feedback;

import lombok.Builder;

@Builder
public record FeedbackResponse(
        Double note,
        String comment,
        boolean ownFeedback
) {
}
