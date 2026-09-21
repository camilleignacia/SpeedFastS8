package modelo;

import java.util.ArrayList;
import java.util.List;

/** Recurso compartido entre repartidores. Métodos synchronized para evitar condiciones de carrera. */
public class ZonaDeCarga {

    private List<Pedido> pedidos;

    public ZonaDeCarga() {
        this.pedidos = new ArrayList<>();
    }

    /** @param p Pedido a agregar a la zona de carga. */
    public synchronized void agregarPedido(Pedido p) {
        pedidos.add(p);
    }

    /**
     * Retira el primer pedido disponible.
     * @return Pedido retirado, o null si la zona está vacía.
     */
    public synchronized Pedido retirarPedido() {
        if (pedidos.isEmpty()) {
            return null;
        }
        return pedidos.remove(0);
    }
}