package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Gestiona la conexión con la base de datos MySQL. */
public class ConexionDB {

    private static final String URL      = "jdbc:mysql://localhost:3306/speedfast_db";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "root1995";

    /**
     * @return Conexión activa con la base de datos.
     * @throws SQLException si la conexión falla.
     */
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}