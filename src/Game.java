import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Game extends JPanel {
	int[][] map;
	double[] objects;
	double[] dist;
	int width, height;
	Camera camera;
	
	public Game(int[][] map) {
		super();
		width = 800;
		height = 450;
		setPreferredSize(new Dimension(width, height));
		this.map = map;
		camera = new Camera(map);
		objects = new double[width];
		dist = new double[width];
		getObjects();
	}
	
	public void update() {
		camera.update();
		getObjects();
		repaint();
	}
	
	public Camera getCam() {
		return camera;
	}
	
	private void getObjects() {
		for (double i = camera.fov / -2; i < camera.fov / 2; i += camera.fov / width) {
			int index = (int)((i + (camera.fov / 2)) * (width / camera.fov));
			objects[index] = sendRay(i)[0];
			dist[index] = sendRay(i)[1];
		}
	}
	
	private double[] sendRay(double angle) {
		double rot = angle + camera.rot;
		double posX = camera.posX, posY = camera.posY;
		double vectX, vectY;
		vectX = Math.cos(rot * Math.PI);
		vectY = Math.sin(rot * Math.PI);
		for (int i = 0; i < 1200; i++) {
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
		for (int i = 0; i < width; i++) {
			if (objects[i] == 1) {
				g.setColor(Color.GRAY);
			} else if (objects[i] == 2) {
				g.setColor(Color.RED);
			} else if (objects[i] == 3) {
				g.setColor(Color.GREEN);
			}
			if (objects[i] != 0) {
				g.fillRect(i, (int)dist[i] / 4, 1, (height - (int)dist[i] / 2));
			}
		}
	}
}
