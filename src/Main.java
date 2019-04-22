import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class Main {
	public static int[][] map = {
			{1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,0,1},
			{1,0,0,2,0,0,0,1},
			{1,0,0,2,3,0,0,1},
			{1,0,0,0,3,2,0,1},
			{1,0,0,2,2,0,0,1},
			{1,0,0,0,0,0,0,1},
			{1,1,1,1,1,1,1,1}
	};
	
	private static Game game;
	public static void main(String[] args) {
		JFrame frame = new JFrame("3D Test");
		game = new Game();
		frame.add(game);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (game.getHealth() <= 0) {
					game = new Game();
				}
				game.update();				
			}
		};
		frame.addKeyListener(game.getCam());
		frame.addMouseListener(game.getCam());
		game.addKeyListener(game.getCam());
		game.addMouseListener(game.getCam());
		Timer timer = new Timer();
		timer.schedule(task, 0, 1000/60);
	}
	
	public static void newMap() {
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map.length - 1; j++) {
				if ((int)(Math.random() * 3) > 0) {
					map[i][j] = 0;
				} else {
					map[i][j] = 2;
				}
			}
		}
		map[1][1] = 0;
		if (!mapCheck()) {
			newMap();
		}
	}

	public static boolean mapCheck() {
		int[][] temp = new int[map.length][map[0].length];
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map.length - 1; j++) {
				temp[i][j] = map[i][j];
			}
		}
		temp[1][1] = -1;
		boolean hasZero = false;
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 1; i < map.length - 1; i++) {
				for (int j = 1; j < map.length - 1; j++) {
					if (temp[i][j] == -1) {
						if (temp[i+1][j] == 0) {
							temp[i+1][j] = -1;
							changed = true;
						}
						if (temp[i-1][j] == 0) {
							temp[i-1][j] = -1;
							changed = true;
						}
						if (temp[i][j+1] == 0) {
							temp[i][j+1] = -1;
							changed = true;
						}
						if (temp[i][j-1] == 0) {
							temp[i][j-1] = -1;
							changed = true;
						}
					}
				}
			}
		}
		for (int i = 1; i < map.length - 1; i++) {
			for (int j = 1; j < map.length - 1; j++) {
				if (temp[i][j] == 0) {
					hasZero = true;
				}
			}
		}
		return !hasZero;
	}
	
}
