package dao;

import modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones JDBC para la tabla repartidor.
 */
public class RepartidorDAO {

    /**
     * Inserta un repartidor en la base de datos.
     */
    public void guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar repartidor: " + e.getMessage());
        }
    }

    /**
     * @return Lista con todos los repartidores almacenados en la BD.
     */
    public List<Repartidor> listarTodos() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar repartidores: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza el nombre de un repartidor en la BD.
     */
    public void actualizar(Repartidor repartidor) {
        String sql = "UPDATE repartidor SET nombre = ? WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, repartidor.getNombre());
            stmt.setInt(2, repartidor.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar repartidor: " + e.getMessage());
        }
    }

    /**
     * Elimina un repartidor de la BD por su ID.
     */
    public void eliminar(int id) {
        String sql = "DELETE FROM repartidor WHERE id = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar repartidor: " + e.getMessage());
        }
    }
}