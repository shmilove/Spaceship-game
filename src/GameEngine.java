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
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class GameEngine 
{
	private int numOfLives = Settings.NUM_OF_LIVES;
	private int width, height;
	private BufferedImage dbImg = null;
	private Image bgImage;
	private Image lifeImage;
	private RibbonsManager rManager;
	private Stages stages;
	private boolean gameStart , moveLeft = true;
	private LinkedList<EnemyshipSprite> enemyShips, deleteEnemyShips;
	private boolean win, lose;
	private boolean isInvulnerable;
	private int score;
	private SpaceshipSprite spaceship;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private final String themeMusicUrl = "./sounds/GameMusic.wav";
	private final String shotSoundUrl = "./sounds/LaserShot.wav";
	private final String explodeSoundUrl = "./sounds/Explosion.wav";
	private final String bigExplosionSoundUrl = "./sounds/bigExplosion.wav";
	private final String extraLifeSoundUrl = "./sounds/ExtraLife.wav";
	private final int hitEnemy = 1, hitByEnemy = -1;

	private LinkedList<BulletSprite> bullets, deleteBullets, enemyBullets ,deleteEnemyBullets;

	private long lastShootTime;
	private long moveTime;
	private long heroCreatedTime;
	private long blinkTime, notBlinkTime;

	public GameEngine(int pWidth, int pHeight)
	{

		(new SoundThread(themeMusicUrl, AudioPlayer.LOOP)).start();
		width = pWidth;
		height = pHeight;
		lifeImage = Toolkit.getDefaultToolkit().getImage((new File(".")).getAbsolutePath() + "//images//life.png");

		rManager = new RibbonsManager(width, height);
		stages = new Stages();
		gameStart = false;
		enemyShips = new LinkedList<EnemyshipSprite>();
		deleteEnemyShips = new LinkedList<EnemyshipSprite>();
		score = 0;
		win = lose = false;
		spaceship = new SpaceshipSprite(width / 2 - 45, height - 100, width, height, 0, Settings.HERO_HP, Settings.HERO_SPEED, "spaceship.png");
		bullets = new LinkedList<BulletSprite>();
		deleteBullets = new LinkedList<BulletSprite>();
		enemyBullets = new LinkedList<BulletSprite>();
		deleteEnemyBullets = new LinkedList<BulletSprite>();
		lastShootTime = System.currentTimeMillis();
		moveTime = System.currentTimeMillis();
		heroCreatedTime = System.currentTimeMillis();
		isInvulnerable = false;

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

		stages.displayStageText(dbg);

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
			enemyShips = stages.initializeNewStage(enemyShips);
			
			if (enemyShips.isEmpty())
				win = stages.moveStage();

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
						if (stages.isBossStage()) {
							Random rand = new Random();
							int randomNum = rand.nextInt(6) + 3;
							for (int i=0 ; i < randomNum ; i++) {
								int randomLoc = rand.nextInt(enemyshipSprite.getImageWidth());
								enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + randomLoc, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.BOSS1_BULLET_SPEED*2, 180, "EnemyBullet.png", "no"));
							}
						}
						else {
							enemyBullets.add(new BulletSprite(enemyshipSprite.getLocX() + enemyshipSprite.getImageWidth()/2, enemyshipSprite.getLocY() + enemyshipSprite.imageHeight/2, width, height, Settings.ENEMIES_BULLET_SPEED[stages.currentStage], 180, "EnemyBullet.png", "no"));
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
				switch(spaceship.getFirePowerLevel()) {
				case 1:
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					break;
				case 2:
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2+20, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2-20, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					break;
				case 3:
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2+20, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2, spaceship.locY + spaceship.imageHeight/2-10, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2-20, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					break;
				case 4:
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2+25, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "right"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2+20, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2-20, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2-25, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "left"));
					break;
				case 5:
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2+25, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "right"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "right"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "no"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "left"));
					bullets.add(new BulletSprite(spaceship.locX + spaceship.getImageWidth()/2-25, spaceship.locY + spaceship.imageHeight/2, width, height, Settings.HERO_BULLET_SPEED, 0, "bullet.png", "left"));
					break;
				}

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
						if (stages.isBossStage() && enemy.hp<=0)
							(new SoundThread(bigExplosionSoundUrl, AudioPlayer.ONCE)).start();
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
//			Image gameOverScreen = toolkit.getImage("./images/Stage2.png");
//			dbg.drawImage(gameOverScreen, 0, 0, width, height, 0, 0, width, height, null);
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
		if (whoHitWho == hitEnemy) {
			for (int i=0 ; i < stages.currentStage ; i++)
			{
				score += 10;
				checkForLifeBonus();
			}
		}
		else {
			if (score >= 30)
				score -= 30;
			else
				score=0;
		}
	}
	
	private void checkForLifeBonus()
	{
		for (int i=0 ; i<Settings.LIFE_BONUSES.length ; i++)
		{
			if (score==Settings.LIFE_BONUSES[i]) {
				numOfLives++;
				(new SoundThread(extraLifeSoundUrl, AudioPlayer.ONCE)).start();
				break;
			}
		}
	}
}
