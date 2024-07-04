package io.github.gabrielnavas.book_network_api.command;

import io.github.gabrielnavas.book_network_api.role.Role;
import io.github.gabrielnavas.book_network_api.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InitDataCommandRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Optional<Role> optionalRole = roleRepository.findByName("USER");
        if (optionalRole.isEmpty()) {
            Role role = Role.builder()
                    .name("USER")
                    .build();
            role = roleRepository.save(role);
            System.out.println("CREATED ROLE: " + role);
        }
    }
}
