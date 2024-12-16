package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Encode un mot de passe brut.
     * @param rawPassword le mot de passe brut
     * @return le mot de passe encodé
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Enregistre un nouvel utilisateur.
     * @param name le nom de l'utilisateur
     * @param email l'email de l'utilisateur
     * @param password le mot de passe brut
     * @return l'utilisateur enregistré
     */
    public User register(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    /**
     * Recherche un utilisateur par email.
     * @param email l'email de l'utilisateur
     * @return un Optional contenant l'utilisateur, ou vide s'il n'existe pas
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void register(User user) {
        userRepository.save(user);  // Sauvegarde l'utilisateur dans la base de données
    }

    // Méthode pour trouver un utilisateur par ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Méthode pour mettre à jour un utilisateur
    public User update(User user) {
        return userRepository.save(user);
    }

    // Méthode pour supprimer un utilisateur
    public void delete(User user) {
        userRepository.delete(user);
    }


}
