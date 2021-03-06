package panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.util.ArrayList;

import frame.MainFrame;
import info.ProgressInfo;

public class EntryPanel extends JPanel {
	// ** VARIABLE **
	// Connect to its parent frame
	private MainFrame mainFrame;

	// For inner panels
	private JPanel northPanel, centerPanel, southPanel;
	private JLabel titleImage;
	private JButton[] CH;
	private JButton enterButton;
	private ArrayList<Icon> charImages = new ArrayList<Icon>();;
	private ArrayList<Icon> charPressedImages = new ArrayList<Icon>();;
	private JTextField nickNameTextField;

	private String imagePath;
	private int selectImageNum;
	InputActionListener inputActionListener = new InputActionListener();

	// ** CONSTRUCTOR **
	public EntryPanel(MainFrame f) {
		this.mainFrame = f;
		initCharImages();
		imagePath = "";
		setPanel();
		setEvent();
	}

	// ** METHOD **
	/** Initialize the panels */
	private void setPanel() {
		this.setLayout(new BorderLayout());

		// For north panel
		northPanel = new JPanel(new FlowLayout());
		northPanel.setPreferredSize(new Dimension(800, 110));
		northPanel.setBackground(new Color(64, 64, 64));
		titleImage = new JLabel();
		titleImage.setIcon(new ImageIcon("src/images/titlePanel.png"));
		titleImage.setPreferredSize(new Dimension(750, 100));
		northPanel.add(titleImage);
		this.add(BorderLayout.NORTH, northPanel);

		// For center panel
		centerPanel = new JPanel(new FlowLayout());
		centerPanel.setPreferredSize(new Dimension(800, 400));
		centerPanel.setBackground(Color.gray);
		initCH();
		this.add(BorderLayout.CENTER, centerPanel);

		// For south panel
		southPanel = new JPanel(new FlowLayout());
		southPanel.setPreferredSize(new Dimension(800, 50));
		southPanel.setBackground(new Color(64, 64, 64));
		nickNameTextField = new JTextField();
		nickNameTextField.setPreferredSize(new Dimension(250, 40));
		nickNameTextField.setBackground(new Color(255, 230, 153));
		nickNameTextField.setFont(new Font(null, Font.BOLD, 25));
		nickNameTextField.setBorder(new LineBorder(new Color(255, 206, 5), 4));
		enterButton = new JButton(new ImageIcon("src/images/enterUp.png"));
		enterButton.setBackground(new Color(64, 64, 64));
		enterButton.setOpaque(true);
		enterButton.setPreferredSize(new Dimension(100, 37));
		southPanel.add(nickNameTextField);
		southPanel.add(enterButton);
		this.add(BorderLayout.SOUTH, southPanel);
	}

	/** Initialize reactions in this panel */
	private void setEvent() {
		// Click buttons for each character to select
		CH[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMaster(0);
			}
		});
		CH[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMaster(1);
			}
		});
		CH[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMaster(2);
			}
		});
		CH[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMaster(3);
			}
		});
		CH[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMaster(4);
			}
		});

		nickNameTextField.addActionListener(inputActionListener);
		enterButton.addActionListener(inputActionListener);
	}

	/** Initialize buttons for characters */
	private void initCH() {
		CH = new JButton[5];
		for (int i = 0; i < 5; i++) {
			CH[i] = new JButton(charImages.get(i));
			CH[i].setPreferredSize(new Dimension(135, 350));
			CH[i].setBorder(new LineBorder(Color.black, 6));
			centerPanel.add(CH[i]);
		}
	}

	/** Initialize images for characters */
	private void initCharImages() {
		ArrayList<String> imagePath = new ArrayList<String>();
		ArrayList<String> imagePathBtnPressed = new ArrayList<String>();

		imagePath = this.mainFrame.getCharImageList();
		imagePathBtnPressed = this.mainFrame.getCharEnteredImageList();

		int length = imagePath.size();
		for (int i = 0; i < length; i++) {
			charImages.add(new ImageIcon(imagePath.get(i)));
			charPressedImages.add(new ImageIcon(imagePathBtnPressed.get(i)));
		}
	}

	/** Display only the selected character with blue border, others with black border */
	private void selectMaster(int selected) {
		for (int i = 0; i < 5; i++) {
			if (i == selected) {
				CH[i].setIcon(charPressedImages.get(i));
				selectImageNum = i;
				imagePath = this.mainFrame.getCharImageList().get(i);

			} else
				CH[i].setIcon(charImages.get(i));
		}
	}

	/** Mouse Entered, Exited -> have to modify */
	// private void selectExited(int selected) {
	// for (int i = 0; i < 5; i++) {
	// if (i == selected) {
	// CH[i].setIcon(charImages.get(i));
	// // CH[i].setBorder(new LineBorder(new Color(91, 155, 213), 8));
	// CH[i].setBorder(new LineBorder(Color.black, 4));
	// }
	// }
	// }

	/** inner class: check client enter all required information */
	public class InputActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (nickNameTextField.getText().equals(""))
				JOptionPane.showMessageDialog(EntryPanel.this.mainFrame.getContentPane(), "Please enter nickname.");
			else if (imagePath == "")
				JOptionPane.showMessageDialog(EntryPanel.this.mainFrame.getContentPane(), "Please select a character.");
			else {
				ProgressInfo pi = new ProgressInfo();
				pi.set_status(ProgressInfo.USER_ACCEPT);
				
				pi.setNickName(nickNameTextField.getText()); //닉네임에 세팅
				pi.setLevel((int) (Math.random() * 30 + 1)); // 랜덤으로 레벨 세팅
				pi.set_selectImageNum(selectImageNum); // 캐릭터 번호 세팅(+캐릭터 이름도)
				pi.set_imagePath(imagePath);
				
				System.out.println("<EntryPanel> call progressInfo setting all info");
				EntryPanel.this.mainFrame.sendProtocol(pi);
				nickNameTextField.setText("");
			}
		}
	}
}