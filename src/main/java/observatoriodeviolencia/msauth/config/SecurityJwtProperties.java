package observatoriodeviolencia.msauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityJwtProperties {

    private String publicKey;
    private String privateKey;

}
