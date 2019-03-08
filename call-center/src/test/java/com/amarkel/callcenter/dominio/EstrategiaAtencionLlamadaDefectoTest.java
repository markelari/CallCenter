package com.amarkel.callcenter.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.amarkel.callcenter.dominio.Empleado;
import com.amarkel.callcenter.dominio.EmpleadoEstado;
import com.amarkel.callcenter.dominio.EmpleadoTipo;
import com.amarkel.callcenter.dominio.EstrategiaAtencionLlamada;
import com.amarkel.callcenter.dominio.EstrategiaAtencionLlamadaDefecto;

public class EstrategiaAtencionLlamadaDefectoTest {

    private EstrategiaAtencionLlamada estrategiaAtencionLlamada;

    public EstrategiaAtencionLlamadaDefectoTest() {
        this.estrategiaAtencionLlamada = new EstrategiaAtencionLlamadaDefecto();
    }

    @Test
    public void testAsignarOperador() {
        Empleado operador = Empleado.crearOperador();
        Empleado supervisor = Empleado.crearSupervisor();
        Empleado director = Empleado.crearDirector();
        List<Empleado> listaEmpleado = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtencionLlamada.buscarEmpleado(listaEmpleado);

        assertNotNull(empleado);
        assertEquals(EmpleadoTipo.OPERADOR, empleado.getEmpleadoTipo());
    }

    @Test
    public void testAsignarSupervisor() {
        Empleado operador = mock(Empleado.class);
        when(operador.getEmpleadoEstado()).thenReturn(EmpleadoEstado.OCUPADO);
        Empleado supervisor = Empleado.crearSupervisor();
        Empleado director = Empleado.crearDirector();
        List<Empleado> listaEmpleado = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtencionLlamada.buscarEmpleado(listaEmpleado);

        assertNotNull(empleado);
        assertEquals(EmpleadoTipo.SUPERVISOR, empleado.getEmpleadoTipo());
    }

    @Test
    public void testAsignarDirector() {
    	Empleado operador = mockearEmpleadoOcupado(EmpleadoTipo.OPERADOR);
    	Empleado supervisor = mockearEmpleadoOcupado(EmpleadoTipo.SUPERVISOR);
    	Empleado director = Empleado.crearDirector();
        List<Empleado> listaEmpleado = Arrays.asList(operador, supervisor, director);

        Empleado empleado = this.estrategiaAtencionLlamada.buscarEmpleado(listaEmpleado);

        assertNotNull(empleado);
        assertEquals(EmpleadoTipo.DIRECTOR, empleado.getEmpleadoTipo());
    }

    @Test
    public void testAsignarNadie() {
    	Empleado operator = mockearEmpleadoOcupado(EmpleadoTipo.OPERADOR);
    	Empleado supervisor = mockearEmpleadoOcupado(EmpleadoTipo.SUPERVISOR);
    	Empleado director = mockearEmpleadoOcupado(EmpleadoTipo.DIRECTOR);
        List<Empleado> listaEmpleado = Arrays.asList(operator, supervisor, director);

        Empleado empleado = this.estrategiaAtencionLlamada.buscarEmpleado(listaEmpleado);

        assertNull(empleado);
    }

    private static Empleado mockearEmpleadoOcupado(EmpleadoTipo empleadoTipo) {
    	Empleado empleado = mock(Empleado.class);
        when(empleado.getEmpleadoTipo()).thenReturn(empleadoTipo);
        when(empleado.getEmpleadoEstado()).thenReturn(EmpleadoEstado.OCUPADO);
        return empleado;
    }

}
