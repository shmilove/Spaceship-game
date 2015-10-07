
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Chopper {
    private static final int LEFT = 1, RIGHT = 2;
    private BufferedImage imgRight, imgLeft;
    private int imgWidth, imgHeigth, pWidth, pHeigth;
    private int facing;
    private int locX, locY, moveInterval;
    
    public Chopper(int w, int h, int moveInterval)
    {
        File pathHLeft = new File(new File(".").getAbsolutePath()+ "//HelicopterLeft.gif");
        File pathHRight = new File(new File(".").getAbsolutePath()+ "//HelicopterRight.gif");
        try {
            imgRight = ImageIO.read(pathHRight);
            imgLeft = ImageIO.read(pathHLeft);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        pWidth = w;
        pHeigth = h;
        imgWidth = imgLeft.getWidth();
        imgHeigth = imgLeft.getHeight();
        facing = RIGHT;
        locX = pWidth / 2 - imgWidth / 2;
        locY = pHeigth / 2 - imgHeigth / 2;
        this.moveInterval = moveInterval;
    }
    
    public void turnLeft()
    {
        facing = LEFT;
    }
    public void turnRight()
    {
        facing = RIGHT;
    }
    
    public void moveUp()
    {
        locY -= moveInterval;
        if(locY <= 10)
            locY = 10;
    }
    
    public void moveDown()
    {
        locY += moveInterval;
        if(locY + imgHeigth >= pHeigth - 50)
            locY = pHeigth - 50 - imgHeigth;
    }
    
    public void display(Graphics g)
    {
        if(facing == LEFT)
            g.drawImage(imgLeft, locX, locY, null);
        else
            g.drawImage(imgRight, locX, locY, null);
        
    }
}
