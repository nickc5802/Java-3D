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
	
	public Game(int[][] map) {
		super();
		width = 800;
		height = 450;
		viewDist = 1200;
		setPreferredSize(new Dimension(width, height));
		this.map = map;
		camera = new Camera(map);
		objects = new double[width][2];
		getObjects();
		setUpDraw();
	}
	
	public void update() {
		camera.update();
		getObjects();
		setUpDraw();
		repaint();
	}
	
	public void setUpDraw() {
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
		for (int i = 0; i < viewDist; i++) {
			if (draw != null) {
				for (int j = 0; j < draw.get(i).size(); j++) {
					int color = draw.get(i).get(j)[1];
					if (color == 1) {
						g.setColor(Color.GRAY);
					} else if (color == 2) {
						g.setColor(Color.RED);
					} else if (color == 3) {
						g.setColor(Color.GREEN);
					}
					g.fillRect(draw.get(i).get(j)[2], i / (viewDist / (height / 2)), 1, (height - i / ((viewDist / (height / 2)) / 2)));
				}
			}
		}
	}
}
