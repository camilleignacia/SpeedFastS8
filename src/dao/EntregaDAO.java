package dao;

import modelo.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones JDBC para la tabla entrega.
 */
public class EntregaDAO {

    /**
     * Inserta una entrega en la base de datos.
     */
    public void guardar(Entrega entrega) {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());
            stmt.setDate(3, Date.valueOf(entrega.getFecha()));
            stmt.setTime(4, Time.valueOf(entrega.getHora()));
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar entrega: " + e.getMessage());
        }
    }

    /**
     * @return Lista con todas las entregas almacenadas, incluyendo datos
     * de pedido y repartidor para su visualización en tabla.
     */
    public List<Entrega> listarTodos() {
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.id_pedido, e.id_repartidor, e.fecha, e.hora, " +
                "p.direccion, r.nombre " +
                "FROM entrega e " +
                "JOIN pedido p ON e.id_pedido = p.id " +
                "JOIN repartidor r ON e.id_repartidor = r.id";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );
                entrega.setTextoPedido("#" + rs.getInt("id_pedido") + " - " + rs.getString("direccion"));
                entrega.setTextoRepartidor(rs.getString("nombre"));
                lista.add(entrega);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar entregas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza los datos de una entrega en la BD.
     */
    public void actualizar(Entrega entrega) {
        String sql = "UPDATE entrega SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, entrega.getIdPedido());
            stmt.setInt(2, entrega.getIdRepartidor());
            stmt.setDate(3, Date.valueOf(entrega.getFecha()));
            stmt.setTime(4, Time.valueOf(entrega.getHora()));
            stmt.setInt(5, entrega.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar entrega: " + e.getMessage());
        }
    }

    /**
     * Elimina una entrega de la BD por su ID.
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM entrega WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar entrega: " + e.getMessage());
        }
    }
}