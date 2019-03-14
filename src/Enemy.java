import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Enemy {
	private BufferedImage sprite;
	private double posX, posY, speed;
	private int health, damage;
	private Camera cam;
	private Game game;
	
	private double attackDelay;
	
	public Enemy(double x, double y, int health, int damage, double speed, String image, Camera cam, Game game) {
		try {
			sprite = ImageIO.read(getClass().getResource(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
		posX = x;
		posY = y;
		this.speed = speed;
		this.cam = cam;
		this.game = game;
		this.damage = damage;
		attackDelay = 0;
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
	
	public boolean onHit(int damage) {
		health -= damage;
		if (health <= 0) {
			return true;
		} else { 
			return false;
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void update() {
		walk();
		attack();
	}
	
	private void attack() {
		double distX = posX - cam.posX;
		double distY = posY - cam.posY;
		if (attackDelay <= 0 && Math.sqrt(distX * distX + distY * distY) < 3) {
			attackDelay = 20;
			game.damage(damage);
		}
		if (attackDelay > 0) {
			attackDelay -= 1;
		}
		System.out.println(attackDelay + " " + (attackDelay <= 0 && Math.sqrt(distX * distX + distY * distY) < 3) + " " + (attackDelay > 0));
	}
	
	private void walk() {
		double distX = posX - cam.posX;
		double distY = posY - cam.posY;
		double dist = Math.sqrt(distX * distX + distY * distY);
		double moveX = -distX / dist;
		double moveY = -distY / dist;
		double mag = Math.sqrt(moveX * moveX + moveY * moveY);
		moveX *= speed / mag;
		moveY *= speed / mag;
		if ((posX + moveX) / 100 < Main.map.length && (posY + moveY) / 100 < Main.map[0].length) {
			if (posX +  moveX >= 0 && posY + moveY >= 0) {
				if (Main.map[(int)((posX + moveX) / 100)][(int)((posY + moveY) / 100)] == 0) {
					posX += moveX;
					posY += moveY;
				}
			}
		}
	}
}
