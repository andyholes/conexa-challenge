package ai.conexa.challenge.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
@Slf4j
public class SwaggerConfig {
    @Value("${deployment.url}")
    private String deploymentUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CONEXA CHALLENGE API")
                        .description("Backend service whose API allows consuming the different endpoints of https://www.swapi.tech/")
                        .contact(new Contact()
                                .name("Andres Hoyos Garcia")
                                .url("https://www.linkedin.com/in/andreshoyosgarcia")
                                .email("andyholesdev@gmail.com")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(AUTHORIZATION)
                        )
                )
                .servers(Collections.singletonList(new Server().url(deploymentUrl)));
    }

    @PostConstruct
    public void init() {
        log.info("Swagger ready to access at: " + deploymentUrl + "/swagger-ui/index.html");
    }
}
