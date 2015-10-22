import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Time;
import java.time.Instant;
import java.util.LinkedList;


public class GameEngine 
{
	private int numOfLives = Settings.NUM_OF_LIVES;
	private int width, height;
	private BufferedImage dbImg = null;
	private Image bgImage;
	private Image lifeImage;
	private RibbonsManager rManager;
	private boolean gameStart , moveLeft = true;
	private LinkedList<EnemyshipSprite> enemyShips, deleteEnemyShips;
	private boolean win, lose, startStage1, startStage2, startBoss;
	private boolean endStage1, endStage2;
	private int score;
	private SpaceshipSprite spaceship;

	private LinkedList<BulletSprite> bullets, deleteBullets, enemyBullets ,deleteEnemyBullets;
	//	private LinkedList<AsteroidSprite> asteroids, deleteAsteroids, addAsteroids;
	private long lastShootTime;
	private long moveTime;
	private long heroCreatedTime;

	public GameEngine(int pWidth, int pHeight)
	{
		width = pWidth;
		height = pHeight;
		lifeImage = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//life.png");

		rManager = new RibbonsManager(width, height);
		gameStart = false;
		enemyShips = new LinkedList<EnemyshipSprite>();
		deleteEnemyShips = new LinkedList<EnemyshipSprite>();
		score = 0;
		win = lose = startStage2 = startBoss = endStage1 = endStage2 = false;
		spaceship = new SpaceshipSprite(width / 2 - 45, height - 100, width, height, 0, Settings.HERO_HP, Settings.HERO_SPEED, "spaceship.png");
		bullets = new LinkedList<BulletSprite>();
		deleteBullets = new LinkedList<BulletSprite>();
		enemyBullets = new LinkedList<BulletSprite>();
		deleteEnemyBullets = new LinkedList<BulletSprite>();
		lastShootTime = System.currentTimeMillis();
		moveTime = System.currentTimeMillis();
		heroCreatedTime = System.currentTimeMillis();
		startStage1 = true;

		rManager.moveDown();
	}

	private void initializeStageOne()
	{
		EnemyshipSprite enemy;
		for (int i =0; i < Settings.ENEMY_IN_A_ROW; i++)
		{
			for (int j =0; j < Settings.ENEMY_ROWS; j++)
			{
				enemy = new EnemyshipSprite(i*Settings.ENEMY1_WIDTH + Settings.ENEMY1_WIDTH_SPACE, j*Settings.ENEMY1_HEIGHT + Settings.ENEMY1_HEIGHT_SPACE, width, height, 90, Settings.ENEMY1_HP, Settings.ENEMY1_SPEED, "enemySpaceship.png");
				enemyShips.add(enemy);	
			}
		}
	}

	private void initializeStageTwo()
	{
		EnemyshipSprite enemy;
		for (int i =0; i < Settings.ENEMY_IN_A_ROW; i++)
		{
			for (int j =0; j < Settings.ENEMY_ROWS; j++)
			{
				enemy = new EnemyshipSprite(i*Settings.ENEMY1_WIDTH + Settings.ENEMY1_WIDTH_SPACE, j*Settings.ENEMY1_HEIGHT + Settings.ENEMY1_HEIGHT_SPACE, width, height, 90, 3, 6, "enemySpaceship.png");
				enemyShips.add(enemy);	
			}
		}
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
		for (BulletSprite bullet : bullets)
			bullet.drawSprite(dbg);
		//		for (AsteroidSprite asteroid : asteroids)
		//			asteroid.drawSprite(dbg);
		for (SpaceshipSprite EnemyshipSprite : enemyShips) 
		{
			if(EnemyshipSprite.getHp() > 0)
				EnemyshipSprite.drawSprite(dbg);
		}
		for (BulletSprite bulletSprite : enemyBullets) 
		{
			bulletSprite.drawSprite(dbg);
		}

		for (int i = 0, x = 10; i < numOfLives; ++i, x+=50)
		{
			dbg.drawImage(lifeImage, x, 10, null);
		}

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

		rManager.update();

		if (gameStart)
		{
			if (startStage1)
			{
				initializeStageOne();
				startStage1 = false;
			}

			if (startStage2)
			{
				initializeStageTwo();
				startStage2 = false;
			}


			if (enemyShips.isEmpty())
			{
				if (!endStage1)
				{
					endStage1 = true;
					startStage2 = true;
				}
				else if (!endStage2)
				{
					endStage2 = true;
					startBoss = true;
				}
				else
				{
					win = true;
				}
			}


			if (checkCollisions())
				doCollisionLogic();

			removeBullets();

			long now = System.currentTimeMillis();
			if (now - moveTime > Settings.MOVE_THRESHOLD)
			{
				moveEnemyShips();
				moveTime = now;
				for (EnemyshipSprite enemyshipSprite : enemyShips) 
				{
					if(enemyshipSprite.fire())
					{
						enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY1_BULLET_SPEED, 180, "EnemyBullet.png"));
					}
				}
			}

			removeEnemies();

			spaceship.updateSprite();
			for (BulletSprite bullet : bullets)
			{
				bullet.updateSprite();

			}
			for (BulletSprite bulletSprite : enemyBullets) {
				bulletSprite.updateSprite();
			}
		}

	}

	private void moveEnemyShips()
	{
		if (!enemyShips.isEmpty())
		{
			if(enemyShips.getFirst().getLocX()>0 && moveLeft)
			{
				for (SpaceshipSprite spaceshipSprite : enemyShips) 
				{		
					spaceshipSprite.moveLeft();
				}
				if(enemyShips.getFirst().getLocX() <= 0)
					moveLeft = false;
			}
			if(!moveLeft)
			{
				for (SpaceshipSprite spaceshipSprite : enemyShips) {
					spaceshipSprite.moveRight();
				}
			}
			if(enemyShips.getLast().getLocX() >= width - enemyShips.getLast().getImageWidth() - 5)
			{
				moveLeft = true;
			}
		}
	}

	public void leftKeyClicked()
	{
		if (gameStart)
			spaceship.moveLeft();
	}

	public void rightKeyClicked()
	{
		if (gameStart)
			spaceship.moveRight();
	}

	public void upKeyClicked()
	{
		if (gameStart)
			spaceship.moveUp();
	}

	public void downKeyClicked()
	{
		if (gameStart)
			spaceship.moveDown();
	}

	public void spaceKeyClicked()
	{
		long now = System.currentTimeMillis();
		if (!gameStart)
		{
			gameStart = true;
		}
		else
		{
			if (now - lastShootTime > Settings.SHOT_THRESHOLD)
			{
				bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png"));
				lastShootTime = now;
			}
		}
	}

	private boolean checkCollisions()
	{
		boolean collision = false;

		for (EnemyshipSprite enemy : enemyShips)
		{
			// check collision with spaceship
			if (!enemy.getIsCollide() && CollisionDetection.isRectangleCollide(enemy.getBoundingBox(), spaceship.getBoundingBox()))
			{
				if (CollisionDetection.isPixelCollide((int)enemy.locX, (int)enemy.locY, enemy.bImage, (int)spaceship.locX, (int)spaceship.locY, spaceship.bImage))
				{
					spaceship.setIsCollide();
					enemy.gotHit(1);
					collision = true;
				}
			}

			// check collision with bullets
			for (BulletSprite bullet : bullets)
			{
				if (!enemy.getIsCollide() && !bullet.getIsCollide() && CollisionDetection.isRectangleCollide(enemy.getBoundingBox(), bullet.getBoundingBox()))
				{
					if (CollisionDetection.isPixelCollide((int)enemy.locX, (int)enemy.locY, enemy.bImage, (int)bullet.locX, (int)bullet.locY, bullet.bImage))
					{
						bullet.setIsCollide();
						enemy.gotHit(1);
						collision = true;
					}
				}
			}
		}
		boolean isInvulnerable = false;
		long now = System.currentTimeMillis();
		if (now - heroCreatedTime < Settings.INVULNERABLE)
			isInvulnerable = true;
		for (BulletSprite bullet : enemyBullets)
		{
			// check collision with spaceship
			if (!isInvulnerable && !bullet.getIsCollide() && CollisionDetection.isRectangleCollide(bullet.getBoundingBox(), spaceship.getBoundingBox()))
			{
				if (CollisionDetection.isPixelCollide((int)bullet.locX, (int)bullet.locY, bullet.bImage, (int)spaceship.locX, (int)spaceship.locY, spaceship.bImage))
				{
					spaceship.setIsCollide();
					bullet.setIsCollide();
					collision = true;
				}
			}
		}


		return collision;
	}

	private void drawInstructions(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 34));
		g.drawString("Welcome to SW Invaders Game!", width/2 - 260, 100);
		g.setFont(new Font("Arial", Font.PLAIN, 26));
		g.drawString("Your mission is to destroy all your enemies", width/2 - 240, 160);
		g.drawString("this is a very dangerous mission, please be", width/2 - 240, 190);
		g.drawString("careful - try to dodge enemy attacks and try", width/2 - 240, 220);
		g.drawString("to kill them as soon as possible.", width/2 - 170, 250);
		g.drawString("when hitted - you are invulnerable for second", width/2 - 250, 280);
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
				spaceship = new SpaceshipSprite(width / 2 - 45, height - 100, width, height, 0, Settings.HERO_HP, Settings.HERO_SPEED, "spaceship.png");
				heroCreatedTime = System.currentTimeMillis();
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
		deleteBullets.clear();
		for (BulletSprite bullet : bullets)
		{
			if (bullet.getIsCollide() || bullet.getMarkForDelete())
			{
				deleteBullets.add(bullet);
			}
		}
		bullets.removeAll(deleteBullets);
		for (BulletSprite bulletSprite : enemyBullets)
		{
			if (bulletSprite.getIsCollide() || bulletSprite.getMarkForDelete())
			{
				deleteEnemyBullets.add(bulletSprite);
			}
		}
		enemyBullets.removeAll(deleteEnemyBullets);
	}

	private void removeEnemies()
	{
		deleteEnemyShips.clear();
		for (EnemyshipSprite enemy: enemyShips)
		{
			if (enemy.getIsCollide() || enemy.getIsDead())
			{
				deleteEnemyShips.add(enemy);
			}
		}
		enemyShips.removeAll(deleteEnemyShips);
	}
}
