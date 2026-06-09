import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class snake extends JPanel implements ActionListener, KeyListener {
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
	static final int DELAY = 125;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten = 0;
	final int WIN_SCORE = 10; // apples needed to win
	int appleX;
	int appleY;
	char direction = 'R'; // U, D, L, R
	boolean running = false;
	boolean won = false;
	Timer timer;
	Random random;

	public snake() {
		random = new Random();
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setBackground(new Color(255, 192, 203)); // pastel pink background
		setFocusable(true);
		addKeyListener(this);
		startGame();
		JFrame frame = new JFrame("Pastel Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		draw(g2);
	}

	public void draw(Graphics g) {
		if (running) {
			// draw apple (brighter pastel purple)
			g.setColor(new Color(218, 112, 214)); // pastel purple apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			// draw snake
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.WHITE);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(Color.WHITE);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			// score
			g.setColor(Color.DARK_GRAY);
			g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			String scoreText = "Score: " + applesEaten;
			g.drawString(scoreText, (SCREEN_WIDTH - metrics.stringWidth(scoreText)) / 2, g.getFont().getSize());
		} else {
			if (won) winScreen(g); else gameOver(g);
		}
	}

	public void newApple() {
		appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
		}
	}

	public void checkApple() {
		if (x[0] == appleX && y[0] == appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
			if (applesEaten >= WIN_SCORE) {
				running = false;
				won = true;
				if (timer != null) timer.stop();
			}
		}
	}

	public void checkCollisions() {
		// check if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check left border
		if (x[0] < 0) running = false;
		// check right border
		if (x[0] >= SCREEN_WIDTH) running = false;
		// check top
		if (y[0] < 0) running = false;
		// check bottom
		if (y[0] >= SCREEN_HEIGHT) running = false;

		if (!running) timer.stop();
	}

	public void gameOver(Graphics g) {
		// score
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		String scoreText = "Score: " + applesEaten;
		g.drawString(scoreText, (SCREEN_WIDTH - metrics1.stringWidth(scoreText)) / 2, g.getFont().getSize());

		// game over text
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 56));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		String overText = "Game Over ✿";
		g.drawString(overText, (SCREEN_WIDTH - metrics2.stringWidth(overText)) / 2, SCREEN_HEIGHT / 2);

		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		String hint = "Press SPACE to restart";
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString(hint, (SCREEN_WIDTH - metrics3.stringWidth(hint)) / 2, SCREEN_HEIGHT / 2 + 40);
	}

	public void winScreen(Graphics g) {
		// score
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		String scoreText = "Score: " + applesEaten;
		g.drawString(scoreText, (SCREEN_WIDTH - metrics1.stringWidth(scoreText)) / 2, g.getFont().getSize());

		// win text
		g.setColor(new Color(75, 0, 130)); // deep pastel accent
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 56));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		String winText = "You Win! ✿";
		g.drawString(winText, (SCREEN_WIDTH - metrics2.stringWidth(winText)) / 2, SCREEN_HEIGHT / 2);

		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		String hint = "Press SPACE to play again";
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString(hint, (SCREEN_WIDTH - metrics3.stringWidth(hint)) / 2, SCREEN_HEIGHT / 2 + 40);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') direction = 'L';
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') direction = 'R';
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') direction = 'U';
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') direction = 'D';
				break;
			case KeyEvent.VK_SPACE:
				if (!running) {
					// reset game
					bodyParts = 6;
					applesEaten = 0;
					direction = 'R';
					won = false;
					for (int i = 0; i < x.length; i++) {
						x[i] = 0;
						y[i] = 0;
					}
					startGame();
				}
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new snake());
	}
}
