/*
 * Bird that flies through pipes
 */
package flappybird;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Matt Halloran
 */
public class Bird {
	
    public float xPosition, yPosition, xVelocity, yVelocity;
    public static final int RADIUS = 50;
    private Image img;
    public boolean secret = false;
    
    /**
     * Constructor for Bird
     */
    public Bird()
    {
        xPosition = (int)(FlappyMain.WIDTH/2.5);
        yPosition = (int)FlappyMain.HEIGHT/2;
        try {
        	if(!secret)
        		img = ImageIO.read(new File("FlappyBirdUp.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Moves bird
     */
    public void physics()
    {
        xPosition+=xVelocity;
        yPosition+=yVelocity;
        yVelocity+=0.5f;
        if(yVelocity > 0){
        	try {
        		if(!secret)
        			img = ImageIO.read(new File("FlappyBirdDown.png"));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Changes velocity and bird image
     */
    public void jump()
    {
    	try {
    		if(!secret)
    			img = ImageIO.read(new File("FlappyBirdUp.png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        yVelocity = -8;
    }
    
    /**
     * Redraws bird
     */
    public void update(Graphics g) 
    {
        g.setColor(Color.BLACK);
        g.drawImage(img, Math.round(xPosition-RADIUS),Math.round(yPosition-RADIUS),2*RADIUS,2*RADIUS, null);
    }
    
    /**
     * Resets positions and velocities
     * Turns off secret mode
     */
    public void restart() 
    {
        xPosition = (int)(FlappyMain.WIDTH/2.5);
        yPosition = FlappyMain.HEIGHT/2;
        xVelocity = yVelocity = 0;
        secret = false;
    }
    
    /**
     * Enables a secret mode ;)
     */
    public void secretMode()
    {
    	secret = true;
    	try {
			img = ImageIO.read(new File("DatBoi.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
