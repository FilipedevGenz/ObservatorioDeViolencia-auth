package observatoriodeviolencia.msauth.controller;

import lombok.RequiredArgsConstructor;
import observatoriodeviolencia.msauth.dto.LoginRequest;
import observatoriodeviolencia.msauth.dto.TokenResponse;
import observatoriodeviolencia.msauth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}
