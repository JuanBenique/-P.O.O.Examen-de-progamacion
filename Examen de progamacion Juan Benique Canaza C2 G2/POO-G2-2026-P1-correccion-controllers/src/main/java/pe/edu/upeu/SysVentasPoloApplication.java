package pe.edu.upeu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pe.edu.upeu.config.AppContext;
import pe.edu.upeu.config.DatabaseConfig;

public class SysVentasPoloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseConfig.init();
        AppContext ctx = AppContext.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        loader.setControllerFactory(ctx::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SysVentas Polos");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}