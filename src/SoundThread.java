/*
 *  Authors: Elad Mizrahi & Ben Nakash
 *  ID's:	 201550142		303140057
 *  Desc:	See README.doc file.
 */

public class SoundThread extends Thread 
{
    private String fName;
    private int playMode;
    private final int inf = (int)Double.POSITIVE_INFINITY;

    public SoundThread(String fileName, int mode)
    {
        fName = fileName;
        playMode = mode;
    }
    public void run()
    {
        if(playMode == AudioPlayer.ONCE)
             AudioPlayer.play(fName);
        else if(playMode == AudioPlayer.LOOP)
            AudioPlayer.playMultiple(fName, inf);
    }
    
}
