package controladores;

import java.net.URL;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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
	private ConexionBDD conexion = ControladorMain.conexion;

	ObservableList<Jugador> jugadores = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		leerTablaJugadores();

		col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		col_procedencia.setCellValueFactory(new PropertyValueFactory<>("procedencia"));
		col_peso.setCellValueFactory(new PropertyValueFactory<>("peso"));
		col_altura.setCellValueFactory(new PropertyValueFactory<>("altura"));
		col_posicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
		col_nombreEquipo.setCellValueFactory(new PropertyValueFactory<>("equipo"));

		// AGREGAR EL OBSERVABLE LIST A TABLA JUGADORES
		tablaJugadores.setItems( jugadores );

		// BORRAR DE AQUI HACIA ABAJO
		addPlayer.setOnMouseClicked( e -> {
			Jugador player = new Jugador("Ruben Saiz", "España", "1.72", "85", "Portero", "Real Madrid");
			jugadores.add( player );
			System.out.println("Adding " + player.getNombre() + " to the table.");
		});

		// TODO hacer que el textfield buscarNombre filtre la tabla
		FilteredList<Jugador> filter = new FilteredList<>(jugadores, e -> true);
		buscarNombre.textProperty().addListener((observableValue, oldValue, newValue) -> {

			filter.setPredicate((Predicate<? super Jugador>) jugador -> {
				if (newValue == null || newValue.isEmpty())
					return true;

				if (jugador.getNombre().contains( newValue ))
					return true;

				return jugador.getEquipo().contains( newValue );

			});

			SortedList<Jugador> datosFiltrados = new SortedList<>(filter);
			datosFiltrados.comparatorProperty().bind(tablaJugadores.comparatorProperty());
			tablaJugadores.setItems( datosFiltrados );
		});

	}

	private void leerTablaJugadores() {

		try {

			ResultSet rs   = conexion.realizarConsulta("SELECT Nombre, Procedencia, Altura, Peso, Posicion, Nombre_equipo FROM jugadores");

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

}
