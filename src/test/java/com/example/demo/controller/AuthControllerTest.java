package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setName("Test User");
    }

    @Test
    void testRegister_success() {
        // Arrange
        String email = "test@example.com";
        String name = "Test User";
        String password = "password123";
        Map<String, String> request = Map.of("email", email, "name", name, "password", password);

        when(authService.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(authService.register(eq(name), eq(email), eq("encodedPassword"))).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = authController.register(request);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("User registered successfully!", response.getBody());
    }

    @Test
    void testRegister_userAlreadyExists() {
        // Arrange
        String email = "test@example.com";
        String name = "Test User";
        String password = "password123";
        Map<String, String> request = Map.of("email", email, "name", name, "password", password);

        when(authService.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<?> response = authController.register(request);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email is already taken.", response.getBody());
    }

    @Test
    void testLogin_success() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        Map<String, String> request = Map.of("email", email, "password", password);

        when(authService.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(email)).thenReturn("fakeJwtToken");

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("fakeJwtToken"));
    }

    @Test
    void testLogin_userNotFound() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        Map<String, String> request = Map.of("email", email, "password", password);

        when(authService.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Connexion échouée", response.getBody());
    }


    @Test
    void testLogin_invalidPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        Map<String, String> request = Map.of("email", email, "password", password);

        when(authService.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Connexion échouée", response.getBody());
    }

    @Test
    void testGetUserById_success() {
        // Arrange
        Long userId = 1L;
        when(authService.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<?> response = authController.getUserById(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void testGetUserById_notFound() {
        // Arrange
        Long userId = 1L;
        when(authService.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.getUserById(userId));
    }

    @Test
    void testUpdateUser_success() {
        // Arrange
        Long userId = 1L;
        Map<String, String> request = Map.of("name", "Updated Name", "email", "updated@example.com", "password", "newpassword");
        when(authService.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        // Act
        ResponseEntity<?> response = authController.updateUser(userId, request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User updated successfully!", response.getBody());
    }

    @Test
    void testDeleteUser_success() {
        // Arrange
        Long userId = 1L;
        when(authService.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        ResponseEntity<?> response = authController.deleteUser(userId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User deleted successfully!", response.getBody());
    }
}
