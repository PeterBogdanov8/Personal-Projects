import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = new Pane();
		Scene scene = new Scene(root);
		GameAssembler gameAssembler=new GameAssembler(root,scene);
		gameAssembler.creatGameWorld();
		gameAssembler.gameMovement();
		primaryStage.setScene(scene);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(1000);
		primaryStage.show();
	}

}
