/**
 * @author Ruben Saiz
 */
package controladores.vistaEquipos;

import com.jfoenix.controls.JFXButton;
import dominio.Equipo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControladorCajaEquipo implements Initializable {

    @FXML private Text textEquipo;
    @FXML private ImageView imgEquipo;
    @FXML private JFXButton btnPlantilla;
    @FXML private JFXButton btnPartidos;

    private Parent root;
    private Equipo equipo;
    private ConexionBDD conexion = new ConexionBDD();

    public ControladorCajaEquipo(Equipo equipo) {
        this.equipo = equipo;
        init();
    }

    private void init() {

        try {

            FXMLLoader loader = new FXMLLoader( getClass().getResource("/fxml/CajaEquipo.fxml") );
            loader.setController( this );
            this.root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnPlantilla.setOnAction(e -> {
            System.out.println( this.equipo );
            mostrarEquipo(btnPlantilla.getText());
        });

        btnPartidos.setOnAction(e -> {
            System.out.println("Partido seleccionado.");
        });

        cargarInformacion();
    }

    private void mostrarEquipo(String ventana) {

        ControladorDetallesEquipo detallesEquipo = new ControladorDetallesEquipo(this.equipo, ventana);
        detallesEquipo.mostrar();

    }

    private void cargarInformacion() {

        this.textEquipo.setText( this.equipo.getNombre() );

        Image imagenEquipo = consultarImagenEquipo();
        this.imgEquipo.setImage( imagenEquipo );
    }

    private Image consultarImagenEquipo() {

        String ruta = getClass().getResource("/imagenes/logosNba/lakers.png").getPath();

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT imagen from equipos where nombre = ?;");
            ps.setString(1, this.equipo.getNombre());

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) ruta = rs.getString("imagen");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Image imagenEquipo = new Image("file:" + ruta);
        return imagenEquipo;
    }

    public Parent getRoot() {
        return this.root;
    }

}
