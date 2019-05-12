package interfaz;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Aplicacion extends Application {

	private double posX;
	private double posY;

	@Override
	public void start(Stage ventana) {

		Parent root = null;

		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene( root );

		root.setOnMousePressed(e -> {
			posX = ventana.getX() - e.getScreenX();
			posY = ventana.getY() - e.getScreenY();
		});
		root.setOnMouseDragged(e -> {
			ventana.setX(e.getScreenX() + posX);
			ventana.setY(e.getScreenY() + posY);
		});

		// PROPIEDADES ESCENARIO
		ventana.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icon.png")));
		ventana.initStyle(StageStyle.UNDECORATED);
		ventana.setScene(scene);
		ventana.setTitle("NBA");
		ventana.setResizable(false);

		ventana.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
