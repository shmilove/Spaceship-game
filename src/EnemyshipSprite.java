import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;


public class EnemyshipSprite extends SpaceshipSprite
{
	public enum Stages{ STAGE_ONE, STAGE_TWO, STAGE_THREE };
	private Stages type;
	private int fireRate;

	public EnemyshipSprite(int x, int y, int w, int h, int stage,String imgName) 
	{
		super(x, y, w, h, 90, 1,0,imgName);
		fireRate = 0;
		switch (stage) 
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
	
	public Stages getType()
	{
		return type;
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
		type = Stages.STAGE_ONE;
		angle = 90;
		hp = Settings.HP_ENEMY_ONE;
		speed = Settings.ENEMY1_SPEED;
		fireRate = 3;
	}
	
	private void stageTwo()
	{
		type = Stages.STAGE_TWO;
		angle = 90;
		hp = Settings.HP_ENEMY_ONE;
		speed = Settings.ENEMY1_SPEED*2;
		fireRate = 4;
	}
	
	private void stageThree()
	{
		type = Stages.STAGE_THREE;
		angle = 90;
		hp = 100;
		speed = Settings.ENEMY1_SPEED*2;
		fireRate = 7;
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
