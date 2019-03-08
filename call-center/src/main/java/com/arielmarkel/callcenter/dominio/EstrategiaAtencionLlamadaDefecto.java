package com.arielmarkel.callcenter.dominio;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EstrategiaAtencionLlamadaDefecto
 * <p>
 * Esta estrategia devuelve el primer empleado disponible.
 * Si todos los empleados operadores estan ocupados, devuelve el primer supervisor disponible.
 * Si todos los supervisores estan ocupados, devuelve el director disponible.
 * Si todos los empleados estan ocupados, devuelve null.
 */
public class EstrategiaAtencionLlamadaDefecto implements EstrategiaAtencionLlamada {

    private static final Logger logger = LoggerFactory.getLogger(EstrategiaAtencionLlamadaDefecto.class);

    @Override
    public Empleado buscarEmpleado(Collection<Empleado> listaEmpleado) {
        Validate.notNull(listaEmpleado);
        List<Empleado> empleadosDisponibles = listaEmpleado.stream().filter(e -> e.getEmpleadoEstado() == EmpleadoEstado.DISPONIBLE).collect(Collectors.toList());
        logger.info("Empleados disponibles: " + empleadosDisponibles.size());
        Optional<Empleado> empleado = empleadosDisponibles.stream().filter(e -> e.getEmpleadoTipo() == EmpleadoTipo.OPERADOR).findAny();
        if (!empleado.isPresent()) {
            logger.info("No se encontraron oepradores disponibles");
            empleado = empleadosDisponibles.stream().filter(e -> e.getEmpleadoTipo() == EmpleadoTipo.SUPERVISOR).findAny();
            if (!empleado.isPresent()) {
                logger.info("No se encontraron supervisores disponibles");
                empleado = empleadosDisponibles.stream().filter(e -> e.getEmpleadoTipo() == EmpleadoTipo.DIRECTOR).findAny();
                if (!empleado.isPresent()) {
                    logger.info("No se encontraron directores disponibles");
                    return null;
                }
            }
        }
        logger.info("No se encontraron Empleados de tipo " + empleado.get().getEmpleadoTipo());
        return empleado.get();
    }

}
