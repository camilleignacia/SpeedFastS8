package modelo;

/** Representa un pedido con identificador, dirección, tipo y estado. */
public class Pedido {

    private int id;
    private String direccionEntrega;
    private String tipo;
    private EstadoPedido estado;

    /**
     * @param id               Identificador único.
     * @param direccionEntrega Dirección de destino.
     * @param tipo             Tipo de pedido.
     */
    public Pedido(int id, String direccionEntrega, String tipo) {
        this.id               = id;
        this.direccionEntrega = direccionEntrega;
        this.tipo             = tipo;
        this.estado           = EstadoPedido.PENDIENTE;
    }

    // Getters

    public int getId()                  { return id; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public String getTipo()             { return tipo; }
    public EstadoPedido getEstado()     { return estado; }

    // Setters

    public void setId(int id)                                { this.id = id; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public void setTipo(String tipo)                         { this.tipo = tipo; }

    /** @param nuevoEstado Estado a asignar usando el enum. */
    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /** @param nuevoEstado Nombre del estado en mayúsculas. */
    public void setEstado(String nuevoEstado) {
        this.estado = EstadoPedido.valueOf(nuevoEstado.toUpperCase());
    }

    @Override
    public String toString() {
        return "Pedido #" + id
                + " | Destino: " + direccionEntrega
                + " | Tipo: "    + tipo
                + " | Estado: "  + estado;
    }
}