package controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorCargando implements Initializable {

    @FXML private ProgressBar barraProgreso;
    @FXML private ProgressIndicator indicadorProgreso;

    private Parent root;

    public ControladorCargando() {
        init();
    }

    public void init() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Cargando.fxml"));
            loader.setController(this);
            this.root = (Parent) loader.load();

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void iniciar() {

        for (int i = 0; i < 100; i++) {

            this.barraProgreso.setProgress( i );
            this.indicadorProgreso.setProgress( i );
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public Parent getRoot() {
        return this.root;
    }

}
