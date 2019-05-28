/**
 * @author Ruben Saiz
 */
package modelo;

import dominio.Equipo;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;

public class FicherosCarpetas {

    private String rutaAbsoluta;
    private ConexionBDD conexion = new ConexionBDD();

    /**
     * Crea una carpeta necesaria para la aplicacion
     */
    public boolean crearFicheros() {

        File carpeta = new File("imagenesNba");

        if (!carpeta.exists()) {
            if (carpeta.mkdir()) {
                crearCampoImagen();
                this.rutaAbsoluta = carpeta.getAbsolutePath();
                System.out.println("Creando carpeta en: " + this.rutaAbsoluta);
                moverImagenes();
                insertarImagenestoNba();
            }
            return true;
        }

        return false;
    }

    /**
     * Añade una columna llamada imagen a la tabla equipos
     */
    private void crearCampoImagen() {

        try {

            PreparedStatement pst = conexion.con.prepareStatement(
                    "alter table Equipos DROP COLUMN Imagen;");
            conexion.realizarUpdate( pst );

            PreparedStatement ps = conexion.con.prepareStatement(
                    "alter table equipos add Imagen varchar(250);");
            conexion.realizarUpdate( ps );

        } catch (SQLSyntaxErrorException e) {
            System.out.println("Puede que la columa Imagen ya existiera.\nO puede que no.");
        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Elimina todos los ficheros de la carpeta pasada coomo parámetro
     */
    private void eliminar(File carpeta) throws IOException {

        if (carpeta.list().length == 0) {
            carpeta.delete();
            System.out.println("Carpeta eliminada: " + carpeta.getAbsolutePath());
        }
        else {

            String[] ficheros = carpeta.list();
            for (String temp : ficheros) {
                File fichero = new File(carpeta, temp);
                fichero.delete();
                System.out.println("Fichero eliminado: " + fichero.getAbsolutePath());
            }
            eliminar( carpeta );
        }

    }

    /**
     * Genera las imagenes utilizadas por la aplicacion
     * @return boolean
     */
    public boolean moverImagenes() {


       ArrayList<String> nombres = getNombresEquipos();

        try {

            for (String nombre : nombres) {
                InputStream source = getClass().getResourceAsStream("/imagenes/logosNba/" + nombre + ".png");
                Files.copy(source, Paths.get( this.rutaAbsoluta + File.separator + nombre + ".png" ), StandardCopyOption.REPLACE_EXISTING);
            }

            return true;

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Consultar todos los nombes de los equipos de la nba
     */
    private ArrayList<String> getNombresEquipos() {

        ArrayList<String> listaNombres = new ArrayList<>();

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT Nombre FROM equipos;");
            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String nombre = rs.getString("Nombre");
                listaNombres.add( nombre.toLowerCase() );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaNombres;
    }


    /**
     * Inserta en la base de datos la ruta de cada una de las imagenes utilizadas
     */
    private void insertarImagenestoNba() {

        String[] imagenes = new File(this.rutaAbsoluta).list();
        try {

            for (String file : imagenes) {
                String nombre = file.substring(0, file.length() - 4); // file("76ers.png") -> "76ers"

                PreparedStatement ps = conexion.con.prepareStatement(
                                    "UPDATE equipos SET imagen = ? WHERE nombre = ?;");
                ps.setString(1, this.rutaAbsoluta + File.separator + nombre + ".png");
                ps.setString(2, nombre);

                conexion.realizarUpdate( ps );
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean almacenarImagen(File archivo, Equipo equipo) {

        ConexionBDD conexion = new ConexionBDD("root", "root");
        conexion.abrirConexion();
        File carpeta = new File("imagenesNba");
        String nombre = archivo.getName();
        String rutaAbsoluta = carpeta.getAbsolutePath();

        if (carpeta.exists()) {

            try {

                InputStream source = new FileInputStream(archivo);
                Files.copy(source, Paths.get(rutaAbsoluta + File.separator + nombre), StandardCopyOption.REPLACE_EXISTING);

                try {

                    PreparedStatement ps = conexion.con.prepareStatement(
                            "INSERT INTO equipos (nombre, ciudad, conferencia, division, imagen) " +
                                "VALUES (?, ?, ?, ?, ?)");

                    ps.setString(1, equipo.getNombre());
                    ps.setString(2, equipo.getCiudad());
                    ps.setString(3, equipo.getConferencia());
                    ps.setString(4, equipo.getDivision());
                    ps.setString(5, rutaAbsoluta + File.separator + nombre);

                    return conexion.realizarUpdate( ps );

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }

        return false;
    }


}
