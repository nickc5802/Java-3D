import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {
	double posX, posY, rot, fov;
	boolean forward, back, left, right;
	int[][] map;
	
	public Camera(int[][] map) {
		posX = 150;
		posY = 150;
		rot = 0;
		fov = .3;
		this.map = map;
	}
	
	public void update() {
		if (forward) {			
			if ((int)((posX + Math.cos(rot * Math.PI) * 5)/100) < map.length && (int)((posY + Math.sin(rot * Math.PI) * 5)/100) < map[0].length) {
				if ((int)((posX + Math.cos(rot * Math.PI) * 5)/100) >= 0 && (int)((posY + Math.sin(rot * Math.PI) * 5)/100) >= 0) {
					if (map[(int)((posX + Math.cos(rot * Math.PI) * 5)/100)][(int)((posY + Math.sin(rot * Math.PI) * 5)/100)] == 0) {
						posX += Math.cos(rot * Math.PI) * 5;
						posY += Math.sin(rot * Math.PI) * 5;
					}
				}
			}
		} else if (back) {
			if ((int)((posX - Math.cos(rot * Math.PI) * 5)/100) < map.length && (int)((posY - Math.sin(rot * Math.PI) * 5)/100) < map[0].length) {
				if ((int)((posX - Math.cos(rot * Math.PI) * 5)/100) >= 0 && (int)((posY - Math.sin(rot * Math.PI) * 5)/100) >= 0) {
					if (map[(int)((posX - Math.cos(rot * Math.PI) * 5)/100)][(int)((posY - Math.sin(rot * Math.PI) * 5)/100)] == 0) {
						posX -= Math.cos(rot * Math.PI) * 5;
						posY -= Math.sin(rot * Math.PI) * 5;
					}
				}
			}
		}
		if (left) {
			rot -= 0.008;
		} else if (right) {
			rot += 0.008;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			forward = true;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			back = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			left = true;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			right = true;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			forward = false;
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			back = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			left = false;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			right = false;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
}
