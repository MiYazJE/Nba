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

public class ControladorPlantilla implements Initializable {

    @FXML private JFXButton btnEditar;
    @FXML private ImageView imgVolver;
    @FXML private ImageView imgEquipo;
    @FXML private Text textNombreEquipo;
    @FXML private Text textVictorias;
    @FXML private Text textDivision;
    @FXML private Text textConferencia;
    @FXML private JFXButton btnAgregarJugador;

    @FXML private TableView<Jugador> tablaJugadores;
    @FXML private TableColumn<Jugador, String> col_Nombre;
    @FXML private TableColumn<Jugador, String> col_Procedencia;
    @FXML private TableColumn<Jugador, String> col_Peso;
    @FXML private TableColumn<Jugador, String> col_Altura;
    @FXML private TableColumn<Jugador, String> col_Posicion;
    @FXML private TableColumn<Jugador, String> col_Equipo;

    private ObservableList<Jugador> jugadores = FXCollections.observableArrayList();
    private ConexionBDD conexion;
    private Equipo equipo;
    private Parent root;

    private int victorias;
    private int derrotas;

    public ControladorPlantilla(Equipo equipo) {
        this.equipo = equipo;
        init();
    }

    private void init() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/PlantillaEquipos.fxml"));
        loader.setController(this);

        try {
            this.root = (Parent) loader.load();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Parent getRoot() {
        return this.root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.conexion = new ConexionBDD();

        cargarPropiedadesTablaJugadores();
        leerJugadores();
        cargarDatos();

        this.btnEditar.setOnAction(e -> {
            editarJugador();
        });

        this.imgVolver.setOnMousePressed(e -> {
            ((Stage)imgVolver.getScene().getWindow()).close();
        });

        this.btnAgregarJugador.setOnAction(e -> crearJugador());

    }

    private void cargarDatos() {
        cargarImagen();
        cargarTextos();
    }

    private void cargarImagen() {

        String ruta = "";

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT imagen from equipos where nombre = ?;");
            ps.setString(1, this.equipo.getNombre());

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                ruta = rs.getString("imagen");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        this.imgEquipo.setImage(new Image( "file:" + ruta ));
    }

    private void cargarTextos() {
        this.textDivision.setText(this.equipo.getDivision());
        this.textNombreEquipo.setText(this.equipo.getNombre());
        this.textConferencia.setText(this.equipo.getConferencia());
        leerVictorias();
        this.textVictorias.setText(this.victorias + " v - " + this.derrotas + " d");
    }

    /**
     * Lee de la base de datos todos los partidos jugados por un equipo y hace
     * un tracking sobre los partidos jugados
     */
    private void leerVictorias() {

        this.derrotas  = 0;
        this.victorias = 0;

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT * FROM partidos WHERE (equipo_local = ? || equipo_visitante = ?) && temporada = ?;");
            ps.setString(1, this.equipo.getNombre());
            ps.setString(2, this.equipo.getNombre());
            ps.setString(3, "07/08");

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String equipoLocal     = rs.getString("equipo_local");
                String equipoVisitante = rs.getString("equipo_visitante");
                int puntosLocal        = rs.getInt("puntos_local");
                int puntosVisitante    = rs.getInt("puntos_visitante");
                String temporada       = rs.getString("temporada");
                Partido partido = new Partido(equipoLocal, equipoVisitante, puntosLocal,
                        puntosVisitante, temporada);

                if (equipoLocal.equals(this.equipo.getNombre())){
                    if (puntosLocal > puntosVisitante) victorias++;
                    else derrotas++;
                }
                else {
                    if (puntosVisitante > puntosLocal) victorias++;
                    else derrotas++;
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void cargarPropiedadesTablaJugadores() {
        col_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_Procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
        col_Peso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        col_Altura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        col_Posicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        col_Equipo.setCellValueFactory(new PropertyValueFactory<>("equipo"));

        // AGREGAR EL OBSERVABLE LIST A TABLA JUGADORES
        tablaJugadores.setItems( jugadores );
    }

    private void leerJugadores() {

        jugadores.clear();

        try {

            PreparedStatement ps = conexion.con.prepareCall("SELECT * FROM jugadores WHERE nombre_equipo = ?;");
            ps.setString(1, this.equipo.getNombre());
            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {

                String nombre = rs.getString("nombre");
                String procedencia = rs.getString("procedencia");
                String altura = rs.getString("altura");
                String peso = rs.getString("peso");
                String posicion = rs.getString("posicion");
                String nombreEquipo = rs.getString("nombre_equipo");

                Jugador jugador = new Jugador(nombre, procedencia, altura, peso, posicion, nombreEquipo);
                jugadores.add( jugador );
                System.out.println("Añadiendo " + jugador.getNombre() + " a la tabla.");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private void editarJugador() {

        Jugador jugador = this.tablaJugadores.getSelectionModel().getSelectedItem();

        if (jugador != null) {
            mostrarEdicion( jugador );
        }
        else {
            StackPane stackPane = (StackPane) this.btnEditar.getScene().getRoot();
            Mensaje.mostrar(stackPane, "Por favor seleccione un jugador.");
        }

    }

    public void mostrarEdicion(Jugador jugador) {

        ControladorDetallesJugador controlador = new ControladorDetallesJugador( jugador );
        controlador.showStage();

        controlador.stage.setOnHiding(e -> {
            leerJugadores();
        });

    }

    private void crearJugador() {

        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/CreacionJugador.fxml"));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("/imagenes/player.png"));
            stage.setTitle("Creación Jugador");
            stage.setResizable(false);
            stage.show();

            stage.setOnHiding(e -> leerJugadores());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
