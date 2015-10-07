import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;


public class GameEngine 
{
	private int numOfLives = Settings.NUM_OF_LIVES;
	private int width, height;
	private BufferedImage dbImg = null;
	private Image bgImage;
	private Image lifeImage;
	private RibbonsManager rManager;
    private boolean gameStart;

	private boolean win, lose;
	private int score;
	private SpaceshipSprite spaceship;
//	private LinkedList<BulletSprite> bullets, deleteBullets;
//	private LinkedList<AsteroidSprite> asteroids, deleteAsteroids, addAsteroids;
	private long lastShootTime;

	public GameEngine(int pWidth, int pHeight)
	{
		width = pWidth;
		height = pHeight;
//		lifeImage = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//life.png");
		
		rManager = new RibbonsManager(width, height);
        gameStart = false;
		
		score = 0;
		win = lose = false;
		spaceship = new SpaceshipSprite(width / 2, height - 100, width, height);
//		bullets = new LinkedList<BulletSprite>();
//		deleteBullets = new LinkedList<BulletSprite>();
//		asteroids = new LinkedList<AsteroidSprite>();
//		deleteAsteroids = new LinkedList<AsteroidSprite>();
//		addAsteroids = new LinkedList<AsteroidSprite>();

//		asteroids.add(new AsteroidSpriteLarge(10, 20, width, height));
//		asteroids.add(new AsteroidSpriteLarge(320, 20, width, height));
//		asteroids.add(new AsteroidSpriteLarge(10, 500, width, height));
//		asteroids.add(new AsteroidSpriteLarge(450, 500, width, height));
		lastShootTime = System.currentTimeMillis();
		
		rManager.moveDown();
	}

	public boolean isGameOver()
	{
		return win || lose;
	}

	public BufferedImage drawGame()
	{
		Graphics2D dbg;

		dbImg = new BufferedImage(width, height, BufferedImage.OPAQUE);
		dbg = dbImg.createGraphics();
		dbg.setColor(Color.WHITE);
		dbg.fillRect(0, 0, width, height);
		dbg.drawImage(bgImage, 0, 0, null);

		rManager.display(dbg);
		
		// draw game elements
		spaceship.drawSprite(dbg);
//		for (BulletSprite bullet : bullets)
//			bullet.drawSprite(dbg);
//		for (AsteroidSprite asteroid : asteroids)
//			asteroid.drawSprite(dbg);
		
//		for (int i = 0, x = 10; i < numOfLives; ++i, x+=50)
//		{
//			dbg.drawImage(lifeImage, x, 10, null);
//		}
		
		dbg.setFont(new Font("Arial", Font.BOLD, 16));
		dbg.setColor(Color.WHITE);
		dbg.drawString("Score: " + score, width - 100, 30);

		if (!gameStart)
        {
        	drawInstructions(dbg);
        }
		
		if (win || lose)
		{
			gameOverMessage(dbg);
		}

		return dbImg;
	}

	public void updateGame()
	{
//		if (asteroids.isEmpty())
//			win = true;
		rManager.update();
		
		if (gameStart)
		{
			if (checkCollisions())
				doCollisionLogic();
			
			removeBullets();
			
			spaceship.updateSprite();
//			for (BulletSprite bullet : bullets)
//				bullet.updateSprite();
//			for (AsteroidSprite asteroid : asteroids)
//				asteroid.updateSprite();
		}
		
	}

	public void leftKeyClicked()
	{
		spaceship.turnLeft();
	}

	public void rightKeyClicked()
	{
		spaceship.turnRight();
	}

	public void upKeyClicked()
	{
		spaceship.speedUp();
	}

	public void spaceKeyClicked()
	{
		if (!gameStart)
    	{
    		gameStart = true;
    	}
		else
		{
			long now = System.currentTimeMillis();
			if (now - lastShootTime > Settings.SHOT_THRESHOLD)
			{
//				bullets.add(new BulletSprite(spaceship.locX, spaceship.locY, width, height, spaceship.angle));
				lastShootTime = now;
			}
		}
	}

	public void upKeyReleased()
	{
		spaceship.stop();
	}

	private boolean checkCollisions()
	{
		boolean collision = false;

//		for (AsteroidSprite asteroid : asteroids)
//		{
//			// check collision with spaceship
//			if (!asteroid.getIsCollide() && CollisionDetection.isRectangleCollide(asteroid.getBoundingBox(), spaceship.getBoundingBox()))
//			{
//				if (CollisionDetection.isPixelCollide((int)asteroid.locX, (int)asteroid.locY, asteroid.bImage, (int)spaceship.locX-(spaceship.imageWidth/2), (int)spaceship.locY-(spaceship.imageHeight/2), spaceship.bImage))
//				{
//					spaceship.setIsCollide();
//					asteroid.setIsCollide();
//					collision = true;
//				}
//			}
//
//			// check collision with bullets
//			for (BulletSprite bullet : bullets)
//			{
//				if (!asteroid.getIsCollide() && !bullet.getIsCollide() && CollisionDetection.isRectangleCollide(asteroid.getBoundingBox(), bullet.getBoundingBox()))
//				{
//					if (CollisionDetection.isPixelCollide((int)asteroid.locX, (int)asteroid.locY, asteroid.bImage, (int)bullet.locX-(bullet.imageWidth/2), (int)bullet.locY-(bullet.imageHeight/2), bullet.bImage))
//					{
//						bullet.setIsCollide();
//						asteroid.setIsCollide();
//						collision = true;
//					}
//				}
//			}
//		}
//
		return collision;
	}
	
	private void drawInstructions(Graphics g)
	{
		g.setColor(Color.WHITE);
    	g.setFont(new Font("Arial", Font.BOLD, 34));
    	g.drawString("Welcome to BLABLABLA Game!", width/2 - 260, 100);
    	g.setFont(new Font("Arial", Font.PLAIN, 26));
    	g.drawString("Your mission is to destroy all your enemies", width/2 - 240, 160);
    	g.drawString("this is a very dangerous mission, please be", width/2 - 240, 190);
    	g.drawString("careful - try to dodge enemy attacks and try", width/2 - 240, 220);
    	g.drawString("to kill them as soon as possible.", width/2 - 170, 250);
    	g.drawString("collect weapon improvemnts - it will help!", width/2 - 240, 280);
    	g.drawString("Please use the following keys:", width/2 - 240, 370);
    	g.drawString("Arrow keys - move your spaceship", width/2 - 240, 430);
    	g.drawString("Space - shoot your enemies", width/2 - 240, 460);
    	g.setFont(new Font("Arial", Font.BOLD, 30));
    	g.drawString("Good Luck!", width/2 - 100, 580);
    	g.setFont(new Font("Arial", Font.PLAIN, 26));
    	g.drawString("Press space when you are ready...", width/2 - 220, 630);
	}

	private void gameOverMessage(Graphics g)
	{
		if (lose)
		{
			g.setFont(new Font("Arial", Font.PLAIN, 34));
			g.setColor(Color.WHITE);
			g.drawString("Game Over!", width/2 - 70, height/2);
			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.drawString("Your Final Score: " + score,width/2 - 76, height/2 + 40);
		}
		else
		{
			g.setFont(new Font("Arial", Font.PLAIN, 34));
			g.setColor(Color.WHITE);
			g.drawString("You Win!", width/2 - 76, height/2);
			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.drawString("Your Final Score: " + score,width/2 - 100, height/2 + 40);
		}
	}
	
	private void doCollisionLogic()
	{
//		deleteAsteroids.clear();
//		addAsteroids.clear();
		if (spaceship.getIsCollide())
		{
			numOfLives--;
			if (numOfLives > 0)
			{
				spaceship = new SpaceshipSprite(width / 2, height / 2, width, height);
			}
			else
			{
				lose = true;
			}
		}
//		for (AsteroidSprite asteroid : asteroids)
//		{
//			if (asteroid.getIsCollide())
//			{
//				addAsteroids = asteroid.setState();
//				deleteAsteroids.add(asteroid);
//				score += asteroid.getScore();
//			}
//		}
//		asteroids.removeAll(deleteAsteroids);
//		asteroids.addAll(addAsteroids);
	}
	
	private void removeBullets()
	{
//		deleteBullets.clear();
//		for (BulletSprite bullet : bullets)
//		{
//			if (bullet.getIsCollide() || bullet.getPassDistance())
//			{
//				deleteBullets.add(bullet);
//			}
//		}
//		bullets.removeAll(deleteBullets);
	}
}
