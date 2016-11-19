package panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.util.ArrayList;

import frame.mainFrame;
import info.progressInfo;

public class entryPanel extends JPanel
{
// ** VARIABLE **
	// Connect to its parent frame
	mainFrame f;
	// For inner panels
	JPanel northPanel, centerPanel, southPanel;
	JLabel titleImage;
	JButton[] CH;
	JButton enterButton;
	ArrayList<Icon> charImages; 
	JTextField typeNickname;
	
	String imagePath;
	
// ** CONSTRUCTOR **
	public entryPanel (mainFrame f)
	{
		this.f = f;
		initCharImages ();
		imagePath = "";
		setPanel ();
		setEvent ();
	}
	
// ** METHOD **
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize the panels
	private void setPanel ()
	{
		this.setLayout (new BorderLayout ());
		// For north panel
		northPanel = new JPanel (new BorderLayout ());
		northPanel.setPreferredSize (new Dimension (1200, 150));
		titleImage = new JLabel ();
		titleImage.setIcon (new ImageIcon ("src/images/titlePanel.png"));
		titleImage.setPreferredSize (new Dimension (1200, 150));
		northPanel.add (titleImage);
		this.add (BorderLayout.NORTH, northPanel);
		
		// For center panel
		centerPanel = new JPanel (new FlowLayout ());
		centerPanel.setPreferredSize (new Dimension (1200, 300));
		centerPanel.setBackground (new Color (189, 215, 238));
		initCH ();
		this.add (BorderLayout.CENTER, centerPanel);
		
		// For south panel
		southPanel = new JPanel (new FlowLayout ());
		southPanel.setPreferredSize (new Dimension (1200, 150));
		southPanel.setBackground (new Color (189, 215, 238));
		typeNickname = new JTextField ();
		typeNickname.setPreferredSize (new Dimension (300, 80));
		typeNickname.setFont (new Font (null, Font.BOLD, 30));
		typeNickname.setBorder (new LineBorder (new Color (91, 155, 213), 4));
		enterButton = new JButton (new ImageIcon ("src/images/enterButton.png"));
		enterButton.setPreferredSize (new Dimension (100, 80));
		//enterButton.setBorder (null);
		southPanel.add (typeNickname); southPanel.add (enterButton);
		this.add (BorderLayout.SOUTH, southPanel);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize reactions in this panel
	private void setEvent ()
	{
		// Click buttons for each character to select
		CH[0].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (0);
			}
		});
		CH[1].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (1);
			}
		});
		CH[2].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (2);
			}
		});
		CH[3].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (3);
			}
		});
		CH[4].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (4);
			}
		});
		CH[5].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (5);
			}
		});
		CH[6].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (6);
			}
		});
		CH[7].addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				selectMaster (7);
			}
		});
		
		// Press the enter key to finish typing nickname
		typeNickname.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (typeNickname.getText ().equals (""))
					JOptionPane.showMessageDialog (entryPanel.this.f.getContentPane (), "Please enter nickname.");
				else if (imagePath == "")
					JOptionPane.showMessageDialog (entryPanel.this.f.getContentPane (), "Please select a character.");
				else
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.USER_ACCEPT);
					pi.set_chat (typeNickname.getText ());
					pi.set_imagePath (imagePath);
					entryPanel.this.f.sendProtocol(pi);
					typeNickname.setText ("");
				}
			}
		});
		
		// Click enter button with mouse to finish typing nickname
		enterButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (typeNickname.getText ().equals (""))
					JOptionPane.showMessageDialog (entryPanel.this.f.getContentPane (), "Please enter nickname.");
				else if (imagePath == "")
					JOptionPane.showMessageDialog (entryPanel.this.f.getContentPane (), "Please select a character.");
				else
				{
					progressInfo pi = new progressInfo ();
					pi.set_status(progressInfo.USER_ACCEPT);
					pi.set_chat(typeNickname.getText ());
					pi.set_imagePath (imagePath);
					entryPanel.this.f.sendProtocol(pi);
					typeNickname.setText ("");
				}
			}
		});
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize buttons for characters
	private void initCH ()
	{
		CH = new JButton[8];
		for (int i = 0 ; i < 8; i++)
		{
			CH[i] = new JButton (charImages.get (i));
			CH[i].setPreferredSize (new Dimension (120, 220));
			CH[i].setBorder (new LineBorder (Color.black, 6));
			centerPanel.add (CH[i]);
		}
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize images for characters
	private void initCharImages ()
	{
		charImages = new ArrayList<Icon> ();
		ArrayList<String> url = new ArrayList<String> (); 
		url = this.f.getCharImageList ();
		int length = url.size ();
		for (int i = 0; i < length; i++)
		{
			charImages.add (new ImageIcon (url.get (i)));
		}
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Display only the selected character with blue border, others with black border
	private void selectMaster (int selected)
	{
		for (int i = 0; i < 8; i++)
		{
			if (i == selected)
			{
				CH[i].setBorder (new LineBorder (new Color (91, 155, 213), 8));
				imagePath = this.f.getCharImageList ().get (i);
			}
			else
				CH[i].setBorder (new LineBorder (Color.black, 6));
		}
	}
}