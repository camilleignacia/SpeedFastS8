package dao;

import modelo.EstadoPedido;
import modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Operaciones JDBC para la tabla pedido. */
public class PedidoDAO {

    /** Inserta un pedido en la base de datos. */
    public void guardar(Pedido pedido) {
        String sql = "INSERT INTO pedido (id, direccion, tipo, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getId());
            stmt.setString(2, pedido.getDireccionEntrega());
            stmt.setString(3, pedido.getTipo());
            stmt.setString(4, pedido.getEstado().name());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** @return Lista con todos los pedidos almacenados en la BD. */
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
                String nombreRepartidor = rs.getString("nombre");
                p.setRepartidor(nombreRepartidor != null ? nombreRepartidor : "Sin asignar");
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /** @return Lista con los pedidos en estado PENDIENTE. */
    public List<Pedido> listarPendientes() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedido WHERE estado = 'PENDIENTE'";
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
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /** Actualiza el estado de un pedido en la BD. */
    public void actualizarEstado(int id, EstadoPedido nuevoEstado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** @return true si ya existe un pedido con ese ID en la BD. */
    public boolean existeId(int id) {
        String sql = "SELECT id FROM pedido WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}