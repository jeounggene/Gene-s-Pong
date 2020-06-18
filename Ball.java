import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball
{

	public int x, y, width = 20, height = 20;
	public int velocityX, velocityY;
	public Random random;
	private Pong pong;
	public String paddleAction = "normal";
	public String ballAction = "normal";
	public String ballSpeedAction = "normal";
	public int speed = 5;

	public void setBallSize (int size) {
		this.width = size;
		this.height = size;
	}		

	public void setBallSpeed (int speed) {
		this.speed = speed;
	}	
	
	public void setBallSpeedAction (String action) {
		this.ballSpeedAction = action;
	}	
	
	public void setBallAction (String action) {
		this.ballAction = action;
	}	
	
	public void setPaddleAction (String action) {
		this.paddleAction = action;
	}		
	
	public Ball(Pong pong){
		this.pong = pong;
		this.random = new Random();
		makeBall();
	}

	public Ball(Pong pong, int ballSize) {
		this.pong = pong;
		this.random = new Random();
		this.width = ballSize;
		this.height = ballSize;
		makeBall();
	}
	
	public void update(Paddle p1, Paddle p2) {

		this.x += velocityX * speed;
		this.y += velocityY * speed;

		if (this.y + height - velocityY > pong.height || this.y + velocityY < 0) {
		
			if (this.ballSpeedAction.equals("random")) {
				int rand = random.nextInt(4);
				this.setBallSpeed(rand*4 + 3);
			} 
			
			if (this.velocityY < 0) {
				this.y = 0;
				this.velocityY = -this.velocityY;

				if (velocityY == 0)				
					velocityY = 1;				
			}
			else {
				this.velocityY = -this.velocityY;
				this.y = pong.height - height;

				if (velocityY == 0)				
					velocityY = -1;
			}
		}

		if (checkCollision(p1) == 1) {
			if (this.ballSpeedAction.equals("random")) {
				int rand = random.nextInt(4);
				this.setBallSpeed(rand*4 + 3);
			} 

			if(this.y + this.width/2 < p1.y &&this.x + this.width/2 < p1.x + p1.width && this.y < p1.y && this.y + this.height > p1.y) { //hitting from top				
				if(this.velocityY > 0)
					this.velocityY = -this.velocityY;
			} else if(this.y + this.width/2 > p1.y + p1.height && this.x + this.width/2 < p1.x + p1.width && this.y < p1.y + p1.height && this.y + this.height > p1.y + this.height) { //hitting from bottom
				if(this.velocityY < 0)
					this.velocityY = -this.velocityY;
			} else { 			
				if(this.velocityX < 0)				
					this.velocityX = -this.velocityX;
				else if (velocityX == 0) velocityX = 1;
			}
			
			if(ballAction.equals("random")) {
				int rand = random.nextInt(5);				
				this.setBallSize(rand*10);				
			}
		}
		else if (checkCollision(p2) == 1) {
			if (this.ballSpeedAction.equals("random")) {
				int rand = random.nextInt(4);
				this.setBallSpeed(rand*4 + 3);
			} 
				
			if(this.y + this.width/2 < p1.y && this.x + this.width/2 > p1.x && this.y < p1.y && this.y + this.height > p1.y) { //hitting from top
				if(this.velocityY > 0)
					this.velocityY = -this.velocityY;
			} else if(this.y + this.width/2 > p1.y + p1.height && this.x + this.width/2 > p1.x && this.y < p1.y + p1.height && this.y + this.height > p1.y + this.height) { //hitting from bottom
				if(this.velocityY < 0)
					this.velocityY = -this.velocityY;
			} else { 
				if(this.velocityX > 0)				
 					this.velocityX = -this.velocityX;
				else if (velocityX == 0) velocityX = -1;
			}
			
			if(ballAction.equals("random")) {
				int rand = random.nextInt(5);
				if(rand == 0) rand = 3;
				this.setBallSize(rand*10);				
			}
		}

		if (checkCollision(p1) == 2) {
			p2.score++;
			if(paddleAction.equals("wild")) {
				if(p2.getHeight() > 60) 
					p2.setHeight(p2.getHeight() - 30);

				p1.setHeight(p1.getHeight() + 30);
			}
			makeBall();
		} else if (checkCollision(p2) == 2) {
			p1.score++;
			if(paddleAction.equals("wild")) {
				if(p1.getHeight() > 60) 
					p1.setHeight(p1.getHeight() - 30);
				
				p2.setHeight(p2.getHeight() + 30);
			}
			makeBall();
		}
	}

	public void makeBall() {
		this.x = pong.width / 2 - this.width / 2;
		this.y = pong.height / 2 - this.height / 2;

		this.velocityY = -2 + random.nextInt(4);

		if (velocityY == 0)
			velocityY = 1;

		if (random.nextBoolean())
			velocityX = 1;
		else
			velocityX = -1;
	}

	public int checkCollisionChecker(Paddle paddle) {
		boolean hitPaddleLeft = this.x == paddle.x + paddle.width;
		boolean hitPaddleRight = this.x + width == paddle.x;
		boolean hitPaddleTop = this.y + height == paddle.y;
		boolean hitPaddleBottom = this.y == paddle.y + paddle.height;
			
		if (hitPaddleLeft || hitPaddleRight || hitPaddleTop || hitPaddleBottom) {
			return 1; //get on the paddle
		} 
		return 0;
	}
	
	public int checkCollision(Paddle paddle) {
		boolean condition1 = this.x < paddle.x + paddle.width;
		boolean condition2 = this.x + width > paddle.x;
		boolean condition3 = this.y + height > paddle.y;
		boolean condition4 = this.y < paddle.y + paddle.height;
			
		if (condition1 && condition2 && condition3 && condition4) {
			return 1; //get on the paddle
		} else if ((paddle.x > x && paddle.id == 1) || 
				   (paddle.x < x - width && paddle.id == 2)) 
			return 2; //get out of the paddle
		
		return 0; 
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}

	public void render(Graphics g, Color newColor) {
		g.setColor(newColor);
		g.fillOval(x, y, width, height);
	}	
}
