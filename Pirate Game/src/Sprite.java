import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite extends ImageView{
	private String type;
	
	public Sprite(Image image,double w,double h,double x,double y,String type) {
		super(image);
		setTranslateX(x);
		setTranslateY(y);
		setFitWidth(w);
		setFitHeight(h);
		this.type=type;
	}
	public void moveUp(){
		if((getTranslateY()-10)>=0) {
			setTranslateY(getTranslateY()-10);
		}
	}
	public void moveDown(){
		if ((getTranslateY()+10)<=850) {
			setTranslateY(getTranslateY()+10);
		}
	}
	public void moveLeft() {
		if ((getTranslateX()-10)>=0) {
			setTranslateX(getTranslateX()-10);
		}
	}
	public void moveRight() {
		if ((getTranslateX()+10)<=880) {
			setTranslateX(getTranslateX()+10);
		}
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
