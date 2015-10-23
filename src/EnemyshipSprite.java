import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;


public class EnemyshipSprite extends SpaceshipSprite
{
	private enum  Stages{ defult, stage_one, stage_two };
	
	public EnemyshipSprite(int x, int y, int w, int h, int stage,String imgName) 
	{
		super(x, y, w, h, 90, 1,0,imgName);
		switch (stage) {
		case 1:
			stageOne();
			break;

		case 2:
			stageTwo();
			break;
		
		}
	}

    public void drawSprite(Graphics2D g)
    {
    	g.drawImage(bImage, locX, locY, null);
    }
    
    public void updateSprite()
    { 
    
    }
    private void stageOne()
    {
    	angle = 90;
     	hp = Settings.HP_ENEMY_ONE;
     	speed = Settings.ENEMY1_SPEED;
    }
    private void stageTwo()
    {
    	angle =90;
    	hp = Settings.HP_ENEMY_TWO;
    	speed = Settings.ENEMY1_SPEED*2;
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
