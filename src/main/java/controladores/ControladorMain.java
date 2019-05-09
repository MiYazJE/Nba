package controladores;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
import modelo.Jugador;

public class ControladorMain implements Initializable {


	@FXML private Button btnConectar;
	@FXML private TableView<Jugador> tablaJugadores;
	@FXML private TableColumn<Jugador, String> col_nombre;
	@FXML private TableColumn<Jugador, String> col_procedencia;
	@FXML private TableColumn<Jugador, String> col_peso;
	@FXML private TableColumn<Jugador, String> col_altura;
	@FXML private TableColumn<Jugador, String> col_posicion;
	@FXML private TableColumn<Jugador, String> col_nombreEquipo;


	private static final String URL = "jdbc:mysql://localhost:3306/nba";
	private static final String USERNAME = "root";
	// WINDOWS -> "root" | LINUX -> "Roo|"
	private static final String PASSWORD = "root";

	ObservableList<Jugador> jugadores = FXCollections.observableArrayList();


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// EVENTO BOTON CONEXION BDDD
		btnConectar.setOnMouseClicked(e -> {
			realizarConexion();
		});

		col_nombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
		col_procedencia.setCellValueFactory(new PropertyValueFactory<>("Procedencia"));
		col_peso.setCellValueFactory(new PropertyValueFactory<>("Peso"));
		col_altura.setCellValueFactory(new PropertyValueFactory<>("Altura"));
		col_posicion.setCellValueFactory(new PropertyValueFactory<>("Posicion"));
		col_nombreEquipo.setCellValueFactory(new PropertyValueFactory<>("Equipo"));

		// Agregar el observableList a la tablaJugadores
		tablaJugadores.setItems( jugadores );

	}

	private void realizarConexion() {

		try {

			// Establece la conexión
			Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

			// Ejecuta la consulta
			Statement stmt = con.createStatement();
			ResultSet rs   = stmt.executeQuery("SELECT Nombre, Procedencia, Altura, Peso, Posicion, Nombre_equipo FROM jugadores");

			// Procesa los resultados
			while (rs.next()) {

				// Leer columnas
				String nombre = rs.getString("Nombre");
				String procedencia = rs.getString("Procedencia");
				String altura = rs.getString("Altura");
				String peso = rs.getString("Peso");
				String posicion = rs.getString("Posicion");
				String nombreEquipo = rs.getString("Nombre_equipo");

				Jugador jugador = new Jugador(nombre, procedencia, altura, peso, posicion, nombreEquipo);
				jugadores.add( jugador );

				System.out.println( "Añadiendo nuevo jugador a la tabla: " + jugador );
			}

			//Cerrar la conexión
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
