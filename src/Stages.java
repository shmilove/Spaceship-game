import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.LinkedList;


public class Stages 
{
	private final int numOfStages = 10;
	private final int bossStage1 = 3, bossStage2 = 6;
	private final int[] bossStage = {bossStage1, bossStage2};

	private boolean doOnce;
	private boolean[] startStage = new boolean[numOfStages];
	private boolean[] endStage = new boolean[numOfStages];
	private boolean[] endStageText = new boolean[numOfStages];
	private boolean[] displayStageText = new boolean[numOfStages];
	private long[] startStageTime = new long[numOfStages];
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private int width, height;
	
	private final String stageCompleteSoundUrl = "./sounds/StageCompleted.wav";
	
	public static int currentStage = 1;

	
	public Stages(int pWidth, int pHeight)
	{
		width = pWidth;
		height = pHeight;
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
				for (int i=0 ; i < bossStage.length ; i++)
				{
					if (currentStage==bossStage[i])
					{
						EnemyshipSprite enemy = new EnemyshipSprite(width/2 - 200, 40, width, height, "boss.png");
						enemyShips.add(enemy);
						return enemyShips;
					}
				}
				// gets here if its not a boss stage;
				for (int i =0; i < Settings.ENEMY_IN_A_ROW; i++)
				{
					for (int j =0; j < Settings.ENEMY_ROWS; j++)
					{
						EnemyshipSprite enemy;
						switch(currentStage) {
						case 1:	enemy = new EnemyshipSprite(i*Settings.ENEMY1_WIDTH + Settings.ENEMY1_WIDTH_SPACE, j*Settings.ENEMY1_HEIGHT + Settings.ENEMY1_HEIGHT_SPACE, width, height, "enemySpaceship.png");
								enemyShips.add(enemy);
								break;
						case 2: enemy = new EnemyshipSprite(i*Settings.ENEMY2_WIDTH + Settings.ENEMY2_WIDTH_SPACE, j*Settings.ENEMY2_HEIGHT + Settings.ENEMY2_HEIGHT_SPACE, width, height, "enemySpaceship2.png");
								enemyShips.add(enemy);
								break;

						}
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
			// temporary in order to finish the game in stage 3
			if (currentStage==4)
				return true;
		}
		return false;
	}
	
	
	public void displayStageText(Graphics2D dbg)
	{
		if (displayStageText[currentStage])
		{
			Image stageText = toolkit.getImage("./images/stage"+ currentStage +".png");
			dbg.drawImage(stageText, 0, 0, width, height, 0, 0, width, height, null);
		}
	}
	
	public boolean isBossStage()
	{
		for(int i=0 ; i < bossStage.length ; i++)
		{
			if (currentStage == bossStage[i])
				return true;
		}
		return false;
	}
}