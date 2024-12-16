package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Méthode d'inscription
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");

        // Validation basique des entrées
        if (name == null || email == null || password == null) {
            return ResponseEntity.badRequest().body("Name, email, and password must be provided.");
        }

        // Vérifier si l'utilisateur existe déjà
        Optional<User> existingUser = authService.findByEmail(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.status(400).body("Email is already taken.");
        }

        // Crypter le mot de passe avant de le sauvegarder
        String encodedPassword = passwordEncoder.encode(password);

        // Créer l'utilisateur
        User user = authService.register(name, email, encodedPassword);
        return ResponseEntity.status(201).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        // Vérification si l'utilisateur existe
        User user = authService.findByEmail(email).orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            // Utilisateur introuvable ou mot de passe incorrect
            return ResponseEntity.status(401).body("Connexion échouée");
        }

        // Génération du token JWT
        String token = jwtUtil.generateToken(email);

        // Retour du token dans la réponse
        return ResponseEntity.ok(Map.of("token", token));
    }

    // Méthode pour obtenir un utilisateur par ID
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        // Chercher l'utilisateur par ID
        User user = authService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }

    // Méthode pour modifier un utilisateur
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> request) {
        // Chercher l'utilisateur par ID
        User user = authService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Mettre à jour les informations de l'utilisateur
        String newName = request.get("name");
        String newEmail = request.get("email");
        String newPassword = request.get("password");

        if (newName != null) user.setName(newName);
        if (newEmail != null) user.setEmail(newEmail);
        if (newPassword != null) {
            user.setPassword(passwordEncoder.encode(newPassword)); // Crypter le nouveau mot de passe
        }

        // Enregistrer les modifications
        authService.update(user);

        return ResponseEntity.ok("User updated successfully!");
    }

    // Méthode pour supprimer un utilisateur
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Chercher l'utilisateur par ID
        User user = authService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Supprimer l'utilisateur
        authService.delete(user);

        return ResponseEntity.ok("User deleted successfully!");
    }
}