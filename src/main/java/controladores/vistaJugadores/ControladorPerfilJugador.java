/**
 * @author Ruben Saiz
 */
package controladores.vistaJugadores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import dominio.Jugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorPerfilJugador implements Initializable {

    @FXML private JFXTextField textNombreJugador;
    @FXML private JFXTextField textProcendencia;
    @FXML private JFXTextField textAltura;
    @FXML private JFXTextField textPeso;
    @FXML private JFXTextField textPosicion;
    @FXML private JFXTextField textEquipo;
    @FXML private JFXTextField buscarJugador;
    @FXML private ImageView imgEquipo;
    @FXML private ImageView imgBuscar;
    @FXML private JFXButton btnEditar;
    @FXML private JFXButton btnGuardar;
    @FXML private JFXComboBox<String> comboPosicion;
    @FXML private JFXComboBox<String> comboEquipos;

    private Jugador jugador;
    private Parent root;
    ConexionBDD conexion = new ConexionBDD();
    private boolean modificado;
    private ObservableList<String> nombreJugadores = FXCollections.observableArrayList();
    private ObservableList<String> posiciones = FXCollections.observableArrayList(Arrays.asList(
            "F-G", "G-F", "C", "G", "F", "C-F", "F-C", "V"
    ));
    private ObservableList<String> equipos = FXCollections.observableArrayList();

    public ControladorPerfilJugador(Jugador jugador) {
        this.jugador = jugador;
        init();
        this.modificado = false;
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

        leerNombreEquipos();
        cargarJugadores();

        // Autocompleta a medida de que escribes
        generarAutoCompletado();

        // Actualizar el jugador buscado por el actual
        buscarJugador.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                actualizarJugador();
            }
        });

        imgBuscar.setOnMousePressed(e -> {
            actualizarJugador();
        });

        btnEditar.setOnAction(e -> {
            editarComponentes();
            obtenerCodigoJugador();
        });

        btnGuardar.setOnAction(e -> {
            verificarConsulta();
        });

        mostrarComboBox( false );
        mostrarElementos( true );

        // Cargar todos los datos del jugador en los campos
        cargarInformacionJugador();
    }

    private void leerNombreEquipos() {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT nombre FROM equipos;");

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String equipo = rs.getString( "nombre" );
                equipos.add( equipo );
            }

            comboEquipos.setItems( equipos );

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void generarAutoCompletado() {
        TextFields.bindAutoCompletion(buscarJugador, nombreJugadores);
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
        this.textProcendencia.setText((jugador.getProcedencia()));
        this.textPosicion.setText(this.jugador.getPosicion());
        imgEquipo.setImage( getImagen(this.jugador.getEquipo() ));

        // Recuperar esto cuando no consigas cargar las imagenes desde la base de datos
        //imgEquipo.setImage(new Image(rutaImagenEquipo(this.jugador.getEquipo())));
    }

    /**
     * Hace una consulta a la base de datos, trae todos los nombres de los jugadores
     */
    private void cargarJugadores() {

        try {

            PreparedStatement ps = conexion.con.prepareStatement("SELECT Nombre FROM jugadores;");
            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String nombreJugador = rs.getString("Nombre");
                nombreJugadores.add( nombreJugador );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Lee la ruta de una imagen desde la base de datos, crea una imagen y la devuelve
     * @param nombreEquipo
     * @return
     */
    private Image getImagen(String nombreEquipo) {

        String ruta = getClass().getResource("/imagenes/logosNba/lakers.png").getPath();

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT imagen FROM equipos WHERE nombre = ?");

            ps.setString(1, nombreEquipo);

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) ruta = rs.getString("imagen");
            System.out.println("Leyendo imagen -> '" + ruta + "'");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Image imagen = new Image("file:" + ruta);
        return imagen;
    }

    /**
     * Cambia la propiedad "editable" por la que tu especifiques
     * @param estado
     */
    private void editable(boolean estado) {
        textNombreJugador.setEditable( estado );
        textProcendencia.setEditable( estado );
        textAltura.setEditable( estado );
        textPeso.setEditable( estado );
        textPosicion.setEditable( estado );
        textEquipo.setEditable( estado );
        this.modificado = estado;
    }

    /**
     *  Obtiene el codigo del jugador actual
     */
    private void obtenerCodigoJugador() {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT codigo FROM jugadores where nombre LIKE ?;");

            ps.setString(1, this.jugador.getNombre());

            ResultSet rs =conexion.realizarConsulta( ps );

            String codigo = "";
            while (rs.next()) codigo = rs.getString("codigo");
            this.jugador.setCodigo( codigo );

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Verifica si la consulta realizada es correcta
     */
    private void verificarConsulta() {

        if (!this.modificado) {
            alertaError("No has modificado ningún campo.");
        }
        else {

            if (!this.textNombreJugador.getText().isEmpty() &&
                !this.textEquipo.getText().isEmpty() &&
                !this.comboPosicion.getValue().isEmpty() &&
                !this.textAltura.getText().isEmpty() &&
                (this.textProcendencia.getText() == null ||
                !this.textProcendencia.getText().isEmpty()) &&
                !this.textPeso.getText().isEmpty()) {

                Jugador jugadorModificado = new Jugador(textNombreJugador.getText(), textProcendencia.getText(),
                        textAltura.getText(), textPeso.getText(), comboPosicion.getValue(), comboEquipos.getValue());
                jugadorModificado.setCodigo(this.jugador.getCodigo());

                if (actualizacionJugador( jugadorModificado )) {
                    alertaInformacion("El jugador ha sido modificado");
                    refrescarJugador( jugadorModificado );
                    mostrarComboBox( false );
                    mostrarElementos( true );
                    editable( false );
                    generarAutoCompletado();
                    cargarInformacionJugador();
                }
                else {
                    alertaError("Han habido problemas actualizando la información.");
                }

            }

        }

    }

    /**
     * Realiza una consulta en la base de datos y actualiza los datos el jugador
     */
    private boolean actualizacionJugador(Jugador newJugador) {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "UPDATE jugadores SET nombre = ?, procedencia = ?, altura = ?" +
                        ", peso = ?, posicion = ?, nombre_equipo = ? " +
                        "WHERE codigo = ?");

            // TODO tira excepcion
            ps.setString(1, newJugador.getNombre());
            //if (newJugador.getProcedencia() == null) newJugador.setProcedencia("");
            ps.setString(2, newJugador.getProcedencia());
            ps.setString(3, newJugador.getAltura());
            ps.setInt(4, Integer.valueOf(newJugador.getPeso()));
            ps.setString(5, newJugador.getPosicion());
            ps.setString(6, newJugador.getEquipo());
            ps.setInt(7, Integer.valueOf(newJugador.getCodigo()));

            return conexion.realizarUpdate( ps );

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Crea una ventana de informacion con el mensaje que le indiques
     * @param mensaje
     */
    private void alertaInformacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText( mensaje );
        alerta.setHeaderText("INFROMACIÓN");
        alerta.showAndWait();
    }

    /**
     * Crea una ventana de error con el mensaje que le indiques
     * @param mensaje
     */
    private void alertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setContentText( mensaje );
        alerta.setHeaderText("ERROR");
        alerta.showAndWait();
    }

    /**
     * El jugador modificado pasa a ser el actual
     * Eliminamos el anterior de la lista e introducimos el nuevo
     */
    private void refrescarJugador(Jugador jugadorModificado) {
        this.nombreJugadores.remove( this.jugador.getNombre() );
        this.nombreJugadores.add( jugadorModificado.getNombre() );
        this.jugador = jugadorModificado;
    }

    /**
     * Cargar imagen de el equipo
     */
    /*private String rutaImagenEquipo(String equipo) {

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
    }*/

    private void mostrarComboBox(boolean estado) {
        comboPosicion.setVisible( estado );
        comboEquipos.setVisible( estado );
    }

    private void mostrarElementos(boolean estado) {
        textPosicion.setVisible( estado );
        textEquipo.setVisible( estado );
    }

    private void editarComponentes() {
        editable( true );
        mostrarComboBox( true );
        mostrarElementos( false );
        comboPosicion.setItems( FXCollections.observableArrayList( posiciones ) );
        comboPosicion.setValue( textPosicion.getText() );
        comboEquipos.setValue( textEquipo.getText() );
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
