/**
 * @author Ruben Saiz
 */
package controladores.controladoresPrincipal;

import com.jfoenix.controls.*;
import com.sun.javafx.application.HostServicesDelegate;
import dominio.Mensaje;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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

        this.imgInfo.setOnMousePressed(e -> mostrarInfoPersonal());

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
                Mensaje.mostrar(stackPane, "El usuario " + user + " no existe o la contrase�a\nes erronea.");
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

    private void mostrarInfoPersonal() {
        JFXDialogLayout content = new JFXDialogLayout();
        Text titulo = new Text("Informaci�n");
        titulo.setFont(new Font(20));
        content.setHeading(titulo);

        Text textoMensaje = new Text("Esta aplicaci�n esta desarrollada por Ruben Saiz, puedes seguir\n" +
                                        "el proyecto en github.");
        textoMensaje.setFont(new Font(15));
        content.setBody(textoMensaje);

        ArrayList<JFXDialog.DialogTransition> transiciones = new ArrayList<>(Arrays.asList(
                JFXDialog.DialogTransition.TOP, JFXDialog.DialogTransition.CENTER, JFXDialog.DialogTransition.BOTTOM,
                JFXDialog.DialogTransition.RIGHT, JFXDialog.DialogTransition.LEFT
        ));

        StackPane stackPane = (StackPane) fieldUsuario.getScene().getRoot();
        JFXDialog dialog = new JFXDialog(stackPane, content, transiciones.get(new Random().nextInt(5)));

        JFXButton button = new JFXButton("Vale");
        button.setStyle("-fx-background-color: #2f2f2fa3; -fx-cursor: HAND; -fx-padding: 10px; -fx-text-fill: white;");

        JFXButton botonGithub = new JFXButton("Ir a github");
        botonGithub.setStyle("-fx-background-color: #2f2f2fa3; -fx-cursor: HAND; -fx-padding: 10px; -fx-text-fill: white;");

        BoxBlur blur = new BoxBlur(3, 3, 3);
        content.setActions(button, botonGithub);
        dialog.show();

        Node pane = (Node) stackPane.getChildren().get(0);

        button.setOnAction(e ->{
            dialog.close();
        });

        botonGithub.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/MiYazJE/Nba").toURI());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (URISyntaxException exe) {
                System.out.println(exe.getMessage());
            }
        });

        dialog.setOnDialogClosed((e) -> {
            pane.setEffect(null);
        });

        pane.setEffect( blur );
    }

}
