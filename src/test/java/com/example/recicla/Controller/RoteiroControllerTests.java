package com.example.recicla.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoteiroControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveReceberRoteiroSemDestinatarios() throws Exception {
        String dataEHora = LocalDateTime.now().plusDays(1).withNano(0).toString();
        String body = """
                {
                  "area": "Área sem usuários",
                  "dataEHora": "%s",
                  "tipoLixo": "seco",
                  "mensagem": ""
                }
                """.formatted(dataEHora);

        mockMvc.perform(post("/api/roteiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Roteiro recebido com sucesso."))
                .andExpect(jsonPath("$.totalDestinatarios").value(0));
    }
}
