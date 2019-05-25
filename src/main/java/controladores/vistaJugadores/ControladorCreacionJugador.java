/**
 * @author Ruben Saiz
 */
package controladores.vistaJugadores;

import com.jfoenix.controls.*;
import dominio.Mensaje;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modelo.ConexionBDD;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControladorCreacionJugador implements Initializable {

    @FXML private JFXTextField fieldNombre;
    @FXML private JFXTextField fieldProcedencia;
    @FXML private JFXTextField fieldAltura;
    @FXML private JFXTextField fieldPeso;
    @FXML private JFXButton btnGuardar;
    @FXML private JFXButton btnCerrar;
    @FXML private JFXComboBox<String> comboEquipo;
    @FXML private JFXComboBox<String> comboPosiciones;
    @FXML private ImageView infoPeso;
    @FXML private ImageView infoAltura;

    private ConexionBDD conexion = new ConexionBDD();
    private ResultSet rs;
    private ObservableList<String> equipos = FXCollections.observableArrayList();
    private ObservableList<String> posiciones = FXCollections.observableArrayList(Arrays.asList(
            "F-G", "G-F", "C", "G", "F", "C-F", "F-C", "V"
    ));

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Mostrar informacion de los campos al ser presionado
        insertalToolTip(infoPeso, "Debe de ser un numero");
        insertalToolTip(infoAltura, "Debe de ser un número y\n longitud máxima: 5 carácteres");

        // Rellenar los equipos en la lista equipos
        consultarEquipos();

        btnGuardar.setOnAction(e -> {
            tramitarJugador();
        });

        // Cerrar la ventana
        btnCerrar.setOnAction(e -> {
            Stage stage = (Stage) btnCerrar.getScene().getWindow();
            stage.close();
        });

        comboPosiciones.setItems( posiciones );
    }

    /**
     * Agregar tooltip con el mensaje que le pases
     * @param mensaje
     */
    private void insertalToolTip(ImageView imagen, String mensaje) {
        Tooltip tooltip = new Tooltip(mensaje);
        tooltip.setFont(new Font("Quicksand", 14));
        tooltip.setShowDelay(Duration.millis(300));
        Tooltip.install(imagen, tooltip);
    }

    /**
     * Valida si los datos introducidos estan bien, si es asi lo añadira a la base de datosy mostrara un mensaje,
     * sino muestra mensaje de error.
     */
    private void tramitarJugador() {
        if (!fieldNombre.getText().isEmpty() &&
            !fieldProcedencia.getText().isEmpty() &&
            !fieldAltura.getText().isEmpty() &&
            !fieldPeso.getText().isEmpty() &&
            !comboPosiciones.getValue().isEmpty() &&
            comboEquipo.getValue() != null) {

            boolean estadoConsulta = consultaCreacionJugador();

            if (estadoConsulta) {
                avisoJugadorAgregado(fieldNombre.getText());
                cerrarVentana();
            }
            else {
                StackPane stackPane = (StackPane) this.comboPosiciones.getScene().getRoot();
                Mensaje.mostrar(stackPane, "Han ocurrido problemas con la consulta.");
            }

        }
        else {// Lanzar una ventana de error cuando algun campo este vacio
            StackPane stackPane = (StackPane) this.comboPosiciones.getScene().getRoot();
            Mensaje.mostrar(stackPane, "Por favor, seleccione todos los campos.");
        }

    }

    /**
     * Cierra la ventana actual
     */
    private void cerrarVentana() {
        ((Stage)comboEquipo.getScene().getWindow()).close();
    }

    /**
     * Abre una ventana con un mensaje que indica que se ha insertado correctamente el usuario
     * @param nombreJugador
     */
    private void avisoJugadorAgregado(String nombreJugador) {

        Stage stage = new Stage();
        HBox caja = new HBox();
        Scene scene = new Scene(caja, 450, 200);
        stage.setScene(scene);

        ImageView imageView = new ImageView( new Image("/imagenes/succes.png") );
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        Label label = new Label("El jugador '" + nombreJugador + "' ha sido agregado\na la base de datos correctamente");
        label.setFont(new Font("Quicksand", 16));

        caja.setAlignment(Pos.CENTER);
        caja.setSpacing(20);
        caja.getChildren().addAll(imageView, label);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Recoger de la base de datos todos los nombres de los equipos en un ResultSet
     */
    private void consultarEquipos() {

        try {

            PreparedStatement ps = conexion.con.prepareCall("SELECT Nombre FROM equipos;");
            rs = conexion.realizarConsulta(ps);

            String nombre;
            while (rs.next()) {
                nombre = rs.getString("Nombre");
                equipos.add(nombre);
            }

            comboEquipo.setItems( equipos );

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Realizar consulta, crear un nuevo jugador
     * @return boolean
     */
    private boolean consultaCreacionJugador() {

        try {

            String update = "insert into jugadores (codigo, nombre, procedencia, altura, peso, posicion, nombre_equipo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = ConexionBDD.con.prepareStatement( update );
            String codigo = obtenerSiguienteCodigo();
            ps.setString(1, codigo);
            ps.setString(2, fieldNombre.getText());
            ps.setString(3, fieldProcedencia.getText());
            ps.setString(4, fieldAltura.getText());
            ps.setString(5, fieldPeso.getText());
            ps.setString(6, comboPosiciones.getValue());
            ps.setString(7, comboEquipo.getValue());

            return conexion.realizarUpdate( ps );

        } catch(SQLException e) {
            return false;
        }

    }

    /**
     * Suma 1 al ultimo codigo de la tabla jugadores en la base de datos
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
