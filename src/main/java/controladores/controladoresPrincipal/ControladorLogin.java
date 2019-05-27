/**
 * @author Ruben Saiz
 */
package controladores.controladoresPrincipal;

import com.jfoenix.controls.*;
import dominio.Mensaje;
import javafx.scene.layout.StackPane;
import modelo.FicherosCarpetas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import modelo.ConexionBDD;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorLogin implements Initializable {

    @FXML private JFXTextField fieldUsuario;
    @FXML private JFXPasswordField fieldPassword;
    @FXML private JFXButton submit;
    @FXML private ImageView imgSalir;
    @FXML private ImageView imgInfo;

    private ConexionBDD conexion;
    protected static Stage ventana;
    private FicherosCarpetas generarFicheros = new FicherosCarpetas();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Color blanco a la imagen salir
        imgSalir.setOnMouseEntered(e -> {
            cambiarBrillo(imgSalir, 1);
        });

        // Color negro a la imagen salir
        imgSalir.setOnMouseExited(e -> {
            cambiarBrillo(imgSalir, 0);
        });

        // Color blanco a la imagen info
        imgInfo.setOnMouseEntered(e -> {
            cambiarBrillo(imgInfo, 1);
        });

        // Color negro a la imagen info
        imgInfo.setOnMouseExited(e -> {
            cambiarBrillo(imgInfo, 0);
        });

        // Salir de la aplicacion
        imgSalir.setOnMousePressed(e -> {
            System.out.println("Saliendo del programa...");
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        });

        // Iniciar sesion al teclear ENTER
        fieldUsuario.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                verificarInicioDeSesion();
            }
        });
        fieldPassword.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                verificarInicioDeSesion();
            }
        });

        // Iniciar sesion al presionar el boton submit
        submit.setOnAction(e -> {
            verificarInicioDeSesion();
        });

    }

    private void verificarInicioDeSesion() {

        String user = fieldUsuario.getText();
        String pass = fieldPassword.getText();

        if (!user.isEmpty() && !pass.isEmpty()){

            conexion = new ConexionBDD(user, pass);
            if (conexion.abrirConexion()) {

                generarFicheros.crearFicheros();

                iniciarSesion();
                System.out.println("Iniciando sesion...");

                Stage stage = (Stage) submit.getScene().getWindow();
                stage.close();

            }
            else {
                // Ventana error no existe el usuario sql
                StackPane stackPane = (StackPane) this.imgInfo.getScene().getRoot();
                Mensaje.mostrar(stackPane, "Debes rellenar todos los campos.");
            }

        }
        else {
            StackPane stackPane = (StackPane) this.imgInfo.getScene().getRoot();
            Mensaje.mostrar(stackPane, "Debes rellenar todos los campos.");
        }

    }

    /**
     * Cambiar brillo de una imagen
     * @param imagen
     * @param valor
     * @return ImageView
     */
    private ImageView cambiarBrillo(ImageView imagen, double valor) {

        ColorAdjust color = new ColorAdjust();
        color.setBrightness(valor);
        imagen.setEffect(color);

        return imagen;
    }

    /**
     * Entrar a la interfaz principal
     */
    private void iniciarSesion() {

        Stage stage = new Stage();
        ventana = stage;

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/InterfazPrincipal.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene( root );

        // PROPIEDADES ESCENARIO
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/icon.png")));
        stage.setScene(scene);
        stage.setTitle("NBA");
        stage.setResizable(false);

        stage.show();
    }

}
