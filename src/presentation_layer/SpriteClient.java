package presentation_layer;
/**
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import business_layer.SpriteServerInterface;

/**
 * Assignment 03 - RMI And Hibernate Bouncing Sprites
 * CST 8277
 * @author Johan Setyobudi, Stanley Pieda
 * SpriteClient class which is the client. Has all the information needed to connect to the SpriteServer
 * Based on the examples given by Stanley Pieda
 * Located inside the presentation layer 
 */
public class SpriteClient extends JPanel {

	/**
	 * JFrame Object
	 */
	private JFrame frame;

	/**
	 * Width of the panel - Given from server
	 */
	private int width;
	
	/**
	 * Height of the panel - Given from server
	 */
	private int height;
	
	/**
	 * Server Object
	 */
	private SpriteServerInterface server;

	/**
	 * Initial Constructor of the SpriteClient
	 * @param server
	 * @throws RemoteException
	 */
	public SpriteClient(SpriteServerInterface server) throws RemoteException {
		Color colour;
		//getting width and height of the panel from server
		width = server.getPanelsize();
		height = server.getPanelsize();
		//setting the class wide server object, so other methods can use same instance
		this.server = server;
		colour = server.getColour();
		//initializing gui parameters
		frame = new JFrame("Bouncing Sprite");
		//compensate for ball, or else the window isn't big enough, and the ball will 'fall'
		//through the window for a little bit before bouncing back up
        frame.setSize(width, height + 30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);
        frame.setResizable(false);
        //setting colour white so almost any colour can be used
		this.setBackground(Color.WHITE);
		this.setSize(width, height);
		//sprite creation at mouse click
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent event){
				try {
					//generating sprite at exact mouse location where panel was pressed
					server.createSprite(event.getX(), event.getY(),colour, width, height);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Main method of the SpriteClient class, will initialize the SpriteClient when run
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException, NotBoundException {
		String serverName = "localhost";
		int port = 8082;
		String myHostName = "localhost";
		switch (args.length) {
		case 0:
			break;
		case 1: 
			serverName = args[0];
			break;
		case 2:
			serverName = args[0];
			port = Integer.parseInt(args[1]);
			break;
		default:
			System.out.println("usage: SpriteClient [hostname [portnum]]");
			break;
		}
		try {
			InetAddress myHost = Inet4Address.getLocalHost();
			myHostName = myHost.getHostName();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			//attempting to connect to the server
			System.out.println("Attempting to connect to rmi://"+serverName+":"+port+"/Assignment3_RMI_Hibernate");
			SpriteServerInterface ss = (SpriteServerInterface) 
					Naming.lookup("rmi://"+serverName+":"+port+"/Assignment3_RMI_Hibernate");
			SpriteClient client = new SpriteClient(ss);
			client.animate();
		} catch (MalformedURLException murle) {
			System.out.println();
			System.out.println(
					"MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		}
	}

	/**
	 * Repainting the balls
	 */
	public void animate(){
		while (true){
			repaint();
			try {
				Thread.sleep(40);  // wake up roughly 25 frames per second
			}
			catch ( InterruptedException exception ) {
				exception.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		try {
			g.drawRect(width/4, height/4, width/2, height/2); //drawing the box
			if (server.getSprites() != null) {
				//repainting all the sprites
				for (int i = 0; i < server.getSprites().size(); i++) {
					server.getSprites().get(i).draw(g);
				}
			}
		} catch(RemoteException ex) {
			ex.printStackTrace();
		}
	}
}