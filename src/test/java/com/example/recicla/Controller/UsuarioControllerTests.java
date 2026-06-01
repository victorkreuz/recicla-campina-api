package com.example.recicla.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCadastrarUsuarioValido() throws Exception {
        String body = """
                {
                  "nome": "Victor",
                  "email": "victor@example.com",
                  "endereco": "Bairro Centro"
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Victor"))
                .andExpect(jsonPath("$.email").value("victor@example.com"))
                .andExpect(jsonPath("$.endereco").value("Bairro Centro"));
    }

    @Test
    void deveRecusarUsuarioInvalido() throws Exception {
        String body = """
                {
                  "nome": "",
                  "email": "email-invalido",
                  "endereco": ""
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Dados inválidos"))
                .andExpect(jsonPath("$.campos[*].campo", hasItem("nome")))
                .andExpect(jsonPath("$.campos[*].campo", hasItem("email")))
                .andExpect(jsonPath("$.campos[*].campo", hasItem("endereco")));
    }
}
