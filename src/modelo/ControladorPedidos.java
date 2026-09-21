package modelo;

import java.util.ArrayList;
import java.util.List;

/** Gestiona la lista de pedidos en memoria, compartida entre ventanas. */
public class ControladorPedidos {

    private List<Pedido> pedidos;

    public ControladorPedidos() {
        this.pedidos = new ArrayList<>();
    }

    /** @param pedido Pedido a agregar a la lista. */
    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    /** @return Lista completa de pedidos. */
    public List<Pedido> getPedidos() {
        return pedidos;
    }

    /**
     * @param id ID a verificar.
     * @return true si ya existe un pedido con ese ID.
     */
    public boolean existeId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }
}