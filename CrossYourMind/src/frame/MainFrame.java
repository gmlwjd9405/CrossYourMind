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
import info.ProgressInfo;
import panel.EntryPanel;
import panel.LobbyPanel;
import panel.GamePanel;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainFrame extends JFrame implements Runnable {
	// ** DEFINE **
	public static final String entryPcard = "entryPanel";
	public static final String lobbyPcard = "lobbyPanel";
	public static final String gamePcard = "gamePanel";
	public static final int entryPwidth = 800;
	public static final int entryPheight = 550;
	public static final int lobbyPwidth = 800;
	public static final int lobbyPheight = 550;
	public static final int gamePwidth = 800;
	public static final int gamePheight = 550;
	public static final String serverIP = "127.0.0.1";
	public static final int serverPort = 3333;

	// ** VARIABLE **
	// For layout
	private CardLayout card;
	private String currentCard;

	// For panels
	private EntryPanel entryP;
	private LobbyPanel lobbyP;
	private GamePanel gameP;

	private Thread thread;

	private Socket s;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private ArrayList<String> entrycharImageList;
	private ArrayList<String> lobbyImageList;
	private ArrayList<String> talkcharImageList;
	private ArrayList<String> gamecharImageList;

	private String myNickname;
	private String myImagePath;

	private Object read;

	// ** CONTRUCTOR **
	public MainFrame() {
		initCharImageList();

		// Initialize layout & panels
		card = new CardLayout();
		entryP = new EntryPanel(this);
		lobbyP = new LobbyPanel(this);
		gameP = new GamePanel(this);
		this.setLayout(card);
		this.getContentPane().add(entryPcard, entryP);
		this.getContentPane().add(lobbyPcard, lobbyP);
		this.getContentPane().add(gamePcard, gameP);
		this.currentCard = entryPcard;
		this.setSize(entryPwidth, entryPheight);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() // For window closing action
		{
			@Override
			public void windowClosing(WindowEvent e) {
				if (currentCard.equals(entryPcard)) {
					System.out.println("entry");
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.EXIT_ENTRY);
					MainFrame.this.sendProtocol(pi);
					MainFrame.this.dispose();
					MainFrame.this.exitGame();
				} else if (currentCard.equals(lobbyPcard)) {
					System.out.println("lobby");
				} else {
					System.out.println("game");
				}
			}
		});
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);

		try {
			// Open connection
			s = new Socket(serverIP, serverPort);
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Start thread to operate
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		card.show(getContentPane(), entryPcard);

		loop: while (true) {
			try {
				read = in.readObject();
				ProgressInfo pi = (ProgressInfo) read;
				switch (pi.get_status()) {
				/* When user's nickname is unavailable */
				case ProgressInfo.USER_DUPLICATE: {
					JOptionPane.showMessageDialog(getContentPane(), "Nickname duplicated.\nTry another one!");
					System.out.println("USER_DUPLICATE");
					break;
				}
				/* When user's nickname is available */
				case ProgressInfo.USER_APPROVE: {
					System.out.println("USER_APPROVE");
					set_myNickname(pi.get_chat());
					set_myImagePath(pi.get_imagePath());
					this.setSize(lobbyPwidth, lobbyPheight);
					this.set_currentCard(lobbyPcard);
					card.show(getContentPane(), lobbyPcard);
					lobbyP.myInfoUpdate();
					break;
				}
				/* When there is new chatting in lobby */
				case ProgressInfo.CHAT_LOBBY_UPDATE: {
					System.out.println("CHAT_LOBBY_UPDATE");
					lobbyP.updateLobbyChat(pi.get_lobbyChat());
					break;
				}
				/* When there is new game created */
				case ProgressInfo.GAME_LOBBY_UPDATE: {
					System.out.println("GAME_LOBBY_UPDATE");
					lobbyP.updateLobbyGame(pi.get_gamesLobby());
					break;
				}
				/*
				 * When there is a user entered lobby / exited lobby / created
				 * game / joined game
				 */
				case ProgressInfo.USER_LOBBY_UPDATE: {
					System.out.println("USER_LOBBY_UPDATE");
					lobbyP.updateLobbyUser(pi.get_usersLobby());
					break;
				}
				/* When creating game succeeds */
				case ProgressInfo.CREATE_GAME_APPROVE: {
					System.out.println("CREATE_GAME_APPROVE");
					lobbyP.createApproved();
					gameP.createApproved(pi.get_usersGame());
					break;
				}
				/* When creating game fails */
				case ProgressInfo.CREATE_GAME_DENIED: {
					System.out.println("CREATE_GAME_DENIED");
					JOptionPane.showMessageDialog(getContentPane(), "Game name duplicated.\nTry another one!");
					break;
				}
				/* When try to join game succeeds: for user trying to join */
				case ProgressInfo.JOIN_GAME_APPROVE: {
					System.out.println("JOIN_GAME_APPROVE");
					lobbyP.joinApproved(pi.get_chat());
					gameP.joinApproved(pi.get_usersGame());
					break;
				}
				/* When new player enters game: for user already in game */
				case ProgressInfo.JOIN_GAME_NEW: {
					System.out.println("JOIN_GAME_NEW");
					gameP.joinApproved(pi.get_usersGame());
					break;
				}
				/* When joining game fails because the game is full */
				case ProgressInfo.JOIN_GAME_DENIED: {
					System.out.println("JOIN_GAME_DENIED");
					lobbyP.joinDenied();
					break;
				}
				/* When a player exited game, update the users in the game */
				case ProgressInfo.USER_GAME_UPDATE: {
					System.out.println("USER_GAME_UPDATE");
					gameP.joinApproved(pi.get_usersGame());
					break;
				}
				/*
				 * When game is started or a round ended, starts game as a
				 * questioner
				 */
				case ProgressInfo.START_APPROVE_QUESTIONER: {
					System.out.println("START_APPROVE_QUESTIONER");
					gameP.clearBroadcasted();
					gameP.gameStarted(pi.get_chat(), pi.get_imagePath());
					gameP.quetionerBorder(pi.get_imagePath());
					break;
				}
				/*
				 * When game is started or a round ended, starts game as a
				 * answerer
				 */
				case ProgressInfo.START_APPROVE_ANSWERER: {
					System.out.println("START_APPROVE_ANSWERER");
					gameP.clearBroadcasted();
					gameP.gameStarted("", pi.get_imagePath());
					gameP.quetionerBorder(pi.get_imagePath());
					break;
				}
				/*
				 * When starting a game fails because user trying to start is
				 * not game master
				 */
				case ProgressInfo.START_DENIED_MASTER: {
					gameP.startDeniedMaster();
					break;
				}
				/*
				 * When starting a game fails because there is not enough player
				 */
				case ProgressInfo.START_DENIED_NUM: {
					gameP.startDeniedNum();
					break;
				}
				/* When questioner draws */
				case ProgressInfo.DRAW_BROADCAST: {
					System.out.println("DRAW_BROADCAST");
					gameP.drawBroadcasted(pi.get_pList());
					break;
				}
				/* When questioner selects clear button */
				case ProgressInfo.SELECT_CLEAR_BROADCAST: {
					System.out.println("SELECT_CLEAR_BROADCAST");
					gameP.clearBroadcasted();
					break;
				}
				/* When questioner selects eraser button */
				case ProgressInfo.SELECT_ERASER_BROADCAST: {
					System.out.println("SELECT_ERASER_BROADCAST");
					gameP.eraserBroadcasted();
					break;
				}
				/* When questioner selects color button */
				case ProgressInfo.SELECT_COLOR_BROADCAST: {
					System.out.println("SELECT_COLOR_BROADCAST");
					gameP.colorBroadcasted(pi.get_drawColor());
					break;
				}
				/* When server's timer elapsed 1 second */
				case ProgressInfo.TIMER_BROADCAST: {
					// System.out.println ("TIMER_BROADCAST");
					gameP.timerBroadcasted();
					break;
				}
				/* When there is a new chat in game */
				case ProgressInfo.CHAT_GAME_UPDATE: {
					System.out.println("CHAT_GAME_UPDATE");
					gameP.gameChatUpdate(pi.get_chat(), pi.get_imagePath());
					break;
				}
				/* When a user got correct by its chat */
				case ProgressInfo.CORRECT_ANSWER: {
					System.out.println("CORRECT_ANSWER");
					gameP.gameChatUpdate(pi.get_chat(), pi.get_imagePath());
					gameP.correctAnswer(pi.get_chat(), pi.get_imagePath());
					gameP.scoreUpdate(pi.get_chat());
					break;
				}
				/* When all the rounds finished for game */
				case ProgressInfo.ROUND_TERMINATE: {
					System.out.println("ROUND_TERMINATE");
					gameP.clearBroadcasted();
					gameP.roundTerminated(pi.get_chat());
					break;
				}
				/*
				 * When user exits game by clicking close button in entry panel
				 */
				case ProgressInfo.EXIT_APPROVE: {
					System.out.println("EXIT_APPROVE");
					exitGame();
					break;
				}
				default: {
					break;
				}
				}
			}
			// For debugging
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.out.println(String.valueOf(read));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ** METHOD **
	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize the path of image resources
	private void initCharImageList() {
		entrycharImageList = new ArrayList<String>();
		lobbyImageList = new ArrayList<String>();
		talkcharImageList = new ArrayList<String>();
		gamecharImageList = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			entrycharImageList.add("src/images/CHAR" + i + ".png");
			lobbyImageList.add("src/images/CHAR" + i + "L.png");
			talkcharImageList.add("src/images/CHAR" + i + "T.png");
			gamecharImageList.add("src/images/CHAR" + i + "H.png");
		}
	}

	// INPUT: progressInfo to send from this client to the server
	// OUTPUT: null
	// Objective: Send object using connection
	public void sendProtocol(ProgressInfo pi) {
		try {
			out.writeObject(pi);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// INPUT: null
	// OUTPUT: current card layout object
	// Objective: Access the layout
	public CardLayout get_card() {
		return card;
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: This client to exit the game in entry panel and disconnect
	// from the server
	public void exitGame() {
		try {
			in.close();
			out.close();
			s.close();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Get methods */
	public String get_myNickname () { return myNickname; }
	public String get_myImagePath () { return myImagePath; }
	public String get_currentCard () { return currentCard; }
	// INPUT: null
	// OUTPUT: the list of path of image resources
	// Objective: Access the image resource
	public ArrayList<String> getCharImageList() {return entrycharImageList;	}
	public ArrayList<String> getLobbyCharImageList() {return lobbyImageList;}
	public ArrayList<String> getTalkCharImageList() {return talkcharImageList;}
	public ArrayList<String> getGameCharImageList() {return gamecharImageList;}
	
	/* Set methods */
	public void set_myNickname (String item) { myNickname = item; }
	public void set_myImagePath (String item) { myImagePath = item; }
	public void set_currentCard (String item) { currentCard = item; }
	
	
	// ** MAIN & RUN **
	public static void main(String[] args) {
		new MainFrame();
	}
}