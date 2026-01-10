import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class GameAssembler {
	private Pane root;
	private Scene scene;
	private Sprite sand;
	private Sprite player;
	private Sprite chest;
	public GameAssembler(Pane root,Scene scene) {
		this.root=root;
		this.scene=scene;
	}
	public void creatGameWorld() {
		
		for(int r=0;r<1000;r+=100) {
			for(int c=0;c<1000;c+=100) {
				Image sandImage=new Image("file:sprites\\sand2.jpg");
				sand=new Sprite(sandImage, 100, 100, c, r, "sand");
				root.getChildren().add(sand);
			}
		}
		Image chestImage=new Image("file:sprites\\chest4.png");
		chest=new Sprite(chestImage, 100, 100, 850, 850, "chest");
		root.getChildren().add(chest);
		Image playerImage=new Image("file:sprites\\pirate2.png");
		player=new Sprite(playerImage, 100, 100, 0, 0, "player");
		root.getChildren().add(player);
		for(int r=0;r<1000;r+=100) {
			for(int c=0;c<1000;c+=100) {
				if(Math.random()<0.2) {
					Image bombImage=new Image("file:sprites\\correctbomb.png");
					Sprite bomb=new Sprite(bombImage, 50, 50, c, r, "bomb");
					if(!(bomb.getBoundsInParent().intersects(player.getBoundsInParent())) 
							&& !(bomb.getBoundsInParent().intersects(chest.getBoundsInParent()))) {
						root.getChildren().add(bomb);
					}
				}
			}
		}
	}
	
	private void checkVictory() {
		if(chest.getBoundsInParent().intersects(player.getBoundsInParent())) {
			Image victoryImage=new Image("file:sprites\\victory.jpg");
			Sprite victorySprite=new Sprite(victoryImage, 1000, 1000, 0, 0, "victory");
			root.getChildren().add(victorySprite);
		}
	}
	
	private List<Sprite> sprites(){
		return root.getChildren().stream().map(n->(Sprite)n).collect(Collectors.toList());
	}
	
	private void checkDefeat() {
		sprites().forEach(s->{
			switch (s.getType()) {
			case "bomb":
				if(s.getBoundsInParent().intersects(player.getBoundsInParent())) {
					Image exploisionImage=new Image("file:sprites\\exploision2.png");
					Sprite explosionSprite=new Sprite(exploisionImage, 50, 50, s.getTranslateX(), s.getTranslateY(), "exploision");
					Image gameOverImage=new Image("file:sprites\\gameover.jpg");
					Sprite gameOverSprite=new Sprite(gameOverImage, 1000, 1000, 0, 0, "gameover");
					root.getChildren().add(explosionSprite);
					root.getChildren().add(gameOverSprite);
				}
				break;

			default:
				break;
			}
		});
	}
	public void gameMovement() {
		scene.setOnKeyPressed(e->{
			switch (e.getCode()) {
			case LEFT:
				player.moveLeft();
				checkVictory();
				checkDefeat();
				break;
			case RIGHT:
				player.moveRight();
				checkVictory();
				checkDefeat();
				break;
			case UP:
				player.moveUp();
				checkVictory();
				checkDefeat();
				break;
			case DOWN:
				player.moveDown();
				checkVictory();
				checkDefeat();
				break;
			default:
				break;
			}
		});
	}
}
