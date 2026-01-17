import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The application that the election officials will use to display the standings
 * @author PO Bogdanov 218029215
 *
 */
public class MainDashboard extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		DashboardCreator gui=new DashboardCreator();
		Scene scene=new Scene(gui);
		primaryStage.setScene(scene);
		primaryStage.setTitle("2021 Municiple Elections: Gauteng");
		primaryStage.setWidth(500);
		primaryStage.setHeight(500);
		primaryStage.show();
	}
}
