package observatoriodeviolencia.msauth.client;

import observatoriodeviolencia.msauth.dto.AuthValidationRequest;
import observatoriodeviolencia.msauth.dto.AuthValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserAuthClient {

    private final WebClient webClient;

    public UserAuthClient(
            WebClient.Builder builder,
            @Value("${ms.user.base-url}") String baseUrl
    ) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public AuthValidationResponse validate(AuthValidationRequest req) {
        return webClient.post()
                .uri("/internal/auth/validate")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(AuthValidationResponse.class)
                .block();
    }
}
