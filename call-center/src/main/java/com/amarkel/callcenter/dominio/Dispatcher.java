package com.amarkel.callcenter.dominio;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public static final Integer MAX_THREADS = 10;

    private Boolean active;

    private ExecutorService executorService;

    private ConcurrentLinkedDeque<Empleado> colaEmpleados;

    private ConcurrentLinkedDeque<Llamada> colaLlamadaEntrante;

    private EstrategiaAtencionLlamada estrategiaAtencionLlamada;

    public Dispatcher(List<Empleado> empleados) {
        this(empleados, new EstrategiaAtencionLlamadaDefecto());
    }

    public Dispatcher(List<Empleado> empleados, EstrategiaAtencionLlamada estrategiaAtencionLlamada) {
        Validate.notNull(empleados);
        Validate.notNull(estrategiaAtencionLlamada);
        this.colaEmpleados = new ConcurrentLinkedDeque(empleados);
        this.estrategiaAtencionLlamada = estrategiaAtencionLlamada;
        this.colaLlamadaEntrante = new ConcurrentLinkedDeque<>();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public synchronized void dispatch(Llamada llamada) {
        logger.info("Dispatch new call of " + llamada.getduracionSegundos() + " seconds");
        this.colaLlamadaEntrante.add(llamada);
    }

    /**
     * Comienza el hilo de Empleado y permite al dispatcher que ejecute el metodo execute.
     */
    public synchronized void start() {
        this.active = true;
        for (Empleado empleado : this.colaEmpleados) {
            this.executorService.execute(empleado);
        }
    }

    /**
     * Detiene el hilo de empleado y el dispatcher.
     */
    public synchronized void stop() {
        this.active = false;
        this.executorService.shutdown();
    }

    public synchronized Boolean getActive() {
        return active;
    }

    /**
     * Este es el metodo que corre en el hilo.
     * Si la cola no esta vacia, busca un empleado disponible para tomar la llamada.
     * Calls will queue up until some workers becomes available.
     */
    @Override
    public void run() {
        while (getActive()) {
            if (this.colaLlamadaEntrante.isEmpty()) {
                continue;
            } else {
                Empleado empleado = this.estrategiaAtencionLlamada.buscarEmpleado(this.colaEmpleados);
                if (empleado == null) {
                    continue;
                }
                Llamada llamada = this.colaLlamadaEntrante.poll();
                try {
                    empleado.attend(llamada);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    this.colaLlamadaEntrante.addFirst(llamada);
                }
            }
        }
    }

}
