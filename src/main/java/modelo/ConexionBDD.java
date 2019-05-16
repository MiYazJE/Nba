package modelo;

import java.sql.*;

public class ConexionBDD {

    public static Connection con;
    private ResultSet rs;
    // private String URL = "jdbc:mysql://localhost:3306/nba";
    private final String URL = "jdbc:mysql://localhost/nba?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT";
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
            /*
            jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
             */
            con = DriverManager.getConnection(URL, usuario, password);
            System.out.println("Conexion exitosa, logeado en la base de datos NBA.");
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

    /**
     * Realiza una consulta de tipo update.
     * @param ps
     * @return boolean
     */
    public boolean realizarUpdate(PreparedStatement ps) {

        try {

            ps.executeUpdate();
            System.out.println("Actualizacion realizada correctamente.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

}
