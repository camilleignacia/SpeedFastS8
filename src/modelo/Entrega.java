package modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una entrega asociada a un pedido y un repartidor.
 */
public class Entrega {

    private int id;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;

    /**
     * Campos de visualización, cargados con JOIN desde la BD.
     */
    private String textoPedido;
    private String textoRepartidor;

    /**
     * Constructor para una entrega nueva; el ID lo genera la BD.
     */
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    /**
     * Constructor para cargar una entrega existente desde la BD.
     */
    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getTextoPedido() {
        return textoPedido;
    }

    public String getTextoRepartidor() {
        return textoRepartidor;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setTextoPedido(String textoPedido) {
        this.textoPedido = textoPedido;
    }

    public void setTextoRepartidor(String textoRepartidor) {
        this.textoRepartidor = textoRepartidor;
    }
}