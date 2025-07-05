package com.camartin.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosConsulta(
        @JsonAlias("count") int cantidadResultados,
        @JsonAlias("results") List<DatosLibro> resultados
) {
}
