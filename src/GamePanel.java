
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable
{
    private static final int PWIDTH = 600, PHEIGHT = 800, PERIOD = 50;
    
    private BufferedImage dbImg = null;
    private boolean running; 
    private RibbonsManager rManager;
    private boolean gameStart;
    
    public GamePanel()
    {
        setFocusable(true);
        requestFocusInWindow();

        rManager = new RibbonsManager(PWIDTH, PHEIGHT);
        gameStart = true;
        
        addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) 
            { processKey(e); }
        });
        
    }
  
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        gameRender();
        g.drawImage(dbImg, 0, 0, this);
    }
  
    public void run()
    {
        long before, sleepTime;
        before = System.currentTimeMillis();
        running = true;
        
        while(running)
        {
            gameUpdate();
            gameRender();
            paintScreen();   // active rendering
       
            sleepTime = PERIOD - before;
            if(sleepTime <= 0)
                sleepTime = 5;

            try {
                Thread.sleep(sleepTime);
            }
            catch(InterruptedException e){}

            before = System.currentTimeMillis();
        }
    }
    
    // only start the animation once the JPanel has been added to the JFrame
    public void addNotify()
    { 
        super.addNotify();   // creates the peer
        startGame();    // start the thread
    }
    
    public void startGame()
    {
        (new Thread(this)).start();
    }
    
    public void gameRender()
    {
        Graphics dbg;
        
        dbImg = new BufferedImage(PWIDTH, PHEIGHT, BufferedImage.OPAQUE);
   
        dbg = dbImg.createGraphics();
        dbg.setColor(Color.WHITE);
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
        
        rManager.display(dbg);
//        chop.display(dbg);
        
        if (gameStart)
        {
        	dbg.setColor(Color.WHITE);
        	dbg.setFont(new Font("Arial", Font.PLAIN, 28));
        	dbg.drawString("Press any key to start...", PWIDTH/2 - 140, PHEIGHT/2);
        }
       // if(gameOver)
        //    gameOverMessage(dbg);
    }
    public void paintScreen()
    {
        Graphics g;
        try 
        {
            g = getGraphics();
            if(g != null && dbImg != null)
            {
                g.drawImage(dbImg, 0, 0, null);
            }
        }
        catch(Exception e)
        {
            System.out.println("Graphics error");
            e.printStackTrace();
        }
    }
    
    public void gameUpdate()
    {
        rManager.update();
    }
    
    public void processKey(KeyEvent e)
    {
    	if (gameStart)
    	{
    		rManager.moveDown();
    		gameStart = false;
    	}
        
//        if(e.getKeyCode() == KeyEvent.VK_LEFT)
//        {
//            rManager.moveRight();
//            chop.turnLeft();
//        }
//        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
//        {
//            rManager.moveLeft();
//            chop.turnRight();
//        }
//        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
//            chop.moveDown();
//        else if(e.getKeyCode() == KeyEvent.VK_UP)
//            chop.moveUp();
            
    }
}

