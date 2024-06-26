package io.github.gabrielnavas.book_network_api.auth;

import io.github.gabrielnavas.book_network_api.email.EmailService;
import io.github.gabrielnavas.book_network_api.email.EmailTemplate;
import io.github.gabrielnavas.book_network_api.exception.OperationNotPermittedException;
import io.github.gabrielnavas.book_network_api.role.Role;
import io.github.gabrielnavas.book_network_api.role.RoleRepository;
import io.github.gabrielnavas.book_network_api.security.JwtService;
import io.github.gabrielnavas.book_network_api.user.Token;
import io.github.gabrielnavas.book_network_api.user.TokenRepository;
import io.github.gabrielnavas.book_network_api.user.User;
import io.github.gabrielnavas.book_network_api.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    @Value("${application.security.mailing.frontend.activation-url}")
    private String activationEmail;

    public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(), authenticateRequest.getPassword())
        );
        final HashMap<String, Object> claims = new HashMap<>();
        final User user = (User) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        String jwtToken = jwtService.generateToken(claims, user);
        return AuthenticateResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void register(RegistrationRequest request) throws MessagingException, IOException {
        Role userRole = roleRepository.findByName("USER")
                // TODO: better exception handling
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER was not initialized"));

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new OperationNotPermittedException("user already exists with email: " + request.getEmail());
        });

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        user = userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException, IOException {
        Token token = tokenService.generateTokenAndSave(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplate.ACTIVATE_ACCOUNT,
                activationEmail,
                token.getToken(),
                "AccountActivation"
        );
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException, IOException {
        Token savedToken = fetchToken(token);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

        User user = userRepository.findByEmail(savedToken.getUser().getEmail())
                .orElseThrow(() -> new OperationNotPermittedException("user not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    private Token fetchToken(String token) throws MessagingException, IOException {
        Token savedToken = tokenRepository.findByToken(token)
                // TODO: exception has to be defined
                .orElseThrow(() -> new RuntimeException("Invalid"));

        LocalDateTime now = LocalDateTime.now();
        boolean tokenExpired = now.isAfter(savedToken.getExpiresAt());
        if (tokenExpired) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Token is expired");
        }

        boolean tokenAlreadyValidate = savedToken.getValidatedAt() != null;
        if (tokenAlreadyValidate) {
            throw new RuntimeException("Token already validated");
        }
        return savedToken;
    }
}
