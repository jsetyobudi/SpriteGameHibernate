package business_layer;

import java.awt.Color;
import java.rmi.Remote;
import java.util.ArrayList;

import persistence_layer.Sprite;

/**
 * Assignment 03 - RMI And Hibernate Bouncing Sprites
 * CST 8277
 * @author Johan Setyobudi, Stanley Pieda
 * SpriteServerInterface , the interface for the RMI
 * Based on the examples given by Stanley Pieda
 * Located inside the business layer 
 */
public interface SpriteServerInterface extends Remote {

	/**
	 * Returning ArrayList of sprites
	 * @return ArrayList of sprites
	 * @throws java.rmi.RemoteException
	 */
	public ArrayList<Sprite> getSprites() throws java.rmi.RemoteException;
	/**
	 * Returning the panel size
	 * @return Panel Size
	 * @throws java.rmi.RemoteException
	 */
	public int getPanelsize() throws java.rmi.RemoteException;
	
	/**
	 * Returning the colour of the sprite
	 * @return Colour of sprite
	 * @throws java.rmi.RemoteException
	 */
	public Color getColour() throws java.rmi.RemoteException;
	
	/**
	 * Moving the sprite
	 * @throws java.rmi.RemoteException
	 */
	public void move() throws java.rmi.RemoteException;

	/**
	 * Creating a sprite
	 * @param x
	 * @param y
	 * @param colour
	 * @param panelx
	 * @param panely
	 * @throws java.rmi.RemoteException
	 */
	public void createSprite(int x, int y, Color colour, int panelx, int panely) throws java.rmi.RemoteException;
}