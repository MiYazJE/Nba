package controladores;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import modelo.Jugador;

public class ControladorMain implements Initializable {


	@FXML private Button btnConectar;
	@FXML private TableView<Jugador> tablaJugadores;


	private static final String URL = "jdbc:mysql://localhost:3306/nba";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "Roo|";


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnConectar.setOnMouseClicked(e -> {
			realizarConexion();
		});

	}

	private void realizarConexion() {

		try {

			// Establece la conexión
			Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

			// Ejecuta la consulta
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Nombre, Procedencia, Peso FROM jugadores");

			// Procesa los resultados
			while (rs.next()) {

				// Leer columnas
				String nombre = rs.getString("Nombre");
				String peso = rs.getString("Peso");
				String procedencia = rs.getString("Procedencia");

				System.out.println(nombre + " " + peso + " " + procedencia);
			}

			//Cerrar la conexión
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
