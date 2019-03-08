package com.amarkel.callcenter.dominio;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.amarkel.callcenter.dominio.Llamada;

public class LlamadaTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreacionLlamaParametroIlegal() {
        new Llamada(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testCreacionLlamadaParametroNull() {
        new Llamada(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLlamadaaleatoriaPrimerParametroInvalido() {
        Llamada.crearLlamadaAleatoria(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLlamadaaleatoriaSegundoParametroInvalido() {
        Llamada.crearLlamadaAleatoria(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLlamadaaleatoriaInvalidoOrdenParametros() {
        Llamada.crearLlamadaAleatoria(2, 1);
    }

    @Test
    public void testLlamadaaleatoriaParametrosCorrectos() {
        Integer min = 5;
        Integer max = 10;
        Llamada llamada = Llamada.crearLlamadaAleatoria(min, max);

        assertNotNull(llamada);
        assertTrue(min <= llamada.getduracionSegundos());
        assertTrue(llamada.getduracionSegundos() <= max);
    }

}
