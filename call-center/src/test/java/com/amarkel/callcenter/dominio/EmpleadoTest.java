package com.amarkel.callcenter.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.amarkel.callcenter.dominio.Empleado;
import com.amarkel.callcenter.dominio.EmpleadoEstado;
import com.amarkel.callcenter.dominio.EmpleadoTipo;
import com.amarkel.callcenter.dominio.Llamada;

public class EmpleadoTest {

    @Test(expected = NullPointerException.class)
    public void testEmpleadoNull() {
        new Empleado(null);
    }

    @Test
    public void testCreacionEmpleado() {
    	Empleado empleado = Empleado.crearOperador();

        assertNotNull(empleado);
        assertEquals(EmpleadoTipo.OPERADOR, empleado.getEmpleadoTipo());
        assertEquals(EmpleadoEstado.DISPONIBLE, empleado.getEmpleadoEstado());
    }

    @Test
    public void testEmpleadoAtiendeDisponible() throws InterruptedException {
        Empleado empleado = Empleado.crearOperador();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(empleado);
        empleado.attend(Llamada.crearLlamadaAleatoria(0, 1));

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(1, empleado.getllamadasAtendidas().size());
    }

    @Test
    public void testEstadoEmpleadoAlAtender() throws InterruptedException {
    	Empleado empleado = Empleado.crearOperador();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(empleado);
        assertEquals(EmpleadoEstado.DISPONIBLE, empleado.getEmpleadoEstado());
        TimeUnit.SECONDS.sleep(1);
        empleado.attend(Llamada.crearLlamadaAleatoria(2, 3));
        empleado.attend(Llamada.crearLlamadaAleatoria(0, 1));
        TimeUnit.SECONDS.sleep(1);
        assertEquals(EmpleadoEstado.OCUPADO, empleado.getEmpleadoEstado());

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertEquals(2, empleado.getllamadasAtendidas().size());
    }

}
