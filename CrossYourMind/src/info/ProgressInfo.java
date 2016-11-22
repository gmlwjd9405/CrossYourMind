package info;

import java.io.Serializable;
import java.awt.Color;
import java.util.ArrayList;

import drawing.UserPoint;

public class ProgressInfo implements Serializable
{
// ** STATUS **
	public static final String FONT = "12롯데마트드림Light";
	
	// in entryPanel
	public static final int IN_ENTRY = 100;
	public static final int USER_ENTRY = 101;
	public static final int USER_DENIED = 102;
	public static final int EXIT_ENTRY = 103;
	public static final int USER_ACCEPT = 104;
	public static final int USER_DUPLICATE = 105;
	public static final int USER_APPROVE = 106;
	public static final int EXIT_APPROVE = 107;
	
	// in lobbyPanel 
	public static final int IN_LOBBY = 200;
	public static final int CREATE_GAME_TRY = 201;
	public static final int CREATE_GAME_APPROVE = 2010;
	public static final int CREATE_GAME_DENIED = 2011;
	public static final int JOIN_GAME_TRY = 202;
	public static final int JOIN_GAME_APPROVE = 2020;
	public static final int JOIN_GAME_DENIED = 2021;
	public static final int JOIN_GAME_NEW = 2022;
	public static final int JOIN_GAME = 203;
	public static final int GAME_LOBBY_UPDATE = 204;
	public static final int USER_LOBBY_UPDATE = 205;
	public static final int CHAT_LOBBY = 206;
	public static final int CHAT_LOBBY_UPDATE = 207;
	public static final int EXIT_LOBBY = 208;
	
	// in gamePanel
	public static final int USER_GAME_UPDATE = 300;
	public static final int EXIT_GAME = 303;
	public static final int START_TRY = 304;
	public static final int START_APPROVE_QUESTIONER = 3050;
	public static final int START_APPROVE_ANSWERER = 3051;
	public static final int START_DENIED_MASTER = 3060;
	public static final int START_DENIED_NUM = 3061;
	public static final int TIMER_BROADCAST = 307;
	
	// for drawing
	public static final int DRAW = 400;
	public static final int DRAW_BROADCAST = 4001;
	public static final int SELECT_COLOR = 401;
	public static final int SELECT_COLOR_BROADCAST = 4011;
	public static final int SELECT_ERASER = 403;
	public static final int SELECT_ERASER_BROADCAST = 4031;
	public static final int SELECT_CLEAR = 404;
	public static final int SELECT_CLEAR_BROADCAST = 4041;
	public static final int TIMER_EXPIRE = 405;
	public static final int TIMER_EXPIRE_BROADCAST = 4051;
	public static final int CHAT_GAME = 406;
	public static final int CORRECT_ANSWER = 407;
	public static final int CHAT_GAME_UPDATE = 408;
	public static final int ROUND_TERMINATE = 409;

// ** VARIABLE **	
	// for general purpose
	private int status;
	private String chat; // typed chatting (lobby or game)
	private String imagePath;
	private String lobbyImagePath;
	private int selectImageNum;
	//private String proFileImagePath;
	private String chatImagePath;
	private String gameCharImagePath;
	
	
	// for lobby
	private ArrayList<String> usersLobby;
	private ArrayList<String> gamesLobby;
	private String lobbyChat;
	
	// for game
	private ArrayList<UserInfo> usersGame;
	private String[][] chatsGame; // double pointer for each user
	private int roundTime;
	
	// for drawing
	private int drawColor;
	private ArrayList<UserPoint> pList;
	
	public ProgressInfo ()
	{ 
		status = 0;
		chat = "";
		imagePath = "";
	}

	public String seperateImagePath(String imagePath) {
		// seperate imageFilename and .png
		System.out.println("<ProgressInfo> imageFilename " + imagePath);
		String frontImagePath = imagePath.substring(0, imagePath.length()-4);
		System.out.println("<ProgressInfo> frontImagePath " + frontImagePath);
		return frontImagePath;
	}
	
// ** GET METHOD **
	public int get_status () { return status; }
	public String get_chat () { return chat; }
	public String get_imagePath () { return imagePath; }
	public String get_lobbyImagePath() { return lobbyImagePath; }
	public String get_chatImagePath () { return chatImagePath; }
	public String get_gameCharImagePath () { return gameCharImagePath; }
	public ArrayList<String> get_usersLobby () { return usersLobby; }
	public ArrayList<String> get_gamesLobby () { return gamesLobby; }
	public String get_lobbyChat () { return lobbyChat; }
	public ArrayList<UserInfo> get_usersGame () { return usersGame; }
	public String[][] get_chatsGame () { return chatsGame; }
	public int get_roundTime () { return roundTime; }
	public int get_drawColor () { return drawColor; }
	//public int get_drawingThickness () { return drawingThickness; }
	public ArrayList<UserPoint> get_pList () { return pList; }
	public int get_selectImageNum () { return selectImageNum; }
// ** SET METHOD **
	public void set_status (int item) { status = item; }
	public void set_chat (String item) { chat = item; }
	public void set_imagePath(String item) {
		imagePath = item;
		String frontImagePath = seperateImagePath(item);
		lobbyImagePath = frontImagePath + "L.png";
		//proFileImagePath;
		chatImagePath = frontImagePath + "T.png";;
		gameCharImagePath = frontImagePath + "H.png";;
	}
	public void set_usersLobby (ArrayList<String> item) { usersLobby = item; }
	public void set_gamesLobby (ArrayList<String> item) { gamesLobby = item; }
	public void set_lobbyChat (String item) { lobbyChat = item; }
	public void set_usersGame (ArrayList<UserInfo> item) { usersGame = item; }
	public void set_chatsGame (String[][] item) { chatsGame = item; }
	public void set_roundTime (int item) { roundTime = item; }
	public void set_drawColor (int item) { drawColor = item; }
	//public void set_drawingThickness (int item) { drawingThickness = item; }
	public void set_pList (ArrayList<UserPoint> item) { pList = item; }
	public void set_selectImageNum (int item) { selectImageNum = item; }
}