// CAMADA: Controller
// RESPONSABILIDADE: expõe cadastro e consultas de culturas usando DTOs.
// ROTAS ATENDIDAS: /culturas, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.controller;

import api.dto.ErroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.util.List;

import api.dto.CulturaRequestDTO;
import api.dto.CulturaResponseDTO;
import api.service.CulturaService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Culturas", description = "Cadastro e consultas de culturas")
@RestController
@RequestMapping("/culturas")
public class CulturaController {

    private final CulturaService service;

    public CulturaController(CulturaService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar cultura")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cadastro realizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CulturaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @PostMapping
    public ResponseEntity<CulturaResponseDTO> criar(
            @Valid @RequestBody CulturaRequestDTO dto
    ) {
        CulturaResponseDTO resposta = service.criar(dto);

        return ResponseEntity
                .created(URI.create("/culturas/" + resposta.id()))
                .body(resposta);
    }

    @Operation(summary = "Listar culturas por ID")
    @ApiResponse(responseCode = "200", description = "Lista de cadastros, possivelmente vazia",
        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CulturaResponseDTO.class))))
    @GetMapping
    public List<CulturaResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Buscar cultura por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cadastro encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CulturaResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "ID com formato inválido",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cadastro não encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroDTO.class)))
    })
    @GetMapping("/{id}")
    public CulturaResponseDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}