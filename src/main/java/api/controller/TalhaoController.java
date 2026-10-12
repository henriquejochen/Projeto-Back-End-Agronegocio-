package api.controller;

import java.net.URI;
import java.util.List;
import api.dto.TalhaoRequestDTO;
import api.dto.TalhaoResponseDTO;
import api.service.TalhaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller: expõe cadastro, listagem e busca de talhões por DTOs. Implementado.
@RestController
@RequestMapping("/talhoes")
public class TalhaoController {
    private final TalhaoService service;

    public TalhaoController(TalhaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TalhaoResponseDTO> criar(@Valid @RequestBody TalhaoRequestDTO dto) {
        TalhaoResponseDTO resposta = service.criar(dto);
        return ResponseEntity.created(URI.create("/talhoes/" + resposta.id())).body(resposta);
    }

    @GetMapping
    public List<TalhaoResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public TalhaoResponseDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
