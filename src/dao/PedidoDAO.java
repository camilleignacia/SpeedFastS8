package dao;

import modelo.EstadoPedido;
import modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Operaciones JDBC para la tabla pedido. */
public class PedidoDAO {

    /** Inserta un pedido en la base de datos. El ID lo genera la BD automáticamente. */
    public void guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipo());
            stmt.setString(3, pedido.getEstado().name());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar pedido: " + e.getMessage());
        }
    }

    /** @return Lista con todos los pedidos almacenados en la BD, incluyendo repartidor asignado. */
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.direccion, p.tipo, p.estado, r.nombre " +
                "FROM pedido p " +
                "LEFT JOIN entrega e ON p.id = e.id_pedido " +
                "LEFT JOIN repartidor r ON e.id_repartidor = r.id";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo")
                );
                p.setEstado(rs.getString("estado"));
                String nombre = rs.getString("nombre");
                p.setRepartidor(nombre != null ? nombre : "Sin asignar");
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * @param estado Estado por el que filtrar.
     * @return Lista con los pedidos que coinciden con ese estado.
     */
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.direccion, p.tipo, p.estado, r.nombre " +
                "FROM pedido p " +
                "LEFT JOIN entrega e ON p.id = e.id_pedido " +
                "LEFT JOIN repartidor r ON e.id_repartidor = r.id " +
                "WHERE p.estado = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        rs.getString("tipo")
                );
                p.setEstado(rs.getString("estado"));
                String nombre = rs.getString("nombre");
                p.setRepartidor(nombre != null ? nombre : "Sin asignar");
                lista.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos por estado: " + e.getMessage());
        }
        return lista;
    }

    /** @return Lista con los pedidos en estado PENDIENTE. */
    public List<Pedido> listarPendientes() {
        return listarPorEstado(EstadoPedido.PENDIENTE);
    }

    /** Actualiza la dirección, tipo y estado de un pedido en la BD. */
    public void actualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pedido.getDireccionEntrega());
            stmt.setString(2, pedido.getTipo());
            stmt.setString(3, pedido.getEstado().name());
            stmt.setInt(4, pedido.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pedido: " + e.getMessage());
        }
    }

    /** Elimina un pedido de la BD por su ID. */
    public void eliminar(int id) {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage());
        }
    }

    /** Actualiza solo el estado de un pedido en la BD. */
    public void actualizarEstado(int id, EstadoPedido nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estado: " + e.getMessage());
        }
    }
}