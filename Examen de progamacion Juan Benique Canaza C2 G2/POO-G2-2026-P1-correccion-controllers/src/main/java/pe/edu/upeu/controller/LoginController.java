package pe.edu.upeu.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pe.edu.upeu.components.StageManager;
import pe.edu.upeu.components.Toast;
import pe.edu.upeu.config.AppContext;
import pe.edu.upeu.dto.SessionManager;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.service.IUsuarioService;

import java.net.URL;

public class LoginController {

    @FXML
    TextField txtUsuario, txtClave;

    private final IUsuarioService usuarioService;

    public LoginController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @FXML
    private void login(ActionEvent event) {
        try {
            usuarioService.loginUsuario(txtUsuario.getText(), txtClave.getText())
                    .ifPresentOrElse(usuario -> abrirmain(event, usuario),
                            () -> mostrarError(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirmain(ActionEvent event, Usuario usu) {
        try {
            SessionManager.getInstance().setUserId(usu.getIdUsuario());

            AppContext ctx = AppContext.getInstance();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/maingui.fxml"));
            loader.setControllerFactory(ctx::getBean);
            Parent mainRoot = loader.load();

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getBounds();
            Scene mainScene = new Scene(mainRoot, bounds.getWidth(), bounds.getHeight() - 30);

            URL cssUrl = getClass().getResource("/css/styles.css");
            if (cssUrl != null) {
                mainScene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            URL iconUrl = getClass().getResource("/img/store.png");
            if (iconUrl != null) {
                stage.getIcons().add(new Image(iconUrl.toExternalForm()));
            }

            stage.setScene(mainScene);
            stage.setTitle("SysVentas SysCenterLife");
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setResizable(true);

            StageManager.setPrimaryStage(stage);
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            stage.show();
        } catch (Exception e) {
            System.err.println("Error abriendo ventana principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarError(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double w = stage.getWidth() * 2;
        double h = stage.getHeight() / 2;
        Toast.showToast(stage, "Credenciales incorrectas", 2000, w, h);
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }
}
