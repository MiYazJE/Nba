package modelo;

import java.sql.*;

public class ConexionBDD {

    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    private static final String URL = "jdbc:mysql://localhost:3306/nba";
    private static final String USERNAME = "root";
    // WINDOWS -> "root" | LINUX -> "Roo|"
    private static final String PASSWORD = "Roo|";


    public void abrirConexion() {

        try {

            // Establece la conexión
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Ejecuta la consulta
            stmt = con.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cerrarConexion() {

        try {
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ResultSet realizarConsulta(String consulta) {

        try {

            rs = stmt.executeQuery( consulta );

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public Connection getStatement() {
        return this.con;
    }

}
