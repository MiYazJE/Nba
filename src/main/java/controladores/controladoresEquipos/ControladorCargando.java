package controladores.controladoresEquipos;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

class Tarea extends Task<Integer> {

    @Override
    public Integer call() throws Exception {

        for (int i = 1; i < 51; i++) {
            updateProgress(i, 49);
            Thread.sleep(50);
        }

        return 51;
    }

}

public class ControladorCargando implements Initializable {

    @FXML private ProgressIndicator indicadorProgreso;

    private Parent root;
    private Tarea tarea;

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

        this.tarea = new Tarea();
        indicadorProgreso.progressProperty().bind(tarea.progressProperty());
        new Thread(tarea).start();
    }

    public Parent getRoot() {
        return this.root;
    }

    public Task<Integer> getTask() {
        return this.tarea;
    }

}
