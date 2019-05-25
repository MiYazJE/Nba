/**
 * @author Ruben Saiz
 */
package controladores;

import com.sun.javafx.fxml.FXMLLoaderHelper;
import dominio.Equipo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControladorEquipos implements Initializable, Runnable {

    @FXML FlowPane contenedor;
    @FXML ScrollPane scroll;

    private ArrayList<Equipo> equiposEste;
    private ArrayList<Equipo> equiposOste;
    private ConexionBDD conexion = new ConexionBDD();
    private Parent root;

    public ControladorEquipos() {
        init();
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/Equipos.fxml"));
            loader.setController(this);
            this.root = (Parent) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrar( false );
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
                System.out.println("Equipo " + equipo.getNombre() + " leido correctamente.");
            }

       } catch (SQLException e) {
           e.printStackTrace();
       }

    }

    public void mostrar(boolean estado) {
        this.contenedor.setVisible(estado);
        this.scroll.setVisible(estado);
    }

    @Override
    public void run() {

        obtenerEquipos();

        int n = 3;
        int left;
        Parent cajaEquipo;

        // EQUIPOS "ESTE"
        HBox cajaTitulo = cajaConferencia("Este");
        this.contenedor.getChildren().add( cajaTitulo );

        if (this.equiposEste == null) return;

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

        System.out.println("Equipos preparados para mostrar!");
    }

    /**
     * Genera un componente Hbox con un titulo que indica lo que le pases como parametro
     * @param conferencia
     * @return Hbox
     */
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

    public Parent getRoot() {
        return this.root;
    }

}
