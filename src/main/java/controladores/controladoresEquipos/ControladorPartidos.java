/**
 * @author Ruben Saiz
 */
package controladores.controladoresEquipos;

import com.jfoenix.controls.JFXComboBox;
import dominio.Equipo;
import dominio.Partido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControladorPartidos implements Initializable {

    @FXML private TableView<Partido> tablaPartidos;
    @FXML private TableColumn<Partido, String> col_Local;
    @FXML private TableColumn<Partido, String> col_Visitante;
    @FXML private TableColumn<Partido, Integer> col_PuntosLocal;
    @FXML private TableColumn<Partido, Integer> col_PuntosVisitante;
    @FXML private TableColumn<Partido, String> col_Temporada;
    @FXML private ImageView imgVolver;
    @FXML private JFXComboBox<String> comboTemporada;

    private ObservableList<Partido> partidos = FXCollections.observableArrayList();
    private Parent root;
    private Equipo equipo;
    ConexionBDD conexion;

    public ControladorPartidos(Equipo equipo) {
        this.equipo = equipo;
        init();
    }

    private void init() {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Partidos.fxml"));
        loader.setController(this);

        try {
            this.root = (Parent) loader.load();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conexion = new ConexionBDD();

        cargarComboTemporadas();

        this.imgVolver.setOnMousePressed(e -> {
            ((Stage)imgVolver.getScene().getWindow()).close();
        });

        comboTemporada.setOnAction(e -> {
            cargarPartidos(this.comboTemporada.getValue());
        });

        cargarPropiedadesTabla();
    }

    private void cargarPropiedadesTabla() {

        col_Local.setCellValueFactory(new PropertyValueFactory<>("local"));
        col_Visitante.setCellValueFactory(new PropertyValueFactory<>("visitante"));
        col_PuntosLocal.setCellValueFactory(new PropertyValueFactory<>("puntosLocal"));
        col_PuntosVisitante.setCellValueFactory(new PropertyValueFactory<>("puntosVisitante"));
        col_Temporada.setCellValueFactory(new PropertyValueFactory<>("temporada"));

        tablaPartidos.setItems( partidos );
    }

    private void cargarComboTemporadas() {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT DISTINCT temporada from partidos;");

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String temporada = rs.getString("temporada");
                this.comboTemporada.getItems().add(temporada);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void cargarPartidos(String temporada) {

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT * FROM partidos WHERE (equipo_local = ? || equipo_visitante = ?) && temporada = ?;");

            ps.setString(1, this.equipo.getNombre());
            ps.setString(2, this.equipo.getNombre());
            ps.setString(3, temporada);

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                String local = rs.getString("equipo_local");
                String visitante = rs.getString("equipo_visitante");
                int puntosLocal = rs.getInt("puntos_local");
                int puntosVisitante = rs.getInt("puntos_visitante");
                String temporadaBDD = rs.getString("temporada");
                Partido partido = new Partido(
                        local, visitante, puntosLocal, puntosVisitante, temporadaBDD);
                this.partidos.add( partido );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public Parent getRoot() {
        return this.root;
    }

}
