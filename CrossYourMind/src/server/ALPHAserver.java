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
	ServerSocket serverSocket;
	ArrayList<ServerClient> serverClientList;
	ArrayList<GameInfo> gameInfoList;
	ArrayList<String> wordList;

	String recentLobbyChat;
	Random random;

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
		wordList.add("네트워크");
		wordList.add("지곡회관");
		wordList.add("학생회관");
		wordList.add("78계단");
		wordList.add("제2공학관");
		wordList.add("스낵바라면");
		wordList.add("개강");
		wordList.add("CC");
		wordList.add("종강");
		wordList.add("MT");
		wordList.add("탄소");
		wordList.add("수소");
		wordList.add("산소");
		wordList.add("질소");
		wordList.add("이산화탄소");
		wordList.add("미적분학");
		wordList.add("일반화학");
		wordList.add("프로그래밍과문제해결");
		wordList.add("일반생명과학");
		wordList.add("일반물리실험");
		wordList.add("장학금");
		wordList.add("장짤");
		wordList.add("영짤");
		wordList.add("대나무");
		wordList.add("열매");
		wordList.add("출석");
		wordList.add("결석");
		wordList.add("지각");
		wordList.add("중간고사");
		wordList.add("기말고사");
		wordList.add("기숙사");
		wordList.add("동아리");
		wordList.add("체육관");
		wordList.add("RC");
		wordList.add("지곡연못");
		wordList.add("버거킹");
		wordList.add("학생식당");
		wordList.add("육일콜");
		wordList.add("포스로이드");
		wordList.add("포비스");
		wordList.add("모네");
		wordList.add("세리오");
		wordList.add("플랑크");
		wordList.add("뉴턴");
		wordList.add("패러데이");
		wordList.add("튜링");
		wordList.add("페이스북");
		wordList.add("카카오톡");
		wordList.add("구글");
		wordList.add("알파고");
		wordList.add("포카전");
		wordList.add("해맞이한마당");
		wordList.add("박태준학술정보관");
		wordList.add("학고새");
		wordList.add("대자보");
		wordList.add("뒤풀이");
		wordList.add("상남관");
		wordList.add("트위터");
		wordList.add("매점");
		wordList.add("전야제");
		wordList.add("조별과제");
		wordList.add("대학원");
		wordList.add("야식");
		wordList.add("분수");
		wordList.add("개구리밥");
		wordList.add("교환학생");
		wordList.add("리그오브레전드");
		wordList.add("셧다운제");
		wordList.add("아침수업");
		wordList.add("제주감귤");
		wordList.add("공강");
		wordList.add("연강");
		wordList.add("에너지드링크");
		wordList.add("식곤증");
		wordList.add("작심삼일");
		wordList.add("수강신청");
		wordList.add("파티스");
		wordList.add("듀이펙트");
		wordList.add("치킨");
		wordList.add("레포트");
		wordList.add("질문");
		wordList.add("컵라면");
		wordList.add("다이어트");
		wordList.add("LMS");
		wordList.add("오픈북");
		wordList.add("쿠폰");
		wordList.add("스마트폰");
		wordList.add("폭풍의언덕");
		wordList.add("전자출결");
		wordList.add("대리출석");
		wordList.add("LINQ");
		wordList.add("데스크탑");
		wordList.add("소화기");
		wordList.add("자동제세동기");
		wordList.add("헬조선");
		wordList.add("학생회장");
		wordList.add("계절학기");
		wordList.add("여름방학");
		wordList.add("겨울방학");
		wordList.add("대체공휴일");
		wordList.add("통나무집");
		wordList.add("보드마카");
		wordList.add("문화콜로퀴움");
		wordList.add("인문과예술의세계");
		wordList.add("과사통");
		wordList.add("메로나");
		wordList.add("조교");
		wordList.add("보조배터리");
		wordList.add("초코에몽");
		wordList.add("빼뺴로");
		wordList.add("한스드림");
		wordList.add("롯데마트");
		wordList.add("홈플러스");
		wordList.add("시외버스터미널");
		wordList.add("고속버스터미널");
		wordList.add("베스킨라빈스");
		wordList.add("물회");
		wordList.add("포항북부해수욕장");
		wordList.add("폭설");
		wordList.add("연지");
		wordList.add("포스트잇");
		wordList.add("국토기행");
		wordList.add("과외");
		wordList.add("아르바이트");
		wordList.add("스쿠터");
		wordList.add("새내기배움터");
		wordList.add("태블릿");
		wordList.add("충전기");
		wordList.add("보고서");
		wordList.add("학사경고");
		wordList.add("쌍권총");
		wordList.add("신선동");
		wordList.add("대청소");
		wordList.add("GSR");
		wordList.add("청암포탈");
		wordList.add("효자시장");
		wordList.add("블루투스");
		wordList.add("와이파이");
		wordList.add("에어컨");
		wordList.add("포스플렉스");
		wordList.add("원자가속기");
		wordList.add("노트필기");
		wordList.add("택배");
		wordList.add("담배");
		wordList.add("자전거");
		wordList.add("난폭운전");
		wordList.add("비밀번호");
		wordList.add("기지개");
		wordList.add("하품");
		wordList.add("포크레인");
		wordList.add("소방관");
		wordList.add("전공서적");
		wordList.add("풋살");
		wordList.add("자퇴");
		wordList.add("휴학");
		wordList.add("군대");
		wordList.add("김치");
		wordList.add("핫식스");
		wordList.add("배달음식");
		wordList.add("쿨러");
		wordList.add("크리스마스");
		wordList.add("동아리연합회");
		wordList.add("대항해시대");
		wordList.add("구끼리");
		wordList.add("고담9동");
		wordList.add("한화");
		wordList.add("인스턴트커피");
		wordList.add("참뼈");
		wordList.add("새천년노래방");
		wordList.add("마인츠돔");
		wordList.add("전기장판");
		wordList.add("버너");
		wordList.add("블라인드");
		wordList.add("커튼");
		wordList.add("고무동력기");
		wordList.add("터미네이터");
		wordList.add("송중기");
		wordList.add("태양의후예");
		wordList.add("냉장고를부탁해");
		wordList.add("창렬");
		wordList.add("응용선형대수학");
		wordList.add("양꼬치엔칭따오");
		wordList.add("옵티머스프라임");
		wordList.add("아이언맨");
		wordList.add("조커");
		wordList.add("캡틴아메리카");
		wordList.add("송아지");
		wordList.add("송혜교");
		wordList.add("고기뷔페");
		wordList.add("보라카이");
		wordList.add("라바");
		wordList.add("미니언");
		wordList.add("캐러비안의해적");
		wordList.add("말할수없는비밀");
		wordList.add("쏘우");
		wordList.add("절규");
		wordList.add("소나무");
		wordList.add("장갑차");
		wordList.add("스타벅스");
		wordList.add("포항공과대학교");
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
		for (GameInfo gameInfo : gameInfoList) {
			if (gameInfo.get_gameName().equals(gameName)) {
				if (gameInfo.get_roundAnswer().equals(chat)) {// System.out.println("CORRECT");
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.CORRECT_ANSWER);
					pi_broadcast.set_chat(nickName);
					System.out.println("<ALPHAserver> call progressInfo SetImagePath");
					pi_broadcast.set_imagePath(chat);
					for (ServerClient serverClient : serverClientList) {
						if (serverClient.getUserInfo().get_gameName().equals(gameName))
							serverClient.lockedWrite(pi_broadcast);
						if (serverClient.getUserInfo().get_nickName().equals(nickName))
							serverClient.getUserInfo().inc_score();
					}
				} else {
					ProgressInfo pi_broadcast = new ProgressInfo();
					pi_broadcast.set_status(ProgressInfo.CHAT_GAME_UPDATE);
					pi_broadcast.set_chat(nickName);
					// heee 원래 코드에서 제거 후 작동 테스트
					// pi_broadcast.set_imagePath(chat); //?????
					for (ServerClient sc : serverClientList) {// System.out.println("CHAT");
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