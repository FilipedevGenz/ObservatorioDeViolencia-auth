package observatoriodeviolencia.msauth.service;

import lombok.RequiredArgsConstructor;
import observatoriodeviolencia.msauth.UserRepository;
import observatoriodeviolencia.msauth.dto.UserDTO.*;

import observatoriodeviolencia.msauth.exceptions.EmailAlreadyExistsException;
import observatoriodeviolencia.msauth.exceptions.ResourceNotFoundException;
import observatoriodeviolencia.msauth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.USER)
                .build();

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UUID id) {
        return UserResponse.from(findUserOrThrow(id));
    }

    @Transactional(readOnly = true)
    public UserResponse getMyProfile() {
        return UserResponse.from(getAuthenticatedUser());
    }

    @Transactional
    public UserResponse updateMyProfile(UpdateUserRequest request) {
        User user = getAuthenticatedUser();

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse adminUpdateUser(UUID id, AdminUpdateUserRequest request) {
        User user = findUserOrThrow(id);

        if (request.name() != null)   user.setName(request.name());
        if (request.role() != null)   user.setRole(request.role());
        if (request.active() != null) user.setActive(request.active());

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteMyAccount() {
        User user = getAuthenticatedUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void adminDeleteUser(UUID id) {
        userRepository.delete(findUserOrThrow(id));
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private User findUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }

    // subject do JWT é o e-mail — definido no AuthService.generateToken()
    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário autenticado não encontrado"));
    }
}