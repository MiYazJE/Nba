package controladores.controladoresEquipos;

import com.jfoenix.controls.*;
import dominio.Equipo;
import dominio.Mensaje;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.ConexionBDD;
import modelo.FicherosCarpetas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControladorCreacionEquipo implements Initializable {

    @FXML private ImageView imgEquipo;
    @FXML private JFXButton btnElegirImagen;
    @FXML private JFXButton btnCrearEquipo;
    @FXML private JFXCheckBox checkEste;
    @FXML private JFXCheckBox checkOeste;
    @FXML private JFXTextField fieldEquipo;
    @FXML private JFXTextField fieldCiudad;
    @FXML private JFXComboBox<String> comboDivision;
    @FXML private JFXProgressBar progressBar;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label labelSinImagen;

    private Stage stage;
    private Parent root;
    private File imagen;
    private String conferencia;
    private boolean imagenSumada;
    private boolean nombreSumado;
    private boolean conferenciaSumada;
    private boolean divisionSumada;
    private boolean ciudadSumada;
    private ConexionBDD conexion;
    private ObservableList<String> divisiones = FXCollections.observableArrayList(Arrays.asList(
            "Atlantic", "SouthEast", "Central", "Pacific", "SouthWest", "NorthWest"
    ));

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

        conexion = new ConexionBDD();

        this.comboDivision.getItems().addAll(divisiones);

        this.comboDivision.valueProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                if (!divisionSumada) {
                    sumarProgreso();
                    divisionSumada = true;
                }
            }
        });

        this.btnElegirImagen.setOnAction(e -> {
            abrirFileChooser();
        });

        verProgreso(false);

        checkEste.setOnAction( e -> {
            checkOeste.setSelected(false);
            if (!conferenciaSumada) {
                sumarProgreso();
            }
            conferenciaSumada = true;
        });
        checkOeste.setOnAction(e -> {
            checkEste.setSelected(false);
            if (!conferenciaSumada) {
                sumarProgreso();
            }
            conferenciaSumada = true;
        });

        this.fieldEquipo.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                nombreSumado = true;
                sumarProgreso();
            }
        });

        this.fieldCiudad.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                ciudadSumada = true;
                sumarProgreso();
            }
        });

        this.btnCrearEquipo.setOnAction(e -> crearEquipo());

    }

    private void verProgreso(boolean estado) {
        this.progressBar.setVisible(estado);
        this.progressIndicator.setVisible(estado);
    }

    private void abrirFileChooser() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Selecciona una imagenSumada");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.gif")
        );

        imagen = fileChooser.showOpenDialog(this.stage);

        if (imagen != null) {
            this.imgEquipo.setImage(new Image("file:" + imagen.getPath()));
            imagenSumada = true;
            sumarProgreso();
            this.labelSinImagen.setVisible(false);
        }

    }

    private void sumarProgreso() {
        verProgreso(true);
        this.progressIndicator.setProgress(progressIndicator.getProgress() + 0.2);
        this.progressBar.setProgress(progressBar.getProgress() + 0.2);
    }

    private void crearEquipo() {

        if (progressBar.getProgress() == 1) {
            String conferencia = (checkOeste.isSelected())
                    ? "west" : "east";
            Equipo equipo = new Equipo(fieldEquipo.getText(), fieldCiudad.getText(),
                    conferencia, comboDivision.getValue(),"");
            if (FicherosCarpetas.almacenarImagen(this.imagen, equipo)) {
                System.out.println("Equipo añadido");
                Mensaje.mostrar((StackPane) root, "El equipo " + equipo.getNombre() + " ha sido agregado\n" +
                        "correctamente a la base de datos");
            }
            else {
                System.out.println("Problemas al guardar el equipo");
                Mensaje.mostrar((StackPane) root, "Han habido problemas al añadir el equipo.");
            }
        }
        else {
            Mensaje.mostrar((StackPane) root, "Por favor, rellene todos los campos.");
        }

    }

}
