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
	public void start(Stage escenario) {

		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/InterfazPrincipal.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(root);
		
		// PROPIEDADES ESCENARIO
		escenario.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icon.png")));
		escenario.setScene(scene);
		escenario.setTitle("NBA");
		escenario.setResizable(false);

		escenario.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
