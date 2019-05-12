/**
 * @author Ruben Saiz
 */
package controladores;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import modelo.ConexionBDD;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControladorCreacionJugador implements Initializable {

    @FXML private JFXTextField fieldNombre;
    @FXML private JFXTextField fieldProcedencia;
    @FXML private JFXTextField fieldAltura;
    @FXML private JFXTextField fieldPeso;
    @FXML private JFXTextField fieldPosicion;
    @FXML private JFXButton btnGuardar;
    @FXML private JFXButton btnCerrar;
    @FXML private JFXComboBox<String> comboEquipo;


    private ConexionBDD conexion = new ConexionBDD();
    private ResultSet rs;
    private ObservableList<String> equipos = FXCollections.observableArrayList();
    private ControladorTablaJugadores tabla = new ControladorTablaJugadores();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Rellenar los equipos en la lista equipos
        consultarEquipos();
        comboEquipo.setItems( equipos );

        btnGuardar.setOnAction(e -> {
            tramitarJugador();
        });

        // Cerrar la ventana
        btnCerrar.setOnAction(e -> {
            Stage stage = (Stage) btnCerrar.getScene().getWindow();
            stage.close();
        });

    }

    /**
     * Valida si los datos introducidos estan bien, si es asi lo añadira a la base de datos.
     */
    private void tramitarJugador() {
        if (!fieldNombre.getText().isEmpty() &&
            !fieldProcedencia.getText().isEmpty() &&
            !fieldAltura.getText().isEmpty() &&
            !fieldPeso.getText().isEmpty() &&
            !fieldPosicion.getText().isEmpty() &&
            !comboEquipo.getValue().isEmpty()) {
            // Añadir nuevo jugador a la base de datos

            String update = "insert into jugadores (codigo, nombre, procedencia, altura, peso, posicion, nombre_equipo) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);";
            try {

                PreparedStatement ps = ConexionBDD.con.prepareStatement( update );
                String codigo = obtenerSiguienteCodigo();
                ps.setString(1, codigo);
                ps.setString(2, fieldNombre.getText());
                ps.setString(3, fieldProcedencia.getText());
                ps.setString(4, fieldAltura.getText());
                ps.setString(5, fieldPeso.getText());
                ps.setString(6, fieldPosicion.getText());
                ps.setString(7, comboEquipo.getValue());

                if (conexion.agregarJugador(ps)) {
                    // TODO Ventana jugador agregado

                    // Actualizar la tabla

                    tabla.leerTablaJugadores();

                }
                else {
                    // TODO Ventana error al agregar jugador

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        else {

            // Lanzar una ventana de error cuando algun campo este vacio
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Por favor ingrese todos los campos.");
            alerta.showAndWait();
        }

    }

    /**
     * Recoger de la base de datos todos los nombres de los equipos en un ResultSet
     */
    private void consultarEquipos() {

        try {

            PreparedStatement ps = conexion.con.prepareCall("SELECT Nombre FROM equipos;");
            rs = conexion.realizarConsulta(ps);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        cargarEquipos();
    }

    /**
     * Cargar en un combobox todos los nombres de los equipos recojidos de un resultset
     */
    private void cargarEquipos() {

        String nombre;

        try {

            while (rs.next()) {
                nombre = rs.getString("Nombre");
                equipos.add(nombre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Obtiene el siguiente codigo respecto a el ultimo codigo insertado en la base de datos en la tabla jugadores
     * @return String
     */
    private String obtenerSiguienteCodigo() {

        String codigo = "";
        try {

            PreparedStatement ps = conexion.con.prepareStatement("SELECT MAX(codigo) FROM jugadores;");
            ResultSet rs = conexion.realizarConsulta( ps );
            while (rs.next()) codigo = rs.getString(1);
            codigo = String.valueOf(Integer.valueOf(codigo) + 1);
            return codigo;

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }

    }

}
