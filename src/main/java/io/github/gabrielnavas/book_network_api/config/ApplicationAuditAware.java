package io.github.gabrielnavas.book_network_api.config;

import io.github.gabrielnavas.book_network_api.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return Optional.empty();
        }
        
        User user = (User) authentication.getPrincipal();

        return Optional.of(user.getId());
    }
}
