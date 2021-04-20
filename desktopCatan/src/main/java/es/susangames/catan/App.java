package es.susangames.catan;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;



/**
 * JavaFX App
 */
public class App extends Application {

    private static Stage estado;

    @Override
    public void start(Stage stage) {
        try {
			estado = stage;
			Pane root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
			
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        launch();
    }

	public static void nuevaPantalla(String fxml) throws IOException {
		Parent scene = FXMLLoader.load(App.class.getResource(fxml));
        estado.getScene().setRoot(scene);
	}

}