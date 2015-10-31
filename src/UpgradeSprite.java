import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UpgradeSprite extends Sprite 
{
	private boolean markForDelete;

	public UpgradeSprite(int x, int y, int w, int h, int speed, int angle, String imageName) 
	{
		super(x, y, w, h, speed, angle, 0);
		
		try 
		{
			setImage(ImageIO.read(new File(System.getProperty("user.dir") + "//images//" + imageName)));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void updateSprite()
    {
		locY = locY + speed;
		
		if (locY >= pHeight)
        	markForDelete = true;
    }
	
	public boolean getMarkForDelete()
	{
		return markForDelete;
	}
	
	public void drawSprite(Graphics2D g)
    {
	    g.drawImage(bImage, locX, locY, null);
    }
	
	public Rectangle getBoundingBox()
    {
        return new Rectangle((int)locX - imageWidth/2, (int)locY - imageHeight/2, imageWidth, imageHeight);
    }

}
