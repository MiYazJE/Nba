package controladores.controladoresEquipos;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorCreacionEquipo implements Initializable {

    @FXML private ImageView imgEquipo;
    @FXML private JFXButton btnElegirImagen;

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
            this.stage.initModality(Modality.APPLICATION_MODAL);
            this.stage.setResizable(false);
            this.stage.getIcons().add(new Image("/imagenes/detalles.png"));
            this.stage.setTitle("Creación de Equipos");
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

        this.btnElegirImagen.setOnAction(e -> {
            abrirFileChooser();
        });

    }

    private void abrirFileChooser() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Selecciona una imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.gif")
        );

        File archivo = fileChooser.showOpenDialog(this.stage);

        if (archivo != null) {
            this.imgEquipo.setImage(new Image("file:" + archivo.getPath()));
        }

    }

}
