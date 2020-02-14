import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener
{

	public static Pong pong;
	public int width = 900, height = 600;
	public Panel jpanel;
	public Paddle player1;
	public Paddle player2;
	public Ball ball;
	public boolean p1_up, p1_down, p2_up, p2_down;
	public boolean setting;
	public String scene = "GAMEPAGE"; //"GAMEPAGE", "BATTLE", "END", "STILL"
	public String control = "MAX_SCORE"; //"TABLE_SIZE", "BALL_SIZE", "PADDLE_SIZE", "BALL_SPEED", "GOLDEN_CHALLENGE"
	public int goldenChallenge = 0; //1:on, 0:off
	public int ballSize = 3; //1:Tiny 2:Small, 3:Medium, 4:Large,  0:Random 
	public int ballSpeed = 2; //1:slow,  2:normal, 3:fast, 0:Random 
	public int paddleSize = 2; //1:Small, 2:Medium, 3:Big,  0:Wild 
	public int maxScore = 3;
	public int winner; 
	public Random random;
	public JFrame jframe;

	public Pong() {
		Timer timer = new Timer(20, this);
		random = new Random();
		jframe = new JFrame("Pong");
		jpanel = new Panel();
		jframe.add(jpanel);
		jframe.setSize(width + 15, height + 35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addKeyListener(this);

		timer.start();
	}

	public void start() {
		scene = "BATTLE";
		player1 = new Paddle(this, 1);
		player2 = new Paddle(this, 2);
		ball = new Ball(this);
	}

	public void update() {
		if (player1.score >= maxScore) {
			winner = 1;
			scene = "END";
		}

		if (player2.score >= maxScore) {
			winner = 2;
			scene = "END";
		}

		if (p1_up)
			player1.move(true);		
		if (p1_down)		
			player1.move(false);
		
		if (p2_up)
			player2.move(true);
		if (p2_down)
			player2.move(false);
		
		ball.update(player1, player2);
	}

	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (scene.equals("GAMEPAGE"))
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", 1, 80));
			g.drawString("Gene's Pong", width / 2 - 220, height / 2 - 80);
			
			g.setFont(new Font("Courier", 1, 20));
			g.drawString("Press Enter: Play", width / 2 - 220, height / 2 - 25);
			g.drawString("Press Space: Pause", width / 2 - 220, height / 2 );

			g.drawString("Paddle Size: "+ paddleSizeString (paddleSize) + "  (press p + right)", width / 2 - 220, height / 2 + 50);
			g.drawString("Ball Size: "+ ballSizeString (ballSize) + "  (press b + right)", width / 2 - 220, height / 2 + 75);
			g.drawString("Ball Speed: "+ speedString (ballSpeed) + "  (press s + right)", width / 2 - 220, height / 2 + 100);
			g.drawString("Max Score: "+ maxScore + "  (press m + left or right)", width / 2 - 220, height / 2 + 125);	
			g.drawString("Golden Game: "+ goldenChallengeString(goldenChallenge) + "  (press g + right)", width / 2 - 220, height / 2 + 150);	
		}

		if (scene.equals("STILL"))
		{
			g.setColor(Color.GRAY);
			g.setFont(new Font("Courier", 1, 30));
			g.drawString("Paused", 20, 40);
		}
		

		if (scene.equals("STILL") || scene.equals("BATTLE"))
		{
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(1f));
			g.drawLine(width / 2, 0, width / 2, height);
			
			g.setColor(Color.GRAY);
			g.setStroke(new BasicStroke(2f));
			g.setFont(new Font("Courier", 1, 150));
			g.drawString(String.valueOf(player1.score), width / 2 - 250, height/2 + 50);
			g.drawString(String.valueOf(player2.score), width / 2 + 180, height/2 + 50);			
			
			//paddle action
			if(paddleSize == 0) {
				ball.setPaddleAction("wild");
			}
			else {
				player1.setHeight(paddleSize * 50 + 50);
				player2.setHeight(paddleSize * 50 + 50);
			}
			
			//ball size
			if(ballSize == 0)
				ball.setBallAction("random");
			else
				ball.setBallSize(ballSize*10);

			//ball speed
			if(ballSpeed == 0)
				ball.setBallSpeedAction("random");
			else
				ball.setBallSpeed(ballSpeed*3);

			//golden challenge round !!!
			if (goldenChallenge > 0 && (player1.score == maxScore - 1) && (player1.score == player2.score) ) {
				player1.render(g, Color.BLACK);
				player2.render(g, Color.BLACK);
				ball.render(g, Color.YELLOW);				
			} else {
				player1.render(g, Color.BLUE);
				player2.render(g, Color.RED);
				ball.render(g, Color.WHITE);				
			}

		}

		if (scene.equals("END"))
		{			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier", 1, 35));
			g.drawString("Player " + winner + " is a pro pong player", width / 2 - 300, height / 2 - 40);
			
			g.setFont(new Font("Courier", 1, 20));
			g.drawString("Press ESC to back to Start Page", width / 2 - 200, height / 2 + 40);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (scene.equals("BATTLE"))
		{
			update();
		}

		jpanel.repaint();
	}

	public static void main(String[] args)
	{
		pong = new Pong();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_A) 
			p1_up = true;
		else if (id == KeyEvent.VK_Z)		
			p1_down = true;
		else if (id == KeyEvent.VK_UP)		
			p2_up = true;
		else if (id == KeyEvent.VK_DOWN)		
			p2_down = true;
		else if (id == KeyEvent.VK_T && scene == "GAMEPAGE")		
			control = "TABLE_SIZE";
		else if (id == KeyEvent.VK_B && scene == "GAMEPAGE")		
			control = "BALL_SIZE";
		else if (id == KeyEvent.VK_P && scene == "GAMEPAGE")		
			control = "PADDLE_SIZE";
		else if (id == KeyEvent.VK_S && scene == "GAMEPAGE")		
			control = "BALL_SPEED";
		else if (id == KeyEvent.VK_M && scene == "GAMEPAGE")		
			control = "MAX_SCORE";
		else if (id == KeyEvent.VK_G && scene == "GAMEPAGE")		
			control = "GOLDEN_CHALLENGE";
		else if (id == KeyEvent.VK_RIGHT && scene == "GAMEPAGE") {
			numberControl(+1);
		} else if (id == KeyEvent.VK_LEFT && scene == "GAMEPAGE") {
			numberControl(-1);
		} else if (id == KeyEvent.VK_ESCAPE && (scene == "BATTLE" || scene == "END")) {
			scene = "GAMEPAGE";
		} else if (id == KeyEvent.VK_ENTER && (scene == "GAMEPAGE" || scene == "END")) { 
				start();
		} else if (id == KeyEvent.VK_SPACE) {
			if (scene == "STILL") 
				scene = "BATTLE";
			else if (scene == "BATTLE") 
				scene = "STILL";
		}
	}
	
	public void numberControl (int num) {
		if (control.equals("BALL_SIZE")) {
			ballSize += num;
			ballSize %= 5;
		} else if (control.equals("PADDLE_SIZE")) {
			paddleSize += num;
			paddleSize %= 4;
		} else if (control.equals("BALL_SPEED")) {
			ballSpeed += num;
			ballSpeed %= 4;
		} else if (control.equals("MAX_SCORE"))	 {
			maxScore += num;
			if(maxScore <0)
				maxScore = 0;
		} else if (control.equals("GOLDEN_CHALLENGE")) {
			goldenChallenge += num;
			goldenChallenge %= 2;
		}
	}
	
	public String goldenChallengeString (int num) {
		if (num > 0) 
			return "On";
		else 
			return "Off";
	}

	public String ballSizeString (int num) {
		String size = "medium";
		
		if(num == 0)
			size = "random";
		else if (num == 1)
			size = "tiny";
		else if (num == 2)
			size = "small";
		else if (num == 3)
			size = "medium";
		else if (num == 4)
			size = "large";
		
		return size;
	}
	
	public String sizeString (int num) {
		String size = "medium";
		
		if(num == 0)
			size = "random";
		else if (num == 1)
			size = "small";
		else if (num == 2)
			size = "medium";
		else if (num == 3)
			size = "large";
		
		return size;
	}

	public String paddleSizeString (int num) {
		String size = "wild";
		
		if(num == 0)
			size = "wild";
		else if (num == 1)
			size = "small";
		else if (num == 2)
			size = "medium";
		else if (num == 3)
			size = "large";
		
		return size;
	}

	public String speedString (int num) {
		String level = "normal";
		if(num == 0)
			level = "random";
		else if (num == 1)
			level = "slow";
		else if (num == 2)
			level = "normal";
		else if (num == 3)
			level = "fast";		
		return level;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_A)
			p1_up = false;
		else if (id == KeyEvent.VK_Z)
			p1_down = false;
		else if (id == KeyEvent.VK_UP)
			p2_up = false;		
		else if (id == KeyEvent.VK_DOWN)		
			p2_down = false;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
