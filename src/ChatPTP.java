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
	JFrame mainFrame;
	
	JTextField input;
	JTextArea display;
	JScrollPane scroll;
	
	JTextField targetAddress;
	JTextField targetPort;
	JButton connectButton;
	
	//JPanel panel;
	
	SpringLayout layout;
	
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
		
		connectPanel.add( new JLabel("Connect to: "));
		connectPanel.add( targetAddress);
		connectPanel.add( new JLabel("Port: "));
		connectPanel.add( targetPort);
		connectPanel.add( connectButton);
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
	
	public void print(String text)
	{
		//Should I put a mutex here to solve multithreading?
		display.append(text);
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	
	
	//**************************************NON GUI STUFF*******************************************************
	
	Socket socOut;
	
	PrintWriter socWrite;
	//BufferedReader socRead;
	
	public void connectToHost(String hostName, int port)
	{
		try
		{
			Thread servThread = new Thread( new Reciever(this, port) );
			servThread.start();
			
			socOut = new Socket(hostName, port);
			print("Connected to other host!\n");
			
			socWrite = new PrintWriter( socOut.getOutputStream());
			socWrite.println("BLEEEERG");
		}
		catch(Exception e)
		{
			print("Error connecting to host.\n");
			print(e.toString() + "\n");
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == connectButton)
		{
			try
			{
				int p = Integer.parseInt( targetPort.getText() );
				connectToHost( targetAddress.getText(), p);
			}
			catch(Exception e)
			{
				print("BeepBoop, error:\n");
				print(e.toString() + "\n");
			}
		}
		else if(evt.getSource() == input)
		{
			print("You: " + input.getText() + "\n");
			socWrite.println("Blerg: " + input.getText());
			input.setText("");
		}
	}

}
