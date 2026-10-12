package api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configura a identificação inicial da API na documentação OpenAPI.
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiDoProjeto() {
        return new OpenAPI().info(new Info()
                .title("Controle de Plantios")
                .version("0.1.0")
                .description("Cadastro de talhões. Cultura e Plantio serão adicionados pela equipe."));
    }
}
