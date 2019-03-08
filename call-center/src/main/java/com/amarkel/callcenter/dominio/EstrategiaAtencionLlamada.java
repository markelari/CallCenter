package com.amarkel.callcenter.dominio;

import java.util.Collection;

/**
 * Interfaz que modela diferentes estrategias para que el proximo empleado disponible atienda la llamada
 */
public interface EstrategiaAtencionLlamada {

    /**
     * Busca el siguiente empleado disponible
     *
     * @param listaEmpleado Lista de los empleados que trabajan
     * @return Siguiente empleado disponible para tomar una llamada o null si todos los empleados estan ocupados
     */
    Empleado buscarEmpleado(Collection<Empleado> listaEmpleado);

}
