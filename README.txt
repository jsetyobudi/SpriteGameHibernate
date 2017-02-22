Assignment 3 RMI Hibernate 
CST 8277
By: Johan Setyobudi

Please read this file before starting the program.
To use this program, please use the following instructions:

1. If you haven't already, install MySQL. Make sure the ConnecterJ option is installed also.
2. Then run MySQL, either by command line or using the GUI. Login using root, or another high level user account. Take note of the port number needed to connect. 
   You will need this later.
3. Create a user named 'hibernate' with the password 'password' and grant user all the necessary permissions.
4. Log into that user and create a database called 'Assignment3_RMI_Hibernate'.
5. Alternatively, it is possible to edit the Hibernate XML File(/src/hibernate.cfg.xml) and use your own settings. Before continuing, make sure the 
   "hibernate.connection.url" in the hibernate.cfg.xml file is set to your configuration. If it is wrong, the database connection won't be possible.
6. Run the SpriteServer. It is also possible to change the default port (Default port set is 8082). It is also possible
   to change the ip address in the Naming.Rebind(Line 90 in SpriteServer) to your choosing.
7. Run the SpriteClient. Make sure the ip address and port numbers are correct, corresponding to the ports in SpriteServer. These
   parameters can be found in the main method(Starting at line 98 in SpriteClient).
8. After the SpriteClient is running, press anywhere inside of the application window to create a new sprite at that position. The client will
   be given a unique colour. Keep clicking to keep creating sprites with the same colour. If any other client is running, they will have their own
   unique colour, different from yours. It is possible to run multiple instances of the SpriteClient.
9. When finished, close all open SpriteClients and then stop the SpriteServer.