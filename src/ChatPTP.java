/*
 * Bodie Malik
 * 3-19-2018
 * 
 * First attempt at socket programming.
 * Peer-to-Peer chat program
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class ChatPTP implements ActionListener, Printer{
	
	//*****All GUI ELEMENTS******
	JFrame mainFrame;
	
	JTextField input;
	JTextArea display;
	JScrollPane scroll;
	
	JTextField targetAddress;
	JTextField targetPort;
	JButton connectButton;
	JButton disconnectButton;
	
	//Used so that you can't press the connect button twice
	private boolean connected = false;
	
	SpringLayout layout;
	
	/**
	 * Creates all the UI classes and sets up the window.
	 */
	public ChatPTP()
	{
		//make a window
		mainFrame = new JFrame("BamChat");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(900, 1000);
		mainFrame.setMinimumSize(new Dimension(400, 300));
		
		layout = new SpringLayout();
		Container pane = mainFrame.getContentPane();
		pane.setLayout(layout);
		
		
		//***************SETUP CONNECT PANEL*************
		JPanel connectPanel = new JPanel();
		connectPanel.setLayout( new FlowLayout(FlowLayout.LEFT) );
		
		targetAddress = new JTextField();
		targetAddress.setColumns(10);
		targetPort = new JTextField();
		targetPort.setColumns(8);
		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(this);
		
		connectPanel.add( new JLabel("Address: "));
		connectPanel.add( targetAddress);
		connectPanel.add( new JLabel("Port: "));
		connectPanel.add( targetPort);
		connectPanel.add( connectButton);
		connectPanel.add(disconnectButton);
		//**********************************************
		
		//***********************SETUP MAIN COMPONENTS******
		input = new JTextField();
		input.addActionListener(this);
		display = new JTextArea();
		display.setEditable(false);
		scroll = new JScrollPane( display,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainFrame.add(connectPanel);
		mainFrame.add(scroll);
		mainFrame.add(input);
		//mainFrame.add(display);
		
		//Setup position of the connect panel
		layout.putConstraint(SpringLayout.WEST, connectPanel, 16, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, connectPanel, 16, SpringLayout.NORTH, pane);
		layout.putConstraint(SpringLayout.EAST, connectPanel, -16, SpringLayout.EAST, pane);
		
		//Setup position of the input line
		layout.putConstraint(SpringLayout.WEST, input, 16, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.SOUTH, input, -16, SpringLayout.SOUTH, pane);
		layout.putConstraint(SpringLayout.EAST, input, -16, SpringLayout.EAST, pane);
		
		//Setup position of the display
		layout.putConstraint(SpringLayout.WEST, scroll, 16, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.EAST, scroll, -16, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.NORTH, scroll, 16, SpringLayout.SOUTH, connectPanel);
		layout.putConstraint(SpringLayout.SOUTH, scroll, -16, SpringLayout.NORTH, input);
		
		mainFrame.setVisible(true);
		
		print("Welcome to BamChat!\n");
		print("Please type an IP address, port, and then press \"Connect\".\n");
	}
	
	public static void createGUI()
	{
		ChatPTP chat = new ChatPTP();
	}
	
	/**
	 * Simply appends some text to the display. Pretty much everything can go here.
	 */
	public void print(String text)
	{
		//Should I put a mutex here to help with multithreading?
		display.append(text);
	}
	
	/**
	 * main
	 * sets up the GUI.
	 * @param args
	 */
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	
	
	
	
	//****************NON GUI STUFF*******************************
	Socket socOut;
	PrintWriter socWrite;
	
	Thread servThread;
	
	/**
	 * Tries to connect to another host.
	 * 
	 * Creates a new server thread to recieve another client.
	 * Then tries to connect to another client with the give ip/port
	 * @param hostName - an IP Address 
	 * @param port
	 */
	public void connectToHost(String hostName, int port)
	{
		if(connected)
			return;
		
		connected = true;
		
		try
		{
			//Create and start server thread.
			servThread = new Thread( new Reciever(this, port) );
			servThread.start();
			
			//Connect to other server.
			socOut = new Socket(hostName, port);
			print("Connected to other host!\n");
			
			//Make a print writer for the socket output.
			socWrite = new PrintWriter( socOut.getOutputStream() );
		}
		catch(Exception e)
		{
			connected = false;
			print("Error connecting to host.\n");
			print(e.toString() + "\n");
		}
	}
	
	
	/**
	 * Disconnects all sockets and writers.
	 */
	@Override
	public void disconnect()
	{
		connected = false;
		
		try
		{
			if(socOut != null)
			{
				socOut.close();
			}
			
			if(socWrite != null)
			{
				socWrite.close();
			}
			
			socOut = null;
			socWrite = null;
			
			if( servThread != null && servThread.isAlive())
			{
				servThread.interrupt();
			}
			
			servThread = null;
			
			print("Disconnected.\n");
		}
		catch(Exception e)
		{
			print("Error Disconnecting.\n");
			print(e.toString() + "\n");
		}
	}

	
	/**
	 * Recieves various actions from the UI.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == connectButton)
		{
			try
			{
				int p = Integer.parseInt( targetPort.getText() );
				connectToHost( targetAddress.getText(), p);
			}
			catch(NumberFormatException e)
			{
				print("Type a real port number, dingus.\n");
			}
		}
		else if(evt.getSource() == input)
		{
			sendMessage();
		}
		else if(evt.getSource() == disconnectButton)
		{
			disconnect();
		}
		else
		{
			print("Didn't recognize that ActionEvent!\n");
		}
	}
	
	/**
	 * Sends text from the input bar through the socket to the other host.
	 * Also prints text here.
	 */
	public void sendMessage()
	{
		if(socWrite != null)
		{
			print("You: " + input.getText() + "\n");
			socWrite.println(input.getText());
			socWrite.flush();
			input.setText("");
		}
		else
		{
			print("You are not connected to another host right now.\n");
		}
	}

}
