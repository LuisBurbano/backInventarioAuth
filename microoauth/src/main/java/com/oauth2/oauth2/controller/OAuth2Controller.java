package com.oauth2.oauth2.controller;


import com.oauth2.oauth2.dto.AuthRequest;
import com.oauth2.oauth2.dto.ValidateResponse;
import com.oauth2.oauth2.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OAuth2Controller {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Datos enviados: " + authRequest);

            // Llamar al microservicio de usuarios para validar credenciales
            ResponseEntity<ValidateResponse> response = restTemplate.postForEntity(
                    "http://localhost:8001/api/users/validate",
                    authRequest,
                    ValidateResponse.class
            );
            System.out.println("Respuesta recibida: " + response.getBody());
            System.out.println("Role object: " + (response.getBody() != null ? response.getBody().getRole() : "null"));
            System.out.println("Respuesta recibida: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                ValidateResponse responseBody = response.getBody();
                if (responseBody != null && responseBody.getRole() != null) {
                    // Extraer el nombre del rol
                    String roleName = responseBody.getRole().getName();

                    // Si las credenciales son válidas, generar token
                    String token = tokenService.generateToken(
                            authRequest.getEmail(),
                            roleName
                    );

                    return ResponseEntity.ok(Map.of("token", token));
                }
            }

            return ResponseEntity.badRequest().body("Credenciales inválidas");
        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("Error de autenticación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de autenticación");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        if (tokenService.validateToken(token)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }
}
