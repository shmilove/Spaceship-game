import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpaceshipSprite extends Sprite
{
	protected boolean isDead = false;
	public SpaceshipSprite(int x, int y, int w, int h, int angle, int hp, int speed, String imgName) 
	{
		super(x, y, w, h, speed, angle, hp);
		try 
		{
			setImage(ImageIO.read(new File(System.getProperty("user.dir") + "//images//" + imgName)));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void moveLeft()
	{
		locX -= speed;
		if (locX <= 0)
			locX = 0;
	}
	
	public void moveRight()
	{
		locX += speed;
		if (locX >= pWidth - imageWidth - 5)
			locX = pWidth - imageWidth - 5;
	}
	
	public void moveUp()
	{
		locY -= speed;
		if (locY <= 0)
			locY = 0;
	}
	
	public void moveDown()
	{
		locY += speed;
		if (locY >= pHeight - imageHeight - 35)
			locY = pHeight - imageHeight - 35;
	}
	
	public void stop()
	{
		speed = 0;
	}
	
    public void drawSprite(Graphics2D g)
    {
	    g.drawImage(bImage, locX, locY, null);
    }
    
    public void gotHit(int damage)
    {
    	hp--;
    	if(hp <= 0)
    		isDead = true;
    }
    
    public boolean getIsDead()
    {
    	return isDead;
    }
    
    public void updateSprite()
    {
    	
    }
}
