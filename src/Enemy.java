import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Enemy {
	private BufferedImage sprite;
	private double posX, posY;
	private int health;
	
	public Enemy(double x, double y, int health, String image) {
		try {
			sprite = ImageIO.read(getClass().getResource(image));
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
