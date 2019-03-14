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
		game = new Game(map);
		frame.add(game);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (game.getHealth() <= 0) {
					game = new Game(map);
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

	
}
