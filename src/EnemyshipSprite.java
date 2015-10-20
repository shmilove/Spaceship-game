import java.awt.Graphics2D;
import java.util.Random;


public class EnemyshipSprite extends SpaceshipSprite
{
	public EnemyshipSprite(int x, int y, int w, int h, int angle, int hp, int speed, String imgName) 
	{
		super(x, y, w, h, angle, hp, speed, imgName);
	}

    public void drawSprite(Graphics2D g)
    {
    	g.drawImage(bImage, locX, locY, null);
    }
    
    public void updateSprite()
    { 
    
    }
    
    public boolean fire()
    {
    	if (!isDead)
    	{
    		Random rand = new Random();
        	int num = rand.nextInt(100);
        	if(num > 98)
        		return true;  
    	}
    	return false;
    }
}
