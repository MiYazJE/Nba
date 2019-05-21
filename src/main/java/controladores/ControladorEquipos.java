/**
 * @author Ruben Saiz
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import dominio.Equipo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import modelo.ConexionBDD;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorEquipos implements Initializable {

    @FXML FlowPane contenedor;

    private ArrayList<Equipo> listaEquipos;
    private ConexionBDD conexion = new ConexionBDD();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        obtenerEquipos();
        mostrarEquipos();
    }

    /**
     * Obtiene todos los equipos de la nba
     */
    private void obtenerEquipos() {

        this.listaEquipos = new ArrayList<>();

        try {

            PreparedStatement ps = conexion.con.prepareStatement(
                    "SELECT * FROM equipos;");

            ResultSet rs = conexion.realizarConsulta( ps );

            while (rs.next()) {

                String nombre = rs.getString("nombre");
                String ciudad = rs.getString("ciudad");
                String conferencia = rs.getString("conferencia");
                String division = rs.getString("division");
                String imagen = rs.getString("imagen");

                Equipo equipo = new Equipo(nombre, ciudad, conferencia, division, imagen);
                this.listaEquipos.add( equipo );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void mostrarEquipos() {

        for (Equipo equipo : this.listaEquipos) {

            ControladorCajaEquipo contenedorEquipo = new ControladorCajaEquipo(equipo);
            Parent cajaEquipo = contenedorEquipo.getRoot();
            this.contenedor.getChildren().add( cajaEquipo );
            FlowPane.setMargin(cajaEquipo, new Insets(20, 20, 20, 20));
        }

    }

}
