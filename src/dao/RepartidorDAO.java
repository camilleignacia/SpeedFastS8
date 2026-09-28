package dao;

import modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Operaciones JDBC para la tabla repartidor. */
public class RepartidorDAO {

    /** @return Lista con todos los repartidores almacenados en la BD. */
    public List<Repartidor> listarTodos() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidor";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Repartidor r = new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}