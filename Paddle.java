import java.awt.Color;
import java.awt.Graphics;

public class Paddle
{

	public int id;
	public int x, y, width = 20, height = 200;
	public int score;

	public int getWidth () {
		return this.width;
	}
	
	public int getHeight () {
		return this.height;
	}
	
	public void setWidth (int size) {
	}
	
	public void setHeight (int size) {
		this.height = size;
	}
	
	public Paddle(Pong pong, int id) {
		this.id = id;

		if (id == 1)
			this.x = 0;		
		else if (id == 2)	
			this.x = pong.width - width;		

		this.y = pong.height / 2 - this.height / 2;
	}

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x, y, width, height);
	}

	public void render(Graphics g, Color newColor) {
		g.setColor(newColor);
		g.fillRect(x, y, width, height);
	}
	
	public void move(boolean up) {
		int speed = 20;

		if (up) {
			if (y - speed > 0)
				y -= speed;
			else
				y = 0;		
		} else {
			if (y + height + speed < Pong.pong.height)			
				y += speed;			
			else			
				y = Pong.pong.height - height;			
		}
	}

}
