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
	
	public ChatPTP()
	{
		//make a window
		mainFrame = new JFrame("BamChat");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(900, 1000);
		
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

}
