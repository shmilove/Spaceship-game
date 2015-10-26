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
import java.util.concurrent.TimeUnit;


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
	private boolean win, lose, startStage1, startStage2, startStage3;
	private boolean endStage1, endStage2, endStage3, isInvulnerable, displayStageOneText, displayStageTwoText, displayStageThreeText;
	private boolean endTextStage1, endTextStage2, endTextStage3, doOnce;
	private int score;
	private SpaceshipSprite spaceship;

	private final String themeMusicUrl = "./sounds/GameMusic.wav";
	private final String shotSoundUrl = "./sounds/LaserShot.wav";
	private final String explodeSoundUrl = "./sounds/Explosion.wav";
	private final String stageCompleteSoundUrl = "./sounds/StageCompleted.wav";
	private final int hitEnemy = 1, hitByEnemy = -1;

	private LinkedList<BulletSprite> bullets, deleteBullets, enemyBullets ,deleteEnemyBullets;

	private long lastShootTime;
	private long moveTime;
	private long heroCreatedTime, startStageOneTime, startStageTwoTime, startStageThreeTime;
	private long blinkTime, notBlinkTime;

	public GameEngine(int pWidth, int pHeight)
	{

		(new SoundThread(themeMusicUrl, AudioPlayer.LOOP)).start();
		width = pWidth;
		height = pHeight;
		lifeImage = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//images//life.png");

		rManager = new RibbonsManager(width, height);
		gameStart = false;
		enemyShips = new LinkedList<EnemyshipSprite>();
		deleteEnemyShips = new LinkedList<EnemyshipSprite>();
		score = 0;
		win = lose = startStage2 = startStage3 = endStage1 = endStage2 = false;
		spaceship = new SpaceshipSprite(width / 2 - 45, height - 100, width, height, 0, Settings.HERO_HP, Settings.HERO_SPEED, "spaceship.png");
		bullets = new LinkedList<BulletSprite>();
		deleteBullets = new LinkedList<BulletSprite>();
		enemyBullets = new LinkedList<BulletSprite>();
		deleteEnemyBullets = new LinkedList<BulletSprite>();
		lastShootTime = System.currentTimeMillis();
		moveTime = System.currentTimeMillis();
		heroCreatedTime = System.currentTimeMillis();
		startStage1 = true;;
		isInvulnerable = false;
		endTextStage1 = endTextStage2 = endTextStage3 = false;
		displayStageOneText = displayStageTwoText = displayStageThreeText = false;
		doOnce = true;

		rManager.moveDown();
	}

	private void initializeStageOne()
	{
		long now = System.currentTimeMillis();
		if (now - startStageOneTime < Settings.DISPLAY_TEXT_TIME)
		{
			displayStageOneText = true;
		}
		else
		{
			displayStageOneText = false;
			endTextStage1 = true;
			startStage1 = false;
			EnemyshipSprite enemy;
			for (int i =0; i < Settings.ENEMY_IN_A_ROW; i++)
			{
				for (int j =0; j < Settings.ENEMY_ROWS; j++)
				{
					enemy = new EnemyshipSprite(i*Settings.ENEMY1_WIDTH + Settings.ENEMY1_WIDTH_SPACE, j*Settings.ENEMY1_HEIGHT + Settings.ENEMY1_HEIGHT_SPACE, width, height,1 , "enemySpaceship.png");
					enemyShips.add(enemy);	
				}
			}
		}
	}

	private void initializeStageTwo()
	{
		long now = System.currentTimeMillis();
		if (now - startStageTwoTime < Settings.DISPLAY_TEXT_TIME)
		{
			displayStageTwoText = true;
		}
		else
		{
			displayStageTwoText = false;
			endTextStage2 = true;
			startStage2 = false;
			EnemyshipSprite enemy;
			for (int i =0; i < Settings.ENEMY_IN_A_ROW; i++)
			{
				for (int j =0; j < Settings.ENEMY_ROWS; j++)
				{
					enemy = new EnemyshipSprite(i*Settings.ENEMY1_WIDTH + Settings.ENEMY1_WIDTH_SPACE, j*Settings.ENEMY1_HEIGHT + Settings.ENEMY1_HEIGHT_SPACE, width, height, 2, "enemySpaceship.png");
					enemyShips.add(enemy);	
				}
			}	
		}
	}
	
	private void initializeStageThree()
	{
		long now = System.currentTimeMillis();
		if (now - startStageThreeTime < Settings.DISPLAY_TEXT_TIME)
		{
			displayStageThreeText = true;
		}
		else
		{
			displayStageThreeText = false;
			endTextStage3 = true;
			startStage3 = false;
			EnemyshipSprite enemy;
			enemy = new EnemyshipSprite(width/2 - 200, 40, width, height, 3, "boss.png");
			enemyShips.add(enemy);	
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

		if (!gameStart)
		{
			drawInstructions(dbg);
			return dbImg;
		}

		// draw game elements
		if (isInvulnerable)
		{
			long now = System.currentTimeMillis();
			if (now - blinkTime < Settings.BLINK_TIME)
			{
				notBlinkTime = now;
			}
			else
			{
				if (now - notBlinkTime < Settings.BLINK_TIME)
					spaceship.drawSprite(dbg);
				else
					blinkTime = now;
			}
		}
		else
		{
			spaceship.drawSprite(dbg);
		}

		if (displayStageOneText)
		{
			dbg.setFont(new Font("Arial", Font.BOLD, 60));
			dbg.setColor(Color.WHITE);
			dbg.drawString("Stage 1", width/2 - 100, height/2);
		}
		if (displayStageTwoText)
		{
			dbg.setFont(new Font("Arial", Font.BOLD, 60));
			dbg.setColor(Color.WHITE);
			dbg.drawString("Stage 2", width/2 - 100, height/2);
		}
		if (displayStageThreeText)
		{
			dbg.setFont(new Font("Arial", Font.BOLD, 60));
			dbg.setColor(Color.WHITE);
			dbg.drawString("Boss Stage", width/2 - 150, height/2);
		}

		for (BulletSprite bullet : bullets)
			bullet.drawSprite(dbg);

		for (SpaceshipSprite EnemyshipSprite : enemyShips) 
		{
			if(EnemyshipSprite.getHp() > 0)
				EnemyshipSprite.drawSprite(dbg);
		}
		for (BulletSprite bulletSprite : enemyBullets) 
		{
			bulletSprite.drawSprite(dbg);
		}

		for (int i = 0, x = 40; i < numOfLives; ++i, x+=50)
		{
			dbg.drawImage(lifeImage, width-x, 10, null);
		}

		dbg.setFont(new Font("Stencil Std", Font.PLAIN, 18));
		dbg.setColor(Color.WHITE);
		dbg.drawString("Score: " + score, 15, 30);

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
				if (doOnce)
				{
					startStageOneTime = System.currentTimeMillis();
					doOnce = !doOnce;
				}
				initializeStageOne();
			}

			if (startStage2)
			{
				if (!doOnce)
				{
					startStageTwoTime = System.currentTimeMillis();
					(new SoundThread(stageCompleteSoundUrl, AudioPlayer.ONCE)).start();
					doOnce = !doOnce;
				}
				initializeStageTwo();
			}

			if (startStage3)
			{
				if (doOnce)
				{
					startStageThreeTime = System.currentTimeMillis();
					(new SoundThread(stageCompleteSoundUrl, AudioPlayer.ONCE)).start();
					doOnce = !doOnce;
				}
				initializeStageThree();
			}


			if (enemyShips.isEmpty())
			{
				if (!endStage1 && endTextStage1)
				{
					endStage1 = true;
					startStage2 = true;
				}
				else if (!endStage2 && endTextStage2)
				{
					endStage2 = true;
					startStage3 = true;
				}
				else if (!endStage3 && endTextStage3)
				{
					endStage3 = true;
				}
				else if (endStage1 && endStage2 && endStage3)
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
						if (enemyshipSprite.getType() == EnemyshipSprite.Stages.STAGE_ONE)
						{
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY1_BULLET_SPEED, 180, "EnemyBullet.png"));
						}
						else if (enemyshipSprite.getType() == EnemyshipSprite.Stages.STAGE_TWO)
						{
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY2_BULLET_SPEED, 180, "EnemyBullet.png"));
						}
						else if (enemyshipSprite.getType() == EnemyshipSprite.Stages.STAGE_THREE)
						{
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY3_BULLET_SPEED*2, 180, "EnemyBullet.png"));
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2 - 80, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY3_BULLET_SPEED*2, 180, "EnemyBullet.png"));
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2 - 160, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY3_BULLET_SPEED*2, 180, "EnemyBullet.png"));
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2 + 80, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY3_BULLET_SPEED*2, 180, "EnemyBullet.png"));
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2 + 160, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMY3_BULLET_SPEED*2, 180, "EnemyBullet.png"));
						}
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
				(new SoundThread(shotSoundUrl, AudioPlayer.ONCE)).start();
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
					(new SoundThread(explodeSoundUrl, AudioPlayer.ONCE)).start();
					updateScore(hitByEnemy);
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
						updateScore(hitEnemy);
					}
				}
			}
		}
		long now = System.currentTimeMillis();
		if (now - heroCreatedTime < Settings.INVULNERABLE)
			isInvulnerable = true;
		else
			isInvulnerable = false;
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
					(new SoundThread(explodeSoundUrl, AudioPlayer.ONCE)).start();
					updateScore(hitByEnemy);
				}
			}
		}


		return collision;
	}

	private void drawInstructions(Graphics g)
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image welcomeScreen = toolkit.getImage("./images/welcomeScreen.png");
		g.drawImage(welcomeScreen, 0, 0, width, height, 0, 0, width, height, null);
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
			g.drawString("You Win!", width/2 - 66, height/2);
			g.setFont(new Font("Arial", Font.PLAIN, 22));
			g.drawString("Your Final Score: " + score,width/2 - 100, height/2 + 40);
		}
	}

	private void doCollisionLogic()
	{
		if (spaceship.getIsCollide())
		{
			numOfLives--;
			if (numOfLives > 0)
			{
				spaceship = new SpaceshipSprite(width / 2 - 45, height - 100, width, height, 0, Settings.HERO_HP, Settings.HERO_SPEED, "spaceship.png");
				heroCreatedTime = System.currentTimeMillis();
				blinkTime = heroCreatedTime;
			}
			else
			{
				lose = true;
			}
		}
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

	private void updateScore(int whoHitWho)
	{
		if (whoHitWho == hitEnemy)
			score += 10;
		else {
			if (score >= 30)
				score -= 30;
			else
				score=0;
		}
	}
}
