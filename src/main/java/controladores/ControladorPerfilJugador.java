/**
 * @author Ruben Saiz
 */
package controladores;

import com.jfoenix.controls.JFXTextField;
import dominio.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import modelo.ConexionBDD;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorPerfilJugador implements Initializable {

    @FXML private Text textNombreJugador;
    @FXML private Text textProcendencia;
    @FXML private Text textAltura;
    @FXML private Text textPeso;
    @FXML private Text textPosicion;
    @FXML private Text textEquipo;
    @FXML private ImageView imgEquipo;
    @FXML private JFXTextField buscarJugador;
    @FXML private ImageView imgBuscar;

    private Jugador jugador;
    private Parent root;
    private List<String> nombreJugadores;
    ConexionBDD conexion = new ConexionBDD();

    public ControladorPerfilJugador(Jugador jugador) {
        this.jugador = jugador;
        init();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Cargar todos los jugadores en una lista
        nombreJugadores = cargarJugadores();

        // Autocompleta a medida de que escribes
        TextFields.bindAutoCompletion(buscarJugador, nombreJugadores);

        // Actualizar el jugador buscado por el actual
        buscarJugador.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                actualizarJugador();
            }
        });

        imgBuscar.setOnMousePressed(e -> {
            actualizarJugador();
        });

        // Cargar todos los datos del jugador en los campos
        cargarInformacionJugador();
    }


    private void actualizarJugador() {
        if (verificarEquipoaBuscar()) {
            consultaJugador(buscarJugador.getText());
            cargarInformacionJugador();
            buscarJugador.clear();
        }
    }

    private boolean verificarEquipoaBuscar() {
        String nombreField = buscarJugador.getText();
        return !nombreField.isEmpty() &&
                nombreJugadores.contains(nombreField);
    }

    /**
     * Buscar un jugador en la base de datos por el nombre.
     */
    private void consultaJugador(String nombreJugador) {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT * FROM jugadores WHERE Nombre LIKE ?;");
            ps.setString(1, nombreJugador);
            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String nombre = rs.getString("Nombre");
                String procendencia = rs.getString("Procedencia");
                String altura = rs.getString("Altura");
                String peso = rs.getString("Peso");
                String posicion = rs.getString("Posicion");
                String nombreEquipo = rs.getString("Nombre_equipo");
                this.jugador = new Jugador(nombre, procendencia, altura, peso, posicion, nombreEquipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Carga la informacion del jugador
     */
    private void cargarInformacionJugador() {

        this.textNombreJugador.setText(this.jugador.getNombre());
        this.textAltura.setText(this.jugador.getAltura());
        this.textEquipo.setText(this.jugador.getEquipo());
        this.textPeso.setText(this.jugador.getPeso());
        this.textProcendencia.setText(this.jugador.getProcedencia());
        this.textPosicion.setText(this.jugador.getPosicion());
        imgEquipo.setImage( getImagen(this.jugador.getEquipo() ));

        // Recuperar esto cuando no consigas cargar las imagenes desde la base de datos
        //imgEquipo.setImage(new Image(rutaImagenEquipo(this.jugador.getEquipo())));
    }

    /**
     * Hace una consulta a la base de datos, trae todos los nombres de los jugadores
     * @return List
     */
    private List<String> cargarJugadores() {

        List<String> nombres = new ArrayList<>();
        try {

            PreparedStatement ps = conexion.con.prepareStatement("SELECT Nombre FROM jugadores;");
            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String nombre = rs.getString("Nombre");
                nombres.add( nombre );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombres;
    }

    /**
     * Lee la ruta de una imagen desde la base de datos, crea una imagen y la devuelve
     * @param nombreEquipo
     * @return
     */
    private Image getImagen(String nombreEquipo) {

        String ruta = "/imagenes/logosNba/lakers.png";

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT Imagen FROM Equipos WHERE Nombre = ?");

            ps.setString(1, nombreEquipo);

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) ruta = rs.getString("Imagen");
            System.out.println("Leyendo imagen -> '" + ruta + "'");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Image imagen = new Image("file:" + ruta);
        return imagen;
        // return ruta;
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
            case "Knicks": ruta += "newYorknicks"; break;
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

    /**
     * Devuelve el jugador
     */
    public Jugador getJugador() {
        return this.jugador;
    }

    /**
     * Devuelve el parent de esta vista
     * @return Parent
     */
    public Parent getRoot() {
        return this.root;
    }

}
