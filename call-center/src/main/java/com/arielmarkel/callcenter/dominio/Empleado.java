package com.arielmarkel.callcenter.dominio;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * Modelo de Empleado
 */
public class Empleado implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Empleado.class);

    private EmpleadoTipo empleadoTipo;

    private EmpleadoEstado empleadoEstado;

    private ConcurrentLinkedDeque<Llamada> llamadaEntrante;

    private ConcurrentLinkedDeque<Llamada> llamadasAtendidas;

    public Empleado(EmpleadoTipo empleadoTipo) {
        Validate.notNull(empleadoTipo);
        this.empleadoTipo = empleadoTipo;
        this.empleadoEstado = EmpleadoEstado.DISPONIBLE;
        this.llamadaEntrante = new ConcurrentLinkedDeque<>();
        this.llamadasAtendidas = new ConcurrentLinkedDeque<>();
    }

    public EmpleadoTipo getEmpleadoTipo() {
        return empleadoTipo;
    }

    public synchronized EmpleadoEstado getEmpleadoEstado() {
        return empleadoEstado;
    }

    private synchronized void setEmpleadoEstado(EmpleadoEstado empleadoEstado) {
        logger.info("Empleado " + Thread.currentThread().getName() + " cambia estado a  " + empleadoEstado);
        this.empleadoEstado = empleadoEstado;
    }

    public synchronized List<Llamada> getllamadasAtendidas() {
        return new ArrayList<>(llamadasAtendidas);
    }

    /**
     * Cola de Llamada para ser atendida por el empleado
     *
     * @param Llamada Llamada a ser atendida
     */
    public synchronized void attend(Llamada Llamada) {
        logger.info("Empleado " + Thread.currentThread().getName() + " pone en la cola la Llamada de " + Llamada.getduracionSegundos() + " seconds");
        this.llamadaEntrante.add(Llamada);
    }

    public static Empleado crearOperador() {
        return new Empleado(EmpleadoTipo.OPERADOR);
    }

    public static Empleado crearSupervisor() {
        return new Empleado(EmpleadoTipo.SUPERVISOR);
    }

    public static Empleado crearDirector() {
        return new Empleado(EmpleadoTipo.DIRECTOR);
    }

    /**
     * This is the method that runs on the thread.
     * Si la cola de llamadas entrantes no esta vacia, cambia su estado de disponible a ocupado, toma la Llamada
     * y cuando termina cambia su estado de ocupado a disponible. esto permite al pool de Threads decidir
     * hacer dispatch de otra Llamada a otro Empleado.
     */
    @Override
    public void run() {
        logger.info("Empleado " + Thread.currentThread().getName() + " empieza a trabajar");
        while (true) {
            if (!this.llamadaEntrante.isEmpty()) {
                Llamada Llamada = this.llamadaEntrante.poll();
                this.setEmpleadoEstado(EmpleadoEstado.OCUPADO);
                logger.info("Empleado " + Thread.currentThread().getName() + " starts working on a Llamada of " + Llamada.getduracionSegundos() + " seconds");
                try {
                    TimeUnit.SECONDS.sleep(Llamada.getduracionSegundos());
                } catch (InterruptedException e) {
                    logger.error("Empleado " + Thread.currentThread().getName() + " was interrupted and could not finish Llamada of " + Llamada.getduracionSegundos() + " segundos");
                } finally {
                    this.setEmpleadoEstado(EmpleadoEstado.DISPONIBLE);
                }
                this.llamadasAtendidas.add(Llamada);
                logger.info("Empleado " + Thread.currentThread().getName() + " finishes a Llamada of " + Llamada.getduracionSegundos() + " segundos");
            }
        }
    }

}
