package br.com.fiap.respirabem.config;

import br.com.fiap.respirabem.dto.request.LeituraArRequest;
import br.com.fiap.respirabem.dto.request.RecomendacaoRequest;
import br.com.fiap.respirabem.dto.response.LeituraArResponse;
import br.com.fiap.respirabem.dto.response.RecomendacaoResponse;
import br.com.fiap.respirabem.model.LeituraAr;
import br.com.fiap.respirabem.model.Recomendacao;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Relações (cidade/grupo) são atribuídas explicitamente no Service.
        mapper.typeMap(LeituraArRequest.class, LeituraAr.class)
                .addMappings(m -> m.skip(LeituraAr::setCidade));
        mapper.typeMap(RecomendacaoRequest.class, Recomendacao.class)
                .addMappings(m -> m.skip(Recomendacao::setGrupo));

        mapper.typeMap(LeituraAr.class, LeituraArResponse.class).addMappings(m -> {
            m.map(src -> src.getCidade().getId(), LeituraArResponse::setCidadeId);
            m.map(src -> src.getCidade().getNome(), LeituraArResponse::setCidadeNome);
        });
        mapper.typeMap(Recomendacao.class, RecomendacaoResponse.class).addMappings(m -> {
            m.map(src -> src.getGrupo().getId(), RecomendacaoResponse::setGrupoId);
            m.map(src -> src.getGrupo().getNome(), RecomendacaoResponse::setGrupoNome);
        });
        return mapper;
    }
}
