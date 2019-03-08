package com.amarkel.callcenter.dominio;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Modelado del objeto de dominio Llamada
 */
public class Llamada {

    private Integer duracionSegundos;

    /**
     * Crea una nueva Llamada con duracion en segundos
     *
     * @param duracionSegundos la duracion en segundos tiene que ser mayor a cinco y menor a 10 segundos
     */
    public Llamada(Integer duracionSegundos) {
        Validate.notNull(duracionSegundos);
        Validate.isTrue(duracionSegundos >= 5);
        Validate.isTrue(duracionSegundos <= 10);
        this.duracionSegundos = duracionSegundos;
    }

    public Integer getduracionSegundos() {
        return duracionSegundos;
    }

    /**
     * Crea una nueva Llamada
     *
     * @param duracionSegundosMin minima duracion en segundos tiene que ser igual o mayor a cero
     * @param duracionSegundosMax maxima duration en segundos tiene que ser igual o mayor a duracionSegundosMin
     * @return una nueva Llamada una duracion aleatoria entre la duracion minima y la duracion maxima
     */
    public static Llamada crearLlamadaAleatoria(Integer duracionSegundosMin, Integer duracionSegundosMax) {
        Validate.isTrue(duracionSegundosMax >= duracionSegundosMin && duracionSegundosMin >= 5);
        return new Llamada(ThreadLocalRandom.current().nextInt(duracionSegundosMin, duracionSegundosMax + 1));
    }

    /**
     * Crea una nueva lista de llamadas aleatorias
     *
     * @param tamanoLista tamano de  Llamadas aleatorias a ser creadas
     * @param duracionSegundosMin minima duracion en segundos tiene que ser igual o mayor a cero
     * @param duracionSegundosMax maxima duration en segundos tiene que ser igual o mayor a duracionSegundosMin
     * @return una nueva lista de llamadas aleatorias cada una con duracion aleatoria entre la duracion minima y la duracion maxima
     */
    public static List<Llamada> crearListaLlamadasAleatoria(Integer tamanoLista, Integer duracionSegundosMin, Integer duracionSegundosMax) {
        Validate.isTrue(tamanoLista >= 0);
        List<Llamada> LlamadaList = new ArrayList<>();
        for (int i = 0; i < tamanoLista; i++) {
            LlamadaList.add(crearLlamadaAleatoria(duracionSegundosMin, duracionSegundosMax));
        }
        return LlamadaList;
    }

}
