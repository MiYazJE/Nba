package controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorMain implements Initializable {

    @FXML AnchorPane cajaTablaJugadores;
    @FXML Button botonJugadores;

    private ConexionBDD conexion = new ConexionBDD();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conexion.abrirConexion();
        esconderTablaJugadores();

        botonJugadores.setOnMouseClicked(e -> {
            // Cargar la tabla jugadores
            cargarTablaJugadores();
        });

    }

    private void esconderTablaJugadores() {
        cajaTablaJugadores.setVisible(false);
    }

    private void mostrarTablaJugadores() {
        cajaTablaJugadores.setVisible(true);
    }

    private void cargarTablaJugadores() {

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/TablaJugadores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //cajaTablaJugadores = new AnchorPane();
        //cajaTablaJugadores.getChildren().add(root);
        mostrarTablaJugadores();
    }

}