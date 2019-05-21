package controladores;

import com.jfoenix.controls.JFXButton;
import dominio.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorDetallesJugador implements Initializable {

    @FXML private JFXButton btnPerfil;
    @FXML private JFXButton btnEstadisticas;
    @FXML private AnchorPane contenedor;

    private Jugador jugador;
    protected static Stage stage;
    // Vista del controlador perfil
    private ControladorPerfilJugador perfil;

    /**
     * Contruye este objeto pasandole un Jugador de parametro
     * @param jugador
     */
    public ControladorDetallesJugador(Jugador jugador) {
        this.jugador = jugador;
        init();
    }

    /**
     * Define la vista y el controlador de esta clase
     */
    private void init() {

        stage = new Stage();

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlantillaJugador.fxml"));
            loader.setController(this);
            stage.setScene(new Scene(loader.load()));
            stage.getIcons().add(new Image("/imagenes/detalles.png"));
            stage.setTitle("Detalles del jugador " + this.jugador.getNombre());
            stage.setResizable( false );
            stage.initModality(Modality.APPLICATION_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Namas cargue la ventana que muestre la vista perfil
        mostrarPerfil();

        // Mostrar Perfil
        btnPerfil.setOnAction(e -> {
            mostrarPerfil();
        });

        // Mostrar Estadisticas
        btnEstadisticas.setOnAction(e -> {
            mostrarEstadisticas();
        });

    }

    /**
     * Inserta dentro del contenedor AnchorPane la vista perfil
     */
    private void mostrarPerfil() {
        this.contenedor.getChildren().clear();
        perfil = new ControladorPerfilJugador(this.jugador);
        this.contenedor.getChildren().add(perfil.getRoot());
    }

    /**
     * Inserta dentro del contenedor AnchorPane la vista estadisticas
     */
    private void mostrarEstadisticas() {
        this.jugador = perfil.getJugador();
        this.contenedor.getChildren().clear();
        ControladorEstadisticas estadisticas = new ControladorEstadisticas(this.jugador);
        this.contenedor.getChildren().add(estadisticas.getRoot());
    }

    public void showStage() {
        stage.show();
    }

}
