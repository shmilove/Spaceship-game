import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;


public class EnemyshipSprite extends SpaceshipSprite
{
	private int fireRate;
	
	public EnemyshipSprite(int x, int y, int w, int h, String imgName) 
	{
		super(x, y, w, h, 90, 1,0,imgName);
		fireRate = 0;
		switch (Stages.currentStage) 
		{
		case 1:
			stageOne();
			break;
		case 2:
			stageTwo();
			break;
		case 3:
			stageThree();
			break;
		}
	}
	

	public void drawSprite(Graphics2D g)
	{
		g.drawImage(bImage, locX, locY, null);
	}

	
	private void stageOne()
	{
		hp = Settings.ENEMY1_HP;
		speed = Settings.ENEMY1_SPEED;
		fireRate = Settings.ENEMY1_FIRERATE;
	}
	
	private void stageTwo()
	{
		hp = Settings.ENEMY2_HP;
		speed = Settings.ENEMY2_SPEED;
		fireRate = Settings.ENEMY2_FIRERATE;
	}
	
	private void stageThree()
	{
		hp = Settings.ENEMY3_HP;
		speed = Settings.ENEMY3_SPEED;
		fireRate = Settings.ENEMY3_FIRERATE;
	}

	public boolean fire()
	{
		if (!isDead)
		{
			Random rand = new Random();
			int num = rand.nextInt(100);
			if(num < fireRate)
				return true;  
		}
		return false;
	}
}
