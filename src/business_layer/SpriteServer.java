package business_layer;

import java.awt.Color;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;

import persistence_layer.Sprite;

/**
 * Assignment 03 - RMI And Hibernate Bouncing Sprites
 * CST 8277
 * @author Johan Setyobudi, Stanley Pieda
 * The SpriteServer class that contains all the information for starting the server
 * Based on the examples given by Stanley Pieda
 * Located inside the business layer 
 */
public class SpriteServer extends RemoteServer implements SpriteServerInterface, Runnable {

	/**
	 * ArrayList of Sprite objects
	 */
	private ArrayList<Sprite> sprites;

	/**
	 * A SessionFactory object
	 */
	SessionFactory factory;

	/**
	 * Setting a static panel size
	 */
	private static final int PANELSIZE = 400;

	/**
	 * Sprite object
	 */
	private Sprite sprite;

	/**
	 * Initial Constructor
	 */
	public SpriteServer() {
		//initializing ArrayList
		sprites = new ArrayList<Sprite>();
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			MetadataImplementor meta = (MetadataImplementor) new MetadataSources(registry)
					.addAnnotatedClass(Sprite.class).buildMetadata();
			// new SchemaExport(meta).create(true, true);
			factory = meta.buildSessionFactory();
		} catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	/**
	 * The main method that will start and initialize the SpriteServer
	 * @param args
	 */
	public static void main(String[] args) {
		//setting port number
		int portNum = 8082;
		if(args.length > 0){
			portNum = Integer.parseInt(args[0]);
		}
		// set the hostname property to the public IP of the server if the server has several IP addresses
		//System.setProperty("java.rmi.server.hostname", "10.70.176.85");
		try {
			SpriteServer ss = new SpriteServer();
			LocateRegistry.createRegistry(portNum);
			System.out.println("Registry created");
			UnicastRemoteObject.exportObject(ss,0);
			System.out.println("Exported");
			Naming.rebind("//localhost:" + portNum + "/Assignment3_RMI_Hibernate", ss);
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
			e.printStackTrace();
		}	
	}

	/* (non-Javadoc)
	 * @see business_layer.SpriteServerInterface#getSprites()
	 */
	@Override
	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	/* (non-Javadoc)
	 * @see business_layer.SpriteServerInterface#getPanelsize()
	 */
	@Override
	public int getPanelsize() {
		return PANELSIZE;
	}

	/* (non-Javadoc)
	 * @see business_layer.SpriteServerInterface#createSprite(int, int, java.awt.Color, int, int, int)
	 */
	@Override
	public void createSprite (int x, int y, Color colour, int panelx, int panely) {
		Session s = factory.getCurrentSession();
		//creating new sprite object
		sprite = new Sprite(x, y, colour, panelx, panely);
		try {
			//beginning mysql transaction
			s.beginTransaction();
			s.save(sprite);
			//getting list of sprites from the database
			sprites = (ArrayList<Sprite>) s.createCriteria(Sprite.class)
					.list();
			s.getTransaction().commit();
		} catch(Exception e) {
			e.printStackTrace();
			s.getTransaction().rollback();
		}
		//generating a new thread
		new Thread(this).start();	
	}

	/* (non-Javadoc)
	 * @see business_layer.SpriteServerInterface#getColour()
	 */
	@Override
	public Color getColour() {
		Random random = new Random();
		int red = 0;
		int green = 0;
		int blue = 0;
		Color colour;
		boolean duplicate;
		//looping in case it generates white, since background is white the ball will be invisible
		do {
			duplicate = false;
			red = random.nextInt(256);
			green = random.nextInt(256);
			blue = random.nextInt(256);
			colour = new Color(red,green,blue);
			for(int i = 0; i < sprites.size(); i++) {
				if(sprites.get(i).getColour().getRGB() == colour.getRGB()) {
					duplicate = true;
					break; //checking for duplicate colours
				}
			}
		} while (red == 255 && green == 255 && blue == 255 && !duplicate);
		//setting the color from the randomly generated numbers
		return colour;
	}

	/* (non-Javadoc)
	 * @see business_layer.SpriteServerInterface#move()
	 */
	@Override
	public void move() {
		// getting the current session
		Session s = factory.getCurrentSession();
		try {
			s.beginTransaction();
			for(Sprite sprite : sprites){
				//moving the sprites
				sprite.move();
				s.update(sprite);
			}
			s.getTransaction().commit();
		} catch(Exception e) {
			e.printStackTrace();
			s.getTransaction().rollback();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true) {
			move();
			try {
				Thread.sleep(40);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
}