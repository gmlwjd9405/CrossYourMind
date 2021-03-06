package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import frame.MainFrame;
import info.ProgressInfo;

public class LobbyPanel extends JPanel {
	// ** VARIABLE **
	// Connect to its parent frame
	MainFrame mainFrame;
	// For inner panels
	private JPanel northPanel, centerPanel;
	private JLabel titleImage;
	private JLabel gameListLabel, userListLabel, myinfoLabel, lobbyChatLabel;

	private JPanel gameListPanel, userListPanel, lobbyChatPanel, infoAndButton;
	private JScrollPane gameListScroll, userListScroll, chattingScroll;
	private JList<String> gameList, userList;

	// private JTextArea showChat;
	private JTextPane ChattingPane;
	private JTextField lobbyChatTextField;

	private JPanel myInfo, buttonPanel;
	private JLabel myChar;
	private JLabel[] idLabel;
	private JLabel[] charNameLabel;
	private JLabel[] levelLabel;
	private JButton createButton, backButton;
	private JDialog createDialog;

	// private String[] recent8Chat;
	private String[] gamesLobby, usersLobby;

	// ** CONSTRUCTOR **
	public LobbyPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		initCreateDialog();
		setPanel();
		setEvent();
	}

	// ** METHOD **

	/**
	 * Initialize the dialog box for creating a new game
	 */
	public void initCreateDialog() {
		createDialog = new JDialog();
		CreateDialog cd = new CreateDialog(this);
		createDialog.setContentPane(cd);
		createDialog.setBounds(400, 300, 350, 150);
		createDialog.setResizable(false);
		createDialog.setVisible(false);
	}

	/** INPUT: null, OUTPUT: null, Objective: Initialize the panels */
	private void setPanel() {
		this.setLayout(null);

		/* For north panel */
		northPanel = new JPanel();
		northPanel.setLayout(null);
		northPanel.setBounds(0, 0, 800, 110);
		northPanel.setBackground(new Color(64, 64, 64));
		titleImage = new JLabel();
		titleImage.setIcon(new ImageIcon("src/images/titlePanel.png"));
		titleImage.setBounds(22, 5, 750, 100);
		northPanel.add(titleImage);
		this.add(northPanel);

		/* For center panel */
		centerPanel = new JPanel(null);
		centerPanel.setBounds(0, 110, 800, 410);
		centerPanel.setBackground(new Color(64, 64, 64));
		centerPanel.setOpaque(true);

		gameListPanel = new JPanel(null);
		gameListPanel.setBounds(11, 0, 200, 400);
		gameListLabel = new JLabel(new ImageIcon("src/images/gameListLabel.png"));
		gameListLabel.setBounds(0, 0, 200, 30);
		gameList = new JList<String>();
		gameList.setBackground(new Color(255, 230, 153));
		gameList.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		gameList.setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 20));
		gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		gameListScroll = new JScrollPane(gameList);
		gameListScroll.setBounds(0, 30, 200, 370);
		gameListPanel.add(gameListLabel);
		gameListPanel.add(gameListScroll);

		userListPanel = new JPanel(null);
		userListPanel.setBounds(216, 0, 250, 200);
		userListLabel = new JLabel(new ImageIcon("src/images/userListLabel.png"));
		userListLabel.setBounds(0, 0, 250, 30);
		userList = new JList<String>();
		userList.setBackground(new Color(255, 230, 153));
		userList.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		userList.setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 20));
		userListScroll = new JScrollPane(userList);
		userListScroll.setBounds(0, 30, 250, 170);
		userListPanel.add(userListLabel);
		userListPanel.add(userListScroll);

		// Right of the south panel: Information of this client & buttons
		infoAndButton = new JPanel(null);
		infoAndButton.setBounds(216, 205, 250, 210);
		infoAndButton.setBackground(new Color(64, 64, 64));
		myinfoLabel = new JLabel(new ImageIcon("src/images/myInfoLabel.png"));
		myinfoLabel.setBounds(0, 0, 250, 30);

		myInfo = new JPanel(null);
		myInfo.setBounds(0, 30, 250, 130);
		myInfo.setBackground(new Color(255, 230, 153));
		myInfo.setOpaque(true);
		myInfo.setBorder(new LineBorder(new Color(255, 206, 5), 4));

		buttonPanel = new JPanel(null);
		buttonPanel.setBounds(0, 162, 250, 38);
		buttonPanel.setBackground(new Color(64, 64, 64));
		buttonPanel.setOpaque(true);
		createButton = new JButton(new ImageIcon("src/images/createUp.png"));
		createButton.setBounds(20, -2, 100, 37);
		createButton.setBackground(new Color(64, 64, 64));
		createButton.setOpaque(true);
		backButton = new JButton(new ImageIcon("src/images/backUp.png"));
		backButton.setBounds(130, -2, 100, 37);
		backButton.setBackground(new Color(64, 64, 64));
		backButton.setOpaque(true);
		buttonPanel.add(createButton);
		buttonPanel.add(backButton);

		infoAndButton.add(myinfoLabel);
		infoAndButton.add(myInfo);
		infoAndButton.add(buttonPanel);

		// Left of the south panel: chats in lobby
		lobbyChatPanel = new JPanel(null);
		lobbyChatPanel.setBounds(471, 0, 310, 400);
		lobbyChatLabel = new JLabel(new ImageIcon("src/images/lobbyChatLabel.png"));
		lobbyChatLabel.setBounds(0, 0, 310, 30);

		chattingScroll = new JScrollPane();
		chattingScroll.setBounds(0, 30, 310, 335);
		ChattingPane = new JTextPane();
		chattingScroll.setViewportView(ChattingPane);
		ChattingPane.setDisabledTextColor(new Color(0, 0, 0));
		ChattingPane.setBackground(new Color(255, 230, 153));
		ChattingPane.setFont(new Font(ProgressInfo.FONT, Font.BOLD, 15));
		ChattingPane.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		ChattingPane.setEditable(false);

		lobbyChatTextField = new JTextField();
		lobbyChatTextField.setBounds(1, 360, 240, 40);
		lobbyChatTextField.setFont(new Font(ProgressInfo.FONT, Font.BOLD, 20));
		lobbyChatTextField.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		lobbyChatPanel.add(lobbyChatLabel);
		lobbyChatPanel.add(chattingScroll);
		lobbyChatPanel.add(lobbyChatTextField);

		centerPanel.add(gameListPanel);
		centerPanel.add(userListPanel);
		this.add(centerPanel);
		centerPanel.add(lobbyChatPanel);
		centerPanel.add(infoAndButton);

		repaint();
		invalidate();
	}

	/**
	 * Initialize reactions in this panel
	 */
	private void setEvent() {
		// Press enter key to finish typing chat
		lobbyChatTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgressInfo pi = new ProgressInfo();
				pi.set_status(ProgressInfo.CHAT_LOBBY);
				pi.set_chat(lobbyChatTextField.getText()); //lobby 채팅입력
				LobbyPanel.this.mainFrame.sendProtocol(pi);
				lobbyChatTextField.setText("");
			}
		});

		// Click create button to create a new game
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCreateDialog();
			}
		});

		// Click back button to go the entry panel
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgressInfo pi = new ProgressInfo();
				pi.set_status(ProgressInfo.EXIT_LOBBY);
				LobbyPanel.this.mainFrame.sendProtocol(pi);
				LobbyPanel.this.mainFrame.setSize(MainFrame.entryPwidth, MainFrame.entryPheight);
				LobbyPanel.this.mainFrame.set_currentCard(MainFrame.entryPcard);
				LobbyPanel.this.mainFrame.get_card().show(LobbyPanel.this.mainFrame.getContentPane(),
						MainFrame.entryPcard);
				// lobbyPanel.this.f.setDefaultCloseOperation
				// (JFrame.DO_NOTHING_ON_CLOSE);
			}
		});

		// Double click the game to join
		gameList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (gameList.getSelectedIndex() == -1) {
						JOptionPane.showMessageDialog(LobbyPanel.this.mainFrame.getContentPane(), "Select the room.");
					} else {
						System.out.println("<LobbyPanel> join the game(gameList double click)");
						ProgressInfo pi = new ProgressInfo();
						pi.set_status(ProgressInfo.JOIN_GAME_TRY);
						//pi.set_chat(gameList.getSelectedValue().trim()); //들어갈 게임방의 이름
						pi.set_RoomName(gameList.getSelectedValue().trim()); //들어갈 게임방의 이름
						System.out.println("<LobbyPanel> set_RoomName: " + gameList.getSelectedValue().trim());
						LobbyPanel.this.mainFrame.sendProtocol(pi);
					}
				}
			}
		});
	}

	/**
	 * Update the list of user in lobby: Right of center panel
	 * 
	 * @param updated
	 *            list of users in lobby
	 */
	public void updateLobbyUser(ArrayList<String> updated) {
		usersLobby = new String[updated.size()];
		for (int i = 0; i < updated.size(); i++) {
			usersLobby[i] = "  " + updated.get(i);
		}
		userList.setListData(usersLobby);
	}

	/**
	 * Update the list of game in lobby: Left of center panel
	 * 
	 * @param updated
	 *            list of games in lobby
	 */
	public void updateLobbyGame(ArrayList<String> updated) {
		gamesLobby = new String[updated.size()];
		for (int i = 0; i < updated.size(); i++) {
			gamesLobby[i] = "  " + updated.get(i);
		}
		gameList.setListData(gamesLobby);
	}

	/**
	 * Update the list of 8 recent chat and display it to lobby chat panel
	 * 
	 * @param most
	 *            recent chat by user in lobby
	 */
	public void updateLobbyChat(String lobbyChat) {

		if (lobbyChat.contains(this.mainFrame.get_myNickname() + ":")) {
			ChattingPane.setFont(new Font(ProgressInfo.FONT, Font.BOLD, 15));
			SimpleAttributeSet attribs = new SimpleAttributeSet();
			StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
			ChattingPane.setParagraphAttributes(attribs, true);
		}

		try {
			Document doc = ChattingPane.getDocument();
			doc.insertString(doc.getLength(), lobbyChat + "\n", null);
		} catch (BadLocationException exc) {
			exc.printStackTrace();
		}
	}

	/**
	 * Invoked when user clicked the create room button, show the dialog box
	 */
	public void showCreateDialog() {
		createDialog.setVisible(true);
	}

	/**
	 * Change the client's display to game panel when joining game succeeds
	 * 
	 * @param name
	 *            of the game to join
	 */
	public void joinApproved(String gameName) {
		ProgressInfo pi = new ProgressInfo();
		pi.set_status(ProgressInfo.JOIN_GAME);
		System.out.println("<LobbyPanel> set_RoomName gameName: " + gameName);
		//heee
		//pi.set_chat(gameName);
		pi.set_RoomName(gameName);
		LobbyPanel.this.mainFrame.sendProtocol(pi);
		LobbyPanel.this.mainFrame.setSize(MainFrame.gamePwidth, MainFrame.gamePheight);
		LobbyPanel.this.mainFrame.set_currentCard(MainFrame.gamePcard);
		LobbyPanel.this.mainFrame.get_card().show(LobbyPanel.this.mainFrame.getContentPane(), MainFrame.gamePcard);
	}

	/**
	 * Notice the client that joining game failed because the game is full or
	 * already started
	 */
	public void joinDenied() {
		JOptionPane.showMessageDialog(LobbyPanel.this.mainFrame.getContentPane(),
				"The game is full or already started.");
	}

	/**
	 * INPUT: null OUTPUT: null Objective: Change the client's display to game
	 * panel when creating game succeeds
	 */
	public void createApproved() {
		LobbyPanel.this.mainFrame.setSize(MainFrame.gamePwidth, MainFrame.gamePheight);
		LobbyPanel.this.mainFrame.set_currentCard(MainFrame.gamePcard);
		LobbyPanel.this.mainFrame.get_card().show(LobbyPanel.this.mainFrame.getContentPane(), MainFrame.gamePcard);
		LobbyPanel.this.closeCreateDialog();
	}

	/**
	 * Close the dialog box for creating a new game when user ,succeeds of
	 * cancels creating
	 */
	public void closeCreateDialog() {
		createDialog.setVisible(false);
	}

	/**
	 * Display the client's information in South-right panel
	 */
	public void myInfoUpdate() {
		myInfo.removeAll();
		myChar = new JLabel(new ImageIcon(this.mainFrame.get_myLobbyImagePath()));
		myChar.setBounds(5, 5, 100, 120);
		myChar.setBackground(Color.red);
		myChar.setOpaque(true);
		myInfo.add(myChar);

		idLabel = new JLabel[2];
		for (int i = 0; i < idLabel.length; i++) {
			idLabel[i] = new JLabel("Id:");
		}

		idLabel[0].setFont(new Font(ProgressInfo.FONT, Font.BOLD, 16));
		idLabel[0].setBounds(110, 5, 50, 20);
		myInfo.add(idLabel[0]);

		idLabel[1].setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 14));
		idLabel[1].setText(this.mainFrame.get_myNickname());
		idLabel[1].setBounds(110, 20, 135, 20);
		myInfo.add(idLabel[1]);

		charNameLabel = new JLabel[2];
		for (int i = 0; i < charNameLabel.length; i++) {
			charNameLabel[i] = new JLabel("Char:");
		}

		charNameLabel[0].setFont(new Font(ProgressInfo.FONT, Font.BOLD, 16));
		charNameLabel[0].setBounds(110, 45, 50, 20);
		myInfo.add(charNameLabel[0]);

		charNameLabel[1].setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 14));
		charNameLabel[1].setText(this.mainFrame.get_myCharName());
		charNameLabel[1].setBounds(110, 60, 135, 20);
		myInfo.add(charNameLabel[1]);

		levelLabel = new JLabel[2];
		for (int i = 0; i < levelLabel.length; i++) {
			levelLabel[i] = new JLabel("Level:");
		}

		levelLabel[0].setFont(new Font(ProgressInfo.FONT, Font.BOLD, 16));
		levelLabel[0].setBounds(110, 85, 50, 20);
		myInfo.add(levelLabel[0]);

		levelLabel[1].setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 14));
		levelLabel[1].setText(Integer.toString(this.mainFrame.get_myLevel()));
		levelLabel[1].setBounds(110, 100, 135, 20);
		myInfo.add(levelLabel[1]);

		myInfo.repaint();
	}
}

// Private class in lobby panel: Dialog box for creating a new game
class CreateDialog extends JPanel {
	// ** VARIABLE **
	// Connect to its parent panel
	LobbyPanel lp;
	// For inner panels
	JPanel southPanel;
	JLabel message;
	JTextField roomNameTextField;
	JButton createButton, exitButton;

	// ** CONSTRUCTOR **
	public CreateDialog(LobbyPanel lp) {
		this.lp = lp;
		setPanel();
		setEvent();
	}

	// ** METHOD **
	/**
	 * INPUT: null, OUTPUT: null, Objective: Initialize the panels
	 */
	private void setPanel() {
		this.setLayout(new BorderLayout());
		// Inform user to enter the game name
		message = new JLabel("Enter the name of game room.");
		message.setPreferredSize(new Dimension(200, 30));
		message.setBackground(new Color(242, 242, 242));
		message.setOpaque(true);
		message.setFont(new Font(ProgressInfo.FONT, Font.BOLD, 20));
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVerticalAlignment(JLabel.CENTER);
		this.add(BorderLayout.NORTH, message);

		// Textfield for user to type game name
		roomNameTextField = new JTextField();
		roomNameTextField.setPreferredSize(new Dimension(200, 60));
		roomNameTextField.setFont(new Font(null, Font.BOLD, 30));
		roomNameTextField.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		this.add(BorderLayout.CENTER, roomNameTextField);

		// South panel for buttons
		southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.setPreferredSize(new Dimension(150, 50));
		southPanel.setBackground(new Color(242, 242, 242));
		createButton = new JButton(new ImageIcon("src/images/createUp.png"));
		createButton.setPreferredSize(new Dimension(100, 37));
		exitButton = new JButton(new ImageIcon("src/images/backUp.png"));
		exitButton.setPreferredSize(new Dimension(100, 37));
		southPanel.add(createButton);
		southPanel.add(exitButton);
		this.add(BorderLayout.SOUTH, southPanel);
	}

	/**
	 * Initialize reactions in this dialog box
	 */
	private void setEvent() {
		// Press enter key
		roomNameTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (roomNameTextField.getText().equals(""))
					JOptionPane.showMessageDialog(CreateDialog.this.lp.mainFrame.getContentPane(),
							"Please enter room name.");
				else {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.CREATE_GAME_TRY);
					//pi.set_chat(roomNameTextField.getText()); //room 이름 설정
					pi.set_RoomName(roomNameTextField.getText()); //room 이름 설정
					CreateDialog.this.lp.mainFrame.sendProtocol(pi);
					roomNameTextField.setText("");
				}
			}
		});

		// Click the create button to try creating
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (roomNameTextField.getText().equals(""))
					JOptionPane.showMessageDialog(CreateDialog.this.lp.mainFrame.getContentPane(),
							"Please enter room name.");
				else {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.CREATE_GAME_TRY);
					//pi.set_chat(roomNameTextField.getText()); //room 이름 설정
					pi.set_RoomName(roomNameTextField.getText()); //room 이름 설정
					CreateDialog.this.lp.mainFrame.sendProtocol(pi);
					roomNameTextField.setText("");
				}
			}
		});

		// Click the exit button to cancel creating
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roomNameTextField.setText("");
				CreateDialog.this.lp.closeCreateDialog();
			}
		});
	}
}