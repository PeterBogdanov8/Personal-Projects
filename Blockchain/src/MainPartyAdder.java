import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The application that the election officials will use to add the political parties that will participate in the elections
 * @author PO Bogdanov 218029215
 *
 */
public class MainPartyAdder extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		PartyAdderCreator gui=new PartyAdderCreator();
		Scene scene=new Scene(gui);
		primaryStage.setScene(scene);
		primaryStage.setTitle("2021 Municiple Elections: Gauteng");
		primaryStage.setWidth(500);
		primaryStage.setHeight(500);
		primaryStage.show();
	}

}
