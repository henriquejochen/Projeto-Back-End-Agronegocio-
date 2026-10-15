// CAMADA: Controller
// RESPONSABILIDADE: expõe as operações de Plantio com validação e DTOs.
// SITUAÇÃO: implementada.
// ROTAS ATENDIDAS: POST /plantios e PUT /plantios/{id}/colher.
package api.controller;
import api.dto.*;
import api.service.PlantioService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
@Tag(name = "Plantios", description = "Registro de plantio e colheita")
@RestController
@RequestMapping("/plantios")
public class PlantioController {
    private final PlantioService service;
    public PlantioController(PlantioService service) { this.service = service; }
    @Operation(summary = "Registrar plantio", description = "Valida área e irrigação e calcula a previsão de colheita")
    @ApiResponse(responseCode = "201", description = "Plantio registrado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra violada")
    @ApiResponse(responseCode = "404", description = "Talhão ou cultura inexistente")
    @PostMapping
    public ResponseEntity<PlantioResponseDTO> criar(@Valid @RequestBody PlantioRequestDTO dto) {
        PlantioResponseDTO resposta = service.criar(dto);
        return ResponseEntity.created(URI.create("/plantios/" + resposta.id())).body(resposta);
    }
    @Operation(summary = "Registrar colheita e liberar área")
    @ApiResponse(responseCode = "200", description = "Colheita registrada")
    @ApiResponse(responseCode = "400", description = "Data inválida ou plantio já colhido")
    @ApiResponse(responseCode = "404", description = "Plantio inexistente")
    @PutMapping("/{id}/colher")
    public PlantioResponseDTO colher(@PathVariable Long id, @Valid @RequestBody ColheitaRequestDTO dto) {
        return service.colher(id, dto);
    }
}
