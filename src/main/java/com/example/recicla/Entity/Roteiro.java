package com.example.recicla.Entity;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Roteiro {

    @NotBlank(message = "Área é obrigatória")
    @Size(max = 160, message = "Área deve ter no máximo 160 caracteres")
    private String area;

    @NotNull(message = "Data e hora são obrigatórias")
    @FutureOrPresent(message = "Data e hora não podem estar no passado")
    private LocalDateTime dataEHora;

    @NotBlank(message = "Tipo de lixo é obrigatório")
    @Size(max = 40, message = "Tipo de lixo deve ter no máximo 40 caracteres")
    private String tipoLixo;

    @Size(max = 1000, message = "Mensagem deve ter no máximo 1000 caracteres")
    private String mensagem;

}

