import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

public class Game extends JPanel {
	private double[][] objects;
	private int width, height;
	private int health;
	private Camera camera;
	private int viewDist;
	private int level;
	private ArrayList<ArrayList<int[]>> draw;
	private ArrayList<Enemy> enemies;
	
	public Game() {
		super();
		level = 1;
		width = 800;
		height = 450;
		viewDist = 1200;
		health = 100;
		setPreferredSize(new Dimension(width, height));
		camera = new Camera(this, 150, 150);
		objects = new double[width][2];
		enemies = new ArrayList();
		spawnEnemies(2);
		update();
	}
	
	public void update() {
		for (Enemy e : enemies) {
			e.update();
		}
		if (enemies.size() == 0) {
			changeLevel();
		}
		camera.update();
		getObjects();
		setUpDraw();
		getEnemies();
		repaint();
	}
	
	private void changeLevel() {
		camera.posX = 150;
		camera.posY = 150;
		level++;
		Main.newMap();
		spawnEnemies(2);
	}

	private void spawnEnemies(int numEnemies) {
		boolean[][] spawnLocations = new boolean[Main.map.length][Main.map[0].length];
		for (boolean[] b : spawnLocations) {
			Arrays.fill(b, false);
		}
		for (int i = 0; i < numEnemies; i++) {
			int x = (int) (Math.random() * (Main.map.length - 2));
			int y = (int) (Math.random() * (Main.map[0].length - 2));
			if (Main.map[x][y] == 0 && !spawnLocations[x][y]) {
				enemies.add(new TargetThing(x * 100 + 50, y * 100 + 50, camera, this));
				spawnLocations[x][y] = true;
			} else {
				i--;
			}
		}
	}
	
	private void setUpDraw() {
		draw = new ArrayList();
		for (int i = 0; i < viewDist; i++) {
			draw.add(new ArrayList());
		}
		for (int i = 0; i < objects.length; i++) {
			draw.get((int) objects[i][1]).add(new int[] {0, (int) objects[i][0], i});
		}
	}
	
	public Camera getCam() {
		return camera;
	}
	
	public void damage(int damage) {
		health -= damage;
	}
	
	public int getHealth() {
		return health;
	}
	
	private void getEnemies() {
		for (double i = camera.fov / -2; i < camera.fov / 2; i += camera.fov / width) {
			double rot = i + camera.rot;
			double posX = camera.posX, posY = camera.posY;
			double vectX, vectY;
			vectX = Math.cos(rot * Math.PI);
			vectY = Math.sin(rot * Math.PI);
			for (int j = 0; j < viewDist; j++) {
				posX += vectX;
				posY += vectY;
				if ((int)(posX / 100) < Main.map.length && (int)(posX / 100) >= 0 && (int)(posY / 100) < Main.map[0].length && (int)(posY / 100) >= 0) {
					for (int k = 0; k < enemies.size(); k++) {
						if ((int) (enemies.get(k).getPosX()) == (int) (posX) && (int) (enemies.get(k).getPosY()) == (int) (posY)) {
							draw.get((int) Math.sqrt((posX - camera.posX) * (posX - camera.posX) + (posY - camera.posY) * (posY - camera.posY))).add(new int[] {1, (int)((i + (camera.fov / 2)) * (width / camera.fov)), k});
						}
					}
				}
			}
		}
	}
	
	private void getObjects() {
		for (double i = camera.fov / -2; i < camera.fov / 2; i += camera.fov / width) {
			int index = (int)((i + (camera.fov / 2)) * (width / camera.fov));
			objects[index] = sendRay(i);
		}
	}
	
	private double[] sendRay(double angle) {
		double rot = angle + camera.rot;
		double posX = camera.posX, posY = camera.posY;
		double vectX, vectY;
		vectX = Math.cos(rot * Math.PI);
		vectY = Math.sin(rot * Math.PI);
		for (int i = 0; i < viewDist; i++) {
			posX += vectX;
			posY += vectY;
			if ((int)(posX / 100) < Main.map.length && (int)(posX / 100) >= 0 && (int)(posY / 100) < Main.map[0].length && (int)(posY / 100) >= 0) {
				if (Main.map[(int)(posX / 100)][(int)(posY / 100)] != 0) {
					return new double[] {Main.map[(int)(posX / 100)][(int)(posY / 100)], Math.sqrt(Math.pow((posX - camera.posX), 2) + Math.pow((posY - camera.posY), 2))};
				}
			}
		}
		return new double[] {0, 0};
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		//roof and floor
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height / 2);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, height / 2, width, height / 2);
		//walls and enemies
		for (int i = viewDist - 1; i >= 0 ; i--) {
			for (int j = 0; j < draw.get(i).size(); j++) {
				if (draw.get(i).get(j)[0] == 0) {
					int color = draw.get(i).get(j)[1];
					if (color == 1) {
						g.setColor(Color.GRAY);
					} else if (color == 2) {
						g.setColor(Color.RED);
					} else if (color == 3) {
						g.setColor(Color.GREEN);
					}
					g.fillRect(draw.get(i).get(j)[2], i / (viewDist / (height / 2)), 1, ((height / 2) - (i / (viewDist / (height / 2)))) * 2);
				} else if (draw.get(i).get(j)[0] == 1) {
					Enemy e = getEnemy(draw.get(i).get(j)[2]);
					double newHeight =  (height - i / ((viewDist / (height / 2.0)) / 2));
					double newWidth = (newHeight * e.getSprite().getWidth()) / e.getSprite().getHeight();
					g.drawImage(e.getSprite(), draw.get(i).get(j)[1] - (int) (newWidth / 2), (int) (i / (viewDist / (height / 4))) + height / 4, (int) (newWidth / 2), (int) (newHeight / 2), null);
				}
			}
		}
		//hud
		//cross heir
		g.setColor(Color.BLACK);
		g.fillRect((int) (width / 2.0) - 5, (int) (height / 2.0) - 1, 10, 2);
		g.fillRect((int) (width / 2.0) - 1, (int) (height / 2.0) - 5, 2, 10);
		//health and ammo
		//g.drawImage(this.getClass().getResource("/hudBackground.png"), height - imgHeight, (width - imgWidth) / 2, null);
		g.setFont(new Font("arial", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("Health: " + health, 0, height - 5);
		g.drawString("Enemies left: " + enemies.size() + "", 0, height - 25);
	}
	
	public Enemy getEnemy(int index) {
		return enemies.get(index);
	}
	
	public void shoot() {
		double rot = camera.rot;
		double posX = camera.posX, posY = camera.posY;
		double vectX, vectY;
		vectX = Math.cos(rot * Math.PI);
		vectY = Math.sin(rot * Math.PI);
		for (int j = 0; j < viewDist; j++) {
			posX += vectX;
			posY += vectY;
			if ((int)(posX / 100) < Main.map.length && (int)(posX / 100) >= 0 && (int)(posY / 100) < Main.map[0].length && (int)(posY / 100) >= 0) {
				if (Main.map[(int)(posX / 100)][(int)(posY / 100)] != 0) {
					return;
				}
				for (int k = 0; k < enemies.size(); k++) {
					Enemy e = enemies.get(k);
					int dist = (int) Math.sqrt((camera.posX - e.getPosX() * camera.posX - e.getPosX()) + (camera.posY - e.getPosY() * camera.posY - e.getPosY()));
					double newHeight =  (height - dist / ((viewDist / (height / 2.0)) / 2));
					double newWidth = (newHeight * e.getSprite().getWidth()) / e.getSprite().getHeight() / 8;
					if ((int) (enemies.get(k).getPosX() - newWidth / 2) < (int) (posX) && (int) (enemies.get(k).getPosX() + newWidth / 2) > (int) (posX) && (int) (enemies.get(k).getPosY()) < (int) (posY) && (int) (enemies.get(k).getPosY()) + newWidth > (int) (posY)) {
						if (enemies.get(k).onHit(25)) {
							enemies.remove(k);
						}
						return;
					}
				}
			}
		}
	}
}
