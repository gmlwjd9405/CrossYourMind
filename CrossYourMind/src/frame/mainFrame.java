package frame;

import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

import java.io.*;

import server.ALPHAserver;
import info.progressInfo;
import panel.entryPanel;
import panel.lobbyPanel;
import panel.gamePanel;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class mainFrame extends JFrame implements Runnable
{
// ** DEFINE **
	public static final String entryPcard = "entryPanel";
	public static final String lobbyPcard = "lobbyPanel";
	public static final String gamePcard = "gamePanel";
	public static final int entryPwidth = 1200;
	public static final int entryPheight = 600;
	public static final int lobbyPwidth = 1200;
	public static final int lobbyPheight = 900;
	public static final int gamePwidth = 1200;
	public static final int gamePheight = 900;
	public static final String serverIP = "127.0.0.1";
	public static final int serverPort = 3333;
	
// ** VARIABLE **
	// For layout
	CardLayout card;
	String currentCard;
	// For panels
	entryPanel entryP;
	lobbyPanel lobbyP;
	gamePanel gameP;
	
	Thread thread;
	
	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	
	ArrayList<String> charImageList;
	
	String myNickname;
	String myImagePath;
	
	Object read;

// ** CONTRUCTOR **	
	public mainFrame ()
	{
		initCharImageList ();
		
		// Initialize layout & panels
		card = new CardLayout ();
		entryP = new entryPanel (this);
		lobbyP = new lobbyPanel (this);
		gameP = new gamePanel (this);
		this.setLayout (card);
		this.getContentPane ().add (entryPcard, entryP);
		this.getContentPane ().add (lobbyPcard, lobbyP);		
		this.getContentPane ().add (gamePcard, gameP);
		this.currentCard = entryPcard;
		this.setSize (entryPwidth, entryPheight);
		this.setResizable (false);
		this.addWindowListener (new WindowAdapter () // For window closing action
		{
			@Override
			public void windowClosing (WindowEvent e)
			{
				if (currentCard.equals (entryPcard))
				{
					System.out.println ("entry");
					progressInfo pi = new progressInfo ();
					pi.set_status (progressInfo.EXIT_ENTRY);
					mainFrame.this.sendProtocol (pi);
					mainFrame.this.dispose ();
					mainFrame.this.exitGame ();
				}
				else if (currentCard.equals (lobbyPcard))
				{
					System.out.println ("lobby");
				}
				else
				{
					System.out.println ("game");
				}
			}
		});
		this.setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible (true);
    	
    	try
    	{
    		// Open connection
    		s = new Socket(serverIP, serverPort);
    		out = new ObjectOutputStream(s.getOutputStream());
    		in = new ObjectInputStream(s.getInputStream());
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	// Start thread to operate
    	thread = new Thread (this);
    	thread.start ();
	}

// ** MAIN & RUN **
	public static void main(String[] args)
	{
		new mainFrame ();
	}
	
	@Override
	public void run ()
	{
		card.show (getContentPane (), entryPcard);
		loop: while (true)
		{
			try
			{
				read = in.readObject ();
				progressInfo pi = (progressInfo) read;
				switch (pi.get_status ())
				{
					case progressInfo.USER_DUPLICATE: // When user's nickname is unavailable
					{
						JOptionPane.showMessageDialog (getContentPane (), "Nickname duplicated.\nTry another one!");
						System.out.println("USER_DUPLICATE");
						break;
					}
					case progressInfo.USER_APPROVE: // When user's nickname is available
					{
						System.out.println("USER_APPROVE");
						set_myNickname (pi.get_chat ());
						set_myImagePath (pi.get_imagePath ());
						this.setSize (lobbyPwidth, lobbyPheight);
						this.set_currentCard (lobbyPcard);
						card.show (getContentPane (), lobbyPcard);
						lobbyP.myInfoUpdate ();
						break;
					}
					case progressInfo.CHAT_LOBBY_UPDATE: // When there is new chatting in lobby
					{
						System.out.println("CHAT_LOBBY_UPDATE");
						lobbyP.updateLobbyChat (pi.get_lobbyChat ());
						break;
					}
					case progressInfo.GAME_LOBBY_UPDATE: // When there is new game created
					{
						System.out.println ("GAME_LOBBY_UPDATE");
						lobbyP.updateLobbyGame (pi.get_gamesLobby());
						break;
					}
					case progressInfo.USER_LOBBY_UPDATE: // When there is a user entered lobby / exited lobby / created game / joined game 
					{
						System.out.println ("USER_LOBBY_UPDATE");
						lobbyP.updateLobbyUser (pi.get_usersLobby());
						break;
					}
					case progressInfo.CREATE_GAME_APPROVE: // When creating game succeeds
					{
						System.out.println ("CREATE_GAME_APPROVE");
						lobbyP.createApproved ();
						gameP.createApproved (pi.get_usersGame ());
						break;
					}
					case progressInfo.CREATE_GAME_DENIED: // When creating game fails
					{
						System.out.println ("CREATE_GAME_DENIED");
						JOptionPane.showMessageDialog (getContentPane (), "Game name duplicated.\nTry another one!");
						break;
					}
					case progressInfo.JOIN_GAME_APPROVE: // When try to join game succeeds: for user trying to join
					{
						System.out.println ("JOIN_GAME_APPROVE");
						lobbyP.joinApproved (pi.get_chat ());
						gameP.joinApproved (pi.get_usersGame ());
						break;
					}
					case progressInfo.JOIN_GAME_NEW: // When new player enters game: for user already in game
					{
						System.out.println ("JOIN_GAME_NEW");
						gameP.joinApproved (pi.get_usersGame ());
						break;
					}
					case progressInfo.JOIN_GAME_DENIED: // When joining game fails because the game is full 
					{
						System.out.println ("JOIN_GAME_DENIED");
						lobbyP.joinDenied ();
						break;
					}
					case progressInfo.USER_GAME_UPDATE: // When a player exited game, update the users in the game
					{
						System.out.println ("USER_GAME_UPDATE");
						gameP.joinApproved(pi.get_usersGame ());
						break;
					}
					case progressInfo.START_APPROVE_QUESTIONER: // When game is started or a round ended, starts game as a questioner
					{
						System.out.println ("START_APPROVE_QUESTIONER");
						gameP.clearBroadcasted ();
						gameP.gameStarted (pi.get_chat (), pi.get_imagePath ());
						gameP.quetionerBorder (pi.get_imagePath ());
						break;
					}
					case progressInfo.START_APPROVE_ANSWERER: // When game is started or a round ended, starts game as a answerer
					{
						System.out.println ("START_APPROVE_ANSWERER");
						gameP.clearBroadcasted ();
						gameP.gameStarted ("", pi.get_imagePath ());
						gameP.quetionerBorder (pi.get_imagePath ());
						break;
					}
					case progressInfo.START_DENIED_MASTER: // When starting a game fails because user trying to start is not game master
					{
						gameP.startDeniedMaster ();
						break;
					}
					case progressInfo.START_DENIED_NUM: // When starting a game fails because there is not enough player 
					{
						gameP.startDeniedNum ();
						break;
					}
					case progressInfo.DRAW_BROADCAST: // When questioner draws
					{
						System.out.println ("DRAW_BROADCAST");
						gameP.drawBroadcasted (pi.get_pList ());
						break;
					}
					case progressInfo.SELECT_CLEAR_BROADCAST: // When questioner selects clear button
					{
						System.out.println ("SELECT_CLEAR_BROADCAST");
						gameP.clearBroadcasted ();
						break;
					}
					case progressInfo.SELECT_ERASER_BROADCAST: // When questioner selects eraser button
					{
						System.out.println ("SELECT_ERASER_BROADCAST");
						gameP.eraserBroadcasted ();
						break;
					}
					case progressInfo.SELECT_COLOR_BROADCAST: // When questioner selects color button
					{
						System.out.println ("SELECT_COLOR_BROADCAST");
						gameP.colorBroadcasted (pi.get_drawColor ());
						break;
					}
					case progressInfo.TIMER_BROADCAST: // When server's timer elapsed 1 second
					{
						//System.out.println ("TIMER_BROADCAST");
						gameP.timerBroadcasted ();
						break;
					}
					case progressInfo.CHAT_GAME_UPDATE: // When there is a new chat in game
					{
						System.out.println ("CHAT_GAME_UPDATE");
						gameP.gameChatUpdate(pi.get_chat (), pi.get_imagePath ());
						break;
					}
					case progressInfo.CORRECT_ANSWER: // When a user got correct by its chat
					{
						System.out.println ("CORRECT_ANSWER");
						gameP.gameChatUpdate (pi.get_chat (), pi.get_imagePath ());
						gameP.correctAnswer (pi.get_chat (), pi.get_imagePath ());
						gameP.scoreUpdate (pi.get_chat ());
						break;
					}
					case progressInfo.ROUND_TERMINATE: // When all the rounds finished for game
					{
						System.out.println ("ROUND_TERMINATE");
						gameP.clearBroadcasted ();
						gameP.roundTerminated (pi.get_chat ());
						break;
					}
					case progressInfo.EXIT_APPROVE: // When user exits game by clicking close button in entry panel
					{
						System.out.println ("EXIT_APPROVE");
						exitGame ();
						break;
					}
					default:
					{
						break;
					}
				}
			}
			// For debugging
			catch (ClassNotFoundException e)
			{
				e.printStackTrace ();
			}
			catch (ClassCastException e)
			{
				System.out.println (String.valueOf (read));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

// ** METHOD **
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize the path of image resources
	private void initCharImageList ()
	{
		charImageList = new ArrayList<String> ();
		for (int i = 0; i < 8; i++)
		{
			charImageList.add ("src/images/CHAR" + i + ".png");
		}
	}
	
	// INPUT: progressInfo to send from this client to the server
	// OUTPUT: null
	// Objective: Send object using connection
	public void sendProtocol (progressInfo pi)
	{
		try
		{
			out.writeObject(pi);
			out.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// INPUT: null
	// OUTPUT: current card layout object
	// Objective: Access the layout
	public CardLayout get_card ()
	{
		return card;
	}
	
	// INPUT: null
	// OUTPUT: the list of path of image resources
	// Objective: Access the image resource
	public ArrayList<String> getCharImageList ()
	{
		return charImageList;
	}
	
	// INPUT: null
	// OUTPUT: null
	// Objective: This client to exit the game in entry panel and disconnect from the server
	public void exitGame ()
	{
		try
		{
			in.close ();
			out.close ();
			s.close ();
			System.exit (0);
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
	}
	
	// Get methods
	public String get_myNickname () { return myNickname; }
	public String get_myImagePath () { return myImagePath; }
	public String get_currentCard () { return currentCard; }
	// Set methods
	public void set_myNickname (String item) { myNickname = item; }
	public void set_myImagePath (String item) { myImagePath = item; }
	public void set_currentCard (String item) { currentCard = item; }
}