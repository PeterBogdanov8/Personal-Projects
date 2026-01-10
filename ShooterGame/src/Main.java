import java.util.List;


import java.util.stream.Collectors;



import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{
	private Pane root=new Pane();
	private double t=0;
	private int numEnemy=5;
	Image playerImage=new Image("file:sprites\\original.png");
	private Sprite player=new Sprite(playerImage,300, 700, 100, 100, "player");
	private List<Sprite> sprites() {
		return root.getChildren().stream().map(n->(Sprite)n).collect(Collectors.toList());
	}
	private void update() {
		t+=0.016;
		sprites().forEach(s->{
			switch(s.getType()) {
				case "enemybullet":
					s.moveDown();
					
					if(s.getBoundsInParent().intersects(player.getBoundsInParent())) {
						s.setDead(true);
						player.setDead(true);
					}
					break;
				
				case "playerbullet":
					s.moveUp();
					sprites().stream().filter(e->e.getType().equals("enemy")).forEach(enemy->{
						if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
							enemy.setDead(true);
							s.setDead(true);
							numEnemy--;
						}
					});
					break;
				case "enemy":
					if(t>2) {
						if(Math.random()<0.3) {
							shoot(s);
						}
						if(Math.random()<=1) {
							s.moveDown();
							if(s.getTranslateY()==player.getTranslateY()) {
								Image gameOverImage=new Image("file:sprites\\gameover.jpg");
								Sprite gameOverSprite=new Sprite(gameOverImage, 0, 0, 1000, 1000, "gameover");
								root.getChildren().add(gameOverSprite);
							}
						}
					}
					break;
			}
		});
		if(player.isDead() && (numEnemy!=0)) {
			Image gameOverImage=new Image("file:sprites\\gameover.jpg");
			Sprite gameOverSprite=new Sprite(gameOverImage, 0, 0, 1000, 1000, "gameover");
			root.getChildren().add(gameOverSprite);
		}
		if((numEnemy==0) && (player.isDead()!=true)) {
			Image victoryImage=new Image("file:sprites\\victory.jpg");
			Sprite victorySprite=new Sprite(victoryImage, 0, 0, 1000, 1000, "victory");
			root.getChildren().add(victorySprite);
		}
		root.getChildren().removeIf(n->{
			Sprite s=(Sprite) n;
			return s.isDead();
		});
		
		if(t>2) {
			t=0;
		}
	}
	
	private void shoot(Sprite who) {
		if(who.getType().equals("player")) {
			Image bulletImage=new Image("file:sprites\\laser.jpg");
			Sprite bullet=new Sprite(bulletImage,(int)who.getTranslateX()+63, (int)who.getTranslateY(), 3, 20,who.getType() +"bullet");
			root.getChildren().add(bullet);
		} else if (who.getType().equals("enemy")) {
			Image bulletImage=new Image("file:sprites\\laser.jpg");
			Sprite bullet=new Sprite(bulletImage,(int)who.getTranslateX()+30, (int)who.getTranslateY()+65, 3, 20,who.getType() +"bullet");
			root.getChildren().add(bullet);
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		root.setPrefSize(1000, 1000);
		Image grassImage=new Image("file:sprites\\grass.png");
		for(int r=0;r<1000;r+=100) {
			for(int c=0;c<1000;c+=100) {
				Sprite grass=new Sprite(grassImage, c, r, 100, 100, "grass");
				root.getChildren().add(grass);
			}
		}
		root.getChildren().add(player);
		AnimationTimer timer=new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				update();
			}
		};
		timer.start();
		for(int i=0;i<5;i++) {
			Image enemyImage=new Image("file:sprites\\enemy.png");
			Sprite enemy = new Sprite(enemyImage,90+i*200, 150, 100, 100, "enemy");
			root.getChildren().add(enemy);
		}
		Scene scene=new Scene(root);
		scene.setOnKeyPressed(e->{
			switch (e.getCode()) {
			case A:
				player.moveLeft();
				break;
			case D:
				player.moveRight();
				break;
			case SPACE:
				shoot(player);
				break;
			case LEFT:
				player.moveLeft();
				break;
			case RIGHT:
				player.moveRight();
				break;
			default:
				break;
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
