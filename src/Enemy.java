import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy {
	private BufferedImage sprite;
	private double posX, posY;
	
	public Enemy(double x, double y) {
		try {
			sprite = ImageIO.read(getClass().getResource("/Head.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		posX = x;
		posY = y;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public double getPosX() {
		return posX;
	}
	
	public double getPosY() {
		return posY;
	}
}
