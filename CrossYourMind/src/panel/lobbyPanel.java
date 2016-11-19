package panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.WindowConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import java.util.ArrayList;

import frame.mainFrame;
import info.progressInfo;

public class lobbyPanel extends JPanel
{
// ** VARIABLE **
	// Connect to its parent frame
	mainFrame f;
	// For inner panels
	JPanel northPanel, centerPanel, southPanel;
	JLabel titleImage, gameListImage;
	JPanel gameListPanel, userListPanel;
	JScrollPane gameListScroll, userListScroll;
	JList<String> gameList, userList;
	JPanel lobbyChat, infoAndButton;
	JTextArea showChat; JTextField typeChat;
	JPanel myInfo, buttonPanel;
	JLabel myChar, myNickname;
	JButton createButton, backButton;
	JDialog createDialog;
	String[] recent8Chat;
	String[] gamesLobby;
	String[] usersLobby;

// ** CONSTRUCTOR **
	public lobbyPanel (mainFrame f)
	{
		this.f = f;
		recent8Chat = new String[8]; // In lobby, show only recent 8 chats
		initR8C ();
		initCreateDialog ();
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
		centerPanel = new JPanel (new BorderLayout ());
		centerPanel.setPreferredSize (new Dimension (1200, 450));
		gameListPanel = new JPanel (new BorderLayout ());
		gameListPanel.setPreferredSize (new Dimension (800, 450));
		gameList = new JList<String> ();
		gameList.setPreferredSize (new Dimension (800, 450));
		gameList.setBackground (new Color (222, 235, 247));
		gameList.setFont (new Font (null, Font.PLAIN, 40));
		gameList.setBorder (new LineBorder (new Color (189, 215, 238), 4));
		gameList.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
		gameListScroll = new JScrollPane (gameList);
		gameListPanel.add (BorderLayout.CENTER, gameListScroll);
		userListPanel = new JPanel (new BorderLayout ());
		userListPanel.setPreferredSize (new Dimension (400, 450));
		userList = new JList<String> ();
		userList.setPreferredSize(new Dimension (400, 450));
		userList.setBackground (new Color (222, 235, 247));
		userList.setFont (new Font (null, Font.PLAIN, 40));
		userList.setBorder (new LineBorder (new Color (189, 215, 238), 4));
		userListScroll = new JScrollPane (userList);
		userListPanel.add (BorderLayout.CENTER, userListScroll);
		centerPanel.add (BorderLayout.WEST, gameListPanel); centerPanel.add (BorderLayout.EAST, userListPanel);
		this.add (BorderLayout.CENTER, centerPanel);
		
		// For south panel
		southPanel = new JPanel (new BorderLayout ());
		southPanel.setPreferredSize (new Dimension (1200, 300));
		// Left of the south panel: chats in lobby
		lobbyChat = new JPanel (new BorderLayout ());
		lobbyChat.setPreferredSize (new Dimension (800, 300));
		showChat = new JTextArea ();
		showChat.setPreferredSize (new Dimension (800, 240));
		showChat.setBackground (new Color (222, 235, 247));
		showChat.setFont (new Font (null, Font.PLAIN, 20));
		showChat.setBorder (new LineBorder (new Color (189, 215, 238), 4));
		showChat.setEditable (false);
		typeChat = new JTextField ();
		typeChat.setPreferredSize (new Dimension (800, 60));
		typeChat.setFont (new Font (null, Font.BOLD, 30));
		typeChat.setBorder (new LineBorder (new Color (91, 155, 213), 4));
		lobbyChat.add (BorderLayout.NORTH, showChat); lobbyChat.add (BorderLayout.SOUTH, typeChat);
		// Right of the south panel: Information of this client & buttons
		infoAndButton = new JPanel (new BorderLayout ());
		infoAndButton.setPreferredSize (new Dimension (400, 300));
		myInfo = new JPanel (new FlowLayout ());
		myInfo.setAlignmentX (0);
		myInfo.setAlignmentY (0);
		myInfo.setPreferredSize (new Dimension (400, 200));
		myInfo.setBackground (new Color (222, 235, 247));
		myInfo.setBorder (new LineBorder (new Color (189, 215, 238), 4));
		buttonPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		buttonPanel.setPreferredSize (new Dimension (400, 100));
		buttonPanel.setBackground (new Color (222, 235, 247));
		buttonPanel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
		createButton = new JButton (new ImageIcon ("src/images/createButton.png"));
		createButton.setPreferredSize (new Dimension (180, 80));
		backButton = new JButton (new ImageIcon ("src/images/backButton.png"));
		backButton.setPreferredSize (new Dimension (180, 80));
		buttonPanel.add (createButton); buttonPanel.add (backButton);
		infoAndButton.add (BorderLayout.NORTH, myInfo); infoAndButton.add (BorderLayout.SOUTH, buttonPanel);
		southPanel.add (BorderLayout.WEST, lobbyChat); southPanel.add (BorderLayout.EAST, infoAndButton);
		this.add (BorderLayout.SOUTH, southPanel);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize reactions in this panel
	private void setEvent ()
	{
		// Press enter key to finish typing chat
		typeChat.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				progressInfo pi = new progressInfo ();
				pi.set_status(progressInfo.CHAT_LOBBY);
				pi.set_chat(typeChat.getText ());
				lobbyPanel.this.f.sendProtocol(pi);
				typeChat.setText ("");
			}
		});
		
		// Click create button to create a new game 
		createButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				showCreateDialog ();	
			}
		});
		
		// Click back button to go the entry panel
		backButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				progressInfo pi = new progressInfo ();
				pi.set_status(progressInfo.EXIT_LOBBY);
				lobbyPanel.this.f.sendProtocol(pi);
				lobbyPanel.this.f.setSize (mainFrame.entryPwidth, mainFrame.entryPheight);
				lobbyPanel.this.f.set_currentCard (mainFrame.entryPcard);
				lobbyPanel.this.f.get_card().show (lobbyPanel.this.f.getContentPane (), mainFrame.entryPcard);
				//lobbyPanel.this.f.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
			}
		});
		
		// Double click the game to join
		gameList.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				if (e.getClickCount () == 2)
				{
					if (gameList.getSelectedIndex () == -1)
					{
						JOptionPane.showMessageDialog(lobbyPanel.this.f.getContentPane (), "Select the room.");
					}
					else
					{
						System.out.println ("I'm here!");
						progressInfo pi = new progressInfo ();
						pi.set_status (progressInfo.JOIN_GAME_TRY);
						pi.set_chat (gameList.getSelectedValue ().trim ());
						lobbyPanel.this.f.sendProtocol(pi);
					}
				}
			}
		});
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize the 8 recent chats in lobby as empty strings
	private void initR8C ()
	{
		for (int i = 0; i < 8; i++)
		{
			recent8Chat[i] = "";
		}
	}
	
	// INPUT: updated list of users in lobby
	// OUTPUT: null
	// Objective: Update the list of user in lobby: Right of center panel
	public void updateLobbyUser (ArrayList<String> updated)
	{
		usersLobby = new String[updated.size ()];
		for (int i = 0; i < updated.size (); i++)
		{
			usersLobby[i] = "  " + updated.get (i);
		}
		userList.setListData (usersLobby);
	}
	
	// INPUT: updated list of games in lobby
	// OUTPUT: null
	// Objective: Update the list of game in lobby: Left of center panel
	public void updateLobbyGame (ArrayList<String> updated)
	{
		gamesLobby = new String[updated.size ()];
		for (int i = 0; i < updated.size (); i++)
		{
			gamesLobby[i] = "  " + updated.get (i);
		}
		gameList.setListData (gamesLobby);
	}
	
	// INPUT: most recent chat by user in lobby
	// OUTPUT: null
	// Objective: Update the list of 8 recent chat and display it to lobby chat panel
	public void updateLobbyChat (String lobbyChat)
	{
		for (int i = 7; i > 0; i--)
		{
			recent8Chat[i] = recent8Chat[i-1];
		}
		recent8Chat[0] = lobbyChat;
		showChat.setText("");
		for (int i = 7; i >= 0; i--)
		{
			showChat.append ("  " + recent8Chat[i]+ "\r\n");
		}
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize the dialog box for creating a new  game
	public void initCreateDialog ()
	{
		createDialog = new JDialog();
    	CreateDialog cd = new CreateDialog (this);
    	createDialog.setContentPane (cd);
    	createDialog.setBounds (400, 300, 400, 300);
    	createDialog.setResizable (false);
    	createDialog.setVisible (false);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Invoked when user clicked the create room button, show the dialog box
	public void showCreateDialog ()
	{
    	createDialog.setVisible (true);
    }
	
	// INPUT: name of the game to join
	// OUTPUT: null
	// Objective: Change the client's display to game panel when joining game succeeds
	public void joinApproved (String gameName)
	{
		progressInfo pi = new progressInfo ();
		pi.set_status (progressInfo.JOIN_GAME);
		pi.set_chat (gameName);
		lobbyPanel.this.f.sendProtocol(pi);
		lobbyPanel.this.f.setSize (mainFrame.gamePwidth, mainFrame.gamePheight);
		lobbyPanel.this.f.set_currentCard (mainFrame.gamePcard);
		lobbyPanel.this.f.get_card().show (lobbyPanel.this.f.getContentPane (), mainFrame.gamePcard);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Notice the client that joining game failed because the game is full or already started
	public void joinDenied ()
	{
		JOptionPane.showMessageDialog (lobbyPanel.this.f.getContentPane (), "The game is full or already started.");
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Change the client's display to game panel when creating game succeeds
	public void createApproved ()
	{
		lobbyPanel.this.f.setSize (mainFrame.gamePwidth, mainFrame.gamePheight);
		lobbyPanel.this.f.set_currentCard (mainFrame.gamePcard);
		lobbyPanel.this.f.get_card().show (lobbyPanel.this.f.getContentPane (), mainFrame.gamePcard);
		lobbyPanel.this.closeCreateDialog ();	
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Close the dialog box for creating a new game when user succeeds of cancels creating
	public void closeCreateDialog ()
	{
		createDialog.setVisible (false);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Display the client's information in South-right panel
	public void myInfoUpdate ()
	{
		myInfo.removeAll ();
		myChar = new JLabel (new ImageIcon (this.f.get_myImagePath ()));
		myChar.setPreferredSize (new Dimension (100, 180));
		myNickname = new JLabel ("  " + this.f.get_myNickname ());
		myNickname.setPreferredSize (new Dimension (200, 180));
		myNickname.setFont (new Font (null, Font.PLAIN, 40));
		myInfo.add (myChar); myInfo.add (myNickname);
		myInfo.repaint ();
	}
}

// Private class in lobby panel: Dialog box for creating a new game
class CreateDialog extends JPanel
{
// ** VARIABLE **
	// Connect to its parent panel
	lobbyPanel lp;
	// For inner panels
	JPanel southPanel;
	JLabel message;
	JTextField typeRoomName;
	JButton createButton, exitButton;
	
// ** CONSTRUCTOR **
	public CreateDialog (lobbyPanel lp)
	{
		this.lp = lp;
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
		// Inform user to enter the game name
		message = new JLabel ("Enter the name of game room.");
		message.setPreferredSize (new Dimension (400, 100));
		message.setBackground(new Color (222, 235, 247));
		message.setFont (new Font (null, Font.PLAIN, 25));
		message.setHorizontalAlignment (JLabel.CENTER);
		message.setVerticalAlignment (JLabel.CENTER);
		this.add (BorderLayout.NORTH, message);
		
		// Textfield for user to type game name
		typeRoomName = new JTextField ();
		typeRoomName.setPreferredSize (new Dimension (400, 60));
		typeRoomName.setFont (new Font (null, Font.BOLD, 30));
		typeRoomName.setBorder (new LineBorder (new Color (91, 155, 213), 4));
		this.add (BorderLayout.CENTER, typeRoomName);
		
		// South panel for buttons
		southPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
		southPanel.setPreferredSize (new Dimension (400, 100));
		southPanel.setBackground (Color.green);
		southPanel.setBackground(new Color (222, 235, 247));
		createButton = new JButton (new ImageIcon ("src/images/createButton.png"));
		createButton.setPreferredSize (new Dimension (180, 80));
		exitButton = new JButton (new ImageIcon ("src/images/backButton.png"));
		exitButton.setPreferredSize (new Dimension (180, 80));
		southPanel.add (createButton); southPanel.add (exitButton);
		this.add (BorderLayout.SOUTH, southPanel);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize reactions in this dialog box
	private void setEvent ()
	{
		// Press enter key
		typeRoomName.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (typeRoomName.getText ().equals (""))
					JOptionPane.showMessageDialog (CreateDialog.this.lp.f.getContentPane (), "Please enter room name.");
				else
				{
					progressInfo pi = new progressInfo ();
					pi.set_status(progressInfo.CREATE_GAME_TRY);
					pi.set_chat (typeRoomName.getText ());
					CreateDialog.this.lp.f.sendProtocol(pi);
					typeRoomName.setText ("");
				}
			}
		});
		
		// Click the create button to try creating
		createButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (typeRoomName.getText ().equals (""))
					JOptionPane.showMessageDialog (CreateDialog.this.lp.f.getContentPane (), "Please enter room name.");
				else
				{
					progressInfo pi = new progressInfo ();
					pi.set_status(progressInfo.CREATE_GAME_TRY);
					pi.set_chat (typeRoomName.getText ());
					CreateDialog.this.lp.f.sendProtocol(pi);
					typeRoomName.setText ("");
				}
			}
		});	

		// Click the exit button to cancel creating
		exitButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				typeRoomName.setText ("");
				CreateDialog.this.lp.closeCreateDialog ();
			}
		});
	}
}