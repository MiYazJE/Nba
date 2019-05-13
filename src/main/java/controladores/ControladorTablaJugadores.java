package controladores;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
	@FXML private Button btnRefrescar;
	@FXML private Button btnEliminar;
	@FXML private Button btnDetalles;
	@FXML private JFXTextField buscarNombre;

	// Encargado de realizar las conexiones con la BDDD
	private ConexionBDD conexion = new ConexionBDD();
	private ObservableList<Jugador> jugadores = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cargarPropiedadesTablaJugadores();
		leerTablaJugadores();

		//Evento boton eliminar el jugador
		btnEliminar.setOnAction(e -> {
			eliminarJugador();
		});

		// Evento mostrar detalles del jugador
		btnDetalles.setOnAction(e -> {
			mostrarDetallesJugador();
		});

		// Evento refrescar, recoger todos los jugadores de la base de datos
		btnRefrescar.setOnAction(e -> {
			buscarNombre.setText("");
			jugadores.clear();
			leerTablaJugadores();
		});

		// Abrir dialogo para la creacion de un nuevo jugador
		addPlayer.setOnMouseClicked( e -> {
			abrirVentanaCreacionJugador();
		});

		// Añadir filtrado a la tabla jugadores
		generarFiltradoTabla();
	}

	/**
	 * Si hay algun campo seleccionado en la tabla eliminara el jugador seleccionado en la tabla.
	 * Sino mostrara un mensaje de error.
	 */
	private void eliminarJugador() {

		Jugador jugador = tablaJugadores.getSelectionModel().getSelectedItem();

		// Si el jugador esta null significa que no hay ninguna fila seleccionada en la tabla
		if (jugador == null) {

			// Mensaje de error
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setContentText("Debes de seleccionar un jugador en la tabla.");
			alerta.showAndWait();
		}
		else {

			// Preguntar si esta seguro
			Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
			alerta.setHeaderText(null);
			alerta.setContentText("Estas seguro que quieres eliminar al jugador '" + jugador.getNombre() + "'\n" +
								  "permanentemente de la base de datos?");

			ButtonType eliminarJugador = new ButtonType("Eliminar");
			ButtonType cancelar = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			alerta.getButtonTypes().setAll(eliminarJugador, cancelar);

			Optional<ButtonType> resultado = alerta.showAndWait();
			if (resultado.get() == eliminarJugador) {

				// Eliminar jugador
				boolean estado = consultaEliminarJugador(jugador.getNombre());
				if (estado) {
					// Consulta realizada
					infoJugadorEliminado(jugador.getNombre());
					System.out.println("El jugador " + jugador.getNombre() + "ha sido eliminado correctamente.");
					jugadores.remove(jugador);
				}
				else {
					// Mensaje de error
					Alert mensajeError = new Alert(Alert.AlertType.ERROR);
					mensajeError.setContentText("Han habido problemas a la hora de eliminar el jugador.\n" +
												"Ponte en contacto con el administrador.");
					mensajeError.showAndWait();
				}

			}

		}

	}

	/**
	 * Realizar consulta para eliminar un jugador a la base de datos.
	 */
	private boolean consultaEliminarJugador(String nombreJugador) {

		try {

			PreparedStatement ps = conexion.con.prepareStatement("delete from jugadores where nombre = ?");
			ps.setString(1, nombreJugador);
			return conexion.realizarUpdate( ps );

		} catch(SQLException e ) {
			return false;
		}

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

				String valor = newValue.toLowerCase();

				if (jugador.getNombre().toLowerCase().contains( valor ))
					return true;

				if (jugador.getProcedencia() != null && jugador.getProcedencia().toLowerCase().contains( valor ))
					return true;

				if (jugador.getEquipo().toLowerCase().contains( valor ))
					return true;

				if (jugador.getPeso().toLowerCase().contains( valor ))
					return true;

				return jugador.getPosicion().toLowerCase().contains( valor );
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
	public void leerTablaJugadores() {

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

			}

			System.out.println("Jugadores cargados en la tabla.");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Abre una ventana con inputs para la creacion del jugador
	 */
	private void abrirVentanaCreacionJugador() {

		Stage stage = new Stage();
		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/CreacionJugador.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene( root );
		stage.setScene( scene );
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.getIcons().add(new Image("/imagenes/player.png"));
		stage.setTitle("Creación Jugador");
		stage.setResizable(false);

		stage.show();
	}

	private void infoJugadorEliminado(String nombreJugador) {

		Stage stage = new Stage();
		HBox caja = new HBox();
		Scene scene = new Scene(caja, 450, 200);
		stage.setScene(scene);

		ImageView imageView = new ImageView( new Image("/imagenes/succes.png") );
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);

		Label label = new Label("El jugador '" + nombreJugador + "' ha sido eliminado\nde la base de datos correctamente");
		label.setFont(new Font("Quicksand", 16));

		caja.setAlignment(Pos.CENTER);
		caja.setSpacing(20);
		caja.getChildren().addAll(imageView, label);

		stage.initStyle(StageStyle.UTILITY);
		stage.setResizable(false);
		stage.show();
	}

	/**
	 * Abre una ventana y muestra las estadisticas del jugador mas detalladamente
	 */
	private void mostrarDetallesJugador() {




	}

}
