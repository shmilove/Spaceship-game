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
			setImage(ImageIO.read(new File(System.getProperty("user.dir") + "\\" + imgName)));
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
	
//	public void turnLeft()
//	{
//		angle += Settings.ROTATION_SPEED;
//	}
//	
//	public void turnRight()
//	{
//		angle -= Settings.ROTATION_SPEED;
//	}
	
//	public void speedUp()
//	{
//		if (speed >= Settings.MAX_SPEED)
//			speed = Settings.MAX_SPEED;
//		else
//			speed += Settings.SPEED_INCREASE_RATE;
//	}
	
	public void stop()
	{
		speed = 0;
	}
	
    public void drawSprite(Graphics2D g)
    {
//    	AffineTransform oldtrans = g.getTransform();
//	    AffineTransform trans = new AffineTransform();
//
//	    trans.setToIdentity();
//	    trans.rotate(Math.toRadians(90 - angle), locX, locY);
//	    trans.translate(locX-(imageWidth/2), locY-(imageHeight/2));
//	    g.setTransform(trans);
	    g.drawImage(bImage, locX, locY, null);
//	    trans.setToIdentity();
//	    g.setTransform(oldtrans);
    }
    public void hasMoreHp(int damage)
    {
    	hp = hp - damage;
    	
    	if(hp <= 0)
    		isDead = true;
    	
    }
    public void updateSprite()
    {
    	
    }
    
    public Rectangle getBoundingBox()
    {
        return new Rectangle((int)locX-(imageWidth/2), (int)locY-(imageHeight/2), imageWidth, imageHeight);
    }
}
