import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy {
	private BufferedImage sprite;
	private double posX, posY;
	
	public Enemy() {
		try {
			sprite = ImageIO.read(getClass().getResource("/Head.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
