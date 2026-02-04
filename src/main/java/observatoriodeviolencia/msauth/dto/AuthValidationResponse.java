package observatoriodeviolencia.msauth.dto;

import java.util.List;

public record AuthValidationResponse(
        boolean valid,
        String userId,
        List<String> roles
) {}