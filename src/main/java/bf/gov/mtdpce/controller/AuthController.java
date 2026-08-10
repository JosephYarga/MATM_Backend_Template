package bf.gov.mtdpce.controller;

import bf.gov.mtdpce.dto.ApiResponse;
import bf.gov.mtdpce.dto.auth.JwtResponse;
import bf.gov.mtdpce.dto.auth.LoginRequest;
import bf.gov.mtdpce.dto.auth.RegisterRequest;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "API d'authentification et d'inscription")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Connexion réussie", jwtResponse));
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription utilisateur", description = "Crée un nouveau compte utilisateur")
    public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = authService.registerUser(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Utilisateur enregistré avec succès", user.getUsername()));
    }
}
