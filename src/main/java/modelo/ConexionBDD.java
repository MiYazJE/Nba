package modelo;

import java.sql.*;

public class ConexionBDD {

    public static Connection con;
    private ResultSet rs;
    private String URL = "jdbc:mysql://localhost:3306/nba";
    // WINDOWS -> "root" | LINUX -> "Roo|"
    private String password;
    private String usuario;

    /**
     * Contructor vacio
     */
    public ConexionBDD() {

    }

    /**
     * Constructor con 2 parámetros, el usuario y la contraseña de la base de datos mysql.
     * @param user
     * @param pass
     */
    public ConexionBDD(String user, String pass) {
        this.usuario  = user;
        this.password = pass;
    }

    public boolean abrirConexion() {

        try {

            // Establece la conexión
            con = DriverManager.getConnection(URL, usuario, password);
            System.out.println("Conexion exitosa, logeado en la base de datos NBA.");
            return true;

        } catch (SQLException e) {
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
