package br.com.fiap.respirabem;

import br.com.fiap.respirabem.model.NivelAqi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NivelAqiTest {

    @Test
    void classificaFaixasDoAqi() {
        assertEquals(NivelAqi.BOA, NivelAqi.deIndice(0));
        assertEquals(NivelAqi.BOA, NivelAqi.deIndice(50));
        assertEquals(NivelAqi.MODERADA, NivelAqi.deIndice(51));
        assertEquals(NivelAqi.SENSIVEIS, NivelAqi.deIndice(150));
        assertEquals(NivelAqi.INSALUBRE, NivelAqi.deIndice(151));
        assertEquals(NivelAqi.MUITO_INSALUBRE, NivelAqi.deIndice(300));
        assertEquals(NivelAqi.PERIGOSA, NivelAqi.deIndice(301));
    }
}
