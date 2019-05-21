package controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorMain implements Initializable {

    @FXML AnchorPane cajaTablaJugadores;
    @FXML Button botonJugadores;
    @FXML Button botonEquipos;
    @FXML ImageView imagenPrincipal;

    private double posX;
    private double posY;
    private Stage stage = ControladorLogin.ventana;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        stage.setOnCloseRequest(e -> {
            e.consume();
            dialogoAlCerrar();
        });

        esconderTablaJugadores();

        botonJugadores.setOnMouseClicked(e -> {
            // Cargar la tabla jugadores
            cargarTablaJugadores();
        });

        botonEquipos.setOnMouseClicked(e -> {
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

        Parent ventanaNueva = null;

        try {
            ventanaNueva = FXMLLoader.load(getClass().getResource("/fxml/TablaJugadores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cajaTablaJugadores.getChildren().add( ventanaNueva );

        mostrarTablaJugadores();
    }

    /**
     * Creacion de una ventana para que en caso de intentar salir de la aplicacion preguntar
     * al usuario si esta seguro.
     */
    private void dialogoAlCerrar() {

        Stage stage = new Stage();
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/DialogoCierre.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene( root );

        // MOVER LA VENTANA
        root.setOnMousePressed(e -> {
            posX = stage.getX() - e.getScreenX();
            posY = stage.getY() - e.getScreenY();
        });
        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() + posX);
            stage.setY(e.getScreenY() + posY);
        });

        stage.setScene( scene );
        stage.initStyle(StageStyle.UTILITY);
        stage.getIcons().add(new Image("/imagenes/information.png"));
        stage.setResizable( false );
        stage.show();
    }

}