import java.io.IOException;
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
			out.print("Starting server...");
			ServerSocket serv = new ServerSocket(port);
			Socket client = serv.accept();
			
			out.print("Accepted a client!");
			
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			while(true)
			{
				out.print( reader.readLine() );
			}
			
			
		} 
		catch (IOException e)
		{
			out.print("There was an error in Reciever class.\n");
			out.print(e.toString() + "\n");
		}
	}

}
