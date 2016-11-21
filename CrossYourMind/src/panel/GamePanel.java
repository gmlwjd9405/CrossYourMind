package panel;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import drawing.UserPoint;
import frame.MainFrame;
import info.ProgressInfo;
import info.UserInfo;

public class GamePanel extends JPanel {
	// ** DEFINE **
	public static final int ROUND_TIME = 60;

	// ** VARIABLE **
	// Connect its parent frame
	MainFrame mainFrame;
	// For inner panels
	private JPanel northPanel, centerPanel, drawingPanel, westPanel, eastPanel, southPanel;
	private JLabel titleImage;
	private JPanel centerToolPanel, centerCanvasPanel;
//	private JPanel user1Panel, user2Panel, user3Panel, user4Panel, user5Panel, user6Panel;
//	private JTextPane user1Chat, user2Chat, user3Chat, user4Chat, user5Chat, user6Chat;
//	private JLabel user1Char, user2Char, user3Char, user4Char, user5Char, user6Char;
//	private JTextPane user1Nickname, user2Nickname, user3Nickname, user4Nickname, user5Nickname, user6Nickname;
	private JPanel[] userPanel = new JPanel[4];
	private JTextPane[] userChat = new JTextPane[4];
	private JLabel[] userChar = new JLabel[4];
	private JTextPane[] userNickname = new JTextPane[4];
	private JTextField gameChat;
	private JTextPane answer, timer;
	private JButton clearAll, eraser, color[];
	//private JButton color0, color1, color2, color3, color4, color5;
	private JButton startButton, backButton;

	// For drawing
	private Canvas canvas;
	int pointX, pointY;
	private ArrayList<UserPoint> pList;
	private Color drawColor;
	private int drawThick;

	// For game operation
	private ArrayList<UserInfo> usersGame;
	private boolean gameStarted;
	private boolean isQuestioner;
	private long gameTime;
	int answerCount;

	Thread thread;
	private int k = 0;

	// ** CONSTRUCTOR **
	public GamePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		drawColor = Color.black;
		drawThick = 10;

		gameStarted = false;
		isQuestioner = false;
		answerCount = 0;

		setPanel();
		setEvent();
	}

	// ** METHOD **
	private void setPanel() {
		// Initialize data
		this.setLayout(null);
		pList = new ArrayList<UserPoint>();
		usersGame = new ArrayList<UserInfo>();

		// For north panel
		northPanel = new JPanel();
		northPanel.setLayout(null);
		northPanel.setBounds(0, 0, 800, 110);
		northPanel.setBackground(new Color(64, 64, 64));
		titleImage = new JLabel();
		titleImage.setIcon(new ImageIcon("src/images/titlePanel.png"));
		titleImage.setBounds(16, 5, 750, 100);
		northPanel.add(titleImage);
		this.add(northPanel);

		/* For center panel */
		centerPanel = new JPanel(null);
		centerPanel.setBounds(0, 110, 800, 360);
		centerPanel.setBackground(new Color(64,64,64));
		centerPanel.setOpaque(true);

		drawingPanel = new JPanel(null);
		drawingPanel.setBounds(142, 7, 500, 340);
		drawingPanel.setBackground(new Color(64, 64, 64));
		drawingPanel.setOpaque(true);

		/* For drawing tools */
		centerToolPanel = new JPanel(null);
		centerToolPanel.setBounds(0, 0, 500, 33);
		centerToolPanel.setBorder(new LineBorder(new Color(219,219,219), 2));
		// centerToolPanel.setAlignmentX (1.0f);
		// centerToolPanel.setAlignmentY (1.0f);
		StyleContext contextAnswer = new StyleContext();
		StyledDocument documentAnswer = new DefaultStyledDocument(contextAnswer);
		Style styleAnswer = contextAnswer.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(styleAnswer, StyleConstants.ALIGN_CENTER);
		answer = new JTextPane(documentAnswer);
		answer.setBounds(0, 0, 150, 35);
		answer.setFont(new Font(null, Font.BOLD, 15));
		answer.setText("ANSWER");
		answer.setBorder(new LineBorder(new Color(64,64,64), 2));
		answer.setEditable(false);
		clearAll = new JButton("CLEAR");
		clearAll.setBounds(155, 0, 60, 35);
		eraser = new JButton("ERASER");
		eraser.setBounds(220, 0, 60, 35);
		color = new JButton[6];
		for (int i = 0; i < color.length; i++) {
			color[i] = new JButton();
			color[i].setBounds(285 + i*32, 0, 30, 35);
		}
		color[0].setBackground(Color.black);
		color[1].setBackground(Color.red);
		color[2].setBackground(Color.yellow);
		color[3].setBackground(Color.green);
		color[4].setBackground(Color.blue);
		color[5].setBackground(new Color(128, 0, 128));
		StyleContext contextTimer = new StyleContext();
		StyledDocument documentTimer = new DefaultStyledDocument(contextTimer);
		Style styleTimer = contextTimer.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(styleTimer, StyleConstants.ALIGN_CENTER);
		timer = new JTextPane(documentTimer);
		timer.setBounds(440, 0, 60, 35); //?
		timer.setFont(new Font(null, Font.BOLD, 15));
		timer.setText("TIMER");
		timer.setBorder(new LineBorder(Color.black, 2));
		timer.setEditable(false);
		centerToolPanel.add(answer);
		centerToolPanel.add(clearAll);
		centerToolPanel.add(eraser);
		for (int i = 0; i < 6; i++) {
			centerToolPanel.add(color[i]);
		}
		centerToolPanel.add(timer);
		// For drawing canvas
		centerCanvasPanel = new JPanel(null);
		centerCanvasPanel.setBounds(0, 35, 500, 305);
		centerCanvasPanel.setBorder(new LineBorder(new Color(255, 206, 5), 2));
		centerCanvasPanel.add(canvas = new Canvas());
		canvas.setBackground(Color.white);
		canvas.setEnabled(true);
		drawingPanel.add(centerToolPanel);
		drawingPanel.add(centerCanvasPanel);

		centerPanel.add(drawingPanel);

		// For west panel: 2 users
		westPanel = new JPanel(null);
		westPanel.setBounds(12, 10, 130, 336);
		westPanel.setBorder(new LineBorder(new Color(255,206,5), 3));
		westPanel.setBackground(new Color(255, 230, 156));
		westPanel.setOpaque(true);
		for(int i=0; i<2; i++){
			userPanel[i] = new JPanel();
			userChat[i] = new JTextPane();
			userChar[i] = new JLabel();
			userNickname[i] = new JTextPane();
			userNickname[i].setText("");
			userPanel[i].setBounds(0, i*180, 140, 180);
			userPanel[i].setBackground(Color.red);
			userPanel[i].setOpaque(true);
			westPanel.add(userPanel[i]);
		}
		centerPanel.add(westPanel);

		// For east panel: 2 users
		eastPanel = new JPanel(null);
		eastPanel.setBounds(642, 10, 130, 336);
		eastPanel.setBorder(new LineBorder(new Color(255,206,5), 3));
		eastPanel.setBackground(new Color(255, 230, 156));
		for(int i=2; i<4; i++){
			userPanel[i] = new JPanel();
			userChat[i] = new JTextPane();
			userChar[i] = new JLabel();
			userNickname[i] = new JTextPane();
			userNickname[i].setText("");
			userPanel[i].setBounds(0, i*180, 140, 180);
			userPanel[i].setBackground(Color.red);
			userPanel[i].setOpaque(true);
			eastPanel.add(userPanel[i]);
		}
		centerPanel.add(eastPanel);

		// For south panel: chat and buttons
		southPanel = new JPanel(null);
		southPanel.setBounds(0, 470, 800, 50);
		southPanel.setBackground(new Color(64, 64, 64));
		gameChat = new JTextField();
		gameChat.setBounds(240, 0, 250, 40);
		gameChat.setFont(new Font(null, Font.BOLD, 30));
		gameChat.setBorder(new LineBorder(new Color(255,206,5), 4));
		startButton = new JButton(new ImageIcon("src/images/startUp.png"));
		startButton.setBounds(530, 2, 100, 37);
		backButton = new JButton(new ImageIcon("src/images/backUp.png"));
		backButton.setBounds(635, 2, 100, 37);
		southPanel.add(gameChat);
		southPanel.add(startButton);
		southPanel.add(backButton);
		
		this.add(centerPanel);
		this.add(southPanel);
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Initialize reactions in this panel
	 */
	private void setEvent() {
		// Press enter key to finish typing chat
		gameChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isQuestioner) {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.CHAT_GAME);
					pi.set_chat(gameChat.getText());
					GamePanel.this.mainFrame.sendProtocol(pi);
					gameChat.setText("");
				}
			}
		});

		// Click start button to start game
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!gameStarted) {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.START_TRY);
					GamePanel.this.mainFrame.sendProtocol(pi);
				}
			}
		});

		// Click back button to go to the lobby panel
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!gameStarted) {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.EXIT_GAME);
					GamePanel.this.mainFrame.sendProtocol(pi);
					GamePanel.this.mainFrame.setSize(MainFrame.lobbyPwidth, MainFrame.lobbyPheight);
					GamePanel.this.mainFrame.set_currentCard(MainFrame.lobbyPcard);
					GamePanel.this.mainFrame.get_card().show(GamePanel.this.mainFrame.getContentPane(),
							MainFrame.lobbyPcard);
				}
			}
		});

		// Mouse drag to draw
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && isQuestioner) {
					pList = new ArrayList<UserPoint>();
					pList.add(new UserPoint(e.getX(), e.getY()));
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.DRAW);
					pi.set_pList(pList);
					GamePanel.this.mainFrame.sendProtocol(pi);
				}
			}
		});

		// Click clear button to erase the whole canvas
		clearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isQuestioner) {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.SELECT_CLEAR);
					GamePanel.this.mainFrame.sendProtocol(pi);
				}
			}
		});

		// Click eraser button to select eraser
		eraser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isQuestioner) {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.SELECT_ERASER);
					GamePanel.this.mainFrame.sendProtocol(pi);
				}
			}
		});

		// Click color buttons to change select drawing color
		for (k = 0; k < color.length; k++) {
			color[k].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (isQuestioner) {
						ProgressInfo pi = new ProgressInfo();
						pi.set_status(ProgressInfo.SELECT_COLOR);
						pi.set_drawColor(k);
						GamePanel.this.mainFrame.sendProtocol(pi);
					}
				}
			});
		}
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: When a new user enters game,
	 * updates the west&east panel with updated user list in game
	 */
	private void updatePanel() {
		westPanel.removeAll();
		eastPanel.removeAll();
		int size = usersGame.size();
		// Initialize score of each player
		for (UserInfo ui : usersGame) {
			ui.set_score(0);
		}
		answer.setText("ANSWER");
		timer.setText("TIMER");

		// Re-draw userNpanel according the number of users currently in game
		switch (size) {
		case 4: {
			userPanel[3] = new JPanel(new BorderLayout());
			userPanel[3].setBorder(new LineBorder(new Color(157, 195, 230), 4));
			StyleContext contextUser4 = new StyleContext();
			StyledDocument documentUser4 = new DefaultStyledDocument(contextUser4);
			Style styleUser4 = contextUser4.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser4, StyleConstants.ALIGN_CENTER);
			userChat[3] = new JTextPane(documentUser4);
			userChat[3].setPreferredSize(new Dimension(200, 40));
			userChat[3].setFont(new Font(null, Font.BOLD, 20));
			userChat[3].setText("");
			userChat[3].setBorder(new LineBorder(Color.black, 2));
			userChat[3].setEditable(false);
			userChar[3] = new JLabel(new ImageIcon(usersGame.get(size - 4).get_gamecharImagePath()));
			userChar[3].setPreferredSize(new Dimension(100, 100));
			StyleContext contextUser4Nickname = new StyleContext();
			StyledDocument documentUser4Nickname = new DefaultStyledDocument(contextUser4Nickname);
			Style styleUser4Nickname = contextUser4Nickname.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser4Nickname, StyleConstants.ALIGN_CENTER);
			userNickname[3] = new JTextPane(documentUser4Nickname);
			userNickname[3]
					.setText(usersGame.get(size - 4).get_nickName() + " SCORE: " + usersGame.get(size - 4).get_score());
			userNickname[3].setPreferredSize(new Dimension(200, 60));
			userNickname[3].setFont(new Font(null, Font.BOLD, 15));
			userPanel[3].add(BorderLayout.NORTH, userChat[3]);
			userPanel[3].add(BorderLayout.CENTER, userChar[3]);
			userPanel[3].add(BorderLayout.SOUTH, userNickname[3]);
			eastPanel.add(BorderLayout.NORTH, userPanel[3]);
		}
		case 3: {
			userPanel[2] = new JPanel(new BorderLayout());
			userPanel[2].setBorder(new LineBorder(new Color(157, 195, 230), 4));
			StyleContext contextUser3 = new StyleContext();
			StyledDocument documentUser3 = new DefaultStyledDocument(contextUser3);
			Style styleUser3 = contextUser3.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser3, StyleConstants.ALIGN_CENTER);
			userChat[2] = new JTextPane(documentUser3);
			userChat[2].setPreferredSize(new Dimension(200, 40));
			userChat[2].setFont(new Font(null, Font.BOLD, 20));
			userChat[2].setText("");
			userChat[2].setBorder(new LineBorder(Color.black, 2));
			userChat[2].setEditable(false);
			userChar[2] = new JLabel(new ImageIcon(usersGame.get(size - 3).get_gamecharImagePath()));
			userChar[2].setPreferredSize(new Dimension(100, 100));
			StyleContext contextUser3Nickname = new StyleContext();
			StyledDocument documentUser3Nickname = new DefaultStyledDocument(contextUser3Nickname);
			Style styleUser3Nickname = contextUser3Nickname.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser3Nickname, StyleConstants.ALIGN_CENTER);
			userNickname[2] = new JTextPane(documentUser3Nickname);
			userNickname[2]
					.setText(usersGame.get(size - 3).get_nickName() + " SCORE: " + usersGame.get(size - 3).get_score());
			userNickname[2].setPreferredSize(new Dimension(200, 60));
			userNickname[2].setFont(new Font(null, Font.BOLD, 15));
			userPanel[2].add(BorderLayout.NORTH, userChat[2]);
			userPanel[2].add(BorderLayout.CENTER, userChar[2]);
			userPanel[2].add(BorderLayout.SOUTH, userNickname[2]);
			westPanel.add(BorderLayout.SOUTH, userPanel[2]);
		}
		case 2: {
			userPanel[1] = new JPanel(new BorderLayout());
			userPanel[1].setBorder(new LineBorder(new Color(157, 195, 230), 4));
			StyleContext contextUser2 = new StyleContext();
			StyledDocument documentUser2 = new DefaultStyledDocument(contextUser2);
			Style styleUser2 = contextUser2.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser2, StyleConstants.ALIGN_CENTER);
			userChat[1] = new JTextPane(documentUser2);
			userChat[1].setPreferredSize(new Dimension(200, 40));
			userChat[1].setFont(new Font(null, Font.BOLD, 20));
			userChat[1].setText("");
			userChat[1].setBorder(new LineBorder(Color.black, 2));
			userChat[1].setEditable(false);
			userChar[1] = new JLabel(new ImageIcon(usersGame.get(size - 2).get_gamecharImagePath()));
			userChar[1].setPreferredSize(new Dimension(100, 100));
			StyleContext contextUser1Nickname = new StyleContext();
			StyledDocument documentUser1Nickname = new DefaultStyledDocument(contextUser1Nickname);
			Style styleUser1Nickname = contextUser1Nickname.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser1Nickname, StyleConstants.ALIGN_CENTER);
			userNickname[1] = new JTextPane(documentUser1Nickname);
			userNickname[1]
					.setText(usersGame.get(size - 2).get_nickName() + " SCORE: " + usersGame.get(size - 2).get_score());
			userNickname[1].setPreferredSize(new Dimension(200, 60));
			userNickname[1].setFont(new Font(null, Font.BOLD, 15));
			userPanel[1].add(BorderLayout.NORTH, userChat[1]);
			userPanel[1].add(BorderLayout.CENTER, userChar[1]);
			userPanel[1].add(BorderLayout.SOUTH, userNickname[1]);
			westPanel.add(BorderLayout.CENTER, userPanel[1]);
		}
		case 1: {
			userPanel[0] = new JPanel(new BorderLayout());
			userPanel[0].setBorder(new LineBorder(new Color(157, 195, 230), 4));
			StyleContext contextUser1 = new StyleContext();
			StyledDocument documentUser1 = new DefaultStyledDocument(contextUser1);
			Style styleUser1 = contextUser1.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser1, StyleConstants.ALIGN_CENTER);
			userChat[0] = new JTextPane(documentUser1);
			userChat[0].setPreferredSize(new Dimension(200, 40));
			userChat[0].setFont(new Font(null, Font.BOLD, 20));
			userChat[0].setText("");
			userChat[0].setBorder(new LineBorder(Color.black, 2));
			userChat[0].setEditable(false);
			userChar[0] = new JLabel(new ImageIcon(usersGame.get(size - 1).get_gamecharImagePath()));
			userChat[0].setPreferredSize(new Dimension(100, 100));
			StyleContext contextUser1Nickname = new StyleContext();
			StyledDocument documentUser1Nickname = new DefaultStyledDocument(contextUser1Nickname);
			Style styleUser1Nickname = contextUser1Nickname.getStyle(StyleContext.DEFAULT_STYLE);
			StyleConstants.setAlignment(styleUser1Nickname, StyleConstants.ALIGN_CENTER);
			userNickname[0] = new JTextPane(documentUser1Nickname);
			userNickname[0]
					.setText(usersGame.get(size - 1).get_nickName() + " SCORE: " + usersGame.get(size - 1).get_score());
			userNickname[0].setPreferredSize(new Dimension(200, 60));
			userNickname[0].setFont(new Font(null, Font.BOLD, 15));
			userPanel[0].add(BorderLayout.NORTH, userChat[0]);
			userPanel[0].add(BorderLayout.CENTER, userChat[0]);
			userPanel[0].add(BorderLayout.SOUTH, userNickname[0]);
			westPanel.add(BorderLayout.NORTH, userPanel[0]);
		}
		}
		eastPanel.revalidate();
		eastPanel.repaint();
		westPanel.revalidate();
		westPanel.repaint();
	}

	/**
	 * INPUT: nickname of target user to update score, OUTPUT: null, Objective:
	 * Update score of user who got correct answer
	 */
	public void scoreUpdate(String nickName) {
		int score = 0;
		// Update the score of target user
		for (UserInfo ui : usersGame) {
			if (ui.get_nickName().equals(nickName)) {
				ui.inc_score();
				score = ui.get_score();
			}
		}

		// Update display of score for target user
		if (!(userNickname[3].getText().equals(""))) {
			if (userNickname[3].getText().substring(0, userNickname[3].getText().length() - 9).equals(nickName))
				userNickname[3].setText(userNickname[3].getText().substring(0, userNickname[3].getText().length() - 1)
						.concat(String.valueOf(score)));
		}
		if (!(userNickname[2].getText().equals(""))) {
			if (userNickname[2].getText().substring(0, userNickname[2].getText().length() - 9).equals(nickName))
				userNickname[2].setText(userNickname[2].getText().substring(0, userNickname[2].getText().length() - 1)
						.concat(String.valueOf(score)));
		}
		if (!(userNickname[1].getText().equals(""))) {
			if (userNickname[1].getText().substring(0, userNickname[1].getText().length() - 9).equals(nickName))
				userNickname[1].setText(userNickname[1].getText().substring(0, userNickname[1].getText().length() - 1)
						.concat(String.valueOf(score)));
		}
		if (!(userNickname[0].getText().equals(""))) {
			if (userNickname[0].getText().substring(0, userNickname[0].getText().length() - 9).equals(nickName))
				userNickname[0].setText(userNickname[0].getText().substring(0, userNickname[0].getText().length() - 1)
						.concat(String.valueOf(score)));
		}
	}

	/**
	 * INPUT: list of the users in this game, OUTPUT: null, Objective: Invoke
	 * updatePanel function to redraw west&east panel
	 */
	public void joinApproved(ArrayList<UserInfo> usersGame) {
		this.usersGame = usersGame;
		System.out.println("f : " + usersGame);
		updatePanel();
	}

	/**
	 * INPUT: list of the users in this game, OUTPUT: null, Objective: Invoke
	 * updatePanel function to draw west&east panel at first
	 */
	public void createApproved(ArrayList<UserInfo> usersGame) {
		this.usersGame = usersGame;
		updatePanel();
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Inform user that unable to start
	 * game because the user is not game master
	 */
	public void startDeniedMaster() {
		JOptionPane.showMessageDialog(GamePanel.this.mainFrame.getContentPane(), "You are not the game master!");
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Inform user that unable to start
	 * game because there is not enough player
	 */
	public void startDeniedNum() {
		JOptionPane.showMessageDialog(GamePanel.this.mainFrame.getContentPane(), "You need at least two players!");
	}

	/**
	 * INPUT: answer of this round, nickname of questioner, OUTPUT: null,
	 * Objective: Start the game. If the client is questioner, set the answer
	 * panel with the round answer For all client, set the timer with game time
	 */
	public void gameStarted(String roundAnswer, String questioner) {
		if (GamePanel.this.mainFrame.get_myNickname().equals(questioner)) {
			isQuestioner = true;
			answer.setText(roundAnswer);
		} else {
			isQuestioner = false;
			answer.setText("ANSWER");
		}
		gameTime = ROUND_TIME;
		timer.setText(String.valueOf(gameTime));
		gameStarted = true;

		final JOptionPane optionPane = new JOptionPane("ROUND STARTS!", JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		};
		Timer timer = new Timer(1000, action);
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	/**
	 * INPUT: list of points drawn by questioner, OUTPUT: null, Objective: Draw
	 * the canvas with point list, selected color
	 */
	public void drawBroadcasted(ArrayList<UserPoint> pList) {
		Graphics g = canvas.getGraphics();

		for (UserPoint p : pList) {
			System.out.println("(" + p.get_pointX() + ", " + p.get_pointY() + ")");
			g.setColor(drawColor);
			g.fillOval(p.get_pointX(), p.get_pointY(), drawThick, drawThick);
		}
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Clear the canvas
	 */
	public void clearBroadcasted() {
		canvas.repaint();
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Questioner selected eraser. Set the
	 * color as white and set the eraser thickness
	 */
	public void eraserBroadcasted() {
		set_drawColor(6);
		set_drawThick(25);
	}

	/**
	 * INPUT: index of selected color, OUTPUT: null, Objective: Questioner
	 * selected color. Set the color as selected
	 */
	public void colorBroadcasted(int drawingColor) {
		set_drawColor(drawingColor);
		set_drawThick(10);
	}

	/**
	 * INPUT: nickname of questioner, OUTPUT: null, Objective: Set border of the
	 * questioner with blue, set with black for others
	 */
	public void quetionerBorder(String questioner) {
		int size = usersGame.size();
		switch (size) {
		case 4: {
			if (usersGame.get(size - 4).get_nickName().equals(questioner)) {
				userPanel[0].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[1].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[2].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[3].setBorder(new LineBorder(Color.black, 4));
			}
		}
		case 3: {
			if (usersGame.get(size - 3).get_nickName().equals(questioner)) {
				userPanel[0].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[1].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[2].setBorder(new LineBorder(Color.black, 4));
				userPanel[3].setBorder(new LineBorder(new Color(189, 215, 238), 4));
			}
		}
		case 2: {
			if (usersGame.get(size - 2).get_nickName().equals(questioner)) {
				userPanel[0].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[1].setBorder(new LineBorder(Color.black, 4));
				userPanel[2].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[3].setBorder(new LineBorder(new Color(189, 215, 238), 4));
			}
		}
		case 1: {
			if (usersGame.get(size - 1).get_nickName().equals(questioner)) {
				userPanel[0].setBorder(new LineBorder(Color.black, 4));
				userPanel[1].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[2].setBorder(new LineBorder(new Color(189, 215, 238), 4));
				userPanel[3].setBorder(new LineBorder(new Color(189, 215, 238), 4));
			}
		}
		}
	}

	/**
	 * INPUT: nickname of the chat's owner, contents of chat, OUTPUT: null,
	 * Objective: Update the chat fields for the players in game
	 */
	public void gameChatUpdate(String nickName, String chat) {
		if (!(userNickname[3].getText().equals(""))) {
			if (userNickname[3].getText().substring(0, userNickname[3].getText().length() - 9).equals(nickName))
				userChat[3].setText(chat);
		}
		if (!(userNickname[2].getText().equals(""))) {
			if (userNickname[2].getText().substring(0, userNickname[2].getText().length() - 9).equals(nickName))
				userChat[2].setText(chat);
		}
		if (!(userNickname[1].getText().equals(""))) {
			if (userNickname[1].getText().substring(0, userNickname[1].getText().length() - 9).equals(nickName))
				userChat[1].setText(chat);
		}
		if (!(userNickname[0].getText().equals(""))) {
			if (userNickname[0].getText().substring(0, userNickname[0].getText().length() - 9).equals(nickName))
				userChat[0].setText(chat);
		}
	}

	/**
	 * INPUT: nickname of the correct chat's owner, contents of answer, OUTPUT:
	 * null, Objective: Inform all the clients the answer and that the chat's
	 * owner got correct
	 */
	public void correctAnswer(String nickName, String answer) {
		gameStarted = false;
		String message = "";
		if (!(userNickname[3].getText().equals(""))) {
			if (userNickname[3].getText().substring(0, userNickname[3].getText().length() - 9).equals(nickName))
				message = new String(userNickname[3].getText().substring(0, userNickname[3].getText().length() - 9)
						+ " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(userNickname[2].getText().equals(""))) {
			if (userNickname[2].getText().substring(0, userNickname[2].getText().length() - 9).equals(nickName))
				message = new String(userNickname[2].getText().substring(0, userNickname[2].getText().length() - 9)
						+ " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(userNickname[1].getText().equals(""))) {
			if (userNickname[1].getText().substring(0, userNickname[1].getText().length() - 9).equals(nickName))
				message = new String(userNickname[1].getText().substring(0, userNickname[1].getText().length() - 9)
						+ " got correct!\n" + "ANSWER: " + answer);
		}
		if (!(userNickname[0].getText().equals(""))) {
			if (userNickname[0].getText().substring(0, userNickname[0].getText().length() - 9).equals(nickName))
				message = new String(userNickname[0].getText().substring(0, userNickname[0].getText().length() - 9)
						+ " got correct!\n" + "ANSWER: " + answer);
		}
		final JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				if (isQuestioner) {
					// Questioner sends the server to notify that the round
					// ended
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.TIMER_EXPIRE);
					GamePanel.this.mainFrame.sendProtocol(pi);
				}
			}
		};
		Timer timer = new Timer(2000, action);
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	/**
	 * INPUT: message noticing that the game ended, OUTPUT: null, Objective:
	 * Redraw all the panel as waiting state (not started)
	 */
	public void roundTerminated(String message) {
		gameStarted = false;
		final JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, new Object[] {}, null);
		final JDialog dialog = new JDialog();
		dialog.setTitle("");
		dialog.setModal(true);
		dialog.setContentPane(optionPane);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.pack();
		ActionListener action = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				updatePanel();
			}
		};
		Timer timer = new Timer(4000, action);
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: When the server notifies that 1
	 * second elapsed If game is playing, decrement the in-game timer If in-game
	 * timer becomes zero, notice the server that the round ended
	 */
	public void timerBroadcasted() {
		if (gameStarted) {
			gameTime--;
			timer.setText(String.valueOf(gameTime));
			if (gameTime == 0) {
				gameStarted = false;
				if (isQuestioner) {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.TIMER_EXPIRE);
					GamePanel.this.mainFrame.sendProtocol(pi);
				}
			}
		}
	}

	// Get methods
	public Color get_drawColor() {
		return drawColor;
	}

	public int get_drawThick() {
		return drawThick;
	}

	// Set methods
	public void set_drawThick(int item) {
		drawThick = item;
	}

	public void set_drawColor(int option) {
		switch (option) {
		case 0: {
			drawColor = Color.black;
			break;
		}
		case 1: {
			drawColor = Color.red;
			break;
		}
		case 2: {
			drawColor = Color.yellow;
			break;
		}
		case 3: {
			drawColor = Color.green;
			break;
		}
		case 4: {
			drawColor = Color.blue;
			break;
		}
		case 5: {
			drawColor = new Color(128, 0, 128);
			break;
		}
		case 6: {
			drawColor = Color.white;
			break;
		}
		}
	}
}