package controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorMain implements Initializable {

    @FXML AnchorPane cajaTablaJugadores;
    @FXML Button botonJugadores;
    @FXML Button botonEstadisticas;
    @FXML Button botonEquipos;
    @FXML ImageView imagenPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        cajaTablaJugadores.getChildren().clear();
        cajaTablaJugadores.setVisible(false);
        imagenPrincipal.setVisible(true);
    }

    private void mostrarTablaJugadores() {
        cajaTablaJugadores.setVisible(true);
        imagenPrincipal.setVisible(false);
    }

    /**
     * Mostrar una tabla con todos los jugadores de la Nba,
     * la vista de la tabla será agregada a cajaTablaJugadores.
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