package observatoriodeviolencia.msauth.dto;

public record AuthValidationRequest(
        String email,
        String password
) {}