/**
 * @author Ruben Saiz
 */
package controladores.controladoresEquipos;

import com.jfoenix.controls.JFXButton;
import controladores.controladoresJugadores.ControladorDetallesJugador;
import dominio.Equipo;
import dominio.Jugador;
import dominio.Mensaje;
import dominio.Partido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControladorDetallesEquipo implements Initializable {

    @FXML private JFXButton btnPlantilla;
    @FXML private JFXButton btnPartidos;
    @FXML private AnchorPane contenedor;

    private Equipo equipo;
    private Parent root;
    private Stage stage;
    private String ventanaMostrar;
    private Scene scene;
    private ControladorPartidos partidos;
    private ControladorPlantilla plantilla;

    public ControladorDetallesEquipo(Equipo equipo, String mostrar) {
        this.ventanaMostrar = mostrar;
        this.equipo = equipo;
        init();
    }

    private void init() {

        this.stage = new Stage();

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/DetallesEquipo.fxml"));
            loader.setController(this);
            this.scene = new Scene(loader.load());

            this.stage.initModality(Modality.APPLICATION_MODAL);
            this.stage.getIcons().add(new Image("/imagenes/pelota.png"));
            this.stage.setTitle("Detalles de los " + this.equipo.getNombre() );
            this.stage.setResizable(false);
            this.stage.setScene(this.scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.partidos  = new ControladorPartidos(this.equipo);
        this.plantilla = new ControladorPlantilla(this.equipo);

        btnPartidos.setOnAction(e -> {
            mostrarPartidos();
        });

        btnPlantilla.setOnAction(e -> {
            mostrarPlantilla();
        });

        eleccionVentana();

    }

    private void mostrarPartidos() {
        limpiarContenedor();
        cargarPartidos();
    }

    private void limpiarContenedor() {
        this.contenedor.getChildren().clear();
    }

    private void cargarPartidos() {
        this.partidos = new ControladorPartidos(this.equipo);
        this.contenedor.getChildren().add(partidos.getRoot());
    }

    private void mostrarPlantilla() {
        limpiarContenedor();
        this.contenedor.getChildren().add(plantilla.getRoot());
    }

    private void eleccionVentana() {
        if (this.ventanaMostrar.equals("Plantilla"))
            mostrarPlantilla();
        else
            mostrarPartidos();
    }

    public void mostrar() {
        this.stage.show();
    }

}
