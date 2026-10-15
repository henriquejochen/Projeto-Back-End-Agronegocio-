// CAMADA: Configuração
// RESPONSABILIDADE: Configura a identificação inicial da API na documentação OpenAPI.
// SITUAÇÃO: implementada.
package api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiDoProjeto() {
        return new OpenAPI().info(new Info()
                .title("Controle de Plantios")
                .version("0.1.0")
                .description("Cadastro e consulta de talhões e culturas. Plantio ainda está planejado."));
    }
}
