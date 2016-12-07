package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import info.ProgressInfo;
import info.UserInfo;

public class ClientManager extends Thread {
	// ** VARIABLE **
	// Connects to the common server
	Server server;
	// For connection
	Socket s;
	ObjectOutputStream out;
	boolean outLock;
	ObjectInputStream in;
	UserInfo userInfo;

	// ** CONSTRUCTOR **
	public ClientManager(Socket s, Server server) {
		this.s = s;
		this.server = server;
		userInfo = new UserInfo();

		try {
			out = new ObjectOutputStream(s.getOutputStream());
			outLock = true;
			in = new ObjectInputStream(s.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ** METHOD **
	@Override
	public void run() {
		try {
			loop: while (true) {
				ProgressInfo pi = (ProgressInfo) in.readObject();

				switch (pi.get_status()) {
				// When user tries to enter the game
				case ProgressInfo.USER_ACCEPT: {
					// heee
					// String s = pi.get_chat();
					String nickName = pi.getNickName();

					if (server.checkDuplicateUser(nickName)) { /* duplicate */
						ProgressInfo pi_ack = new ProgressInfo();
						// The typed nickname is already in use
						pi_ack.set_status(ProgressInfo.USER_DUPLICATE);
						lockedWrite(pi_ack);
					} else { /* accept */
						userInfo.set_nickName(nickName);
						userInfo.setSelectImageNum(pi.get_selectImageNum());
						userInfo.set_level(pi.getLevel());
						userInfo.set_imagePath(pi.get_imagePath());
						userInfo.set_status(UserInfo.IN_LOBBY);

						server.lobbyGameAllUpdate();
						server.lobbyUserAllUpdate();

						ProgressInfo pi_ack = new ProgressInfo();
						pi_ack.set_status(ProgressInfo.USER_APPROVE);
						// heee
						pi_ack.setNickName(pi.getNickName()); // 닉네임 세팅
						pi_ack.set_selectImageNum(pi.get_selectImageNum()); // 캐릭터 이름 세팅
						pi_ack.setLevel(pi.getLevel()); // 레벨 세팅
						pi_ack.set_imagePath(pi.get_imagePath());
						System.out.println("<ServerClient> call progressInfo SetImagePath");

						lockedWrite(pi_ack);
						server.printUsers();
					}
					break;
				}
				case ProgressInfo.CHAT_LOBBY: // When user types chat in lobby
				{
					System.out.println("<ClientManager> CHAT_LOBBY get_chat: " + pi.get_chat());
					String s = pi.get_chat();
					server.set_RLC(userInfo.get_nickName() + ": " + s);
					server.lobbyChatUpdateAll();
					break;
				}
				case ProgressInfo.EXIT_LOBBY: // When user exits lobby
				{
					userInfo.set_nickName("");
					server.lobbyUserAllUpdate();
					server.printUsers();
					break;
				}
				case ProgressInfo.CREATE_GAME_TRY: // When user tries to create
													// a game
				{
					String s = pi.get_chat();
					if (server.checkDuplicateGame(s)) {
						System.out.println("CREATE_GAME_DENIED");
						ProgressInfo pi_ack = new ProgressInfo();
						pi_ack.set_status(ProgressInfo.CREATE_GAME_DENIED);
						lockedWrite(pi_ack);
					} else {
						System.out.println("CREATE_GAME_APPROVE");
						//heeee
						//server.newGame(pi.get_chat());
						server.newGame(pi.get_RoomName());
						server.lobbyGameAllUpdate();
						userInfo.set_status(UserInfo.IN_GAME);
						//heeee
						//userInfo.set_gameName(pi.get_chat());
						userInfo.set_gameName(pi.get_RoomName());
						userInfo.set_isMaster(true);
						ProgressInfo pi_ack = new ProgressInfo();
						pi_ack.set_status(ProgressInfo.CREATE_GAME_APPROVE);
						//heee
						//pi_ack.set_usersGame(server.getUsersGame(pi.get_chat()));
						System.out.println("<ClientManager> CREATE_GAME_APPROVE get_RoomName: " + pi.get_RoomName());
						pi_ack.set_usersGame(server.getUsersGame(pi.get_RoomName()));
						lockedWrite(pi_ack);
						server.lobbyUserAllUpdate();
					}
					break;
				}
				case ProgressInfo.JOIN_GAME_TRY: // When user tries to join a
													// game
				{
					System.out.println("JOIN_GAME_TRY");
					if (server.checkFull(pi.get_RoomName())) {
						ProgressInfo pi_ack = new ProgressInfo();
						pi_ack.set_status(ProgressInfo.JOIN_GAME_DENIED);
						lockedWrite(pi_ack);
					} else {
						userInfo.set_status(UserInfo.IN_GAME);
						userInfo.set_isMaster(false);
						userInfo.set_gameName(pi.get_RoomName());
						ProgressInfo pi_ack = new ProgressInfo();
						pi_ack.set_status(ProgressInfo.JOIN_GAME_APPROVE);
						//heeee
						System.out.println("???<ClientManager> JOIN_GAME_TRY set_RoomName: " + pi.get_RoomName());
						//pi_ack.set_chat(pi.get_chat());
						pi_ack.set_RoomName(pi.get_RoomName());
						//pi_ack.set_usersGame(server.getUsersGame(pi.get_chat()));
						pi_ack.set_usersGame(server.getUsersGame(pi.get_RoomName()));
						lockedWrite(pi_ack);
					}
					break;
				}
				case ProgressInfo.JOIN_GAME: // When user succeeds to join a game
				{
					System.out.println("JOIN_GAME [ClientManager]");
					server.userJoinGame(userInfo.get_nickName(), userInfo.get_gameName());
					server.lobbyUserAllUpdate();
					break;
				}
				case ProgressInfo.EXIT_GAME: // When user exits the game
				{
					System.out.println("EXIT_GAME [ClientManager]");
					String gameName = userInfo.get_gameName();
					userInfo.set_gameName("");
					userInfo.set_status(UserInfo.IN_LOBBY);
					server.userExitGame(gameName);
					server.lobbyUserAllUpdate();
					server.gameUserAllUpdate(gameName);
					server.printGi();

					break;
				}
				case ProgressInfo.START_TRY: // When users tries to start a game
				{
					System.out.println("START_TRY");
					pi = new ProgressInfo();

					if (userInfo.get_isMaster()) {
						if (server.startAvailable(userInfo.get_gameName()))
							server.startGameAll(userInfo.get_gameName());
						else
							pi.set_status(ProgressInfo.START_DENIED_NUM);
					} else
						pi.set_status(ProgressInfo.START_DENIED_MASTER);
					lockedWrite(pi);
					break;
				}
				case ProgressInfo.DRAW: // When questioner draws to canvas
				{
					System.out.println("DRAW");
					System.out.println("userInfo.get_gameName(): " + userInfo.get_gameName());
					server.drawBroadcast(userInfo.get_gameName(), pi.get_pList());
					break;
				}
				case ProgressInfo.SELECT_CLEAR: // When user selects clear
												// button
				{
					server.clearBroadcast(userInfo.get_gameName());
					break;
				}
				case ProgressInfo.SELECT_ERASER: // When user selects eraser
													// button
				{
					server.eraserBroadcast(userInfo.get_gameName());
					break;
				}
				case ProgressInfo.SELECT_COLOR: // When user selects color
												// button
				{
					System.out.println("<ClientManager> SELECT_COLOR nickname: " + userInfo.get_gameName());
					server.colorBroadcast(userInfo.get_gameName(), pi.get_drawColor());
					break;
				}
				case ProgressInfo.TIMER_EXPIRE: // When the round timer becomes
												// zero in playing game
				{
					System.out.println("<ClientManager> TIMER_EXPIRE get_gameName(): " + userInfo.get_gameName());
					System.out.println("<ClientManager> TIMER_EXPIRE get_nickName(): " + userInfo.get_nickName());
					server.timerExpireBroadcast(userInfo.get_gameName(), userInfo.get_nickName());
					break;
				}
				case ProgressInfo.CHAT_GAME: // When user types chat in game
				{
					System.out.println("CHAT_GAME");
					server.checkAnswer(userInfo.get_gameName(), userInfo.get_nickName(), pi.get_chattingSentence());
					break;
				}
				case ProgressInfo.EXIT_ENTRY: // When user exits in entry panel:
												// Closes the connection and
												// destroy data structures
				{
					System.out.println("EXIT_ENTRY");
					ProgressInfo pi_ack = new ProgressInfo();
					pi_ack.set_status(ProgressInfo.EXIT_APPROVE);
					lockedWrite(pi_ack);
					server.exitUser(s);
					break loop;
				}
				default: {
					break;
				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * When serverClient try to send progressInfo to client, we should prevent
	 * race condition of output stream Race condition can occur when timer
	 * broadcasting function is executed
	 * 
	 * @param progressInfo
	 */
	public void lockedWrite(ProgressInfo pi) {
		while (!outLock) {
			System.out.println("@In while");
		}
		outLock = false;
		try {
			out.writeObject(pi);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		outLock = true;
	}

	// Get methods
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public int getUserStatus() {
		return userInfo.get_status();
	}

	public String getUserNickname() {
		return userInfo.get_nickName();
	}

	public boolean userInLobby() {
		return userInfo.get_status() == UserInfo.IN_LOBBY;
	}

	public Socket getSocket() {
		return s;
	}

	/**
	 * When user try to exit game, server should disconnect socket and stream to
	 */
	// client
	public void exitClient() {
		try {
			in.close();
			out.close();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}