package frame;

import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
	public static final String ImagePath = "src/images/";
	public static final String entryPcard = "entryPanel";
	public static final String lobbyPcard = "lobbyPanel";
	public static final String gamePcard = "gamePanel";
	public static final int entryPwidth = 800;
	public static final int entryPheight = 560;
	public static final int lobbyPwidth = 800;
	public static final int lobbyPheight = 560;
	public static final int gamePwidth = 800;
	public static final int gamePheight = 560;
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

	// For charImages
	private ArrayList<String> entrycharImageList;
	private ArrayList<String> entryEnteredcharImageList;
	private ArrayList<String> lobbyImageList;
	private ArrayList<String> talkcharImageList;
	private ArrayList<String> gamecharImageList;

	private String myNickname;
	private String myLobbyImagePath; // seleced charNum
	private int myLevel;
	private String myCharName;

	private Object read;

	// ** CONTRUCTOR **
	public MainFrame() {
		initCharImageList();
		this.setTitle("CrossYourMind");

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
					ProgressInfo progressInfo = new ProgressInfo();
					progressInfo.set_status(ProgressInfo.EXIT_ENTRY);
					MainFrame.this.sendProtocol(progressInfo);
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
				ProgressInfo progressInfo = (ProgressInfo) read;
				switch (progressInfo.get_status()) {
				/* When user's nickname is unavailable */
				case ProgressInfo.USER_DUPLICATE: {
					JOptionPane.showMessageDialog(getContentPane(), "Nickname duplicated.\nTry another one!");
					System.out.println("USER_DUPLICATE");
					break;
				}
				/* When user's nickname is available */
				case ProgressInfo.USER_APPROVE: {
					System.out.println("USER_APPROVE");
					System.out.println("----------------");
					System.out.println(progressInfo.getNickName());
					System.out.println(progressInfo.getCharName());
					System.out.println(progressInfo.getLevel());
					System.out.println("----------------");
					set_myNickname(progressInfo.getNickName());
					set_myLobbyImagePath(progressInfo.get_lobbyImagePath());
					set_myCharName(progressInfo.getCharName());
					set_myLevel(progressInfo.getLevel());

					this.setSize(lobbyPwidth, lobbyPheight);
					this.set_currentCard(lobbyPcard);
					card.show(getContentPane(), lobbyPcard);
					lobbyP.myInfoUpdate();
					break;
				}
				/* When there is new chatting in lobby */
				case ProgressInfo.CHAT_LOBBY_UPDATE: {
					System.out.println("CHAT_LOBBY_UPDATE");
					lobbyP.updateLobbyChat(progressInfo.get_lobbyChat());
					break;
				}
				/* When there is new game created */
				case ProgressInfo.GAME_LOBBY_UPDATE: {
					System.out.println("GAME_LOBBY_UPDATE");
					lobbyP.updateLobbyGame(progressInfo.get_gamesLobby());
					break;
				}
				/*
				 * When there is a user entered lobby / exited lobby / created
				 * game / joined game
				 */
				case ProgressInfo.USER_LOBBY_UPDATE: {
					System.out.println("USER_LOBBY_UPDATE");
					lobbyP.updateLobbyUser(progressInfo.get_usersLobby());
					break;
				}
				/* When creating game succeeds */
				case ProgressInfo.CREATE_GAME_APPROVE: {
					System.out.println("CREATE_GAME_APPROVE");
					lobbyP.createApproved();
					gameP.createApproved(progressInfo.get_usersGame());
					break;
				}
				/* When creating game fails */
				case ProgressInfo.CREATE_GAME_DENIED: {
					System.out.println("CREATE_GAME_DENIED");
					JOptionPane.showMessageDialog(getContentPane(), "Game name duplicated.\nTry another one!");
					break;
				}
				/*
				 * When try to join game succeeds: for user trying to join
				 */
				case ProgressInfo.JOIN_GAME_APPROVE: {
					System.out.println("JOIN_GAME_APPROVE");
					lobbyP.joinApproved(progressInfo.get_chat());
					gameP.joinApproved(progressInfo.get_usersGame());
					break;
				}
				/* When new player enters game: for user already in game */
				case ProgressInfo.JOIN_GAME_NEW: {
					System.out.println("JOIN_GAME_NEW");
					gameP.joinApproved(progressInfo.get_usersGame());
					break;
				}
				/* When joining game fails because the game is full */
				case ProgressInfo.JOIN_GAME_DENIED: {
					System.out.println("JOIN_GAME_DENIED");
					lobbyP.joinDenied();
					break;
				}
				/*
				 * When a player exited game, update the users in the game
				 */
				case ProgressInfo.USER_GAME_UPDATE: {
					System.out.println("USER_GAME_UPDATE");
					gameP.joinApproved(progressInfo.get_usersGame());
					break;
				}
				/*
				 * When game is started or a round ended, starts game as a
				 * questioner
				 */
				case ProgressInfo.START_APPROVE_QUESTIONER: {
					System.out.println("START_APPROVE_QUESTIONER");
					gameP.clearBroadcasted();
					// heeee
					// gameP.gameStarted(progressInfo.get_chat(),
					// progressInfo.get_imagePath());
					gameP.gameStarted(progressInfo.get_chat(), progressInfo.getNickName());
					gameP.quetionerBorder(progressInfo.getNickName());
					break;
				}
				/*
				 * When game is started or a round ended, starts game as a
				 * answerer
				 */
				case ProgressInfo.START_APPROVE_ANSWERER: {
					System.out.println("START_APPROVE_ANSWERER");
					gameP.clearBroadcasted();
					gameP.gameStarted("", progressInfo.getNickName());
					gameP.quetionerBorder(progressInfo.getNickName());
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
					gameP.drawBroadcasted(progressInfo.get_pList());
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
					gameP.colorBroadcasted(progressInfo.get_drawColor());
					break;
				}
				/* When server's timer elapsed 1 second */
				case ProgressInfo.TIMER_BROADCAST: {
					gameP.timerBroadcasted();
					break;
				}
				/* When there is a new chat in game */
				case ProgressInfo.CHAT_GAME_UPDATE: {
					System.out.println("CHAT_GAME_UPDATE");
					gameP.gameChatUpdate(progressInfo.get_chat(), progressInfo.get_chattingSentence());
					break;
				}
				/* When a user got correct by its chat */
				case ProgressInfo.CORRECT_ANSWER: {
					System.out.println("CORRECT_ANSWER");
					gameP.gameChatUpdate(progressInfo.get_chat(), progressInfo.get_chattingSentence());
					// heee 수정 필요!!
					// gameP.correctAnswer(progressInfo.get_chat(),
					// progressInfo.get_imagePath());
					gameP.correctAnswer(progressInfo.get_chat(), progressInfo.get_chattingSentence());
					gameP.scoreUpdate(progressInfo.get_chat());
					break;
				}
				/* When all the rounds finished for game */
				case ProgressInfo.ROUND_TERMINATE: {
					System.out.println("ROUND_TERMINATE");
					gameP.clearBroadcasted();
					gameP.roundTerminated(progressInfo.get_chat());
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
	/**
	 * INPUT: null, OUTPUT: null, Objective: Initialize the path of image
	 * resources
	 */
	private void initCharImageList() {
		entrycharImageList = new ArrayList<String>();
		entryEnteredcharImageList = new ArrayList<String>();
		lobbyImageList = new ArrayList<String>();
		talkcharImageList = new ArrayList<String>();
		gamecharImageList = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			entrycharImageList.add(ImagePath + "Char" + i + ".png");
			entryEnteredcharImageList.add(ImagePath + "Char" + i + "E.png");
			lobbyImageList.add(ImagePath + "Char" + i + "L.png");
			talkcharImageList.add(ImagePath + "Char" + i + "T.png");
			gamecharImageList.add(ImagePath + "Char" + i + "H.png");
		}
	}

	/**
	 * INPUT: progressInfo to send from this client to the server, OUTPUT: null,
	 * Objective: Send object using connection
	 */
	public void sendProtocol(ProgressInfo pi) {
		try {
			out.writeObject(pi);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * INPUT: null, OUTPUT: current card layout object, Objective: Access the
	 * layout
	 */
	public CardLayout get_card() {
		return card;
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: This client to exit the game in
	 * entry panel and disconnect from the server
	 */
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
	public String get_myNickname() {
		return myNickname;
	}

	public String get_myLobbyImagePath() {
		return myLobbyImagePath;
	}

	public int get_myLevel() {
		return myLevel;
	}

	public String get_myCharName() {
		return myCharName;
	}

	public String get_currentCard() {
		return currentCard;
	}

	/**
	 * INPUT: null, OUTPUT: the list of path of image resources, Objective:
	 * Access the image resource
	 */
	public ArrayList<String> getCharImageList() {
		return entrycharImageList;
	}

	public ArrayList<String> getCharEnteredImageList() {
		return entryEnteredcharImageList;
	}

	/* Set methods */
	public void set_myLobbyImagePath(String item) {
		myLobbyImagePath = item;
	}

	public void set_myNickname(String item) {

		this.myNickname = item;
		System.out.println("<MainFrame> set_myNickname: " + this.myNickname);
	}

	public void set_myLevel(int item) {
		this.myLevel = item;
		System.out.println("<MainFrame> set_myLevel: " + this.myLevel);
	}

	public void set_myCharName(String item) {
		this.myCharName = item;
		System.out.println("<MainFrame> set_myCharName: " + this.myCharName);
	}

	public void set_currentCard(String item) {
		currentCard = item;
	}

	// ** MAIN & RUN **
	public static void main(String[] args) {
		new MainFrame();
	}
}