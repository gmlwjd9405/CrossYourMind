package server;

import info.UserInfo;
import info.GameInfo;
import info.ProgressInfo;
import drawing.UserPoint;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Server extends Thread {
	// ** VARIABLE **
	// For connection
	private ServerSocket serverSocket;
	private ArrayList<ClientManager> serverClientList;
	private ArrayList<GameInfo> gameInfoList;
	private ArrayList<String> wordList;

	private String recentLobbyChat;
	private Random random;

	// For timer management
	long startTime;
	long currentTime;
	long elapsedTime;

	static final int ROUND_NUM = 7;

	// **CONSTRUCTOR **
	public Server() {
		// Initialize data
		recentLobbyChat = new String();
		serverClientList = new ArrayList<ClientManager>();
		gameInfoList = new ArrayList<GameInfo>();

		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timerBroadcast();
			}
		};
		try {
			serverSocket = new ServerSocket(3333);
			System.out.println("open server!");
			Timer ALPHAtimer = new Timer(1000, action);
			ALPHAtimer.setRepeats(true);
			ALPHAtimer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ** METHOD **
	public static void main(String[] args) {
		new Server().start();
	}

	@Override
	public void run() {
		initWordList();
		initRandom();
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected!");
				ClientManager serverClient = new ClientManager(socket, this);
				serverClientList.add(serverClient);
				serverClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** Initialize the word data */
	private void initWordList() {
		wordList = new ArrayList<String>();
		wordList.add("네트워크");
		wordList.add("귓속말");
		wordList.add("학생회관");
	}

	/** : For all the clients, update chats in lobby panel */
	public void lobbyChatUpdateAll() {
		for (ClientManager sc : serverClientList) {
			try {
				if (sc.userInLobby()) {
					ProgressInfo pi_ack = new ProgressInfo();
					pi_ack.set_status(ProgressInfo.CHAT_LOBBY_UPDATE);
					pi_ack.set_lobbyChat(recentLobbyChat);
					sc.lockedWrite(pi_ack);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** For debugging */
	public void printUsers() {
		System.out.print("<SERVER> USERS: ");
		for (ClientManager sc : serverClientList) {
			System.out.print("| " + sc.getUserNickname());
		}
		System.out.println();
	}

	/** For debugging */
	public void printGames() {
		System.out.println("GAMES:");
		for (GameInfo gi : gameInfoList) {
			System.out.println("| " + gi.get_gameName());
		}
	}

	/**
	 * Check if the nickname is already in use
	 * 
	 * @param nickname
	 *            of the user trying to enter the lobby
	 */
	public boolean checkDuplicateUser(String new_nickName) {
		for (ClientManager serverClient : serverClientList) {
			if (serverClient.userInfo.get_nickName().equals(new_nickName))
				return true;
		}
		return false;
	}

	/**
	 * Check if the name is already in user
	 * 
	 * @param new_gameName(name
	 *            of the game trying to create)
	 */
	public boolean checkDuplicateGame(String new_gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(new_gameName))
				return true;
		}
		return false;
	}

	/**
	 * Create data structure for the game
	 * 
	 * @param available
	 *            name of the newly created game
	 */
	public void newGame(String roomName) {
		gameInfoList.add(new GameInfo(GameInfo.WATING, roomName, 1));
	}

	/** For all the clients, update list of games in lobby panel */
	public void lobbyGameAllUpdate() {
		for (ClientManager serverClient : serverClientList) {
			try {
				ProgressInfo pi_ack = new ProgressInfo();
				pi_ack.set_status(ProgressInfo.GAME_LOBBY_UPDATE);
				pi_ack.set_gamesLobby(giListNames());
				serverClient.lockedWrite(pi_ack);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** For all the clients, update list of users in lobby panel */
	public void lobbyUserAllUpdate() {
		for (ClientManager serverClient : serverClientList) {
			try {
				ProgressInfo pi_ack = new ProgressInfo();
				pi_ack.set_status(ProgressInfo.USER_LOBBY_UPDATE);
				pi_ack.set_usersLobby(userListNames());
				serverClient.lockedWrite(pi_ack);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For all the clients in the game, update list of users in game panel
	 * 
	 * @param name
	 *            of the target game
	 */
	public void gameUserAllUpdate(String gameName) {
		for (ClientManager serverClient : serverClientList) {
			try {
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_ack = new ProgressInfo();
					pi_ack.set_status(ProgressInfo.USER_GAME_UPDATE);
					pi_ack.set_usersGame(getUsersGame(gameName));
					serverClient.lockedWrite(pi_ack);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get names of the existing games list of game names
	 */
	public ArrayList<String> giListNames() {
		int length = gameInfoList.size();
		ArrayList<String> names = new ArrayList<String>();
		System.out.print("<SERVER> giListNames: ");
		for (int i = 0; i < length; i++) {
			names.add(gameInfoList.get(i).get_gameName());
			System.out.print(gameInfoList.get(i).get_gameName() + " | ");
		}
		System.out.println();
		return names;
	}

	/**
	 * Get nicknames of connected users /* @return list of users' nicknames
	 */
	public ArrayList<String> userListNames() {
		int length = serverClientList.size();
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			if (serverClientList.get(i).getUserStatus() == UserInfo.IN_LOBBY
					&& !(serverClientList.get(i).getUserNickname().equals("")))
				names.add(serverClientList.get(i).getUserNickname());
		}
		return names;
	}

	/**
	 * Update data structures about input informations. @param nickname of the
	 * joining user, name of the target game
	 */
	public void userJoinGame(String nickName, String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			try {
				if (gameInfo.get_gameName().equals(gameName)) {
					gameInfo.inc_participants();
					System.out.println("NUM: " + gameInfo.get_participants());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (ClientManager serverClient : serverClientList) {
			try {
				System.out.println("username: " + serverClient.getUserInfo().get_nickName() + ", gamename: "
						+ serverClient.getUserInfo().get_gameName());
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_ack = new ProgressInfo();
					pi_ack.set_status(ProgressInfo.JOIN_GAME_NEW);
					pi_ack.set_usersGame(getUsersGame(gameName));
					serverClient.lockedWrite(pi_ack);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Check if the game is full or already started
	 * 
	 * @param of
	 *            the target game
	 */
	public boolean checkFull(String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			try {
				if (gameInfo.get_gameName().equals(gameName)) {
					if (gameInfo.get_participants() == 4 || gameInfo.get_status() == GameInfo.PLAYING)
						return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Update data structure of game. Notify and update panels for the left
	 * users in that game.
	 * 
	 * @param name
	 *            of the target game
	 */
	public void userExitGame(String gameName) {
		loop: for (int i = 0; i < gameInfoList.size(); i++) {
			try {
				if (gameInfoList.get(i).get_gameName().equals(gameName)) {
					if (gameInfoList.get(i).get_participants() == 1) {
						gameInfoList.remove(i);
						lobbyGameAllUpdate();
					} else
						gameInfoList.get(i).dec_participants();
					break loop;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get the list of users for given game name
	 * 
	 * @param name
	 *            of the target game
	 * @return list of users in that game
	 */
	public ArrayList<UserInfo> getUsersGame(String gameName) {
		ArrayList<UserInfo> ui = new ArrayList<UserInfo>();
		for (ClientManager serverClient : serverClientList) {
			if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
				ui.add(serverClient.getUserInfo());
			}
		}
		return ui;
	}

	/** For debugging */
	public void printGi() {
		for (GameInfo gameInfo : gameInfoList) {
			System.out.println("<Server> printGi");
			System.out.println("status: " + gameInfo.get_status() + "gameName: " + gameInfo.get_gameName()
					+ "participants: " + gameInfo.get_participants());
		}
	}

	/**
	 * Check if the game can be started (enough participants)
	 * 
	 * @param name
	 *            of the target game
	 * @return true or false
	 */
	public boolean startAvailable(String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				if (gameInfo.get_participants() >= 2)
					return true;
			}
		}
		return false;
	}

	/**
	 * For all the clients in the game, notify to start the game
	 * 
	 * @param name
	 *            of the target game
	 */
	public void startGameAll(String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				gameInfo.set_status(GameInfo.PLAYING);
				gameInfo.set_roundNum(GameInfo.ROUND_NUM);
			}
		}
		String questioner = "";
		for (ClientManager serverClient : serverClientList) {
			try {
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_ack = new ProgressInfo();
					if (serverClient.getUserInfo().get_isMaster()) {
						questioner = new String(serverClient.getUserNickname());
						serverClient.getUserInfo().set_status(UserInfo.IN_GAME_QUESTIONER);
						pi_ack.set_status(ProgressInfo.START_APPROVE_QUESTIONER);
						for (GameInfo gameInfo : gameInfoList) {
							try {
								if (gameInfo.get_gameName().equals(gameName)) {
									gameInfo.set_roundNum(ROUND_NUM - 1);
									gameInfo.set_roundAnswer(getRandomWord());
									pi_ack.set_chat(gameInfo.get_roundAnswer()); // 게임의
																					// 정답
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						serverClient.getUserInfo().set_status(UserInfo.IN_GAME_ANSWERER);
						pi_ack.set_status(ProgressInfo.START_APPROVE_ANSWERER);
					}
					System.out.println("<ALPHAserver startGameAll> call progressInfo SetImagePath");
					pi_ack.setNickName(questioner);
					pi_ack.set_imagePath(serverClient.getUserInfo().get_gamecharImagePath());
					serverClient.lockedWrite(pi_ack);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * If questioner draws to canvas, broadcast the drawing to the users in the
	 * game
	 * 
	 * @param name
	 *            of the target game,
	 * @param point
	 *            list to draw
	 */
	public void drawBroadcast(String gameName, ArrayList<UserPoint> pList) {
		for (ClientManager serverClient : serverClientList) {
			try {
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.DRAW_BROADCAST);
					pi_broadcast.set_pList(pList);
					serverClient.lockedWrite(pi_broadcast);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For all the clients in the game, clear the canvas
	 * 
	 * @param name
	 *            of the target game
	 */
	public void clearBroadcast(String gameName) {
		for (ClientManager serverClient : serverClientList) {
			try {
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.SELECT_CLEAR_BROADCAST);
					serverClient.lockedWrite(pi_broadcast);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For all the clients in the game, set the eraser mode
	 * 
	 * @param name
	 *            of the target game
	 */
	public void eraserBroadcast(String gameName) {
		for (ClientManager serverClient : serverClientList) {
			try {
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.SELECT_ERASER_BROADCAST);
					serverClient.lockedWrite(pi_broadcast);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For all the clients in the game, change the color as questioner selected
	 * 
	 * @param name
	 *            of the target game
	 */
	public void colorBroadcast(String gameName, int drawingColor) {
		for (ClientManager serverClient : serverClientList) {
			try {
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.SELECT_COLOR_BROADCAST);
					pi_broadcast.set_drawColor(drawingColor);
					serverClient.lockedWrite(pi_broadcast);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * For all the clients in the game, notifies that the current round ended
	 * and the next questioner For the new questioner, notify new round answer
	 * 
	 * @param name
	 *            of the target game
	 * @param nickname
	 *            of the most recent questioner
	 */
	public void timerExpireBroadcast(String gameName, String recentQuestioner) {
		int i;
		String nextQuestioner;
		while (true) {
			System.out.println("SERVER: In while: " + serverClientList.size());
			i = random.nextInt(serverClientList.size());
			UserInfo selected = serverClientList.get(i).getUserInfo();
			if (!(selected.get_nickName().equals(recentQuestioner)) && (selected.get_gameName().equals(gameName))) {
				nextQuestioner = new String(serverClientList.get(i).getUserInfo().get_nickName());
				break;
			}
		}
		for (GameInfo gameInfo : gameInfoList) {
			try {
				if (gameInfo.get_gameName().equals(gameName)) {
					if (gameInfo.get_roundNum() > 0) {
						gameInfo.set_roundNum(gameInfo.get_roundNum() - 1);

						System.out.println("roundNum : " + gameInfo.get_roundNum() + "in timer expired");

						for (ClientManager clientManager : serverClientList) {
							try {
								if (clientManager.getUserInfo().get_gameName().equals(gameName)) {
									ProgressInfo pi_ack = new ProgressInfo();
									System.out.println("<SERVER timerExpireBroadcast>");
									// pi_ack.set_imagePath(nextQuestioner); //
									// ??????
									pi_ack.setNickName(nextQuestioner);

									if (clientManager.getUserInfo().get_nickName().equals(nextQuestioner)) {
										System.out.println("IN if");
										clientManager.getUserInfo().set_status(UserInfo.IN_GAME_QUESTIONER);
										pi_ack.set_status(ProgressInfo.START_APPROVE_QUESTIONER);
										gameInfo.set_roundAnswer(getRandomWord());
										pi_ack.set_chat(gameInfo.get_roundAnswer()); // 게임의
																						// 정답
									} else {
										System.out.println("IN else");
										clientManager.getUserInfo().set_status(UserInfo.IN_GAME_ANSWERER);
										pi_ack.set_status(ProgressInfo.START_APPROVE_ANSWERER);
									}
									System.out.println("++++++++++++++++++++++++++++++++++++++++++");
									System.out.println(" | FROM SERVER SEND MESSAGE | ");
									System.out.println("  chat: " + pi_ack.get_chat());
									System.out.println("  roomName: " + pi_ack.get_RoomName());
									System.out.println("  iPath: " + pi_ack.get_imagePath());
									System.out.println("  status: " + pi_ack.get_status());
									System.out.println("++++++++++++++++++++++++++++++++++++++++++");
									clientManager.lockedWrite(pi_ack); // progressInfo를
																		// 보낸다
									System.out.println("AFTER lockedWrite - expire broadcast");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else // roundNum <= 0
					{
						ProgressInfo pi_broadcast = new ProgressInfo();
						pi_broadcast.set_status(ProgressInfo.ROUND_TERMINATE);
						pi_broadcast.set_chat(findWinner(gameName));
						for (ClientManager sc : serverClientList) {
							if (sc.getUserInfo().get_gameName().equals(gameName)) {
								sc.lockedWrite(pi_broadcast);
								sc.getUserInfo().set_score(0);
							}
						}
						gameInfo.set_status(GameInfo.WATING);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * For all the clients in the game, show the whole game result
	 * 
	 * @param name
	 *            of the target game
	 * @return
	 */
	private String findWinner(String gameName) {
		String winner = "";
		int scoreMax = 0;
		for (ClientManager serverClient : serverClientList) {
			if (serverClient.getUserInfo().get_score() >= scoreMax)
				scoreMax = serverClient.getUserInfo().get_score();
		}
		for (ClientManager sc : serverClientList) {
			System.out.println(sc.getUserNickname() + ": " + sc.getUserInfo().get_score());
			if (sc.getUserInfo().get_score() == scoreMax)
				winner += (sc.getUserNickname() + " ");
		}
		winner += ("won this game!");
		System.out.println(winner);
		return winner;
	}

	/**
	 * Initialize random variable that is used to select next questioner and
	 * next round answer
	 */
	private void initRandom() {
		random = new Random();
	}

	/**
	 * Get random word from word list
	 * 
	 * @return round answer
	 */
	private String getRandomWord() {
		// heeee
		// return wordList.get(random.nextInt(199));
		return wordList.get(random.nextInt(3));
	}

	/**
	 * For newly typed chat, check if it is correct If correct, notify all the
	 * users in the game that the chat's owner got correct If not correct, just
	 * update the chats in game
	 * 
	 * @param name
	 *            of the target game
	 * @param nickname
	 *            of chat's owner
	 * @param contents
	 *            of
	 */
	public void checkAnswer(String gameName, String nickName, String chat) {
		System.out.println("<SERVER>");
		System.out.println("---------------------------");
		System.out.println("gameName: " + gameName);
		System.out.println("nickName: " + nickName);
		System.out.println("chat: " + chat);
		System.out.println("---------------------------");
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				if (gameInfo.get_roundAnswer().equals(chat)) {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.CORRECT_ANSWER);
					System.out.println("<ALPHAserver_checkAnswer> pi.setNickName(nickName): " + nickName);
					System.out.println("<ALPHAserver_checkAnswer> pi.set_chattingSentence(chat): " + chat);
					// heee
					// pi_broadcast.set_chat(nickName);
					pi_broadcast.setNickName(nickName);
					pi_broadcast.set_chattingSentence(chat); // ?????
					// pi_broadcast.set_imagePath(chat);
					for (ClientManager serverClient : serverClientList) {
						if (serverClient.getUserInfo().get_gameName().equals(gameName))
							serverClient.lockedWrite(pi_broadcast);
						if (serverClient.getUserInfo().get_nickName().equals(nickName))
							serverClient.getUserInfo().inc_score();
					}
				} else {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.CHAT_GAME_UPDATE);
					System.out.println("<ALPHAserver_checkAnswer> pi.setNickName(nickName): " + nickName);
					System.out.println("<ALPHAserver_checkAnswer> pi.set_chattingSentence(chat): " + chat);
					// heee
					// pi_broadcast.set_chat(nickName);
					pi_broadcast.setNickName(nickName);
					pi_broadcast.set_chattingSentence(chat); // ?????
					// pi_broadcast.set_imagePath(chat); //?????
					for (ClientManager sc : serverClientList) {
						if (sc.getUserInfo().get_gameName().equals(gameName))
							sc.lockedWrite(pi_broadcast);
					}
				}
			}
		}
	}

	/**
	 * Close the connection and remove server client
	 * 
	 * @param socket
	 */
	public void exitUser(Socket s) {
		for (ClientManager serverClient : serverClientList) {
			if (serverClient.getSocket() == s) {
				try {
					// sc.destroy ();
					serverClient.exitClient();
					serverClientList.remove(serverClient);
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/** For all the clients, notify that 1 second elapsed */
	public void timerBroadcast() {
		ProgressInfo pi_broadcast = new ProgressInfo();
		pi_broadcast.set_status(ProgressInfo.TIMER_BROADCAST);
		for (ClientManager serverClient : serverClientList) {
			try {
				serverClient.lockedWrite(pi_broadcast);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Get method */
	public String get_RLC() {
		return recentLobbyChat;
	}

	/* Set method */
	public void set_RLC(String item) {
		recentLobbyChat = item;
	}
}