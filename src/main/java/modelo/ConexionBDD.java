package modelo;

import java.sql.*;

public class ConexionBDD {

    public static Connection con;
    private ResultSet rs;

    /**
     * Constructor con 2 parámetros, el usuario y la contraseña de la base de datos mysql.
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


    public static boolean abrirConexion() {

        try {

            // Establece la conexión
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Conexion con la base de datos exitosa.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void cerrarConexion() {

        try {
            con.close();
            System.out.println("Cerrando la conexion con la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ResultSet realizarConsulta(PreparedStatement ps) {

        try {

            rs = ps.executeQuery();
            System.out.println("Consulta realizada correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

}
