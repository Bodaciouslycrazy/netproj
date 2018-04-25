import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import java.net.*;
import java.io.*;

public class Reciever implements Runnable{

	Printer out;
	int port;
	boolean connected = true;
	
	public Reciever(Printer print, int prt)
	{
		out = print;
		port = prt;
	}

	@Override
	public void run() {
		ServerSocket serv;
		Socket client;
		
		try 
		{
			//out.print("Starting server...\n");
			serv = new ServerSocket(port);
			client = serv.accept();
			serv.close();
			
			//out.print("Accepted a client!\n");
			
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			
			while(connected)
			{
				String line = reader.readLine();
				if(line == null) break;
				
				out.print( "Other: " + line + "\n");
			}
			
			client.close();
		} 
		catch (Exception e)
		{
			if(e.getClass() == InterruptedException.class)
			{
				out.print("Server disconnected.");
			}
			else
			{
				out.print("There was an error in Reciever class.\n");
				out.print(e.toString() + "\n");
			}
			
			out.disconnect();
		}
		
	}

}
