package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.jfoenix.controls.JFXTextField;
import dominio.Jugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modelo.*;

public class ControladorTablaJugadores implements Initializable {

	@FXML private TableView<Jugador> tablaJugadores;
	@FXML private TableColumn<Jugador, String> col_nombre;
	@FXML private TableColumn<Jugador, String> col_procedencia;
	@FXML private TableColumn<Jugador, String> col_peso;
	@FXML private TableColumn<Jugador, String> col_altura;
	@FXML private TableColumn<Jugador, String> col_posicion;
	@FXML private TableColumn<Jugador, String> col_nombreEquipo;

	@FXML private Button addPlayer;
	@FXML private JFXTextField buscarNombre;

	// Encargado de realizar las conexiones con la BDDD
	private ConexionBDD conexion = new ConexionBDD();
	private ObservableList<Jugador> jugadores = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cargarPropiedadesTablaJugadores();
		leerTablaJugadores();

		// Abrir dialogo para la creacion de un nuevo jugador
		addPlayer.setOnMouseClicked( e -> {
			abrirVentanaCreacionJugador();
		});

		// Añadir filtrado a la tabla jugadores
		generarFiltradoTabla();
	}

	/**
	 * Aqui se crean las propiedades de las columnas de la tabla jugadores.
	 * A cada una de las columnas se les indica que atributo del objeto Jugadores van a leer.
	 */
	private void cargarPropiedadesTablaJugadores() {
		col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		col_procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		col_peso.setCellValueFactory(new PropertyValueFactory<>("peso"));
		col_altura.setCellValueFactory(new PropertyValueFactory<>("altura"));
		col_posicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
		col_nombreEquipo.setCellValueFactory(new PropertyValueFactory<>("equipo"));

		// AGREGAR EL OBSERVABLE LIST A TABLA JUGADORES
		tablaJugadores.setItems( jugadores );
	}

	/**
	 * Genera un evento para que la tabla vaya buscando las coincidencias que
	 * se van escribiendo en el textField buscarNombre.
	 */
	private void generarFiltradoTabla() {

		/* Creamos el siguiente filtrado para la busqueda de la tabla jugadores:
				1.- Busqueda por nombre.
				2.- Busqueda por procedencia.
				3.- Busqueda por equipo.
				4.- Busqueda por posicion.
		 */
		FilteredList<Jugador> filter = new FilteredList<>(jugadores, e -> true);
		buscarNombre.textProperty().addListener((observableValue, oldValue, newValue) -> {

			// System.out.println("Observable value - " + observableValue + "\nOld Value - " + oldValue + "\nNewValue - " + newValue);
			filter.setPredicate((Predicate<? super Jugador>) jugador -> {
				if (newValue == null || newValue.isEmpty())
					return true;

				if (jugador.getNombre().contains( newValue ))
					return true;

				if (jugador.getProcedencia() != null && jugador.getProcedencia().contains( newValue ))
					return true;

				if (jugador.getEquipo().contains( newValue ))
					return true;

				if (jugador.getPeso().contains( newValue ))
					return true;

				return jugador.getPosicion().contains( newValue );
			});

			SortedList<Jugador> datosFiltrados = new SortedList<>(filter);
			datosFiltrados.comparatorProperty().bind(tablaJugadores.comparatorProperty());
			tablaJugadores.setItems( datosFiltrados );
		});

	}

	/**
	 * Se leen de la base de datos "nba" todos los campos de la tabla "Jugadores" y se añaden al
	 * observableList jugadores.
	 */
	private void leerTablaJugadores() {

		try {

			PreparedStatement ps = conexion.con.prepareCall("SELECT * FROM jugadores");
			ResultSet rs = conexion.realizarConsulta( ps );

			while (rs.next()) {

				String nombre = rs.getString("Nombre");
				String procedencia = rs.getString("Procedencia");
				String altura = rs.getString("Altura");
				String peso = rs.getString("Peso");
				String posicion = rs.getString("Posicion");
				String nombreEquipo = rs.getString("Nombre_equipo");

				Jugador jugador = new Jugador(nombre, procedencia, altura, peso, posicion, nombreEquipo);
				jugadores.add( jugador );

				System.out.println("Adding " + jugador.getNombre() + " to the table.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void abrirVentanaCreacionJugador() {

		Stage stage = new Stage();
		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/CreacionJugador.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		stage.setScene(new Scene(root));
		stage.getIcons().add(new Image("/imagenes/player.png"));
		stage.setTitle("Creación Jugador");
		stage.setResizable(false);
		stage.show();
	}

}
