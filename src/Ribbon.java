/*
 *  Authors: Elad Mizrahi & Ben Nakash
 *  ID's:	 201550142		303140057
 *  Desc:	See README.doc file.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Ribbon 
{
    private BufferedImage img;
    private int pWidth, pHeight, yImgHead, imgHeight;
    private int moveInterval;
    private boolean isMovingDown;
    
    public Ribbon(int w, int h, BufferedImage img, int moveInterval)
    {
        this.img = img;
        pWidth = w;
        pHeight = h;
        this.moveInterval = moveInterval;
        imgHeight = img.getHeight();
        isMovingDown = false;
        yImgHead = 0;
    }
    
    public void moveDown()
    {
    	isMovingDown = true;
    }
    
    public void stayStill()
    {
        isMovingDown = false;
    }
    
    public void update()
    {
        if(isMovingDown)
        {
        	yImgHead = (yImgHead + moveInterval) % imgHeight;
        }	
    }
    
    public void display(Graphics g)
    {
    	 if(yImgHead == 0)
             draw(g, pHeight, 0, pHeight, 0);
         else if(yImgHead > 0 && yImgHead < pHeight)
         {
             draw(g, yImgHead, 0, imgHeight, imgHeight-yImgHead);
             draw(g, pHeight, yImgHead, pHeight-yImgHead, 0);
         }
         else if(yImgHead >= pHeight)
             draw(g, pHeight, 0, imgHeight - yImgHead + pHeight, imgHeight - yImgHead);
         else if(yImgHead < 0 && yImgHead >= pHeight - imgHeight)
             draw(g, pHeight, 0, pHeight - yImgHead, -yImgHead);
         else if(yImgHead < pHeight - imgHeight)
         {
             draw(g, imgHeight + yImgHead, 0, imgHeight, -yImgHead);
             draw(g, pHeight, imgHeight + yImgHead, pHeight - imgHeight - yImgHead, 0);
         }    
    }
    
    private void draw(Graphics g, int sX1, int sX2, int imgX1, int imgX2)
    {
        g.drawImage(img, 0, sX1, pWidth, sX2, 0, imgX1, pWidth, imgX2, null);
    }
}
