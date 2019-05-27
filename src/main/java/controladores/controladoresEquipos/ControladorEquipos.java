/**
 * @author Ruben Saiz
 */
package controladores.controladoresEquipos;

import com.jfoenix.controls.JFXButton;
import dominio.Equipo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private boolean obtenerEquipos() {

        this.equiposEste = new ArrayList<>();
        this.equiposOste = new ArrayList<>();

        return  consultaEquipos(this.equiposEste, "East") &&
                consultaEquipos(this.equiposOste, "West");
    }

    private boolean consultaEquipos(ArrayList<Equipo> equipos, String zona) {

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
                System.out.println("Equipo " + equipo.getNombre() + " leído correctamente.");
            }
            return true;

       } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
       }

    }

    public void mostrar(boolean estado) {
        this.contenedor.setVisible(estado);
        this.scroll.setVisible(estado);
    }

    @Override
    public void run() {

        // Las consultas han fallado
        if (!obtenerEquipos()) return;

        int n = 3;
        int left;
        Parent cajaEquipo;

        // EQUIPOS "ESTE"
        VBox cajaTitulo = cajaConferencia("Este");
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
    private VBox cajaConferencia(String conferencia) {

        Text titulo = new Text("Conferencia " + conferencia);
        titulo.setWrappingWidth(1085);
        titulo.getStyleClass().add( "tituloEquipo" );
        titulo.setTextAlignment(TextAlignment.CENTER);

        JFXButton boton = new JFXButton("Crear equipo");
        boton.setMinHeight(70);
        boton.setMinWidth(150);
        boton.setOnAction(e -> {
            ControladorCreacionEquipo crearEquipo = new ControladorCreacionEquipo(conferencia);
            crearEquipo.mostrar();
        });
        boton.getStyleClass().add("btnSlideLeft");

        VBox cajaBoton = new VBox( boton );
        cajaBoton.setMinHeight(100);
        cajaBoton.setMinWidth(1085);
        cajaBoton.setAlignment(Pos.CENTER);

        VBox cajaTitulo = new VBox( titulo );
        cajaTitulo.setMaxWidth(1085);
        cajaTitulo.setMinHeight( 80 );
        cajaTitulo.getStyleClass().add( "cajaTitulo" );
        cajaTitulo.setAlignment(Pos.CENTER);
        VBox.setMargin(cajaTitulo, new Insets(0, 0, 10, 0));

        VBox contenedor = new VBox( cajaTitulo, cajaBoton );

        return contenedor;
    }

    public Parent getRoot() {
        return this.root;
    }

}
