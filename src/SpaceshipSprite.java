import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpaceshipSprite extends Sprite
{
	public SpaceshipSprite(double x, double y, int w, int h) 
	{
		super(x, y, w, h, 0, 90);
		try 
		{
			setImage(ImageIO.read(new File(System.getProperty("user.dir") + "//spaceship.png")));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void turnLeft()
	{
		angle += Settings.ROTATION_SPEED;
	}
	
	public void turnRight()
	{
		angle -= Settings.ROTATION_SPEED;
	}
	
	public void speedUp()
	{
		if (speed >= Settings.MAX_SPEED)
			speed = Settings.MAX_SPEED;
		else
			speed += Settings.SPEED_INCREASE_RATE;
	}
	
	public void stop()
	{
		speed = 0;
	}
	
    public void drawSprite(Graphics2D g)
    {
    	AffineTransform oldtrans = g.getTransform();
	    AffineTransform trans = new AffineTransform();

	    trans.setToIdentity();
	    trans.rotate(Math.toRadians(90 - angle), locX, locY);
	    trans.translate(locX-(imageWidth/2), locY-(imageHeight/2));
	    g.setTransform(trans);
	    g.drawImage(bImage, 0, 0, null);
	    trans.setToIdentity();
	    g.setTransform(oldtrans);
    }
    
    public Rectangle getBoundingBox()
    {
        return new Rectangle((int)locX-(imageWidth/2), (int)locY-(imageHeight/2), imageWidth, imageHeight);
    }
}
