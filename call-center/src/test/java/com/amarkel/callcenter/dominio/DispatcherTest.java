package com.amarkel.callcenter.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.arielmarkel.callcenter.dominio.Dispatcher;
import com.arielmarkel.callcenter.dominio.Empleado;
import com.arielmarkel.callcenter.dominio.Llamada;

public class DispatcherTest {

    private static final int CANTIDAD_LLAMADAS = 10;

    private static final int DURACION_LLAMADA_MINIMA = 5;

    private static final int DURACION_LLAMADA_MAXIMA = 10;

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreaCionEmpleadosNull() {
        new Dispatcher(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreacionEstrategiaNull() {
        new Dispatcher(new ArrayList<>(), null);
    }

    @Test
    public void testDispatchLlamadaEmpleado() throws InterruptedException {
        List<Empleado> listaEmpleado = crearListaEmpleado();
        Dispatcher dispatcher = new Dispatcher(listaEmpleado);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(3);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(3);

        crearListaLlamada().stream().forEach(call -> {
            dispatcher.dispatch(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });

        executorService.awaitTermination(DURACION_LLAMADA_MAXIMA * 2, TimeUnit.SECONDS);
        assertEquals(CANTIDAD_LLAMADAS, listaEmpleado.stream().mapToInt(employee -> employee.getllamadasAtendidas().size()).sum());
    }

    private static List<Empleado> crearListaEmpleado() {
        Empleado operador1 = Empleado.crearOperador();
        Empleado operador2 = Empleado.crearOperador();
        Empleado operador3 = Empleado.crearOperador();
        Empleado operador4 = Empleado.crearOperador();
        Empleado operador5 = Empleado.crearOperador();
        Empleado operador6 = Empleado.crearOperador();
        Empleado supervisor1 = Empleado.crearSupervisor();
        Empleado supervisor2 = Empleado.crearSupervisor();
        Empleado supervisor3 = Empleado.crearSupervisor();
        Empleado director = Empleado.crearDirector();
        return Arrays.asList(operador1, operador2, operador3, operador4, operador5, operador6,
                supervisor1, supervisor2, supervisor3, director);
    }

    private static List<Llamada> crearListaLlamada() {
        return Llamada.crearListaLlamadasAleatoria(CANTIDAD_LLAMADAS, DURACION_LLAMADA_MINIMA, DURACION_LLAMADA_MAXIMA);
    }

}
