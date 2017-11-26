/*
 * Flappy Bird GUI
 */
package flappybird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Matt Halloran
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
    
    private Bird bird;
    private ArrayList<Rectangle> rects;
    private FlappyMain flappyMain;
	private Font pausedFont, currentScoreFont, highScoreFont;
    public static final Color BACKGROUND_COLOR = new Color(66, 221, 213);
    public static final int PIPE_WIDTH = 50, PIPE_HEIGHT = 30, GROUND_HEIGHT = (int)(FlappyMain.HEIGHT/10), SKYLINE_HEIGHT = (int)(FlappyMain.HEIGHT);
    private Image pipeHead, pipeLength, ground, skyline;

    /**
     * Constructor for GamePanel
     * @param flappyBird
     * @param bird Bird that moves through pipes
     * @param rects List of pipes
     */
    public GamePanel(FlappyMain flappyMain, Bird bird, ArrayList<Rectangle> rects) {
        this.flappyMain = flappyMain;
        this.bird = bird;
        this.rects = rects;
        try {
			Fonts.addFont(new Fonts("Dimis.TTF"));
			currentScoreFont = new Font("Dimitri Swank", Font.PLAIN,(int)FlappyMain.WIDTH/5);
		} catch (Exception e) {
			e.printStackTrace();
		}
        pausedFont = new Font("Arial", Font.BOLD, (int)FlappyMain.WIDTH/13);
        highScoreFont = new Font("Arial", Font.BOLD, (int)FlappyMain.WIDTH/25);
        
        try {
        	pipeHead = ImageIO.read(new File("Pipe.png"));
        	pipeLength = ImageIO.read(new File("PipePart.png"));
        	ground = ImageIO.read(new File("Ground.png")).getScaledInstance(FlappyMain.WIDTH, GROUND_HEIGHT, Image.SCALE_DEFAULT);
        	skyline = ImageIO.read(new File("Skyline.png")).getScaledInstance(FlappyMain.WIDTH, SKYLINE_HEIGHT, Image.SCALE_DEFAULT);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	
    	Graphics2D g2d = (Graphics2D) g;
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0,0,FlappyMain.WIDTH,FlappyMain.HEIGHT);
        g2d.drawImage(skyline, 0, FlappyMain.HEIGHT - SKYLINE_HEIGHT, FlappyMain.WIDTH, SKYLINE_HEIGHT, null);
        bird.update(g);
        
        //drawing pipes
        boolean topPipe = true;
        for(Rectangle r : rects) {
            g2d.setColor(Color.GREEN);
            AffineTransform old = g2d.getTransform();
            g2d.translate(r.x+PIPE_WIDTH/2, r.y+PIPE_HEIGHT/2);
            if(topPipe){
            	g2d.translate(0, r.height);
            	g2d.rotate(Math.PI);
            }
            topPipe ^= true;
            g2d.drawImage(pipeHead, -PIPE_WIDTH/2, -PIPE_HEIGHT/2, GamePanel.PIPE_WIDTH, GamePanel.PIPE_HEIGHT, null);
            g2d.drawImage(pipeLength, -PIPE_WIDTH/2, PIPE_HEIGHT/2, GamePanel.PIPE_WIDTH, r.height, null);
            g2d.setTransform(old);
        }
        
        //drawing moving ground
        for(int i = 0; i < 2; i++){
        	g2d.drawImage(ground, FlappyMain.getGroundLocation(i), FlappyMain.HEIGHT - GROUND_HEIGHT, FlappyMain.WIDTH, GROUND_HEIGHT, null);
        }
        
        //highscore
        g.setFont(highScoreFont);
        g.setColor(Color.BLACK);
        g.drawString("High score: " + flappyMain.getHighScore(), 0, 20);
        
        //score text and pause screen text
        
        if(flappyMain.paused()) {
            g.setFont(pausedFont);
            g.setColor(new Color(0,0,0,170));
            g.drawString("PAUSED", (int)(FlappyMain.WIDTH/2-FlappyMain.WIDTH/6.4), (int)(FlappyMain.HEIGHT/2-FlappyMain.HEIGHT/4.8));
            g.drawString("PRESS SPACE TO START", (int)(FlappyMain.WIDTH/2-FlappyMain.WIDTH/2.13), (int)(FlappyMain.HEIGHT/2+FlappyMain.HEIGHT/9.6));
        }
        else{
        	try{
            	g.setFont(currentScoreFont);
            }catch (Exception e) {
    			e.printStackTrace();
    		}
        	g.setColor(Color.BLACK);
            g.drawString("" + flappyMain.getcurrentScore(), (int)FlappyMain.WIDTH/2, (int)FlappyMain.HEIGHT/4);
            
        }
    }
}
