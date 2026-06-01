package com.example.recicla.Controller;

import com.example.recicla.Entity.Roteiro;
import com.example.recicla.Service.RoteiroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/roteiros")
public class RoteiroController {
    private final RoteiroService roteiroService;

    public RoteiroController(RoteiroService roteiroService) {
        this.roteiroService = roteiroService;
    }

    @PostMapping
    public ResponseEntity<EnvioRoteiroResponse> criarRoteiro(@Valid @RequestBody Roteiro roteiro) {
        int totalDestinatarios = roteiroService.enviarAlertas(roteiro);
        return ResponseEntity.ok(new EnvioRoteiroResponse("Roteiro recebido com sucesso.", totalDestinatarios));
    }

    public record EnvioRoteiroResponse(String mensagem, int totalDestinatarios) {
    }
}

