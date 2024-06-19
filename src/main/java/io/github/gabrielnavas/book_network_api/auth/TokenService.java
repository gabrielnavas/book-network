package io.github.gabrielnavas.book_network_api.auth;

import io.github.gabrielnavas.book_network_api.user.Token;
import io.github.gabrielnavas.book_network_api.user.TokenRepository;
import io.github.gabrielnavas.book_network_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Value("${application.security.register.token.characters-token}")
    private String charactersTokenCharacters;

    @Value("${application.security.register.token.expiration-in-minutes}")
    private long expirationInMinutes;

    public Token generateTokenAndSave(User user) {
        String generateToken = generateActivationToken(6);
        Token token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expirationInMinutes))
                .user(user)
                .build();
        return tokenRepository.save(token);
    }

    private String generateActivationToken(int length) {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(charactersTokenCharacters.length());
            char character = charactersTokenCharacters.charAt(randomIndex);
            codeBuilder.append(character);
        }
        return codeBuilder.toString();
    }
}
