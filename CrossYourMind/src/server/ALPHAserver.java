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

public class ALPHAserver extends Thread {
	// ** VARIABLE **
	// For connection
	private ServerSocket serverSocket;
	private ArrayList<ServerClient> serverClientList;
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
	public ALPHAserver() {
		// Initialize data
		recentLobbyChat = new String();
		serverClientList = new ArrayList<ServerClient>();
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
		new ALPHAserver().start();
	}

	@Override
	public void run() {
		initWordList();
		initRandom();
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected!");
				ServerClient serverClient = new ServerClient(socket, this);
				serverClientList.add(serverClient);
				serverClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize the word data
	private void initWordList() {
		wordList = new ArrayList<String>();
		wordList.add("��Ʈ��ũ");
		wordList.add("����ȸ��");
		wordList.add("�л�ȸ��");
		wordList.add("78���");
		wordList.add("��2���а�");
		wordList.add("�����ٶ��");
		wordList.add("����");
		wordList.add("CC");
		wordList.add("����");
		wordList.add("MT");
		wordList.add("ź��");
		wordList.add("����");
		wordList.add("���");
		wordList.add("����");
		wordList.add("�̻�ȭź��");
		wordList.add("��������");
		wordList.add("�Ϲ�ȭ��");
		wordList.add("���α׷��ְ������ذ�");
		wordList.add("�Ϲݻ������");
		wordList.add("�Ϲݹ�������");
		wordList.add("���б�");
		wordList.add("��©");
		wordList.add("��©");
		wordList.add("�볪��");
		wordList.add("����");
		wordList.add("�⼮");
		wordList.add("�Ἦ");
		wordList.add("����");
		wordList.add("�߰����");
		wordList.add("�⸻���");
		wordList.add("�����");
		wordList.add("���Ƹ�");
		wordList.add("ü����");
		wordList.add("RC");
		wordList.add("�����");
		wordList.add("����ŷ");
		wordList.add("�л��Ĵ�");
		wordList.add("������");
		wordList.add("�������̵�");
		wordList.add("����");
		wordList.add("���");
		wordList.add("������");
		wordList.add("�ö�ũ");
		wordList.add("����");
		wordList.add("�з�����");
		wordList.add("Ʃ��");
		wordList.add("���̽���");
		wordList.add("īī����");
		wordList.add("����");
		wordList.add("���İ�");
		wordList.add("��ī��");
		wordList.add("�ظ����Ѹ���");
		wordList.add("�������м�������");
		wordList.add("�а��");
		wordList.add("���ں�");
		wordList.add("��Ǯ��");
		wordList.add("�󳲰�");
		wordList.add("Ʈ����");
		wordList.add("����");
		wordList.add("������");
		wordList.add("��������");
		wordList.add("���п�");
		wordList.add("�߽�");
		wordList.add("�м�");
		wordList.add("��������");
		wordList.add("��ȯ�л�");
		wordList.add("���׿��극����");
		wordList.add("�˴ٿ���");
		wordList.add("��ħ����");
		wordList.add("���ְ���");
		wordList.add("����");
		wordList.add("����");
		wordList.add("�������帵ũ");
		wordList.add("�İ���");
		wordList.add("�۽ɻ���");
		wordList.add("������û");
		wordList.add("��Ƽ��");
		wordList.add("������Ʈ");
		wordList.add("ġŲ");
		wordList.add("����Ʈ");
		wordList.add("����");
		wordList.add("�Ŷ��");
		wordList.add("���̾�Ʈ");
		wordList.add("LMS");
		wordList.add("���º�");
		wordList.add("����");
		wordList.add("����Ʈ��");
		wordList.add("��ǳ�Ǿ��");
		wordList.add("�������");
		wordList.add("�븮�⼮");
		wordList.add("LINQ");
		wordList.add("����ũž");
		wordList.add("��ȭ��");
		wordList.add("�ڵ���������");
		wordList.add("������");
		wordList.add("�л�ȸ��");
		wordList.add("�����б�");
		wordList.add("��������");
		wordList.add("�ܿ����");
		wordList.add("��ü������");
		wordList.add("�볪����");
		wordList.add("���帶ī");
		wordList.add("��ȭ�ݷ�����");
		wordList.add("�ι��������Ǽ���");
		wordList.add("������");
		wordList.add("�޷γ�");
		wordList.add("����");
		wordList.add("�������͸�");
		wordList.add("���ڿ���");
		wordList.add("������");
		wordList.add("�ѽ��帲");
		wordList.add("�Ե���Ʈ");
		wordList.add("Ȩ�÷���");
		wordList.add("�ÿܹ����͹̳�");
		wordList.add("��ӹ����͹̳�");
		wordList.add("����Ų���");
		wordList.add("��ȸ");
		wordList.add("���׺Ϻ��ؼ�����");
		wordList.add("����");
		wordList.add("����");
		wordList.add("����Ʈ��");
		wordList.add("�������");
		wordList.add("����");
		wordList.add("�Ƹ�����Ʈ");
		wordList.add("������");
		wordList.add("����������");
		wordList.add("�º�");
		wordList.add("������");
		wordList.add("����");
		wordList.add("�л���");
		wordList.add("�ֱ���");
		wordList.add("�ż���");
		wordList.add("��û��");
		wordList.add("GSR");
		wordList.add("û����Ż");
		wordList.add("ȿ�ڽ���");
		wordList.add("�������");
		wordList.add("��������");
		wordList.add("������");
		wordList.add("�����÷���");
		wordList.add("���ڰ��ӱ�");
		wordList.add("��Ʈ�ʱ�");
		wordList.add("�ù�");
		wordList.add("���");
		wordList.add("������");
		wordList.add("��������");
		wordList.add("��й�ȣ");
		wordList.add("������");
		wordList.add("��ǰ");
		wordList.add("��ũ����");
		wordList.add("�ҹ��");
		wordList.add("��������");
		wordList.add("ǲ��");
		wordList.add("����");
		wordList.add("����");
		wordList.add("����");
		wordList.add("��ġ");
		wordList.add("�ֽĽ�");
		wordList.add("�������");
		wordList.add("��");
		wordList.add("ũ��������");
		wordList.add("���Ƹ�����ȸ");
		wordList.add("�����ؽô�");
		wordList.add("������");
		wordList.add("���9��");
		wordList.add("��ȭ");
		wordList.add("�ν���ƮĿ��");
		wordList.add("����");
		wordList.add("��õ��뷡��");
		wordList.add("��������");
		wordList.add("��������");
		wordList.add("����");
		wordList.add("����ε�");
		wordList.add("Ŀư");
		wordList.add("�����±�");
		wordList.add("�͹̳�����");
		wordList.add("���߱�");
		wordList.add("�¾����Ŀ�");
		wordList.add("�������Ź��");
		wordList.add("â��");
		wordList.add("���뼱�������");
		wordList.add("�粿ġ��Ī����");
		wordList.add("��Ƽ�ӽ�������");
		wordList.add("���̾��");
		wordList.add("��Ŀ");
		wordList.add("ĸƾ�Ƹ޸�ī");
		wordList.add("�۾���");
		wordList.add("������");
		wordList.add("������");
		wordList.add("����ī��");
		wordList.add("���");
		wordList.add("�̴Ͼ�");
		wordList.add("ĳ�����������");
		wordList.add("���Ҽ����º��");
		wordList.add("���");
		wordList.add("����");
		wordList.add("�ҳ���");
		wordList.add("�尩��");
		wordList.add("��Ÿ����");
		wordList.add("���װ������б�");
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: For all the clients, update chats in lobby panel
	public void lobbyChatUpdateAll() {
		for (ServerClient sc : serverClientList) {
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

	// INPUT: null
	// OUTPUT: null
	// Objective: For debugging
	public void printUsers() {
		System.out.println("USERS:");
		for (ServerClient sc : serverClientList) {
			System.out.println("| " + sc.getUserNickname());
		}
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: For debugging
	public void printGames() {
		System.out.println("GAMES:");
		for (GameInfo gi : gameInfoList) {
			System.out.println("| " + gi.get_gameName());
		}
	}

	// INPUT: nickname of the user trying to enter the lobby
	// OUTPUT: null
	// Objective: Check if the nickname is already in use
	public boolean checkDuplicateUser(String new_nickName) {
		for (ServerClient serverClient : serverClientList) {
			if (serverClient.userInfo.get_nickName().equals(new_nickName))
				return true;
		}
		return false;
	}

	// INPUT: name of the game trying to create
	// OUTPUT: null
	// Objective: Check if the name is already in user
	public boolean checkDuplicateGame(String new_gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(new_gameName))
				return true;
		}
		return false;
	}

	// INPUT: available name of the newly created game
	// OUTPUT: null
	// Objective: Create data structure for the game
	public void newGame(String roomName) {
		gameInfoList.add(new GameInfo(GameInfo.WATING, roomName, 1));
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: For all the clients, update list of games in lobby panel
	public void lobbyGameAllUpdate() {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: null
	// OUTPUT: null
	// Objective: For all the clients, update list of users in lobby panel
	public void lobbyUserAllUpdate() {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: For all the clients in the game, update list of users in game
	// panel
	public void gameUserAllUpdate(String gameName) {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: null
	// OUTPUT: list of game names
	// Objective: Get names of the existing games
	public ArrayList<String> giListNames() {
		int length = gameInfoList.size();
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			names.add(gameInfoList.get(i).get_gameName());
		}
		return names;
	}

	// INPUT: null
	// OUTPUT: list of users' nicknames
	// Objective: Get nicknames of connected users
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

	// INPUT: nickname of the joining user, name of the target game
	// OUTPUT: null
	// Objective: Update data structures about input informations.
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

		for (ServerClient serverClient : serverClientList) {
			try {
				System.out.println(
						serverClient.getUserInfo().get_nickName() + " : " + serverClient.getUserInfo().get_gameName());
				if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
					System.out.println(serverClient.getUserInfo().get_nickName());
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

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: Check if the game is full or already started
	public boolean checkFull(String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			try {
				if (gameInfo.get_gameName().equals(gameName)) {
					if (gameInfo.get_participants() == 6 || gameInfo.get_status() == GameInfo.PLAYING)
						return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: Update data structure of game. Notify and update panels for
	// the left users in that game.
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

	// INPUT: name of the target game
	// OUTPUT: list of users in that game
	// Objective: Get the list of users for given game name
	public ArrayList<UserInfo> getUsersGame(String gameName) {
		ArrayList<UserInfo> ui = new ArrayList<UserInfo>();
		for (ServerClient serverClient : serverClientList) {
			if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
				ui.add(serverClient.getUserInfo());
			}
		}
		return ui;
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: For debugging
	public void printGi() {
		for (GameInfo gameInfo : gameInfoList) {
			System.out.println("status: " + gameInfo.get_status() + "gameName: " + gameInfo.get_gameName()
					+ "participants: " + gameInfo.get_participants());
		}
	}

	// INPUT: name of the target game
	// OUTPUT: true or false
	// Objective: Check if the game can be started (enough participants)
	public boolean startAvailable(String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				if (gameInfo.get_participants() >= 2)
					return true;
			}
		}
		return false;
	}

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: For all the clients in the game, notify to start the game
	public void startGameAll(String gameName) {
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				gameInfo.set_status(GameInfo.PLAYING);
				gameInfo.set_roundNum(GameInfo.ROUND_NUM);
			}
		}
		String questioner = "";
		for (ServerClient serverClient : serverClientList) {
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
									pi_ack.set_chat(gameInfo.get_roundAnswer());
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
					// pi_ack.set_imagePath(questioner);
					pi_ack.set_imagePath(serverClient.getUserInfo().get_gamecharImagePath());
					serverClient.lockedWrite(pi_ack);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// INPUT: name of the target game, point list to draw
	// OUTPUT: null
	// Objective: If questioner draws to canvas, broadcast the drawing to the
	// users in the game
	public void drawBroadcast(String gameName, ArrayList<UserPoint> pList) {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: For all the clients in the game, clear the canvas
	public void clearBroadcast(String gameName) {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: For all the clients in the game, set the eraser mode
	public void eraserBroadcast(String gameName) {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: For all the clients in the game, change the color as
	// questioner selected
	public void colorBroadcast(String gameName, int drawingColor) {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: name of the target game, nickname of the most recent questioner
	// OUTPUT: null
	// Objective:
	// For all the clients in the game, notifies that the current round ended
	// and the next questioner
	// For the new questioner, notify new round answer
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
		// System.out.println ("SERVER: Out while");
		for (GameInfo gameInfo : gameInfoList) {
			try {
				if (gameInfo.get_gameName().equals(gameName)) {
					if (gameInfo.get_roundNum() > 0) {
						gameInfo.set_roundNum(gameInfo.get_roundNum() - 1);
						System.out.println("roundNum : " + gameInfo.get_roundNum() + "in timer expired");
						for (ServerClient serverClient : serverClientList) {
							try {
								if (serverClient.getUserInfo().get_gameName().equals(gameName)) {
									ProgressInfo pi_ack = new ProgressInfo();
									System.out.println(
											"<ALPHAserver timerExpireBroadcast> call progressInfo SetImagePath");
									pi_ack.set_imagePath(nextQuestioner);
									if (serverClient.getUserInfo().get_nickName().equals(nextQuestioner)) {
										System.out.println("IN if");
										serverClient.getUserInfo().set_status(UserInfo.IN_GAME_QUESTIONER);
										pi_ack.set_status(ProgressInfo.START_APPROVE_QUESTIONER);
										gameInfo.set_roundAnswer(getRandomWord());
										pi_ack.set_chat(gameInfo.get_roundAnswer());
									} else {
										System.out.println("IN else");
										serverClient.getUserInfo().set_status(UserInfo.IN_GAME_ANSWERER);
										pi_ack.set_status(ProgressInfo.START_APPROVE_ANSWERER);
									}
									System.out.println(
											"  SEND| chat:" + pi_ack.get_chat() + " iPath:" + pi_ack.get_imagePath());
									System.out.println("        status:" + pi_ack.get_status());
									serverClient.lockedWrite(pi_ack);
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
						for (ServerClient sc : serverClientList) {
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

	// INPUT: name of the target game
	// OUTPUT: null
	// Objective: For all the clients in the game, show the whole game result
	private String findWinner(String gameName) {
		String winner = "";
		int scoreMax = 0;
		for (ServerClient serverClient : serverClientList) {
			if (serverClient.getUserInfo().get_score() >= scoreMax)
				scoreMax = serverClient.getUserInfo().get_score();
		}
		for (ServerClient sc : serverClientList) {
			System.out.println(sc.getUserNickname() + ": " + sc.getUserInfo().get_score());
			if (sc.getUserInfo().get_score() == scoreMax)
				winner += (sc.getUserNickname() + " ");
		}
		winner += ("won this game!");
		System.out.println(winner);
		return winner;
	}

	// INPUT: null
	// OUTPUT: null
	// Objective: Initialize random variable that is used to select next
	// questioner and next round answer
	private void initRandom() {
		random = new Random();
	}

	// INPUT: null
	// OUTPUT: round answer
	// Objective: Get random word from word list
	private String getRandomWord() {
		return wordList.get(random.nextInt(199));
	}

	// INPUT: name of the target game, nickname of chat's owner, contents of
	// chat
	// OUTPUT: null
	// Objective:
	// For newly typed chat, check if it is correct
	// If correct, notify all the users in the game that the chat's owner got
	// correct
	// If not correct, just update the chats in game
	public void checkAnswer(String gameName, String nickName, String chat) {
		System.out.println("<ALPHAserver>");
		System.out.println("gameName: " + gameName);
		System.out.println("nickName: " + nickName);
		System.out.println("chat: " + chat);
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				if (gameInfo.get_roundAnswer().equals(chat)) {// System.out.println("CORRECT");
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.CORRECT_ANSWER);
					System.out.println("<ALPHAserver_checkAnswer> call progressInfo set_chat(nickName): " + nickName);
					pi_broadcast.set_chat(nickName);
					System.out.println("<ALPHAserver_checkAnswer> call progressInfo set_chattingSentence(chat): " + chat);
					// heee 
					pi_broadcast.set_chattingSentence(chat); //?????
					//pi_broadcast.set_imagePath(chat);
					for (ServerClient serverClient : serverClientList) {
						if (serverClient.getUserInfo().get_gameName().equals(gameName))
							serverClient.lockedWrite(pi_broadcast);
						if (serverClient.getUserInfo().get_nickName().equals(nickName))
							serverClient.getUserInfo().inc_score();
					}
				} else {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.CHAT_GAME_UPDATE);
					System.out.println("<ALPHAserver_checkAnswer> call progressInfo set_chat(nickName): " + nickName);
					pi_broadcast.set_chat(nickName);
					// heee 
					pi_broadcast.set_chattingSentence(chat); //?????
					//pi_broadcast.set_imagePath(chat); //?????
					for (ServerClient sc : serverClientList) {
						if (sc.getUserInfo().get_gameName().equals(gameName))
							sc.lockedWrite(pi_broadcast);
					}
				}
			}
		}
	}

	// INPUT: socket
	// OUTPUT: null
	// Objective: Close the connection and remove server client
	public void exitUser(Socket s) {
		for (ServerClient serverClient : serverClientList) {
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

	// INPUT: null
	// OUTPUT: null
	// Objective: For all the clients, notify that 1 second elapsed
	public void timerBroadcast() {
		ProgressInfo pi_broadcast = new ProgressInfo();
		pi_broadcast.set_status(ProgressInfo.TIMER_BROADCAST);
		for (ServerClient serverClient : serverClientList) {
			try {
				serverClient.lockedWrite(pi_broadcast);
				// System.out.println ("AFTER lockedWrite - timer broadcast: " +
				// sc.getUserNickname ());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Get method
	public String get_RLC() {
		return recentLobbyChat;
	}

	// Set method
	public void set_RLC(String item) {
		recentLobbyChat = item;
	}
}