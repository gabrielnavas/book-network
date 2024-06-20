package io.github.gabrielnavas.book_network_api.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticateResponse {
    private String token;
}
