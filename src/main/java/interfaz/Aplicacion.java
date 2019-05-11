package interfaz;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.ConexionBDD;

public class Aplicacion extends Application {

	private double posX;
	private double posY;

	@Override
	public void start(Stage primaryStage) {

		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/InterfazPrincipal.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene( root );

		// PROPIEDADES ESCENARIO
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icon.png")));
		primaryStage.setScene(scene);
		primaryStage.setTitle("NBA");
		primaryStage.setResizable(false);

		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			dialogoAlCerrar();
		});

		// TODO realizar un loggin para la conexion con la BDD
		ConexionBDD.abrirConexion();

		primaryStage.show();
	}

	private void dialogoAlCerrar() {

		Stage stage = new Stage();
		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/DialogoCierre.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene( root );

		// MOVER LA VENTANA
		root.setOnMousePressed(e -> {
			posX = stage.getX() - e.getScreenX();
			posY = stage.getY() - e.getScreenY();
		});
		root.setOnMouseDragged(e -> {
			stage.setX(e.getScreenX() + posX);
			stage.setY(e.getScreenY() + posY);
		});

		stage.setScene( scene );
		stage.initStyle(StageStyle.UTILITY);
		stage.getIcons().add(new Image("/imagenes/information.png"));
		stage.setResizable( false );
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
