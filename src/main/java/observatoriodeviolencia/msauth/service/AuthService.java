package observatoriodeviolencia.msauth.service;

import lombok.RequiredArgsConstructor;
import observatoriodeviolencia.msauth.client.UserAuthClient;
import observatoriodeviolencia.msauth.dto.AuthValidationRequest;
import observatoriodeviolencia.msauth.dto.AuthValidationResponse;
import observatoriodeviolencia.msauth.dto.LoginRequest;
import observatoriodeviolencia.msauth.dto.TokenResponse;
import observatoriodeviolencia.msauth.exceptions.InvalidCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAuthClient userAuthClient;
    private final TokenService tokenService;

    public TokenResponse login(LoginRequest request) {

        AuthValidationResponse auth =
                userAuthClient.validate(
                        new AuthValidationRequest(
                                request.username(),
                                request.password()
                        )
                );

        if (!auth.valid()) {
            throw new InvalidCredentialsException();
        }

        String token = tokenService.generateToken(
                auth.userId(),
                auth.roles()
        );

        return new TokenResponse(token);
    }
}
