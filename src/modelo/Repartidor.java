package modelo;

import java.util.Random;

/** Hilo que retira y entrega pedidos de la ZonaDeCarga compartida. */
public class Repartidor implements Runnable {

    private int         id;
    private String      nombre;
    private ZonaDeCarga zonaDeCarga;
    private Random      random;

    /** Constructor para mapear desde la base de datos. */
    public Repartidor(int id, String nombre) {
        this.id     = id;
        this.nombre = nombre;
    }

    /**
     * @param nombre      Nombre del repartidor.
     * @param zonaDeCarga Zona de carga compartida.
     */
    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre       = nombre;
        this.zonaDeCarga  = zonaDeCarga;
        this.random       = new Random();
    }

    public int    getId()     { return id; }
    public String getNombre() { return nombre; }

    /** Retira pedidos uno a uno hasta que la zona quede vacía. */
    @Override
    public void run() {
        Pedido pedido;
        while ((pedido = zonaDeCarga.retirarPedido()) != null) {
            procesarPedido(pedido);
        }
        System.out.println("[Repartidor - " + nombre + "] Sin más pedidos. Turno finalizado.");
    }

    /** @param pedido Pedido a procesar: EN_REPARTO → espera simulada → ENTREGADO. */
    private void procesarPedido(Pedido pedido) {
        try {
            pedido.setEstado(EstadoPedido.EN_REPARTO);
            System.out.println("[Repartidor - " + nombre + "] Retirando pedido #" + pedido.getId() + "...");
            System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

            System.out.println("[Repartidor - " + nombre + "] Entregando pedido #" + pedido.getId() + "...");
            Thread.sleep(1000 + random.nextInt(2000));

            pedido.setEstado(EstadoPedido.ENTREGADO);
            System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[Repartidor - " + nombre
                    + "] Interrumpido durante la entrega del pedido #" + pedido.getId() + ".");
        }
    }
}