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

public class ChatPTP {
	JFrame mainFrame;
	
	JTextField input;
	JTextArea display;
	JScrollPane scroll;
	
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
		
		input = new JTextField();
		display = new JTextArea();
		display.setEditable(false);
		scroll = new JScrollPane( display,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainFrame.add(scroll);
		mainFrame.add(input);
		//mainFrame.add(display);
		
		//Setup position of the input line
		layout.putConstraint(SpringLayout.WEST, input, 16, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.SOUTH, input, -16, SpringLayout.SOUTH, pane);
		layout.putConstraint(SpringLayout.EAST, input, -16, SpringLayout.EAST, pane);
		
		//Setup position of the display
		layout.putConstraint(SpringLayout.WEST, scroll, 16, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.EAST, scroll, -16, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.NORTH, scroll, 16, SpringLayout.NORTH, pane);
		layout.putConstraint(SpringLayout.SOUTH, scroll, -16, SpringLayout.NORTH, input);
		
		displayText("Welcome to BamChat!\n");
		
		mainFrame.setVisible(true);
	}
	
	public static void createGUI()
	{
		ChatPTP chat = new ChatPTP();
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createGUI();
			}
		});
	}
	
	public void displayText(String text)
	{
		display.append(text);
	}

}
