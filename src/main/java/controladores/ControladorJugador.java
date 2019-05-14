package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dominio.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorJugador implements Initializable {

    @FXML private JFXTextField nombre;
    @FXML private JFXTextField posicion;
    @FXML private ImageView imagen;
    @FXML private JFXButton btnEditar;

    private Jugador jugador;
    private Stage stage;


    public ControladorJugador(Jugador jugador, ControladorTablaJugadores tabla) {
        init();
        this.jugador = jugador;
        cargarDatosJugador();
    }

    private void init() {

        this.stage = new Stage();

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PlantillaJugador.fxml"));
            loader.setController(this);
            this.stage.setScene(new Scene(loader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }

    /**
     * Carga los datos del jugador en los diferentes campos, tambien agrega una imagen de su equipo
     */
    private void cargarDatosJugador() {

        nombre.setText(jugador.getNombre());
        posicion.setText(jugador.getPosicion());
        imagen.setImage(new Image(rutaImagenEquipo(jugador.getEquipo())));

    }

    /**
     * Cargar imagen de el equipo
     */
    private String rutaImagenEquipo(String equipo) {

        String ruta = "/imagenes/logosNba/";

        switch(equipo) {

            case "76ers": ruta += "philadelphia76ers"; break;
            case "Bobcats": ruta += ""; break;
            case "Bucks":ruta += "milwaukeeBucks"; break;
            case "Bulls": ruta += "chicagoBulls"; break;
            case "Cavaliers": ruta += "clevelandCavaliers"; break;
            case "Celtics": ruta += "celtics"; break;
            case "Clippers": ruta += "losAngelesClippers"; break;
            case "Grizzlies": ruta += "memphisGrizzlies"; break;
            case "Hawks": ruta += "atlantaHawks"; break;
            case "Heat": ruta += "miamiHeat"; break;
            case "Hornets": ruta+= "charlotteHornets"; break;
            case "Jazz": ruta += "utahJazz"; break;
            case "Kings": ruta += ""; break;
            case "Knicks": ruta += "newYorkNicks"; break;
            case "Lakers": ruta += "losAngelesLakers"; break;
            case "Magig": ruta += "orlandoMagic"; break;
            case "Mavericks": ruta += "dallasMaverick"; break;
            case "Nets": ruta += "brooklynNets"; break;
            case "Nuggets": ruta += "denverNuggets"; break;
            case "Pacers": ruta += "indianaPacers"; break;
            case "Pistons": ruta += "detroitPistons"; break;
            case "Raptors": ruta += "torontoRaptors"; break;
            case "Rockets": ruta += "houstonRockets"; break;
            case "Spurs": ruta += "sanAntonioSpurs"; break;
            case "Suns": ruta += "phoenixSuns"; break;
            case "Supersonics": ruta += ""; break;
            case "Timberwolves": ruta += "minesotaTimewolves"; break;
            case "Trail Blazers": ruta += "portlandTrailBlazers"; break;
            case "Warriors": ruta += "goldenStateWarriors"; break;
            case "Wizards": ruta += "washingtonWizards"; break;
            default: ruta += "washingtonWizards";
        }

        ruta += ".png";
        return ruta;
    }



    public void showStage() {
        this.stage.showAndWait();
    }

}
