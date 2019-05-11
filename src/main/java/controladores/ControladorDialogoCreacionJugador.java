/**
 * @author Ruben Saiz
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import modelo.ConexionBDD;

import java.net.URL;
import java.util.ResourceBundle;

public class ControladorDialogoCreacionJugador implements Initializable {

    @FXML private JFXTextField fieldNombre;
    @FXML private JFXTextField fieldProcedencia;
    @FXML private JFXTextField fieldAltura;
    @FXML private JFXTextField fieldPeso;
    @FXML private JFXTextField fieldPosicion;
    @FXML private JFXTextField fieldEquipo;
    @FXML private JFXButton btnGuardar;
    @FXML private JFXButton btnCerrar;

    private ConexionBDD conexion = new ConexionBDD();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnGuardar.setOnAction(e -> {
            // Validar inputs
            if (validarInformacion()) {
                // Crear la consulta e insertarla en la base de datos
            }
            else {
                // Lanzar una ventana de error

            }

        });

        // Cerrar la ventana
        btnCerrar.setOnAction(e -> {
            Stage stage = (Stage) btnCerrar.getScene().getWindow();
            stage.close();
        });

    }

    /**
     * Valida si hay algun text field vacio, si es asi devuelve false, sino true.
     * @return boolean
     */
    private boolean validarInformacion() {
        return  !fieldNombre.getText().isEmpty() &&
                !fieldProcedencia.getText().isEmpty() &&
                !fieldAltura.getText().isEmpty() &&
                !fieldPeso.getText().isEmpty() &&
                !fieldPosicion.getText().isEmpty() &&
                !fieldEquipo.getText().isEmpty();
    }

    private void ventanaError() {



    }

}
