import java.awt.Graphics2D;
import java.util.Random;


public class EnemyshipSprite extends SpaceshipSprite{

	private boolean isDead = false;
	
	public EnemyshipSprite(int x, int y, int w, int h, int angle, int hp,
			int speed, String imgName) {
		super(x, y, w, h, angle, hp, speed, imgName);
		
		
	}

    public void drawSprite(Graphics2D g)
    {
    	if(!isDead)
    		g.drawImage(bImage, locX, locY, null);
    	
    }
    
    public boolean fire()
    {
    	Random rand = new Random();
    	int num = rand.nextInt(100);
    	if(num > 80)
    		return true;
    	return false;
    }

}
