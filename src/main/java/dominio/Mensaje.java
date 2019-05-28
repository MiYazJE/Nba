/**
 * @author Ruben Saiz
 */
package dominio;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Mensaje {

    public static void mostrar(StackPane stackPane, String mensaje) {

        JFXDialogLayout content = new JFXDialogLayout();
        Text titulo = new Text("ATENCIÓN!");
        titulo.setFont(new Font(20));
        content.setHeading(titulo);

        Text textoMensaje = new Text(mensaje);
        textoMensaje.setFont(new Font(15));
        content.setBody(textoMensaje);

        ArrayList<JFXDialog.DialogTransition> transiciones = new ArrayList<>(Arrays.asList(
                JFXDialog.DialogTransition.TOP, JFXDialog.DialogTransition.CENTER, JFXDialog.DialogTransition.BOTTOM,
                JFXDialog.DialogTransition.RIGHT, JFXDialog.DialogTransition.LEFT
        ));

        JFXDialog dialog = new JFXDialog(stackPane, content, transiciones.get(new Random().nextInt(5)));

        JFXButton button = new JFXButton("Vale");
        button.setStyle("-fx-background-color: #2f2f2fa3; -fx-cursor: HAND; -fx-padding: 10px; -fx-text-fill: white;");

        BoxBlur blur = new BoxBlur(3, 3, 3);
        content.setActions(button);
        dialog.show();

        Node pane = (Node) stackPane.getChildren().get(0);

        button.setOnAction(e ->{
            dialog.close();
        });

        dialog.setOnDialogClosed((e) -> {
            pane.setEffect(null);
        });

        pane.setEffect( blur );
    }

}
