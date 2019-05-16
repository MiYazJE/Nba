/**
 * @author Ruben Saiz
 */
package controladores;

import dominio.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorPerfilJugador implements Initializable {

    @FXML private Text nombreJugador;
    @FXML private Text procedencia;
    @FXML private Text altura;
    @FXML private Text peso;
    @FXML private Text posicion;
    @FXML private Text nombreEquipo;
    @FXML private ImageView imgEquipo;

    private Jugador jugador;
    private Parent root;

    public ControladorPerfilJugador(Jugador jugador) {
        this.jugador = jugador;
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cargarInformacionJugador();
    }

    /**
     * Define la vista y el controlador
     */
    private void init() {

        root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PerfilJugador.fxml"));
            loader.setController( this );
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Devuelve el parent de esta vista
     * @return Parent
     */
    public Parent getRoot() {
        return this.root;
    }

    /**
     * Carga la informacion del jugador
     */
    private void cargarInformacionJugador() {

        this.nombreJugador.setText(jugador.getNombre());
        this.altura.setText(jugador.getAltura());
        this.nombreEquipo.setText(jugador.getEquipo());
        this.peso.setText(jugador.getPeso());
        this.procedencia.setText(jugador.getProcedencia());
        this.posicion.setText(jugador.getPosicion());
        imgEquipo.setImage(new Image(rutaImagenEquipo(jugador.getEquipo())));

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

}
