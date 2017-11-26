/*
 * Main logic for flappy bird game
 */
package flappybird;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Matt Halloran
 */
public class FlappyMain implements ActionListener, KeyListener {
    
    public static final int FPS = 60, HEIGHT = 1000, WIDTH = 500,  
    						GAP = 200, MAX_TRAVEL = 400, SPEED = 3,//width was 640, height was 480
    						PIPE_DISTANCE = 90;
    
    private Bird bird;

    //GUI items
    private JFrame frame;
    private JPanel panel;
    private ArrayList<Rectangle> rects;
    private static Integer[] grounds;
    
    private int scroll, currentScoreWait, lastTopHeight, currentScore, highScore;
    
    private BufferedReader br;
    private BufferedWriter bw;
    
    private Timer t;
    
    private boolean paused, game;
    
    public static void main(String[] args) {
        new FlappyMain().begin();
    }
    
    public void begin() {
    	grounds = new Integer[2];
    	grounds[0] = 0;
    	grounds[1] = WIDTH;
    	
        frame = new JFrame("Flappy Bird");
        bird = new Bird();
        rects = new ArrayList<Rectangle>();
        panel = new GamePanel(this, bird, rects);//grounds
        frame.add(panel);
        
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);
        
        paused = true;
        
        try {
        	br = new BufferedReader(new FileReader(new File("HighScore.txt")));
        	String s = br.readLine();
        	System.out.println(s);
        	highScore = Integer.parseInt(s);
			bw = new BufferedWriter(new FileWriter(new File("HighScore.txt")));
			bw.write("" + highScore);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			highScore = 0;
		}
        
        currentScore = 0;
        currentScoreWait = 0;
        lastTopHeight = 0;
        t = new Timer(1000/FPS, this);
        t.start();
    }
    
    @SuppressWarnings("static-access")
	@Override
    public void actionPerformed(ActionEvent e) 
    {
    	currentScoreWait--;
        panel.repaint();
        
        if(!paused) {
            bird.physics();
            
            
            //moving grounds
            for(int i = 0; i < 2; i++){
            	grounds[i]-=SPEED;
            	if(grounds[i] <= -WIDTH)
            		grounds[i] = WIDTH;
            }
            
            //pipe creation
            if(scroll % PIPE_DISTANCE == 0) {
            	boolean done = false;
            	int topHeight = 0;
            	while(!done){
            		topHeight = (int)(Math.random()*HEIGHT);
            		if(topHeight > GAP && topHeight < (HEIGHT - GamePanel.GROUND_HEIGHT - GAP) && Math.abs(topHeight - lastTopHeight) < MAX_TRAVEL)
            			done = true;
            	}
            	lastTopHeight = topHeight;
            	Rectangle topRectangle = new Rectangle(WIDTH, 0, GamePanel.PIPE_WIDTH, topHeight);
            	Rectangle bottomRectangle = new Rectangle(WIDTH, topHeight + GAP, GamePanel.PIPE_WIDTH, HEIGHT - (topHeight + GAP));
                rects.add(topRectangle);
                rects.add(bottomRectangle);
            }
            
            ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
            game = true;
            
            //moving/removing pipes and checking if bird touched pipe
            for(Rectangle r : rects) {
	            r.x-=SPEED;
	            if(r.x + r.width <= 0) {
	                toRemove.add(r);
	            }
	            //checks 5 bird points for pipe contact
	            if(r.contains(bird.xPosition - Bird.RADIUS/1.9, bird.yPosition - Bird.RADIUS/1.9) || r.contains(bird.xPosition + Bird.RADIUS/1.9, bird.yPosition - Bird.RADIUS/1.9)
	                || r.contains(bird.xPosition - Bird.RADIUS/1.9, bird.yPosition + Bird.RADIUS/1.9) || r.contains(bird.xPosition + Bird.RADIUS/1.9, bird.yPosition + Bird.RADIUS/1.9)
	                || r.contains(bird.xPosition, bird.yPosition - Bird.RADIUS)) {
	                youLose();
	            }
	            else if(r.getX() < bird.xPosition && r.getX() + GamePanel.PIPE_WIDTH > bird.xPosition){
	            	if(currentScoreWait < 0)
	                	currentScore++;
	                currentScoreWait = GamePanel.PIPE_WIDTH;
	            }
            }
            rects.removeAll(toRemove);
            scroll++;

            //out of bounds check
            if(bird.yPosition > HEIGHT-GamePanel.GROUND_HEIGHT || bird.yPosition+bird.RADIUS < 0) {
                youLose();
            }

            if(!game) {
                rects.clear();
                bird.restart();
                currentScore = 0;
                scroll = 0;
                paused = true;
            }
        }
        else {
            
        }
    }
    
    /**
     * Resets game values and displays score
     * Called at the end of each game
     */
    public void youLose()
    {
    	JOptionPane.showMessageDialog(frame, "You lose!\n" + "Your score was: "+ currentScore +".");
    	highScore = (highScore < currentScore) ? currentScore : highScore;
    	try {
    		bw = new BufferedWriter(new FileWriter(new File("HighScore.txt")));
			bw.write("" + highScore);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        game = false;
    }
    
    /**
     * @return Score of current game
     */
    public int getcurrentScore()
    {
    	return currentScore;
    }
    
    /**
     * @return High score
     */
    public int getHighScore()
    {
    	return highScore;
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP) {
        	if(paused)
        		paused = false;
        	else
        		bird.jump();
        }
        else if(e.getKeyCode()==KeyEvent.VK_SPACE) {
        	if(paused)
        		paused = false;
        	else
        		bird.jump();
        }
        else if(e.getKeyCode()==KeyEvent.VK_S){
        	bird.secretMode();
        }
    }
    
    /**
     * Not used but has to be here
     */
    public void keyReleased(KeyEvent e) {
        
    }
    
    /**
     * Not used but has to be here
     */
    public void keyTyped(KeyEvent e) {
        
    }
    
    /**
     * @return true if game is paused
     */
    public boolean paused() {
        return paused;
    }
    
    /** 
     * @param i Index of ground png
     * @return X position of ground png
     */
    public static int getGroundLocation(int i)
    {
    	return grounds[i];
    }
}