package dao;

import modelo.Entrega;

import java.sql.*;

/** Operaciones JDBC para la tabla entrega. */
public class EntregaDAO {

    /** Inserta una entrega en la base de datos. */
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
            e.printStackTrace();
        }
    }
}