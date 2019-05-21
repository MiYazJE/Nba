/**
 * @author Ruben Saiz
 */
package controladores;

import dominio.Equipo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import modelo.ConexionBDD;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorEquipos implements Initializable {

    @FXML FlowPane contenedor;

    private ArrayList<Equipo> equiposEste;
    private ArrayList<Equipo> equiposOste;
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

        this.equiposEste = new ArrayList<>();
        this.equiposOste = new ArrayList<>();

        consultaEquipos(this.equiposEste, "East");
        consultaEquipos(this.equiposOste, "West");
    }

    private void consultaEquipos(ArrayList<Equipo> equipos, String zona) {

        PreparedStatement ps = null;
        ResultSet rs;
        String nombre, ciudad, conferencia, division, imagen;

        try {

            ps = conexion.con.prepareStatement(
                   "SELECT * FROM equipos WHERE conferencia = ?;");
            ps.setString(1, zona);

            rs = conexion.realizarConsulta( ps );

            while (rs.next()) {
                nombre = rs.getString("nombre");
                ciudad = rs.getString("ciudad");
                conferencia = rs.getString("conferencia");
                division = rs.getString("division");
                imagen = rs.getString("imagen");
                Equipo equipo = new Equipo(nombre, ciudad, conferencia, division, imagen);
                equipos.add( equipo );
            }

       } catch (SQLException e) {
           e.printStackTrace();
       }

    }

    private void mostrarEquipos() {

        int n = 3;
        int left;
        Parent cajaEquipo;

        // EQUIPOS "ESTE"
        HBox cajaTitulo = cajaConferencia("Este");
        this.contenedor.getChildren().add( cajaTitulo );

        for (Equipo equipo : this.equiposEste) {

            ControladorCajaEquipo contenedorEquipo = new ControladorCajaEquipo(equipo);
            cajaEquipo = contenedorEquipo.getRoot();
            this.contenedor.getChildren().add( cajaEquipo );

            left = (n % 3 == 0) ? 70 : 20;
            FlowPane.setMargin(cajaEquipo, new Insets(20, 20, 20, left));
            n++;
        }

        // EQUIPOS OESTE
        cajaTitulo = cajaConferencia("Oeste");
        this.contenedor.getChildren().add( cajaTitulo );
        FlowPane.setMargin(cajaTitulo, new Insets(40, 0, 0, 0));

        n = 3;
        for (Equipo equipo : this.equiposOste) {

            ControladorCajaEquipo contenedorEquipo = new ControladorCajaEquipo(equipo);
            cajaEquipo = contenedorEquipo.getRoot();
            this.contenedor.getChildren().add( cajaEquipo );

            left = (n % 3 == 0) ? 80 : 20;
            FlowPane.setMargin(cajaEquipo, new Insets(20, 20, 20, left));
            n++;
        }

    }

    private HBox cajaConferencia(String conferencia) {

        Text titulo = new Text("Conferencia " + conferencia);
        titulo.setWrappingWidth(1085);
        titulo.getStyleClass().add( "tituloEquipo" );
        titulo.setTextAlignment(TextAlignment.CENTER);

        HBox cajaTitulo = new HBox( titulo );
        cajaTitulo.setMaxWidth(1085);
        cajaTitulo.setMinHeight( 100 );
        cajaTitulo.getStyleClass().add( "cajaTitulo" );
        cajaTitulo.setAlignment(Pos.CENTER);

        return cajaTitulo;
    }

}
