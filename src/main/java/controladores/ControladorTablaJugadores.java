package controladores;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.*;

public class ControladorTablaJugadores implements Initializable {


	@FXML private TableView<Jugador> tablaJugadores;
	@FXML private TableColumn<Jugador, String> col_nombre;
	@FXML private TableColumn<Jugador, String> col_procedencia;
	@FXML private TableColumn<Jugador, String> col_peso;
	@FXML private TableColumn<Jugador, String> col_altura;
	@FXML private TableColumn<Jugador, String> col_posicion;
	@FXML private TableColumn<Jugador, String> col_nombreEquipo;

	// Encargado de realizar las conexiones con la BDDD
	private ConexionBDD conexion = new ConexionBDD();

	ObservableList<Jugador> jugadores = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		leerTablaJugadores();

		col_nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		col_procedencia.setCellValueFactory(new PropertyValueFactory<>("Procedencia"));
		col_peso.setCellValueFactory(new PropertyValueFactory<>("Peso"));
		col_altura.setCellValueFactory(new PropertyValueFactory<>("Altura"));
		col_posicion.setCellValueFactory(new PropertyValueFactory<>("Posicion"));
		col_nombreEquipo.setCellValueFactory(new PropertyValueFactory<>("Equipo"));

		// AGREGAR EL OBSERVABLE LIST A TABLA JUGADORES
		tablaJugadores.setItems( jugadores );

	}

	private void leerTablaJugadores() {

		try {

			Statement stmt = conexion.getConection().createStatement();
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
