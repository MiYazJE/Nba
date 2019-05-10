package modelo;

import java.sql.*;

public class ConexionBDD {

    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    /**
     * Constructor con 2 par�metros, el usuario y la contrase�a de la base de datos mysql.
     * @param user
     * @param pass
     */
    /*public ConexionBDD(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }*/

    private static final String URL = "jdbc:mysql://localhost:3306/nba";
    private static final String USERNAME = "root";
    // WINDOWS -> "root" | LINUX -> "Roo|"
    private static final String PASSWORD = "root";


    public void abrirConexion() {

        try {

            // Establece la conexi�n
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

    public Connection getConection() {
        return this.con;
    }

}
