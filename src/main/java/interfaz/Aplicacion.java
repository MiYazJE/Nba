package interfaz;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Aplicacion extends Application {


	@Override
	public void start(Stage ventana) {

		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene( root );

		// PROPIEDADES ESCENARIO
		ventana.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icon.png")));
		ventana.setScene(scene);
		ventana.setTitle("NBA");
		ventana.setResizable(false);

		ventana.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
