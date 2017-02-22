package persistence_layer;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Assignment 03 - RMI And Hibernate Bouncing Sprites
 * CST 8277
 * @author Johan Setyobudi, Stanley Pieda
 * Sprite Class, contains database information, and movemovent for the sprite
 * Based on the examples given by Stanley Pieda
 * Located inside the persistance layer 
 */

@Entity
@Table(name="Sprites")
public class Sprite implements Serializable{

	/**
	 * Random variable so different speeds are possible
	 */
	final static Random random = new Random();

	/**
	 * The size of the sprite
	 */
	final static int SIZE = 10;
	
	/**
	 * The max speed of the sprite
	 */
	final static int MAX_SPEED = 5;

	/**
	 * X coordinate of initial sprite spawn
	 */
	private int x;
	
	/**
	 * Y coordinate of initial sprite spawn
	 */
	private int y;

	/**
	 * Speed in the x direction
	 */

	private int dx;
	
	/**
	 * Speed in the y direction
	 */

	private int dy;
	
	/**
	 * Color of the sprite
	 */

	private Color colour;

	/**
	 * Panel width 
	 */
	private int panelx;

	/**
	 * Panel height
	 */
	private int panely;

	/**
	 * ID of the sprite itself
	 */
	private int id;


	/**
	 * Default constructor
	 */
	public Sprite() {
	}
	
	/**
	 * Inital constructor
	 * @param panel
	 */	
	public Sprite(int x, int y, Color colour, int panelx, int panely) {
		//initializing variables
		this.x = x;
		this.y = y;
		this.colour = colour;
		dx = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
		dy = random.nextInt(2*MAX_SPEED) - MAX_SPEED;
		this.panelx = panelx;
		this.panely = panely;
	}
	
	/**
	 * Return x coordinate
	 * @return the x
	 */
	@Column
	public int getX() {
		return x;
	}

	/**
	 * Setting x coordinate
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Return y coordinate
	 * @return the y
	 */
	@Column
	public int getY() {
		return y;
	}

	/**
	 * Setting y coordinate
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returning speed in x direction
	 * @return the dx
	 */
	@Column
	public int getDx() {
		return dx;
	}

	/**
	 * Setting speed in x direction
	 * @param dx the dx to set
	 */
	public void setDx(int dx) {
		this.dx = dx;
	}

	/**
	 * Returning speed in y direction
	 * @return the dy
	 */
	@Column
	public int getDy() {
		return dy;
	}

	/**
	 * Setting speed in y direction
	 * @param dy the dy to set
	 */
	public void setDy(int dy) {
		this.dy = dy;
	}

	/**
	 * Returning colour of the sprite
	 * @return the colour
	 */
	@Column
	public Color getColour() {
		return colour;
	}

	/**
	 * Setting colour of the sprite
	 * @param colour the colour to set
	 */
	public void setColour(Color colour) {
		this.colour = colour;
	}

	/**
	 * Returning size of panel width
	 * @return the panelx
	 */
	@Column
	public int getPanelx() {
		return panelx;
	}

	/**
	 * Setting size of panel width
	 * @param panelx the panelx to set
	 */
	public void setPanelx(int panelx) {
		this.panelx = panelx;
	}

	/**
	 * Returning size of panel height
	 * @return the panely
	 */
	@Column
	public int getPanely() {
		return panely;
	}

	/**
	 * Setting size of panel height
	 * @param panely the panely to set
	 */
	public void setPanely(int panely) {
		this.panely = panely;
	}

	/**
	 * Returning ID of sprite
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	/**
	 * Setting ID of sprite
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Drawing the ball
	 * @param g
	 */
	public void draw(Graphics g){
		g.setColor(colour);
		g.fillOval(x, y, SIZE, SIZE);
	}

	/**
	 * Moving the sprite
	 */
	public void move(){
		// check for bounce and make the ball bounce if necessary
		if (x < 0 && dx < 0){
			//bounce off the left wall 
			x = 0;
			dx = -dx;
		}
		if (y < 0 && dy < 0){
			//bounce off the top wall
			y = 0;
			dy = -dy;
		}
		if (x > panelx - SIZE && dx > 0){
			//bounce off the right wall
			x = panelx - SIZE;
			dx = - dx;
		}       
		if (y > panely - SIZE && dy > 0){
			//bounce off the bottom wall
			y = panely - SIZE;
			dy = -dy;
		}
		//make the ball move
		x += dx;
		y += dy;
	}
}