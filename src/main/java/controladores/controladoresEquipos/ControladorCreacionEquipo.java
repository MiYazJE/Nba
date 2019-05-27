package controladores.controladoresEquipos;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorCreacionEquipo implements Initializable {

    private Stage stage;
    private Parent root;
    private String conferencia;

    public ControladorCreacionEquipo(String conferencia) {
        this.conferencia = conferencia;
        init();
    }

    private void init() {

        this.stage = new Stage();

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/CreacionEquipo.fxml"));
            loader.setController(this);
            this.root = loader.load();
            this.stage.setScene( new Scene(root) );

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void mostrar() {
        this.stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
