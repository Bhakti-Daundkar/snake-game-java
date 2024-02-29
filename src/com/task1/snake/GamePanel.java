package com.task1.snake;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;  //size of items
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_WIDTH));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw (Graphics g) {
		if(running) {
		//grid
//		for(int i =0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
//			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//			g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH,i*UNIT_SIZE);
//		}
		
		//draw apples
		g.setColor(Color.red);
		g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
		//snake
		for(int i =0 ;i<bodyParts;i++) {
			if(i == 0) {
				g.setColor(Color.green);
				g.fillRect(x[i] ,y[i],UNIT_SIZE, UNIT_SIZE);
			}
			else {
				g.setColor(new Color(45,180,0));
				g.fillRect(x[i] ,y[i],UNIT_SIZE, UNIT_SIZE);
			}
		}
		g.setColor(Color.cyan);
		g.setFont(new Font("Ink Free",Font.BOLD,30));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH- metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
	}
	
	//generate apples
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
			
		}
		switch(direction) {
		case'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		
	    case'D':
		    y[0] = y[0] + UNIT_SIZE;
		    break;
		
	    case'L':
		    x[0] = x[0] - UNIT_SIZE;
		    break;
		
	    case'R':
		   x[0] = x[0] + UNIT_SIZE;
		   break;
		}
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;//increases snake size
			applesEaten++;//counts appleEaten
			newApple();//adds when eaten
		}
	}
	//game over
	public void checkCollisions() {
		//head collision with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//head collides with border
		
		//left border
		if(x[0] < 0) {
			running = false;
		}
		//right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//top border
		if(y[0] < 0) {
			running = false;
		}
		//bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//display after game over
		g.setColor(Color.cyan);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH- metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
		
		//display game over
		g.setColor(Color.cyan);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH- metrics1.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
		
		g.setFont(new Font("Ink Free", Font.BOLD, 30));
	    g.drawString("Press Enter key to restart", ((SCREEN_WIDTH - metrics.stringWidth("Press any key to restart"))/ 2)+10, SCREEN_HEIGHT / 2 + 75);

	    running = false;
	    timer.stop();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public void restartGame() {
	    bodyParts = 6;
	    applesEaten = 0;
	    running = true;
	    direction = 'R';
	    newApple();

	    // Reset the snake's position to a default starting position
	    // You might need to adjust these values based on your game logic
	    for (int i = 0; i < bodyParts; i++) {
	        x[i] = 0;  // Set the x-coordinate to the default starting position
	        y[i] = 0;  // Set the y-coordinate to the default starting position
	    }

	    timer.start();  // Restart the timer
	    repaint();      // Repaint the panel to reflect the changes
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			 if (!running) {
		            // Restart the game
		            restartGame();
		        } else {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
				
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
				
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				
			}
		}
	}
	}

}
