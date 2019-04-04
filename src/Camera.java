import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Camera implements KeyListener, MouseListener {
	double posX, posY, rot, fov;
	boolean forward, back, left, right;
	Game game;
	
	public Camera(Game g, double x, double y) {
		posX = x;
		posY = y;
		rot = 0;
		fov = .3;
		game = g;
	}
	
	public void update() {
		if (forward) {			
			if ((int)((posX + Math.cos(rot * Math.PI) * 5)/100) < Main.map.length && (int)((posY + Math.sin(rot * Math.PI) * 5)/100) < Main.map[0].length) {
				if ((int)((posX + Math.cos(rot * Math.PI) * 5)/100) >= 0 && (int)((posY + Math.sin(rot * Math.PI) * 5)/100) >= 0) {
					if (Main.map[(int)((posX + Math.cos(rot * Math.PI) * 5)/100)][(int)((posY + Math.sin(rot * Math.PI) * 5)/100)] == 0) {
						posX += Math.cos(rot * Math.PI) * 5;
						posY += Math.sin(rot * Math.PI) * 5;
					}
				}
			}
		} else if (back) {
			if ((int)((posX - Math.cos(rot * Math.PI) * 5)/100) < Main.map.length && (int)((posY - Math.sin(rot * Math.PI) * 5)/100) < Main.map[0].length) {
				if ((int)((posX - Math.cos(rot * Math.PI) * 5)/100) >= 0 && (int)((posY - Math.sin(rot * Math.PI) * 5)/100) >= 0) {
					if (Main.map[(int)((posX - Math.cos(rot * Math.PI) * 5)/100)][(int)((posY - Math.sin(rot * Math.PI) * 5)/100)] == 0) {
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

	@Override
	public void mouseClicked(MouseEvent arg0) {
		game.shoot();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
