package controladores.controladoresPrincipal;

import controladores.controladoresEquipos.ControladorCargando;
import controladores.controladoresEquipos.ControladorEquipos;
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

    @FXML AnchorPane contenedor;
    @FXML Button botonJugadores;
    @FXML Button botonEquipos;
    @FXML ImageView imagenPrincipal;

    private double posX;
    private double posY;
    private Stage stage = ControladorLogin.ventana;
    private boolean esperaValida;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        stage.setOnCloseRequest(e -> {
            e.consume();
            dialogoAlCerrar();
        });

        esconderContenedor();

        botonJugadores.setOnMouseClicked(e -> {
            // Cargar la tabla jugadores
            esconderContenedor();
            cargarTablaJugadores();
            esperaValida = false;
        });

        botonEquipos.setOnMouseClicked(e -> {
            esconderContenedor();
            mostrarEquipos();
        });

    }

    private void esconderContenedor() {
        imagenPrincipal.setVisible(true);
        contenedor.getChildren().clear();
        contenedor.setVisible(false);
    }

    private void mostrarContenedor() {
        imagenPrincipal.setVisible(false);
        contenedor.setVisible(true);
    }

    /**
     * Mostrar una tabla con todos los jugadores de la Nba,
     * la vista de la tabla será agregada a contenedor.
     */
    private void cargarTablaJugadores() {

        Parent ventanaJugadores = null;

        try {
            ventanaJugadores = FXMLLoader.load(getClass().getResource("/fxml/TablaJugadores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        contenedor.getChildren().add( ventanaJugadores );
        mostrarContenedor();
    }

    /**
     * Indexar la vista de equipos en un contenedor
     */
    private void mostrarEquipos() {

        mostrarContenedor();

        ControladorEquipos equipos = new ControladorEquipos();
        Thread hiloEquipos = new Thread(equipos);
        hiloEquipos.start();
        esperaValida = true;

        ControladorCargando cargando = new ControladorCargando();
        contenedor.getChildren().add(cargando.getRoot());

        cargando.getTask().setOnSucceeded(e -> {
            // Si despues de activar el hiloEquipos pulsas ver jugadores esto no tiene que ejecutarse
            if (esperaValida) {
                contenedor.getChildren().clear();
                contenedor.getChildren().add(equipos.getRoot());
                equipos.mostrar(true);
            }
        });

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