import java.awt.Graphics2D;


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

}
