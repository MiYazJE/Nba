/**
 * @author Ruben Saiz
 */
package controladores;

import dominio.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorEstadisticas implements Initializable {

    private Jugador jugador;
    private Parent root;

    public ControladorEstadisticas(Jugador jugador) {
        this.jugador = jugador;
        init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Define la vista y el controlador
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

    public Parent getRoot() {
        return this.root;
    }


}
