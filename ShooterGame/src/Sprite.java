import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView{
	private boolean dead=false;
	private final String type;
	
	public Sprite(Image image,int x,int y,int w,int h,String type) {
		super(image);
		this.type=type;
		setTranslateX(x);
		setTranslateY(y);
		setFitWidth(w);
		setFitHeight(h);
	}
	
	public void moveLeft() {
		if((getTranslateX()-5)>=-20) {
			setTranslateX(getTranslateX()-5);
		}
	}
	
	public void moveRight() {
		if ((getTranslateX()+5)<925) {
			setTranslateX(getTranslateX()+5);
		}
	}
	
	public void moveUp() {
		setTranslateY(getTranslateY()-5);
	}
	
	public void moveDown() {
		setTranslateY(getTranslateY()+5);
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public String getType() {
		return type;
	}
	
}
