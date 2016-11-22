package panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import java.util.ArrayList;

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

	private JPanel gameListPanel, userListPanel, lobbyChat, infoAndButton;
	private JScrollPane gameListScroll, userListScroll;
	private JList<String> gameList, userList;

	private JTextArea showChat;
	private JTextField typeChat;

	private JPanel myInfo, buttonPanel;
	private JLabel myChar;
	private JLabel [] idLabel;
    private JLabel [] charNameLabel;
    private JLabel [] levelLabel;
	private JButton createButton, backButton;
	private JDialog createDialog;

	private String[] recent8Chat, gamesLobby, usersLobby;

	// ** CONSTRUCTOR **
	public LobbyPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		recent8Chat = new String[8]; // In lobby, show only recent 8 chats
		initR8C();
		initCreateDialog();
		setPanel();
		setEvent();
	}

	// ** METHOD **
	/**
	 * INPUT: null, OUTPUT: null, Objective: Initialize the 8 recent chats in
	 * lobby as empty strings
	 */
	private void initR8C() {
		for (int i = 0; i < 8; i++) {
			recent8Chat[i] = "";
		}
	}

	/**
	 * INPUT: null OUTPUT: null Objective: Initialize the dialog box for
	 * creating a new game
	 */
	public void initCreateDialog() {
		createDialog = new JDialog();
		CreateDialog cd = new CreateDialog(this);
		createDialog.setContentPane(cd);
		createDialog.setBounds(400, 300, 400, 300);
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
		lobbyChat = new JPanel(null);
		lobbyChat.setBounds(471, 0, 310, 400);
		lobbyChatLabel = new JLabel(new ImageIcon("src/images/lobbyChatLabel.png"));
		lobbyChatLabel.setBounds(0, 0, 310, 30);
		showChat = new JTextArea();
		showChat.setBounds(0, 30, 310, 335);
		showChat.setBackground(new Color(255, 230, 153));
		showChat.setFont(new Font(ProgressInfo.FONT, Font.BOLD, 15));
		showChat.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		showChat.setEditable(false);
		typeChat = new JTextField();
		typeChat.setPreferredSize(new Dimension(400, 60));
		typeChat.setFont(new Font(ProgressInfo.FONT, Font.BOLD, 30));
		typeChat.setBorder(new LineBorder(new Color(91, 155, 213), 4));
		lobbyChat.add(lobbyChatLabel);
		lobbyChat.add(showChat);
		lobbyChat.add(typeChat);

		centerPanel.add(gameListPanel);
		centerPanel.add(userListPanel);
		this.add(centerPanel);
		centerPanel.add(lobbyChat);
		centerPanel.add(infoAndButton);
		// this.add(BorderLayout.SOUTH, southPanel);

		repaint();
		invalidate();
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Initialize reactions in this panel
	 */
	private void setEvent() {
		// Press enter key to finish typing chat
		typeChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgressInfo pi = new ProgressInfo();
				pi.set_status(ProgressInfo.CHAT_LOBBY);
				pi.set_chat(typeChat.getText());
				LobbyPanel.this.mainFrame.sendProtocol(pi);
				typeChat.setText("");
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
						System.out.println("I'm here!");
						ProgressInfo pi = new ProgressInfo();
						pi.set_status(ProgressInfo.JOIN_GAME_TRY);
						pi.set_chat(gameList.getSelectedValue().trim());
						LobbyPanel.this.mainFrame.sendProtocol(pi);
					}
				}
			}
		});
	}

	/**
	 * INPUT: updated list of users in lobby, OUTPUT: null, Objective: Update
	 * the list of user in lobby: Right of center panel
	 */
	public void updateLobbyUser(ArrayList<String> updated) {
		usersLobby = new String[updated.size()];
		for (int i = 0; i < updated.size(); i++) {
			usersLobby[i] = "  " + updated.get(i);
		}
		userList.setListData(usersLobby);
	}

	/**
	 * INPUT: updated list of games in lobby, OUTPUT: null, Objective: Update
	 * the list of game in lobby: Left of center panel
	 */
	public void updateLobbyGame(ArrayList<String> updated) {
		gamesLobby = new String[updated.size()];
		for (int i = 0; i < updated.size(); i++) {
			gamesLobby[i] = "  " + updated.get(i);
		}
		gameList.setListData(gamesLobby);
	}

	/**
	 * INPUT: most recent chat by user in lobby, OUTPUT: null, Objective: Update
	 * the list of 8 recent chat and display it to lobby chat panel
	 */
	public void updateLobbyChat(String lobbyChat) {
		for (int i = 7; i > 0; i--) {
			recent8Chat[i] = recent8Chat[i - 1];
		}
		recent8Chat[0] = lobbyChat;
		showChat.setText("");
		for (int i = 7; i >= 0; i--) {
			showChat.append("  " + recent8Chat[i] + "\r\n");
		}
	}

	/**
	 * INPUT: null OUTPUT: null Objective: Invoked when user clicked the create
	 * room button, show the dialog box
	 */
	public void showCreateDialog() {
		createDialog.setVisible(true);
	}

	/**
	 * INPUT: name of the game to join OUTPUT: null Objective: Change the
	 * client's display to game panel when joining game succeeds
	 */
	public void joinApproved(String gameName) {
		ProgressInfo pi = new ProgressInfo();
		pi.set_status(ProgressInfo.JOIN_GAME);
		pi.set_chat(gameName);
		LobbyPanel.this.mainFrame.sendProtocol(pi);
		LobbyPanel.this.mainFrame.setSize(MainFrame.gamePwidth, MainFrame.gamePheight);
		LobbyPanel.this.mainFrame.set_currentCard(MainFrame.gamePcard);
		LobbyPanel.this.mainFrame.get_card().show(LobbyPanel.this.mainFrame.getContentPane(), MainFrame.gamePcard);
	}

	/**
	 * INPUT: null OUTPUT: null Objective: Notice the client that joining game
	 * failed because the game is full or already started
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
	 * INPUT: null, OUTPUT: null, Objective: Close the dialog box for creating a
	 * new game when user ,succeeds of cancels creating
	 */
	public void closeCreateDialog() {
		createDialog.setVisible(false);
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Display the client's information in
	 * South-right panel
	 */
	public void myInfoUpdate() {
		myInfo.removeAll();
		myChar = new JLabel(new ImageIcon(this.mainFrame.get_myLobbyImagePath()));
		myChar.setBounds(5, 5, 100, 120);
		myChar.setBackground(Color.red);
		myChar.setOpaque(true);
		myInfo.add(myChar);
		
		idLabel = new JLabel[2];
        for(int i=0; i<idLabel.length; i++) {
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
        for(int i=0; i<charNameLabel.length; i++) {
           charNameLabel[i] = new JLabel("Char:");
        }
        
        charNameLabel[0].setFont(new Font(ProgressInfo.FONT, Font.BOLD, 16));
        charNameLabel[0].setBounds(110, 45, 50, 20);
        myInfo.add(charNameLabel[0]);
        
        charNameLabel[1].setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 14));
        charNameLabel[1].setText("임시charName!");
        charNameLabel[1].setBounds(110, 60, 135, 20);
        myInfo.add(charNameLabel[1]);
        
        levelLabel = new JLabel[2];
        for(int i=0; i<levelLabel.length; i++) {
           levelLabel[i] = new JLabel("Level:");
        }
        
        levelLabel[0].setFont(new Font(ProgressInfo.FONT, Font.BOLD, 16));
        levelLabel[0].setBounds(110, 85, 50, 20);
        myInfo.add(levelLabel[0]);
        
        levelLabel[1].setFont(new Font(ProgressInfo.FONT, Font.PLAIN, 14));
        levelLabel[1].setText("임시level가져와!" + Integer.toString(1));
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
	JTextField typeRoomName;
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
		message.setPreferredSize(new Dimension(400, 100));
		message.setBackground(new Color(222, 235, 247));
		message.setFont(new Font(null, Font.PLAIN, 25));
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setVerticalAlignment(JLabel.CENTER);
		this.add(BorderLayout.NORTH, message);

		// Textfield for user to type game name
		typeRoomName = new JTextField();
		typeRoomName.setPreferredSize(new Dimension(400, 60));
		typeRoomName.setFont(new Font(null, Font.BOLD, 30));
		typeRoomName.setBorder(new LineBorder(new Color(91, 155, 213), 4));
		this.add(BorderLayout.CENTER, typeRoomName);

		// South panel for buttons
		southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.setPreferredSize(new Dimension(400, 100));
		southPanel.setBackground(Color.green);
		southPanel.setBackground(new Color(222, 235, 247));
		createButton = new JButton(new ImageIcon("src/images/createButton.png"));
		createButton.setPreferredSize(new Dimension(180, 80));
		exitButton = new JButton(new ImageIcon("src/images/backButton.png"));
		exitButton.setPreferredSize(new Dimension(180, 80));
		southPanel.add(createButton);
		southPanel.add(exitButton);
		this.add(BorderLayout.SOUTH, southPanel);
	}

	/**
	 * INPUT: null, OUTPUT: null, Objective: Initialize reactions in this dialog
	 * box
	 */
	private void setEvent() {
		// Press enter key
		typeRoomName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (typeRoomName.getText().equals(""))
					JOptionPane.showMessageDialog(CreateDialog.this.lp.mainFrame.getContentPane(),
							"Please enter room name.");
				else {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.CREATE_GAME_TRY);
					pi.set_chat(typeRoomName.getText());
					CreateDialog.this.lp.mainFrame.sendProtocol(pi);
					typeRoomName.setText("");
				}
			}
		});

		// Click the create button to try creating
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (typeRoomName.getText().equals(""))
					JOptionPane.showMessageDialog(CreateDialog.this.lp.mainFrame.getContentPane(),
							"Please enter room name.");
				else {
					ProgressInfo pi = new ProgressInfo();
					pi.set_status(ProgressInfo.CREATE_GAME_TRY);
					pi.set_chat(typeRoomName.getText());
					CreateDialog.this.lp.mainFrame.sendProtocol(pi);
					typeRoomName.setText("");
				}
			}
		});

		// Click the exit button to cancel creating
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeRoomName.setText("");
				CreateDialog.this.lp.closeCreateDialog();
			}
		});
	}
}