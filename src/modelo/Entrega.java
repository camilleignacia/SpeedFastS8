package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/** Representa una entrega asociada a un pedido y un repartidor. */
public class Entrega {

    private int       id;
    private int       idPedido;
    private int       idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    /**
     * @param idPedido     ID del pedido asociado.
     * @param idRepartidor ID del repartidor asignado.
     * @param fecha        Fecha de la entrega.
     * @param hora         Hora de la entrega.
     */
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idPedido     = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha        = fecha;
        this.hora         = hora;
    }

    public int       getId()           { return id; }
    public int       getIdPedido()     { return idPedido; }
    public int       getIdRepartidor() { return idRepartidor; }
    public LocalDate getFecha()        { return fecha; }
    public LocalTime getHora()         { return hora; }
}