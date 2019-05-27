/**
 * @author Ruben Saiz
 */
package controladores.controladoresJugadores;

import dominio.Estadisticas;
import dominio.Jugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControladorEstadisticas implements Initializable {

    @FXML private TableView<Estadisticas> tablaEstadisticas;
    @FXML private TableColumn<Estadisticas, String> col_Temporada;
    @FXML private TableColumn<Estadisticas, String> col_PuntosPartido;
    @FXML private TableColumn<Estadisticas, String> col_AsistenciasPartido;
    @FXML private TableColumn<Estadisticas, String> col_TaponesPartido;
    @FXML private TableColumn<Estadisticas, String> col_RebotesPartido;

    @FXML private Text nombre;

    private Jugador jugador;
    private Parent root;
    private ConexionBDD conexion = new ConexionBDD();
    private ObservableList<Estadisticas> estadisticasJugador = FXCollections.observableArrayList();

    public ControladorEstadisticas(Jugador jugador) {
        this.jugador = jugador;
        init();
    }

    /**
     * Define la vista y el controlador de esta clase
     */
    private void init() {

        root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EstadisticasJugador.fxml"));
            loader.setController( this );
            root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        propiedadesTabla();
        leerTabaEstadisticas();
        nombre.setText(nombre.getText() + jugador.getNombre());

    }

    /**
     * Define las propiedades de la tabla estadisticas:
     *  - Indica a cada columna que atributo de Estadisticas va a obtener
     */
    private void propiedadesTabla() {
        col_Temporada.setCellValueFactory(new PropertyValueFactory<>("temporada"));
        col_PuntosPartido.setCellValueFactory(new PropertyValueFactory<>("puntosPartido"));
        col_AsistenciasPartido.setCellValueFactory(new PropertyValueFactory<>("asistenciasPartido"));
        col_TaponesPartido.setCellValueFactory(new PropertyValueFactory<>("taponesPartido"));
        col_RebotesPartido.setCellValueFactory(new PropertyValueFactory<>("rebotesPartido"));
        tablaEstadisticas.setItems( this.estadisticasJugador );
    }

    /**
     * Realiza una consulta y trae las estadisticas del jugador, cada una de las estadisticas obtenidas
     * las introduce en una lista
     */
    private void leerTabaEstadisticas() {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT * FROM estadisticas WHERE jugador IN (" +
                            "SELECT codigo from jugadores WHERE Nombre LIKE ?);");
            ps.setString(1, this.jugador.getNombre());
            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String temporada = rs.getString("temporada");
                String puntosPartido = rs.getString("Puntos_por_partido");
                String asistenciasPartido = rs.getString("Asistencias_por_partido");
                String taponesPartido = rs.getString("Tapones_por_partido");
                String rebotesPartido = rs.getString("Rebotes_por_partido");
                Estadisticas estadisticas = new Estadisticas(temporada, puntosPartido,
                        asistenciasPartido, taponesPartido, rebotesPartido);
                estadisticasJugador.add( estadisticas );
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public Parent getRoot() {
        return this.root;
    }

}
