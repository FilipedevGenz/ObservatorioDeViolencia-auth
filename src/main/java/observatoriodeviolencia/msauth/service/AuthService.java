package observatoriodeviolencia.msauth.service;

import lombok.RequiredArgsConstructor;
import observatoriodeviolencia.msauth.dto.LoginRequest;
import observatoriodeviolencia.msauth.dto.TokenResponse;
import observatoriodeviolencia.msauth.model.User;
import observatoriodeviolencia.msauth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService    tokenService;

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByMail(request.username())
                .filter(User::isActive)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return new TokenResponse(tokenService.generateToken(user));
    }
}