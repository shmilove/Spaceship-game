import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BulletSprite extends Sprite 
{
	private double distance;
	private boolean passDistance;
	
	public BulletSprite(int x, int y, int w, int h, int angle) 
	{
		super(x, y, w, h, Settings.BULLET_SPEED, angle,0);
		distance = 0;
		passDistance = false;
		try 
		{
			setImage(ImageIO.read(new File(System.getProperty("user.dir") + "//bullet.png")));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean getPassDistance()
	{
		return passDistance;
	}
	
	public void drawSprite(Graphics2D g)
    {
		AffineTransform oldtrans = g.getTransform();
	    AffineTransform trans = new AffineTransform();

	    trans.setToIdentity();
	    trans.rotate(Math.toRadians(angle), locX, locY);
	    trans.translate(locX-(imageWidth/2), locY-(imageHeight/2));
	    g.setTransform(trans);
	    g.drawImage(bImage, 0, 0, null);
	    trans.setToIdentity();
	    g.setTransform(oldtrans);
    }
	
	public void updateSprite()
    {
		super.updateSprite();
		
		double radian = Math.toRadians(360 - angle);
        distance += Math.sqrt(Math.pow(speed * Math.cos(radian), 2) + Math.pow(speed * Math.sin(radian), 2));
        
        if (distance >= Settings.MAX_DISTANCE)
        	passDistance = true;
    }
	
	public Rectangle getBoundingBox()
    {
        return new Rectangle((int)locX-(imageWidth/2), (int)locY-(imageHeight/2), imageWidth, imageHeight);
    }
}
