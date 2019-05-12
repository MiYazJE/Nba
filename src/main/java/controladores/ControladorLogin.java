/**
 * @author Ruben Saiz
 */
package controladores;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import interfaz.Aplicacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Color blanco a la imagen salir
        imgSalir.setOnMouseEntered(e -> {
            ColorAdjust color = new ColorAdjust();
            color.setBrightness(1);
            imgSalir.setEffect(color);
        });

        // Color negro a la imagen salir
        imgSalir.setOnMouseExited(e -> {
            ColorAdjust color = new ColorAdjust();
            color.setBrightness(0);
            imgSalir.setEffect(color);
        });

        // Salir de la aplicacion
        imgSalir.setOnMousePressed(e -> {
            System.out.println("Saliendo del programa...");
            ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
        });



        // Iniciar sesion al teclear ENTER
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
                // TODO Lanzar ventana succes login

                System.out.println("Iniciando sesion...");
                iniciarSesion();
                System.out.println("Sesion iniciada.");

                Stage stage = (Stage) submit.getScene().getWindow();
                stage.close();
            }
            else {
                // Ventana error no existe el usuario sql

            }

        }
        else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setContentText("Por favor, rellene todos los campos.");
            alerta.setHeaderText("Error campos incompletos");
            alerta.showAndWait();
        }

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
