
public class Settings 
{
	// Screen
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 700;
	
	// Game Engine
	public static final int NUM_OF_LIVES = 3;
	public static final int SHOT_THRESHOLD = 150;
	public static final int BLINK_TIME = 300;
	public static final int DISPLAY_TEXT_TIME = 3000;
	
	// Enemies
	public static final int MOVE_THRESHOLD = 150;
	
	// Enemy Ships
	private static final int ENEMY1_HP = 1;
	private static final int ENEMY1_SPEED = 3;
	private static final int ENEMY1_BULLET_SPEED = 2;
	private static final int ENEMY1_FIRERATE = 3;
	private static final int ENEMY1_WIDTH = 80;
	private static final int ENEMY1_HEIGHT = 50;		
	private static final int ENEMY1_WIDTH_SPACE = 30;
	private static final int ENEMY1_HEIGHT_SPACE = 50;
	
	private static final int ENEMY2_HP = 2;
	private static final int ENEMY2_SPEED = 4;
	private static final int ENEMY2_BULLET_SPEED = 3;
	private static final int ENEMY2_FIRERATE = 4;
	private static final int ENEMY2_WIDTH = 80;
	private static final int ENEMY2_HEIGHT = 50;			
	private static final int ENEMY2_WIDTH_SPACE = 30;
	private static final int ENEMY2_HEIGHT_SPACE = 50;

	private static final int ENEMY3_HP = 3;
	private static final int ENEMY3_SPEED = 4;
	private static final int ENEMY3_BULLET_SPEED = 4;
	private static final int ENEMY3_FIRERATE = 5;
	private static final int ENEMY3_WIDTH = 80;
	private static final int ENEMY3_HEIGHT = 50;			
	private static final int ENEMY3_WIDTH_SPACE = 30;
	private static final int ENEMY3_HEIGHT_SPACE = 50;
	
	private static final int ENEMY4_HP = 4;
	private static final int ENEMY4_SPEED = 5;
	private static final int ENEMY4_BULLET_SPEED = 3;
	private static final int ENEMY4_FIRERATE = 5;
	private static final int ENEMY4_WIDTH = 80;
	private static final int ENEMY4_HEIGHT = 70;			
	private static final int ENEMY4_WIDTH_SPACE = 30;
	private static final int ENEMY4_HEIGHT_SPACE = 50;
	
	private static final int ENEMY5_HP = 5;
	private static final int ENEMY5_SPEED = 6;
	private static final int ENEMY5_BULLET_SPEED = 4;
	private static final int ENEMY5_FIRERATE = 6;
	private static final int ENEMY5_WIDTH = 80;
	private static final int ENEMY5_HEIGHT = 70;			
	private static final int ENEMY5_WIDTH_SPACE = 30;
	private static final int ENEMY5_HEIGHT_SPACE = 50;
	
	private static final int ENEMY6_HP = 6;
	private static final int ENEMY6_SPEED = 7;
	private static final int ENEMY6_BULLET_SPEED = 5;
	private static final int ENEMY6_FIRERATE = 7;
	private static final int ENEMY6_WIDTH = 80;
	private static final int ENEMY6_HEIGHT = 70;			
	private static final int ENEMY6_WIDTH_SPACE = 30;
	private static final int ENEMY6_HEIGHT_SPACE = 50;
	
	public static final int BOSS1_STAGE = 4;
	public static final int BOSS1_HP = 100;
	public static final int BOSS1_SPEED = 5;
	public static final int BOSS1_BULLET_SPEED = 2;
	public static final int BOSS1_FIRERATE = 13; 

	public static final int BOSS2_STAGE = 8;
	public static final int BOSS2_HP = 200;
	public static final int BOSS2_SPEED = 7;
	public static final int BOSS2_BULLET_SPEED = 4;
	public static final int BOSS2_FIRERATE = 17; 
	
	
	private static final int IRRELEVANT= 0;
	// the first spot is always 'IRRELEVANT' because the variable 'currentStage' starts from 1, same for bosses stages;
	public static final int[] ENEMIES_HP = {IRRELEVANT, ENEMY1_HP, ENEMY2_HP, ENEMY3_HP, IRRELEVANT, ENEMY4_HP, ENEMY5_HP, ENEMY6_HP};
	public static final int[] ENEMIES_SPEED = {IRRELEVANT, ENEMY1_SPEED, ENEMY2_SPEED, ENEMY3_SPEED, IRRELEVANT, ENEMY4_SPEED, ENEMY5_SPEED, ENEMY6_SPEED};
	public static final int[] ENEMIES_BULLET_SPEED = {IRRELEVANT, ENEMY1_BULLET_SPEED, ENEMY2_BULLET_SPEED, ENEMY3_BULLET_SPEED, IRRELEVANT, ENEMY4_BULLET_SPEED, ENEMY5_BULLET_SPEED, ENEMY6_BULLET_SPEED};
	public static final int[] ENEMIES_FIRE_RATE = {IRRELEVANT, ENEMY1_FIRERATE, ENEMY2_FIRERATE, ENEMY3_FIRERATE, IRRELEVANT, ENEMY4_FIRERATE, ENEMY5_FIRERATE, ENEMY6_FIRERATE};
	public static final int[] ENEMIES_WIDTH  = {IRRELEVANT, ENEMY1_WIDTH, ENEMY2_WIDTH, ENEMY3_WIDTH, IRRELEVANT, ENEMY4_WIDTH, ENEMY5_WIDTH, ENEMY6_WIDTH};
	public static final int[] ENEMIES_HEIGTH = {IRRELEVANT, ENEMY1_HEIGHT, ENEMY2_HEIGHT, ENEMY3_HEIGHT, IRRELEVANT, ENEMY4_HEIGHT, ENEMY5_HEIGHT, ENEMY6_HEIGHT};
	public static final int[] ENEMIES_WIDTH_SPACE  = {IRRELEVANT, ENEMY1_WIDTH_SPACE, ENEMY2_WIDTH_SPACE, ENEMY3_WIDTH_SPACE, IRRELEVANT, ENEMY4_WIDTH_SPACE, ENEMY5_WIDTH_SPACE, ENEMY6_WIDTH_SPACE};
	public static final int[] ENEMIES_HEIGHT_SPACE = {IRRELEVANT, ENEMY1_HEIGHT_SPACE, ENEMY2_HEIGHT_SPACE, ENEMY3_HEIGHT_SPACE, IRRELEVANT, ENEMY4_HEIGHT_SPACE, ENEMY5_HEIGHT_SPACE, ENEMY6_HEIGHT_SPACE};
	

	
	private static final int FIRST_LIFE_BONUS = 3000;
	private static final int SECOND_LIFE_BONUS = 15000;
	private static final int THIRD_LIFE_BONUS = 35000;
	public static final int[] LIFE_BONUSES = {FIRST_LIFE_BONUS, SECOND_LIFE_BONUS, THIRD_LIFE_BONUS};
	
	public static final int ENEMY_ROWS = 4;
	public static final int ENEMY_IN_A_ROW = 7;
	
	// Spaceship
	public static final int ROTATION_SPEED = 5;
	public static final double SPEED_INCREASE_RATE = 0.1;
	public static final int MAX_SPEED = 7;
	
	// Spaceship = Hero
	public static final int HERO_SPEED = 3;
	public static final int HERO_HP = 1;
	public static final int INVULNERABLE = 1500;

	// Bullets
	public static final int HERO_BULLET_SPEED = 8;
	public static final int MAX_DISTANCE = 650;
	
	// Asteroids
	public static final int ASTEROID_SPEED = 2;
	public static final int LARGE_ASTEROID_SCORE = 20;
	public static final int MEDIUM_ASTEROID_SCORE = 50;
	public static final int SMALL_ASTEROID_SCORE = 100;
}
