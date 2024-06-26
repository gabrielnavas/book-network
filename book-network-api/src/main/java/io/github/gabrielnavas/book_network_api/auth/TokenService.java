package io.github.gabrielnavas.book_network_api.auth;

import io.github.gabrielnavas.book_network_api.user.Token;
import io.github.gabrielnavas.book_network_api.user.TokenRepository;
import io.github.gabrielnavas.book_network_api.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Value("${application.security.register.token.characters-token}")
    private String charactersTokenCharacters;
    @Value("${application.security.register.token.expiration-in-minutes}")
    private long expirationInMinutes;
    @Value("${application.security.register.token.token-length}")
    private long tokenLength;

    @Transactional
    public Token generateTokenAndSave(User user) {
        String generateToken = tryGenerateToken();
        Token token = Token.builder()
                .token(generateToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expirationInMinutes))
                .user(user)
                .build();
        return tokenRepository.save(token);
    }


    private String tryGenerateToken() {
        String generatedToken;
        Optional<Token> optionalToken;
        int attemps = 10;

        do {
            attemps--;
            generatedToken = generateActivationToken();
            optionalToken = tokenRepository.findByToken(generatedToken);
            if (optionalToken.isEmpty()) {
                break;
            }
        } while (attemps > 0);

        if (attemps > 0) {
            return generatedToken;
        } else {
            throw new RuntimeException("Number of tokens already exceeded");
        }
    }

    private String generateActivationToken() {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < tokenLength; i++) {
            int randomIndex = secureRandom.nextInt(charactersTokenCharacters.length());
            char character = charactersTokenCharacters.charAt(randomIndex);
            codeBuilder.append(character);
        }
        return codeBuilder.toString();
    }
}
