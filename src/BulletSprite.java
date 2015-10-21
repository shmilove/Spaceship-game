import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BulletSprite extends Sprite 
{
	private boolean markForDelete;
	
	public BulletSprite(int x, int y, int w, int h, int speed, int angle, String imageName) 
	{
		super(x, y, w, h, speed, angle,0);
		markForDelete = false;
		try 
		{
			setImage(ImageIO.read(new File(System.getProperty("user.dir") + "//" + imageName)));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public boolean getMarkForDelete()
	{
		return markForDelete;
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
        
        if (locY <= 0)
        	markForDelete = true;
        
        if (locY >= pHeight)
        	markForDelete = true;
    }
	
	public Rectangle getBoundingBox()
    {
        return new Rectangle((int)locX - imageWidth/2, (int)locY - imageHeight/2, imageWidth, imageHeight);
    }
}
