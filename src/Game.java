import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Game extends JPanel {
	int[][] map;
	double[][] objects;
	int width, height;
	Camera camera;
	int viewDist;
	ArrayList<ArrayList<int[]>> draw;
	ArrayList<ArrayList<int[]>> enemyDraw;
	ArrayList<Enemy> enemies;
	
	public Game(int[][] map) {
		super();
		width = 800;
		height = 450;
		viewDist = 1200;
		setPreferredSize(new Dimension(width, height));
		this.map = map;
		camera = new Camera(map, this);
		objects = new double[width][2];
		enemies = new ArrayList<Enemy>();
		enemies.add(new TargetThing(650, 650));
		update();
	}
	
	public void update() {
		camera.update();
		getObjects();
		setUpDraw();
		getEnemies();
		repaint();
	}
	
	private void setUpDraw() {
		draw = new ArrayList<ArrayList<int[]>>();
		for (int i = 0; i < viewDist; i++) {
			draw.add(new ArrayList<int[]>());
		}
		for (int i = 0; i < objects.length; i++) {
			draw.get((int) objects[i][1]).add(new int[] {0, (int) objects[i][0], i});
		}
	}
	
	public Camera getCam() {
		return camera;
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
				if ((int)(posX / 100) < map.length && (int)(posX / 100) >= 0 && (int)(posY / 100) < map[0].length && (int)(posY / 100) >= 0) {
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
			if ((int)(posX / 100) < map.length && (int)(posX / 100) >= 0 && (int)(posY / 100) < map[0].length && (int)(posY / 100) >= 0) {
				if (map[(int)(posX / 100)][(int)(posY / 100)] != 0) {
					return new double[] {map[(int)(posX / 100)][(int)(posY / 100)], Math.sqrt(Math.pow((posX - camera.posX), 2) + Math.pow((posY - camera.posY), 2))};
				}
			}
		}
		return new double[] {0, 0};
	}
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height / 2);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, height / 2, width, height / 2);
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
					g.fillRect(draw.get(i).get(j)[2], i / (viewDist / (height / 2)), 1, (height - i / ((viewDist / (height / 2)) / 2)));
				} else if (draw.get(i).get(j)[0] == 1) {
					Enemy e = getEnemy(draw.get(i).get(j)[2]);
					double newHeight =  (height - i / ((viewDist / (height / 2)) / 2));
					double newWidth = (newHeight * e.getSprite().getWidth()) / e.getSprite().getHeight();
					g.drawImage(e.getSprite(), draw.get(i).get(j)[1] - (int) (newWidth / 2), (int) (i / (viewDist / (height / 4))) + height / 4, (int) (newWidth / 2), (int) (newHeight / 2), null);
				}
			}
		}
		g.setColor(Color.BLACK);
		g.fillRect((int) (width / 2.0) - 5, (int) (height / 2.0) - 1, 10, 2);
		g.fillRect((int) (width / 2.0) - 1, (int) (height / 2.0) - 5, 2, 10);
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
			if ((int)(posX / 100) < map.length && (int)(posX / 100) >= 0 && (int)(posY / 100) < map[0].length && (int)(posY / 100) >= 0) {
				if (map[(int)(posX / 100)][(int)(posY / 100)] != 0) {
					return;
				}
				for (int k = 0; k < enemies.size(); k++) {
					Enemy e = enemies.get(k);
					int dist = (int) Math.sqrt((camera.posX - e.getPosX() * camera.posX - e.getPosX()) + (camera.posY - e.getPosY() * camera.posY - e.getPosY()));
					double newHeight =  (height - dist / ((viewDist / (height / 2)) / 2));
					double newWidth = (newHeight * e.getSprite().getWidth()) / e.getSprite().getHeight();
					if ((int) (enemies.get(k).getPosX()) < (int) (posX) && (int) (enemies.get(k).getPosX()) + newWidth > (int) (posX) && (int) (enemies.get(k).getPosY()) < (int) (posY) && (int) (enemies.get(k).getPosY()) + newWidth > (int) (posY)) {
						if (enemies.get(k).onHit(25)) {
							enemies.remove(k);
						}
						System.out.println("hit");
						return;
					}
				}
			}
		}
	}
}
