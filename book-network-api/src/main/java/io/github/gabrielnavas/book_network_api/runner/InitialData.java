package io.github.gabrielnavas.book_network_api.runner;

import io.github.gabrielnavas.book_network_api.role.Role;
import io.github.gabrielnavas.book_network_api.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialData implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        addRoles();
    }

    private void addRoles() {
        // all roles
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("USER").build());

        // try save all roles
        for (Role role : roles) {
            if (roleRepository.findByName(role.getName()).isEmpty()) {
                System.out.println("Adding role: " + role.getName());
                roleRepository.save(role);
            }
        }
    }
}
