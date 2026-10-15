// CAMADA: Controller
// RESPONSABILIDADE: expõe cadastro, consultas e colheita de plantios por DTOs.
// ROTAS ATENDIDAS: POST/GET /plantios, GET /plantios/{id}, PUT /plantios/{id}/colher.
// SITUAÇÃO: implementada.
package api.controller;

import java.net.URI;
import java.util.List;
import api.dto.ColheitaRequestDTO;
import api.dto.ErroDTO;
import api.dto.PlantioRequestDTO;
import api.dto.PlantioResponseDTO;
import api.service.PlantioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;

@RestController
@RequestMapping("/plantios")
@Tag(name = "Plantios", description = "Ocupação dos talhões, previsão e colheita")
public class PlantioController {
    private final PlantioService service;

    public PlantioController(PlantioService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Cadastrar plantio", description = "Aplica R1 (área), R2 (irrigação) e R3 (previsão)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Plantio criado", content = @Content(schema = @Schema(implementation = PlantioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Campo inválido ou regra violada", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Talhão ou cultura inexistente", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public ResponseEntity<PlantioResponseDTO> criar(@Valid @RequestBody PlantioRequestDTO dto) {
        PlantioResponseDTO resposta = service.criar(dto);
        return ResponseEntity.created(URI.create("/plantios/" + resposta.id())).body(resposta);
    }

    @GetMapping
    @Operation(summary = "Listar plantios por ID")
    @ApiResponse(responseCode = "200", description = "Lista de plantios", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlantioResponseDTO.class))))
    public List<PlantioResponseDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar plantio por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plantio encontrado", content = @Content(schema = @Schema(implementation = PlantioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Plantio inexistente", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public PlantioResponseDTO buscar(@PathVariable Long id) { return service.buscar(id); }

    @PutMapping("/{id}/colher")
    @Operation(summary = "Registrar colheita", description = "Libera a área e preserva a previsão original")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Colheita registrada", content = @Content(schema = @Schema(implementation = PlantioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Data inválida ou colheita repetida", content = @Content(schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Plantio inexistente", content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    public PlantioResponseDTO colher(@PathVariable Long id, @Valid @RequestBody ColheitaRequestDTO dto) {
        return service.colher(id, dto);
    }
}
