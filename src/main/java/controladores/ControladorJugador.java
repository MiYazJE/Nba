package controladores;

import com.jfoenix.controls.JFXTextField;
import dominio.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorJugador {

    @FXML private JFXTextField nombre;
    @FXML private JFXTextField posicion;

    private Jugador jugador;
    private Stage stage;


    public ControladorJugador(Jugador jugador) {
        init();
        this.jugador = jugador;
    }


    private void init() {

        this.stage = new Stage();

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlantillaJugador.fxml"));
            loader.setController(this);
            // loader.setLocation(getClass().getResource("/fxml/PlantillaJugador.fxml"));

            this.stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        this.stage.showAndWait();
    }

}
