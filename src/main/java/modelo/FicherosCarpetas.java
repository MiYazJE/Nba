/**
 * @author Ruben Saiz
 */
package modelo;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
     * Genera las imagenes necesarias de la aplicación a la carpeta del usuario
     * @return boolean
     */
    public boolean moverImagenes() {

        URL location = FicherosCarpetas.class.getProtectionDomain()
                .getCodeSource().getLocation();
        File srcDir = new File(location.getPath() + "imagenes/logosNba");
        File destDir = new File(this.rutaAbsoluta);

        try {
            FileUtils.copyDirectory(srcDir, destDir);
            System.out.println("Copiando imagenes a '" + this.rutaAbsoluta + "'");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Inserta en la base de datos la ruta a las imagenes generadas
     */
    private void insertarImagenestoNba() {

        String[] imagenes = new File(this.rutaAbsoluta).list();
        try {

            for (String file : imagenes) {
                String nombre = file.substring(0, file.length() - 4); // file("76ers.png") -> "76ers"

                PreparedStatement ps = conexion.con.prepareStatement(
                                    "UPDATE EQUIPOS SET Imagen = ? WHERE Nombre = ?;");
                ps.setString(1, this.rutaAbsoluta + "\\" + nombre + ".png");
                ps.setString(2, nombre);

                conexion.realizarUpdate( ps );
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

}
