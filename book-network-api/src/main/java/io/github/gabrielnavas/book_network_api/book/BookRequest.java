package io.github.gabrielnavas.book_network_api.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        @Size(min = 2, max = 100)
        String title,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        @Size(min = 2, max = 50)
        String authorName,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        @Size(min = 1, max = 50)
        String isbn,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        @Size(min = 10, max = 500)
        String synopsis,
        boolean shareable
) {
}
