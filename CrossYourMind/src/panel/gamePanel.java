package panel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.WindowConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.border.LineBorder;
import javax.swing.AbstractAction;
import javax.swing.Timer;

import frame.mainFrame;
import info.progressInfo;
import info.userInfo;
import drawing.point;

import java.util.ArrayList;

public class gamePanel extends JPanel
{
// ** DEFINE **
	public static final int ROUND_TIME = 60;
	
// ** VARIABLE **
	// Connect its parent frame
	mainFrame f;
	// For inner panels
	JPanel northPanel, centerPanel, westPanel, eastPanel, southPanel;
	JLabel titleImage;
	JPanel centerTool, centerCanvas;
	JPanel user1Panel, user2Panel, user3Panel, user4Panel, user5Panel, user6Panel;
	JTextPane user1Chat, user2Chat, user3Chat, user4Chat, user5Chat, user6Chat;
	JLabel user1Char, user2Char, user3Char, user4Char, user5Char, user6Char;
	JTextPane user1Nickname, user2Nickname, user3Nickname, user4Nickname, user5Nickname, user6Nickname;
	JTextField gameChat;
	JTextPane answer, timer;
	JButton clearAll, eraser, color0, color1, color2, color3, color4, color5;
	JButton startButton, backButton;
	
	// For drawing
	Canvas cv;
	int pointX, pointY;
	ArrayList<point> pList;
	Color drawColor;
	int drawThick;
	
	// For game operation
	ArrayList<userInfo> usersGame;
	boolean gameStarted;
	boolean isQuestioner;
	long gameTime;
	int answerCount;
	
	Thread thread;
	
// ** CONSTRUCTOR **
	public gamePanel (mainFrame f)
	{
		this.f = f;
		
		drawColor = Color.black;
		drawThick = 10;

		gameStarted = false;
		isQuestioner = false;
		answerCount = 0;
		
		setPanel ();
		setEvent ();
	}
	
// ** METHOD **	
	private void setPanel ()
	{
		// Initialize data
		this.setLayout (new BorderLayout ());
		pList = new ArrayList<point> ();
		usersGame = new ArrayList<userInfo> ();
		
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
		centerPanel.setPreferredSize (new Dimension (800, 650));
		centerPanel.setBackground (Color.black);
		// For drawing tools
		centerTool = new JPanel (new FlowLayout ());
		centerTool.setPreferredSize (new Dimension (800, 60));
		centerTool.setBorder (new LineBorder (new Color (157, 195, 230), 4));
		centerTool.setAlignmentX (1.0f);
		centerTool.setAlignmentY (1.0f);
		StyleContext contextAnswer = new StyleContext ();
	    StyledDocument documentAnswer = new DefaultStyledDocument (contextAnswer);
	    Style styleAnswer = contextAnswer.getStyle (StyleContext.DEFAULT_STYLE);
	    StyleConstants.setAlignment (styleAnswer, StyleConstants.ALIGN_CENTER);
	    answer = new JTextPane (documentAnswer);
		answer.setPreferredSize (new Dimension (200, 40));
		answer.setFont (new Font (null, Font.BOLD, 15));
		answer.setText ("ANSWER");
		answer.setBorder (new LineBorder (Color.black, 2));
		answer.setEditable(false);
		clearAll = new JButton ("CLEAR");
		clearAll.setPreferredSize (new Dimension (80, 40));
		eraser = new JButton ("ERASER");
		eraser.setPreferredSize (new Dimension (80, 40));
		color0 = new JButton ();
		color0.setPreferredSize (new Dimension (40, 40));
		color0.setBackground (Color.black);
		color1 = new JButton ();
		color1.setPreferredSize (new Dimension (40, 40));
		color1.setBackground (Color.red);
		color2 = new JButton ();
		color2.setPreferredSize (new Dimension (40, 40));
		color2.setBackground (Color.yellow);
		color3 = new JButton ();
		color3.setPreferredSize (new Dimension (40, 40));
		color3.setBackground (Color.green);
		color4 = new JButton ();
		color4.setPreferredSize (new Dimension (40, 40));
		color4.setBackground (Color.blue);
		color5 = new JButton ();
		color5.setPreferredSize (new Dimension (40, 40));
		color5.setBackground (new Color (128, 0, 128));
		StyleContext contextTimer = new StyleContext ();
	    StyledDocument documentTimer = new DefaultStyledDocument (contextTimer);
	    Style styleTimer = contextTimer.getStyle (StyleContext.DEFAULT_STYLE);
	    StyleConstants.setAlignment (styleTimer, StyleConstants.ALIGN_CENTER);
	    timer = new JTextPane (documentTimer);
	    timer.setPreferredSize (new Dimension (80, 40));
	    timer.setFont (new Font (null, Font.BOLD, 15));
	    timer.setText ("TIMER");
	    timer.setBorder (new LineBorder (Color.black, 2));
	    timer.setEditable(false);
		centerTool.add (answer); centerTool.add (clearAll); centerTool.add (eraser);
		centerTool.add (color0); centerTool.add (color1); centerTool.add (color2); centerTool.add (color3); centerTool.add (color4); centerTool.add (color5);
		centerTool.add (timer);
		// For drawing canvas
		centerCanvas = new JPanel (new BorderLayout ());
		centerCanvas.setPreferredSize (new Dimension (800, 570));
		centerCanvas.setBorder (new LineBorder (new Color (157, 195, 230), 4));
		centerCanvas.add (cv = new Canvas());
		cv.setBackground (Color.white);
		cv.setEnabled(true);
		centerPanel.add (BorderLayout.NORTH, centerTool); centerPanel.add (BorderLayout.CENTER, centerCanvas);
		this.add (BorderLayout.CENTER, centerPanel);
		
		// For west panel: 3 users
		westPanel = new JPanel (new GridLayout (3, 1));
		westPanel.setPreferredSize (new Dimension (200, 650));
		westPanel.setBackground (new Color (189, 215, 238));
		user1Panel = new JPanel ();
		user1Chat = new JTextPane ();
		user1Char = new JLabel ();
		user1Nickname = new JTextPane ();
		user1Nickname.setText ("");
		user2Panel = new JPanel ();
		user2Chat = new JTextPane ();
		user2Char = new JLabel ();
		user2Nickname = new JTextPane ();
		user2Nickname.setText ("");
		user3Panel = new JPanel ();
		user3Chat = new JTextPane ();
		user3Char = new JLabel ();
		user3Nickname = new JTextPane ();
		user3Nickname.setText ("");
		westPanel.add (user1Panel); westPanel.add (user2Panel); westPanel.add (user3Panel); 
		this.add (BorderLayout.WEST, westPanel);
		
		// For east panel: 3 users
		eastPanel = new JPanel (new GridLayout (3, 1));
		eastPanel.setPreferredSize (new Dimension (200, 650));
		eastPanel.setBackground (new Color (189, 215, 238));
		user4Panel = new JPanel ();
		user4Chat = new JTextPane ();
		user4Char = new JLabel ("");
		user4Nickname = new JTextPane ();
		user4Nickname.setText ("");
		user5Panel = new JPanel ();
		user5Chat = new JTextPane ();
		user5Char = new JLabel ();
		user5Nickname = new JTextPane ();
		user5Nickname.setText ("");
		user6Panel = new JPanel ();
		user6Chat = new JTextPane ();
		user6Char = new JLabel ();
		user6Nickname = new JTextPane ();
		user6Nickname.setText ("");
		eastPanel.add (user4Panel); eastPanel.add (user5Panel); eastPanel.add (user6Panel);
		this.add (BorderLayout.EAST, eastPanel);
		
		// For south panel: chat and buttons
		southPanel = new JPanel (new FlowLayout ());
		southPanel.setAlignmentX (0);
		southPanel.setAlignmentY (0);
		southPanel.setPreferredSize (new Dimension (1200, 100));
		southPanel.setBackground (new Color (222, 235, 247));
		southPanel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
		gameChat = new JTextField ();
		gameChat.setPreferredSize (new Dimension (800, 60));
		gameChat.setFont (new Font (null, Font.BOLD, 30));
		gameChat.setBorder (new LineBorder (new Color (91, 155, 213), 4));
		startButton = new JButton (new ImageIcon ("src/images/startButton.png"));
		startButton.setPreferredSize (new Dimension (180, 80));
		backButton = new JButton (new ImageIcon ("src/images/backButton.png"));
		backButton.setPreferredSize (new Dimension (180, 80));
		southPanel.add (gameChat); southPanel.add (startButton); southPanel.add (backButton);
		this.add (BorderLayout.SOUTH, southPanel);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize reactions in this panel
	private void setEvent ()
	{	
		// Press enter key to finish typing chat
		gameChat.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (!isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.CHAT_GAME);
					pi.set_chat (gameChat.getText ());
					gamePanel.this.f.sendProtocol (pi);
					gameChat.setText ("");
				}
			}
		});
		
		// Click start button to start game
		startButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (!gameStarted)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.START_TRY);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		
		// Click back button to go to the lobby panel
		backButton.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (!gameStarted)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.EXIT_GAME);
					gamePanel.this.f.sendProtocol (pi);
					gamePanel.this.f.setSize (mainFrame.lobbyPwidth, mainFrame.lobbyPheight);
					gamePanel.this.f.set_currentCard (mainFrame.lobbyPcard);
					gamePanel.this.f.get_card().show (gamePanel.this.f.getContentPane (), mainFrame.lobbyPcard);
				}
			}
		});
		
		// Mouse drag to draw
		cv.addMouseMotionListener (new MouseMotionAdapter ()
		{
			@Override
			public void mouseDragged (MouseEvent e)
			{
				if (SwingUtilities.isLeftMouseButton(e) && isQuestioner)
				{
					pList = new ArrayList<point> ();
					pList.add (new point (e.getX (), e.getY ()));
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.DRAW);
					pi.set_pList (pList);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		
		// Click clear button to erase the whole canvas
		clearAll.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_CLEAR);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		
		// Click eraser button to select eraser
		eraser.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_ERASER);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		
		// Click color buttons to change select drawing color
		color0.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_COLOR);
					pi.set_drawColor (0);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		color1.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_COLOR);
					pi.set_drawColor (1);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		color2.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_COLOR);
					pi.set_drawColor (2);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		color3.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_COLOR);
					pi.set_drawColor (3);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		color4.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_COLOR);
					pi.set_drawColor (4);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
		color5.addActionListener (new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.SELECT_COLOR);
					pi.set_drawColor (5);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		});
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: When a new  user enters game, updates the west&east panel with updated user list in game
	private void updatePanel ()
	{
		westPanel.removeAll ();
		eastPanel.removeAll ();
		int size = usersGame.size ();
		// Initialize score of each player
		for (userInfo ui: usersGame)
		{
			ui.set_score (0);
		}
		answer.setText ("ANSWER");
		timer.setText("TIMER");
		
		// Re-draw userNpanel according the number of users currently in game
		switch (size)
		{
			case 6: 
			{
				user6Panel = new JPanel (new BorderLayout ());
				user6Panel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
				StyleContext contextUser6 = new StyleContext ();
			    StyledDocument documentUser6 = new DefaultStyledDocument (contextUser6);
			    Style styleUser6 = contextUser6.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser6, StyleConstants.ALIGN_CENTER);
			    user6Chat = new JTextPane (documentUser6);
			    user6Chat.setPreferredSize (new Dimension (200, 40));
			    user6Chat.setFont (new Font (null, Font.BOLD, 20));
			    user6Chat.setText ("");
			    user6Chat.setBorder (new LineBorder (Color.black, 2));
			    user6Chat.setEditable(false);
				user6Char = new JLabel (new ImageIcon (usersGame.get (size - 6).get_imagePath ().substring(0, usersGame.get (size - 6).get_imagePath ().length () - 4) + "H.png"));
				user6Char.setPreferredSize (new Dimension (100, 100));
				StyleContext contextUser6Nickname = new StyleContext ();
			    StyledDocument documentUser6Nickname = new DefaultStyledDocument (contextUser6Nickname);
			    Style styleUser6Nickname = contextUser6Nickname.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser6Nickname, StyleConstants.ALIGN_CENTER);
			    user6Nickname = new JTextPane (documentUser6Nickname);
				user6Nickname.setText(usersGame.get (size - 6).get_nickName () + " SCORE: " + usersGame.get (size - 6).get_score ());
				user6Nickname.setPreferredSize (new Dimension (200, 60));
				user6Nickname.setFont (new Font (null, Font.BOLD, 15));
				user6Panel.add (BorderLayout.NORTH, user6Chat); user6Panel.add (BorderLayout.CENTER, user6Char); user6Panel.add (BorderLayout.SOUTH, user6Nickname);
				eastPanel.add (BorderLayout.SOUTH, user6Panel);
			}
			case 5:
			{
				user5Panel = new JPanel (new BorderLayout ());
				user5Panel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
				StyleContext contextUser5 = new StyleContext ();
			    StyledDocument documentUser5 = new DefaultStyledDocument (contextUser5);
			    Style styleUser5 = contextUser5.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser5, StyleConstants.ALIGN_CENTER);
			    user5Chat = new JTextPane (documentUser5);
			    user5Chat.setPreferredSize (new Dimension (200, 40));
			    user5Chat.setFont (new Font (null, Font.BOLD, 20));
			    user5Chat.setText ("");
			    user5Chat.setBorder (new LineBorder (Color.black, 2));
			    user5Chat.setEditable(false);
			    user5Char = new JLabel (new ImageIcon (usersGame.get (size - 5).get_imagePath ().substring(0, usersGame.get (size - 5).get_imagePath ().length () - 4) + "H.png"));
				user5Char.setPreferredSize (new Dimension (100, 100));
				StyleContext contextUser5Nickname = new StyleContext ();
			    StyledDocument documentUser5Nickname = new DefaultStyledDocument (contextUser5Nickname);
			    Style styleUser5Nickname = contextUser5Nickname.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser5Nickname, StyleConstants.ALIGN_CENTER);
			    user5Nickname = new JTextPane (documentUser5Nickname);
			    user5Nickname.setText(usersGame.get (size - 5).get_nickName () + " SCORE: " + usersGame.get (size - 5).get_score ());
				user5Nickname.setPreferredSize (new Dimension (200, 60));
				user5Nickname.setFont (new Font (null, Font.BOLD, 15));
				user5Panel.add (BorderLayout.NORTH, user5Chat); user5Panel.add (BorderLayout.CENTER, user5Char); user5Panel.add (BorderLayout.SOUTH, user5Nickname);
				eastPanel.add (BorderLayout.CENTER, user5Panel);
			}
			case 4: 
			{
				user4Panel = new JPanel (new BorderLayout ());
				user4Panel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
				StyleContext contextUser4 = new StyleContext ();
			    StyledDocument documentUser4 = new DefaultStyledDocument (contextUser4);
			    Style styleUser4 = contextUser4.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser4, StyleConstants.ALIGN_CENTER);
			    user4Chat = new JTextPane (documentUser4);
			    user4Chat.setPreferredSize (new Dimension (200, 40));
			    user4Chat.setFont (new Font (null, Font.BOLD, 20));
			    user4Chat.setText ("");
			    user4Chat.setBorder (new LineBorder (Color.black, 2));
			    user4Chat.setEditable(false);
			    user4Char = new JLabel (new ImageIcon (usersGame.get (size - 4).get_imagePath ().substring(0, usersGame.get (size - 4).get_imagePath ().length () - 4) + "H.png"));
				user4Char.setPreferredSize (new Dimension (100, 100));
				StyleContext contextUser4Nickname = new StyleContext ();
			    StyledDocument documentUser4Nickname = new DefaultStyledDocument (contextUser4Nickname);
			    Style styleUser4Nickname = contextUser4Nickname.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser4Nickname, StyleConstants.ALIGN_CENTER);
			    user4Nickname = new JTextPane (documentUser4Nickname);
			    user4Nickname.setText(usersGame.get (size - 4).get_nickName () + " SCORE: " + usersGame.get (size - 4).get_score ());
				user4Nickname.setPreferredSize (new Dimension (200, 60));
				user4Nickname.setFont (new Font (null, Font.BOLD, 15));
				user4Panel.add (BorderLayout.NORTH, user4Chat); user4Panel.add (BorderLayout.CENTER, user4Char); user4Panel.add (BorderLayout.SOUTH, user4Nickname);
				eastPanel.add (BorderLayout.NORTH, user4Panel);
			}
			case 3: 
			{
				user3Panel = new JPanel (new BorderLayout ());
				user3Panel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
				StyleContext contextUser3 = new StyleContext ();
			    StyledDocument documentUser3 = new DefaultStyledDocument (contextUser3);
			    Style styleUser3 = contextUser3.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser3, StyleConstants.ALIGN_CENTER);
			    user3Chat = new JTextPane (documentUser3);
			    user3Chat.setPreferredSize (new Dimension (200, 40));
			    user3Chat.setFont (new Font (null, Font.BOLD, 20));
			    user3Chat.setText ("");
			    user3Chat.setBorder (new LineBorder (Color.black, 2));
			    user3Chat.setEditable(false);
			    user3Char = new JLabel (new ImageIcon (usersGame.get (size - 3).get_imagePath ().substring(0, usersGame.get (size - 3).get_imagePath ().length () - 4) + "H.png"));
				user3Char.setPreferredSize (new Dimension (100, 100));
				StyleContext contextUser3Nickname = new StyleContext ();
			    StyledDocument documentUser3Nickname = new DefaultStyledDocument (contextUser3Nickname);
			    Style styleUser3Nickname = contextUser3Nickname.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser3Nickname, StyleConstants.ALIGN_CENTER);
			    user3Nickname = new JTextPane (documentUser3Nickname);
			    user3Nickname.setText(usersGame.get (size - 3).get_nickName () + " SCORE: " + usersGame.get (size - 3).get_score ());
				user3Nickname.setPreferredSize (new Dimension (200, 60));
				user3Nickname.setFont (new Font (null, Font.BOLD, 15));
				user3Panel.add (BorderLayout.NORTH, user3Chat); user3Panel.add (BorderLayout.CENTER, user3Char); user3Panel.add (BorderLayout.SOUTH, user3Nickname);
				westPanel.add (BorderLayout.SOUTH, user3Panel);
			}
			case 2: 
			{
				user2Panel = new JPanel (new BorderLayout ());
				user2Panel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
				StyleContext contextUser2 = new StyleContext ();
			    StyledDocument documentUser2 = new DefaultStyledDocument (contextUser2);
			    Style styleUser2 = contextUser2.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser2, StyleConstants.ALIGN_CENTER);
			    user2Chat = new JTextPane (documentUser2);
			    user2Chat.setPreferredSize (new Dimension (200, 40));
			    user2Chat.setFont (new Font (null, Font.BOLD, 20));
			    user2Chat.setText ("");
			    user2Chat.setBorder (new LineBorder (Color.black, 2));
			    user2Chat.setEditable(false);
			    user2Char = new JLabel (new ImageIcon (usersGame.get (size - 2).get_imagePath ().substring(0, usersGame.get (size - 2).get_imagePath ().length () - 4) + "H.png"));
				user2Char.setPreferredSize (new Dimension (100, 100));
				StyleContext contextUser2Nickname = new StyleContext ();
			    StyledDocument documentUser2Nickname = new DefaultStyledDocument (contextUser2Nickname);
			    Style styleUser2Nickname = contextUser2Nickname.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser2Nickname, StyleConstants.ALIGN_CENTER);
			    user2Nickname = new JTextPane (documentUser2Nickname);
			    user2Nickname.setText(usersGame.get (size - 2).get_nickName () + " SCORE: " + usersGame.get (size - 2).get_score ());
				user2Nickname.setPreferredSize (new Dimension (200, 60));
				user2Nickname.setFont (new Font (null, Font.BOLD, 15));
				user2Panel.add (BorderLayout.NORTH, user2Chat); user2Panel.add (BorderLayout.CENTER, user2Char); user2Panel.add (BorderLayout.SOUTH, user2Nickname);
				westPanel.add (BorderLayout.CENTER, user2Panel);
			}
			case 1: 
			{
				user1Panel = new JPanel (new BorderLayout ());
				user1Panel.setBorder (new LineBorder (new Color (157, 195, 230), 4));
				StyleContext contextUser1 = new StyleContext ();
			    StyledDocument documentUser1 = new DefaultStyledDocument (contextUser1);
			    Style styleUser1 = contextUser1.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser1, StyleConstants.ALIGN_CENTER);
			    user1Chat = new JTextPane (documentUser1);
			    user1Chat.setPreferredSize (new Dimension (200, 40));
			    user1Chat.setFont (new Font (null, Font.BOLD, 20));
			    user1Chat.setText ("");
			    user1Chat.setBorder (new LineBorder (Color.black, 2));
			    user1Chat.setEditable(false);
			    user1Char = new JLabel (new ImageIcon (usersGame.get (size - 1).get_imagePath ().substring(0, usersGame.get (size - 1).get_imagePath ().length () - 4) + "H.png"));
				user1Char.setPreferredSize (new Dimension (100, 100));
				StyleContext contextUser1Nickname = new StyleContext ();
			    StyledDocument documentUser1Nickname = new DefaultStyledDocument (contextUser1Nickname);
			    Style styleUser1Nickname = contextUser1Nickname.getStyle (StyleContext.DEFAULT_STYLE);
			    StyleConstants.setAlignment (styleUser1Nickname, StyleConstants.ALIGN_CENTER);
			    user1Nickname = new JTextPane (documentUser1Nickname);
			    user1Nickname.setText(usersGame.get (size - 1).get_nickName () + " SCORE: " + usersGame.get (size - 1).get_score ());
				user1Nickname.setPreferredSize (new Dimension (200, 60));
				user1Nickname.setFont (new Font (null, Font.BOLD, 15));
				user1Panel.add (BorderLayout.NORTH, user1Chat); user1Panel.add (BorderLayout.CENTER, user1Char); user1Panel.add (BorderLayout.SOUTH, user1Nickname);
				westPanel.add (BorderLayout.NORTH, user1Panel);
			}
		} 
		eastPanel.revalidate ();
		eastPanel.repaint ();
		westPanel.revalidate ();
		westPanel.repaint ();
	}
	
	// INPUT: nickname of target user to update score
	// OUTPUT: null
	// Objective: Update score of user who got correct answer
	public void scoreUpdate (String nickName)
	{
		int score = 0;
		// Update the score of target user
		for (userInfo ui: usersGame)
		{
			if (ui.get_nickName ().equals (nickName))
			{
				ui.inc_score ();
				score = ui.get_score ();
			}
		}
		
		// Update display of score for target user
		if (!(user6Nickname.getText().equals ("")))
		{
			if (user6Nickname.getText ().substring (0, user6Nickname.getText ().length () - 9).equals (nickName))
				user6Nickname.setText (user6Nickname.getText ().substring (0, user6Nickname.getText ().length () - 1).concat(String.valueOf (score)));
		}
		if (!(user5Nickname.getText().equals ("")))
		{
			if (user5Nickname.getText().substring (0, user5Nickname.getText ().length () - 9).equals (nickName))
				user5Nickname.setText (user5Nickname.getText ().substring (0, user5Nickname.getText ().length () - 1).concat(String.valueOf (score)));
		}
		if (!(user4Nickname.getText().equals ("")))
		{
			if (user4Nickname.getText().substring (0, user4Nickname.getText ().length () - 9).equals (nickName))
				user4Nickname.setText (user4Nickname.getText ().substring (0, user4Nickname.getText ().length () - 1).concat(String.valueOf (score)));
		}
		if (!(user3Nickname.getText().equals ("")))
		{
			if (user3Nickname.getText().substring (0, user3Nickname.getText ().length () - 9).equals (nickName))
				user3Nickname.setText (user3Nickname.getText ().substring (0, user3Nickname.getText ().length () - 1).concat(String.valueOf (score)));
		}
		if (!(user2Nickname.getText().equals ("")))
		{
			if (user2Nickname.getText().substring (0, user2Nickname.getText ().length () - 9).equals (nickName))
				user2Nickname.setText (user2Nickname.getText ().substring (0, user2Nickname.getText ().length () - 1).concat(String.valueOf (score)));
		}
		if (!(user1Nickname.getText().equals ("")))
		{
			if (user1Nickname.getText().substring (0, user1Nickname.getText ().length () - 9).equals (nickName))
				user1Nickname.setText (user1Nickname.getText ().substring (0, user1Nickname.getText ().length () - 1).concat(String.valueOf (score)));
		}
	}
	
	// INPUT: list of the users in this game 
	// OUTPUT: null
	// Objective: Invoke updatePanel function to redraw west&east panel
	public void joinApproved (ArrayList<userInfo> usersGame)
	{
		this.usersGame = usersGame;
		System.out.println("f : " + usersGame);
		updatePanel ();
	}
	
	// INPUT: list of the users in this game 
	// OUTPUT: null
	// Objective: Invoke updatePanel function to draw west&east panel at first
	public void createApproved (ArrayList<userInfo> usersGame)
	{
		this.usersGame = usersGame;
		updatePanel ();
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Inform user that unable to start game because the user is not game master
	public void startDeniedMaster ()
	{
		JOptionPane.showMessageDialog (gamePanel.this.f.getContentPane (), "You are not the game master!");
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Inform user that unable to start game because there is not enough player
	public void startDeniedNum ()
	{
		JOptionPane.showMessageDialog (gamePanel.this.f.getContentPane (), "You need at least two players!");
	}
	
	// INPUT: answer of this round, nickname of questioner
	// OUTPUT: null
	// Objective:
	// Start the game. If the client is questioner, set the answer panel with the round answer
	// For all client, set the timer with game time
	public void gameStarted (String roundAnswer, String questioner)
	{
		if (gamePanel.this.f.get_myNickname ().equals (questioner))
		{
			isQuestioner = true;
			answer.setText (roundAnswer);
		}
		else
		{
			isQuestioner = false;
			answer.setText ("ANSWER");
		}
		gameTime = ROUND_TIME;
		timer.setText (String.valueOf (gameTime));
		gameStarted = true;
		
		final JOptionPane optionPane = new JOptionPane("ROUND STARTS!", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		ActionListener action = new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				dialog.dispose ();
			}
		};
		Timer timer = new Timer (1000, action);
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}
	
	// INPUT: list of points drawn by questioner
	// OUTPUT: null
	// Objective: Draw the canvas with point list, selected color
	public void drawBroadcasted (ArrayList<point> pList)
	{
		Graphics g = cv.getGraphics ();
		
		for (point p: pList)
		{
			System.out.println ("(" + p.get_pointX() + ", " + p.get_pointY() + ")");
			g.setColor (drawColor);
			g.fillOval (p.get_pointX (), p.get_pointY (), drawThick, drawThick);
		}
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Clear the canvas
	public void clearBroadcasted ()
	{
		cv.repaint ();
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: Questioner selected eraser. Set the color as white and set the eraser thickness
	public void eraserBroadcasted ()
	{
		set_drawColor (6);
		set_drawThick (25);
	}
	
	// INPUT: index of selected color
	// OUTPUT: null
	// Objective: Questioner selected color. Set the color as selected
	public void colorBroadcasted (int drawingColor)
	{
		set_drawColor (drawingColor);
		set_drawThick (10);
	}
	
	// INPUT: nickname of questioner
	// OUTPUT: null
	// Objective: Set border of the questioner with blue, set with black for others
	public void quetionerBorder (String questioner)
	{
		int size = usersGame.size ();
		switch (size)
		{
			case 6:
			{
				if (usersGame.get (size - 6).get_nickName ().equals (questioner))
				{
					user1Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user2Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user3Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user4Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user5Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user6Panel.setBorder (new LineBorder (Color.black, 4));
				}	
			}
			case 5:
			{
				if (usersGame.get (size - 5).get_nickName ().equals (questioner))
				{
					user1Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user2Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user3Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user4Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user5Panel.setBorder (new LineBorder (Color.black, 4));
					user6Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
				}	
			}
			case 4:
			{
				if (usersGame.get (size - 4).get_nickName ().equals (questioner))
				{
					user1Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user2Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user3Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user4Panel.setBorder (new LineBorder (Color.black, 4));
					user5Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user6Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
				}	
			}
			case 3:
			{
				if (usersGame.get (size - 3).get_nickName ().equals (questioner))
				{
					user1Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user2Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user3Panel.setBorder (new LineBorder (Color.black, 4));
					user4Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user5Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user6Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
				}	
			}
			case 2:
			{
				if (usersGame.get (size - 2).get_nickName ().equals (questioner))
				{
					user1Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user2Panel.setBorder (new LineBorder (Color.black, 4));
					user3Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user4Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user5Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user6Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
				}	
			}
			case 1:
			{
				if (usersGame.get (size - 1).get_nickName ().equals (questioner))
				{
					user1Panel.setBorder (new LineBorder (Color.black, 4));
					user2Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user3Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user4Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user5Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
					user6Panel.setBorder (new LineBorder (new Color (189, 215, 238), 4));
				}	
			}
		}
	}
	
	// INPUT: nickname of the chat's owner, contents of chat
	// OUTPUT: null
	// Objective: Update the chat fields for the players in game
	public void gameChatUpdate (String nickName, String chat)
	{
		if (!(user6Nickname.getText ().equals ("")))
		{
			if (user6Nickname.getText ().substring(0, user6Nickname.getText ().length() - 9).equals (nickName))
				user6Chat.setText (chat);
		}
		if (!(user5Nickname.getText ().equals ("")))
		{
			if (user5Nickname.getText ().substring(0, user5Nickname.getText ().length() - 9).equals (nickName))
				user5Chat.setText (chat);
		}
		if (!(user4Nickname.getText ().equals ("")))
		{
			if (user4Nickname.getText ().substring(0, user4Nickname.getText ().length() - 9).equals (nickName))
				user4Chat.setText (chat);
		}
		if (!(user3Nickname.getText ().equals ("")))
		{
			if (user3Nickname.getText ().substring(0, user3Nickname.getText ().length() - 9).equals (nickName))
				user3Chat.setText (chat);
		}
		if (!(user2Nickname.getText ().equals ("")))
		{
			if (user2Nickname.getText ().substring(0, user2Nickname.getText ().length() - 9).equals (nickName))
				user2Chat.setText (chat);
		}
		if (!(user1Nickname.getText ().equals ("")))
		{
			if (user1Nickname.getText ().substring(0, user1Nickname.getText ().length() - 9).equals (nickName))
				user1Chat.setText (chat);
		}
	}
	
	// INPUT: nickname of the correct chat's owner, contents of answer
	// OUTPUT: null
	// Objective: Inform all the clients the answer and that the chat's owner got correct
	public void correctAnswer (String nickName, String answer)
	{
		gameStarted = false;
		String message = "";
		if (!(user6Nickname.getText().equals("")))
		{
			if (user6Nickname.getText ().substring (0, user6Nickname.getText ().length () - 9).equals (nickName))
				message = new String (user6Nickname.getText().substring (0, user6Nickname.getText ().length () - 9) + " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(user5Nickname.getText().equals("")))
		{
			if (user5Nickname.getText ().substring (0, user5Nickname.getText ().length () - 9).equals (nickName))
				message = new String (user5Nickname.getText().substring (0, user5Nickname.getText ().length () - 9) + " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(user4Nickname.getText().equals("")))
		{
			if (user4Nickname.getText ().substring (0, user4Nickname.getText ().length () - 9).equals (nickName))
				message = new String (user4Nickname.getText().substring (0, user4Nickname.getText ().length () - 9) + " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(user3Nickname.getText().equals("")))
		{
			if (user3Nickname.getText ().substring (0, user3Nickname.getText ().length () - 9).equals (nickName))
				message = new String (user3Nickname.getText().substring (0, user3Nickname.getText ().length () - 9) + " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(user2Nickname.getText().equals("")))
		{
			if (user2Nickname.getText ().substring (0, user2Nickname.getText ().length () - 9).equals (nickName))
				message = new String (user2Nickname.getText().substring (0, user2Nickname.getText ().length () - 9) + " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(user1Nickname.getText().equals("")))
		{
			if (user1Nickname.getText ().substring (0, user1Nickname.getText ().length () - 9).equals (nickName))
				message = new String (user1Nickname.getText().substring (0, user1Nickname.getText ().length () - 9) + " got correct!\n" + "ANSWER: " + answer);
		}
		final JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		ActionListener action = new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				dialog.dispose ();
				if (isQuestioner)
				{
					// Questioner sends the server to notify that the round ended
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.TIMER_EXPIRE);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		};
		Timer timer = new Timer (2000, action);
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}
	
	// INPUT: message noticing that the game ended
	// OUTPUT: null
	// Objective: Redraw all the panel as waiting state (not started)
	public void roundTerminated (String message)
	{
		gameStarted = false;
		final JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		ActionListener action = new ActionListener ()
		{
			public void actionPerformed (ActionEvent e)
			{
				dialog.dispose ();
				updatePanel ();
			}
		};
		Timer timer = new Timer (4000, action);
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective:
	// When the server notifies that 1 second elapsed
	// If game is playing, decrement the in-game timer
	// If in-game timer becomes zero, notice the server that the round ended
	public void timerBroadcasted ()
	{
		if (gameStarted)
		{
			gameTime--;
			timer.setText (String.valueOf (gameTime));
			if (gameTime == 0)
			{
				gameStarted = false;
				if (isQuestioner)
				{
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.TIMER_EXPIRE);
					gamePanel.this.f.sendProtocol (pi);
				}
			}
		}
	}
	
	// Get methods
	public Color get_drawColor () { return drawColor; }
	public int get_drawThick () { return drawThick; }
	
	// Set methods
	public void set_drawColor (int option)
	{
		switch (option)
		{
			case 0:
			{
				drawColor = Color.black;
				break;
			}
			case 1:
			{
				drawColor = Color.red;
				break;
			}
			case 2:
			{
				drawColor = Color.yellow;
				break;
			}
			case 3:
			{
				drawColor = Color.green;
				break;
			}
			case 4:
			{
				drawColor = Color.blue;
				break;
			}
			case 5:
			{
				drawColor = new Color (128, 0, 128);
				break;
			}
			case 6:
			{
				drawColor = Color.white;
				break;
			}
		}
	}
	
	public void set_drawThick (int item) { drawThick = item; }
}