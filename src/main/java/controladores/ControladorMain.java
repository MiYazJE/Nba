package controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorMain implements Initializable {

    @FXML AnchorPane cajaTablaJugadores;
    @FXML Button botonJugadores;
    @FXML Button botonEstadisticas;
    @FXML Button botonEquipos;


    public static ConexionBDD conexion = new ConexionBDD();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conexion.abrirConexion();
        esconderTablaJugadores();

        botonJugadores.setOnMouseClicked(e -> {
            // Cargar la tabla jugadores
            cargarTablaJugadores();
        });

        botonEquipos.setOnMouseClicked(e -> {
            esconderTablaJugadores();
        });

        botonEstadisticas.setOnMouseClicked(e -> {
            esconderTablaJugadores();
        });

    }

    private void esconderTablaJugadores() {
        cajaTablaJugadores.setVisible(false);
    }

    private void mostrarTablaJugadores() {
        cajaTablaJugadores.setVisible(true);
    }

    /**
     * Mostrar una tabla con todos los jugadores de la Nba que por defecto
     * estar� invisible, la vista de la tabla ser� agregada a cajaTablaJugadores.
     */
    private void cargarTablaJugadores() {

        // AnchorPane panel = new AnchorPane();

        Parent ventanaNueva = null;

        try {
            ventanaNueva = FXMLLoader.load(getClass().getResource("/fxml/TablaJugadores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cajaTablaJugadores.getChildren().add( ventanaNueva );

        mostrarTablaJugadores();
    }

}