import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.LinkedList;


public class Stages 
{
	public static int currentStage;

	private final int numOfStages = 10;
	private boolean doOnce;
	private boolean[] startStage = new boolean[numOfStages];
	private boolean[] endStage = new boolean[numOfStages];
	private boolean[] endStageText = new boolean[numOfStages];
	private boolean[] displayStageText = new boolean[numOfStages];
	private long[] startStageTime = new long[numOfStages];
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private int width = Settings.SCREEN_WIDTH;
	private int height = Settings.SCREEN_HEIGHT;
	
	private final String stageCompleteSoundUrl = "./sounds/StageCompleted.wav";
	
	
	
	
	public Stages()
	{
		currentStage = 1;
		startStage[currentStage] = true;
		doOnce = true;
	}

	public LinkedList<EnemyshipSprite> initializeNewStage(LinkedList<EnemyshipSprite> enemyShips)
	{	
		if (startStage[currentStage])
		{
			if (doOnce)
			{
				startStageTime[currentStage] = System.currentTimeMillis();
				doOnce = !doOnce;
			}
				
			long now = System.currentTimeMillis();
			if (now - startStageTime[currentStage] < Settings.DISPLAY_TEXT_TIME)
			{
				displayStageText[currentStage] = true;
			}
			else
			{
				displayStageText[currentStage] = false;
				endStageText[currentStage] = true;
				startStage[currentStage] = false;
				// Check if its a boss stage
				if (isBossStage()) {
						EnemyshipSprite enemy;
						if (currentStage==Settings.BOSS1_STAGE)
							enemy = new EnemyshipSprite(width/2 - 200, 40, width, height, "./EnemyShips/boss1.png");
						else
							enemy = new EnemyshipSprite(width/2 - 200, 40, width, height, "./EnemyShips/boss2.png");
						enemyShips.add(enemy);
						return enemyShips;
				}
				// gets here if its not a boss stage;
				for (int i =0; i < Settings.ENEMY_IN_A_ROW; i++)
				{
					for (int j =0; j < Settings.ENEMY_ROWS; j++)
					{
						EnemyshipSprite enemy;
						enemy = new EnemyshipSprite(i*Settings.ENEMIES_WIDTH[currentStage] + Settings.ENEMIES_WIDTH_SPACE[currentStage], j*Settings.ENEMIES_HEIGTH[currentStage] + Settings.ENEMIES_HEIGHT_SPACE[currentStage], width, height, "./EnemyShips/stage"+currentStage+"Enemy.png");
						enemyShips.add(enemy);
					}
				}
			}
		}
		return enemyShips;
	}
	
	
	public boolean moveStage()
	{
		if (!endStage[currentStage] && endStageText[currentStage])
		{
			endStage[currentStage] = true;
			(new SoundThread(stageCompleteSoundUrl, AudioPlayer.ONCE)).start();
			currentStage++;
			startStage[currentStage] = true;
			doOnce = true;
			// temporary in order to finish the game in stage 4
			if (currentStage==9)
				return true;
		}
		return false;
	}
	
	
	public void displayStageText(Graphics2D dbg)
	{
		if (displayStageText[currentStage])
		{
			Image stageText = toolkit.getImage("./images/Stages/stage"+ currentStage +".png");
			dbg.drawImage(stageText, 0, 0, width, height, 0, 0, width, height, null);
		}
	}
	
	public static boolean isBossStage()
	{
		if (currentStage==Settings.BOSS1_STAGE || currentStage==Settings.BOSS2_STAGE)
			return true;
		return false;
	}
	
}