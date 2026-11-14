package br.com.fiap.respirabem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudApiIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    private long criar(String url, String corpo) throws Exception {
        MvcResult r = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isCreated()).andReturn();
        JsonNode n = json.readTree(r.getResponse().getContentAsString());
        return n.get("id").asLong();
    }

    @Test
    void fluxoCompletoCidadeLeituraGrupoRecomendacao() throws Exception {
        long cidadeId = criar("/api/v1/cidades",
                "{\"nome\":\"Campinas\",\"estado\":\"sp\",\"latitude\":-22.9,\"longitude\":-47.06,\"populacao\":1200000}");

        mvc.perform(get("/api/v1/cidades/" + cidadeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("SP"));

        // leitura: nível derivado do índice
        long leituraId = criar("/api/v1/leituras",
                "{\"cidadeId\":" + cidadeId + ",\"indiceAqi\":120,\"pm25\":40.5,\"dataHoraMedicao\":\"2026-01-10T10:00:00\"}");
        mvc.perform(get("/api/v1/leituras/" + leituraId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nivelAqi").value("SENSIVEIS"))
                .andExpect(jsonPath("$.cidadeNome").value("Campinas"));

        mvc.perform(put("/api/v1/leituras/" + leituraId).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cidadeId\":" + cidadeId + ",\"indiceAqi\":30,\"dataHoraMedicao\":\"2026-01-10T11:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nivelAqi").value("BOA"));

        long grupoId = criar("/api/v1/grupos-sensiveis",
                "{\"nome\":\"Crianças\",\"descricao\":\"Respiram mais rápido\",\"ativo\":true}");
        long recId = criar("/api/v1/recomendacoes",
                "{\"grupoId\":" + grupoId + ",\"nivelAqi\":\"SENSIVEIS\",\"titulo\":\"Evite esforço\","
                        + "\"descricao\":\"Prefira ambientes internos\",\"prioridade\":1,\"ativa\":true}");
        mvc.perform(get("/api/v1/recomendacoes?grupoId=" + grupoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(recId));

        mvc.perform(delete("/api/v1/recomendacoes/" + recId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/recomendacoes/" + recId)).andExpect(status().isNotFound());
        mvc.perform(delete("/api/v1/leituras/" + leituraId)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/v1/cidades/" + cidadeId)).andExpect(status().isNoContent());
    }

    @Test
    void validacaoRetorna400ComCorpoPadronizado() throws Exception {
        mvc.perform(post("/api/v1/cidades").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detalhes").isArray())
                .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test
    void recursoInexistenteRetorna404() throws Exception {
        mvc.perform(get("/api/v1/cidades/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
        mvc.perform(delete("/api/v1/grupos-sensiveis/999999")).andExpect(status().isNotFound());
    }

    @Test
    void cidadeDuplicadaRetorna409() throws Exception {
        String corpo = "{\"nome\":\"Santos\",\"estado\":\"SP\",\"latitude\":-23.9,\"longitude\":-46.3}";
        criar("/api/v1/cidades", corpo);
        mvc.perform(post("/api/v1/cidades").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isConflict());
    }

    @Test
    void leituraComCidadeInexistenteRetorna404() throws Exception {
        mvc.perform(post("/api/v1/leituras").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cidadeId\":999999,\"indiceAqi\":10,\"dataHoraMedicao\":\"2026-01-10T10:00:00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoAtivaRecomendacaoParaGrupoInativo() throws Exception {
        long grupoId = criar("/api/v1/grupos-sensiveis",
                "{\"nome\":\"Grupo inativo\",\"descricao\":\"teste\",\"ativo\":false}");
        mvc.perform(post("/api/v1/recomendacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"grupoId\":" + grupoId + ",\"nivelAqi\":\"BOA\",\"titulo\":\"t\","
                                + "\"descricao\":\"d\",\"prioridade\":2,\"ativa\":true}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void raizRedirecionaParaSwagger() throws Exception {
        MvcResult r = mvc.perform(get("/")).andExpect(status().is3xxRedirection()).andReturn();
        assertEquals("/swagger-ui/index.html", r.getResponse().getRedirectedUrl());
    }
}
