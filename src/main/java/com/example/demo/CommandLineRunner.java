package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public CommandLineRunner(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Vérifie si un utilisateur avec un email spécifique existe déjà
        if (authService.findByEmail("admin@example.com").isEmpty()) {
            // Créer un utilisateur par défaut
            String name = "Admin";
            String email = "admin@example.com";
            String password = "admin123"; // Le mot de passe en clair, il sera encodé ensuite

            // Encoder le mot de passe avant de l'enregistrer
            String encodedPassword = passwordEncoder.encode(password);

            // Créer l'utilisateur avec toutes les propriétés
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(encodedPassword);

            // Enregistrer l'utilisateur
            authService.register(user);

            System.out.println("Utilisateur admin créé avec succès.");
        } else {
            System.out.println("L'utilisateur admin existe déjà.");
        }
    }
}
