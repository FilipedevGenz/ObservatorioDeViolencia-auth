package observatoriodeviolencia.msauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import observatoriodeviolencia.msauth.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {

    public record CreateUserRequest(
            @NotBlank(message = "Nome é obrigatório")
            @Size(max = 100)
            String name,

            @NotBlank(message = "E-mail é obrigatório")
            @Email(message = "E-mail inválido")
            String email,

            @NotBlank(message = "Senha é obrigatória")
            @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
            String password
    ) {}

    public record UpdateUserRequest(
            @Size(max = 100)
            String name,

            @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
            String password
    ) {}

    public record AdminUpdateUserRequest(
            String name,
            User.Role role,
            Boolean active
    ) {}

    public record UserResponse(
            UUID id,
            String name,
            String email,
            User.Role role,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static UserResponse from(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.isActive(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        }
    }
}