/**
 * @author Ruben Saiz
 */
package controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import modelo.ConexionBDD;

public class ControladorDialogoCierre {

    @FXML
    void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void salir(ActionEvent event) {
        ConexionBDD.cerrarConexion();
        System.exit(0);
    }

}
