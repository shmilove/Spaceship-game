import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;


public class RibbonsManager 
{
	private Ribbon spaceRibbon;
	private int pWidth, pHeight;

	public RibbonsManager(int w, int h)
	{
		pWidth = w;
		pHeight = h;

		File pathSky = new File(new File(".").getAbsolutePath()+ "//Sky.png");

		BufferedImage skyImg = null;
		try 
		{
			skyImg = ImageIO.read(pathSky);
		} 
		catch (IOException ex) 
		{
			Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
		}
		spaceRibbon = new Ribbon(pWidth, pHeight, skyImg, 2);

		spaceRibbon.stayStill();
	}

	public void display(Graphics g)
	{
		spaceRibbon.display(g);
	}

	public void update()
	{
		spaceRibbon.update();
	}

	public void moveDown()
	{
		spaceRibbon.moveDown();
	}
}
