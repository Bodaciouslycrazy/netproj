import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Reciever implements Runnable{

	Printer out;
	int port;
	
	public Reciever(Printer print, int prt)
	{
		out = print;
		port = prt;
	}

	@Override
	public void run() {
		
		try 
		{
			out.print("Starting server...\n");
			ServerSocket serv = new ServerSocket(port);
			Socket client = serv.accept();
			
			out.print("Accepted a client!\n");
			
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			while(true)
			{
				String line = reader.readLine();
				
				//Since swing is not thread safe, we must call invodeAndWait with a new runnable.
				SwingUtilities.invokeAndWait( new Runnable() {
					public void run()
					{
						out.print(line);
					}
				});
				
				//out.print( reader.readLine() );
			}
			
			
		} 
		catch (Exception e)
		{
			out.print("There was an error in Reciever class.\n");
			out.print(e.toString() + "\n");
		}
	}

}
