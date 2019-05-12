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
     * Constructor con 2 par�metros, el usuario y la contrase�a de la base de datos mysql.
     * @param user
     * @param pass
     */
    public ConexionBDD(String user, String pass) {
        this.usuario  = user;
        this.password = pass;
    }

    public boolean abrirConexion() {

        try {

            // Establece la conexi�n
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

    /**
     * Agrega a la base de datos un nuevo jugador.
     * @param ps
     * @return boolean
     */
    public boolean agregarJugador(PreparedStatement ps) {

        try {

            ps.executeUpdate();
            System.out.println("Jugador agregado correctamente.");
            return true;

        } catch (SQLException e) {
            System.out.println("Problemas al agregar el jugador a la base de datos.");
            return false;
        }

    }

    /**
     * Elimina de la base de datos un jugador
     * @param ps
     * @return boolean
     */
    public boolean eliminarJugador(PreparedStatement ps) {

        try {

            ps.executeUpdate();
            System.out.println("Jugador eliminado correctamente");
            return true;

        } catch (SQLException e) {
            System.out.println("Problemas al eliminar el jugador");
            return false;
        }


    }

}
