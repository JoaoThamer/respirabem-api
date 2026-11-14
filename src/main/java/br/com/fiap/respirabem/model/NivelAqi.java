package br.com.fiap.respirabem.model;

/**
 * Categorias do índice de qualidade do ar (US AQI), as mesmas usadas no app Flutter.
 */
public enum NivelAqi {
    BOA,
    MODERADA,
    SENSIVEIS,
    INSALUBRE,
    MUITO_INSALUBRE,
    PERIGOSA;

    public static NivelAqi deIndice(int indice) {
        if (indice <= 50) return BOA;
        if (indice <= 100) return MODERADA;
        if (indice <= 150) return SENSIVEIS;
        if (indice <= 200) return INSALUBRE;
        if (indice <= 300) return MUITO_INSALUBRE;
        return PERIGOSA;
    }
}
