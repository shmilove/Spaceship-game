import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Sprite 
{
    protected BufferedImage bImage;
    protected int imageWidth, imageHeight; // image dimensions
    
    protected int angle;
    protected double speed, locX, locY;
    protected int pWidth, pHeight;  // panel's dimensions
    protected boolean isCollide;
    
    public Sprite(double x, double y, int w, int h, int speed, int angle) 
    {
        locX = x;
        locY = y;
        this.speed = speed;
        this.angle = angle;
        pWidth = w;
        pHeight = h;
        isCollide = false;
    }
    
    public boolean getIsCollide()
	{
		return isCollide;
	}

	public void setIsCollide()
	{
		isCollide = true;
	}
    
    public void setImage(BufferedImage img)
    {
    	bImage = img;
    	if(bImage != null)
        {
            imageWidth = bImage.getWidth(null);
            imageHeight = bImage.getHeight(null);
        }
    }
    
    public Rectangle getBoundingBox()
    {
        return new Rectangle((int)getLocX(), (int)getLocY(), imageWidth, imageHeight);
    }

    public double getLocX() {
        return locX;
    }
    
    public double getLocY() {
        return locY;
    }
    
    public double getSpeed()
    {
    	return speed;
    }
    
    public int getAngle()
    {
    	return angle;
    }
    
    public int getImageWidth() 
    { 
        return imageWidth;
    }
    
    public int getImageHeight()
    {
        return imageHeight;
    }
    
    public void updateSprite()
    {
    	double radian = Math.toRadians(360 - angle);
    	// move at the x axis
        locX += speed * Math.cos(radian);
        if (locX >= pWidth + imageWidth)
        	locX = 0 - imageWidth;
        else if (locX < 0 - imageWidth)
        	locX = pWidth;
        // move at the y axis
        locY += speed * Math.sin(radian);
        if (locY >= pHeight)
        	locY = 0 - imageHeight;
        else if (locY < 0 - imageHeight)
        	locY = pHeight;
    }
    
    public void drawSprite(Graphics2D g)
    {
       g.fillRect((int)locX, (int)locY, imageWidth, imageHeight);
    }
    
    
}

