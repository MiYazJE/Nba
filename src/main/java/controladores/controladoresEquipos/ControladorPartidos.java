/**
 * @author Ruben Saiz
 */
package controladores.controladoresEquipos;

import dominio.Equipo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ControladorPartidos {

    private Parent root;
    private Equipo equipo;

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

    public Parent getRoot() {
        return this.root;
    }

}
