package info;

import java.io.Serializable;

public class UserInfo implements Serializable {
	// ** STATUS **
	public static final int IN_LOBBY = 100;
	public static final int IN_GAME = 101;
	public static final int IN_GAME_QUESTIONER = 102;
	public static final int IN_GAME_ANSWERER = 103;

	// ** VARIABLE **
	private int status;
	
	private String nickName;
	private String imagePath;
	private String gameName;
	private boolean isMaster;
	private int score;
	private int level; // random
	private int selectimageNum; // select character
	private String characterName;

	// ** CONSTRUCTOR **
	public UserInfo() {
		nickName = "";
		gameName = "";
		isMaster = false;
		score = 0;
		level = 0;
	}

	// public UserInfo (int _status, String _nickName, String _imagePath)
	// {
	// status = _status;
	// nickName = _nickName;
	// imagePath = _imagePath;
	// gameName = "";
	// isMaster = false;
	// score = 0;
	// }

	// ** METHOD **
	// getter
	public int get_status() {
		return status;
	}

	public String get_nickName() {
		return nickName;
	}

	public String get_gamecharImagePath() {
		String frontImagePath = imagePath.substring(0, imagePath.length() - 4);
		return frontImagePath + "H.png";
	}

	public String get_gameName() {
		return gameName;
	}

	public boolean get_isMaster() {
		return isMaster;
	}

	public int get_score() {
		return score;
	}

	public int get_level() {
		return level;
	}

	public int getSelectImageNum() {
		return selectimageNum;
	}

	public String getCharacterName() {
		return characterName;
	}

	// setter
	public void set_status(int item) {
		status = item;
	}

	public void set_nickName(String item) {
		nickName = item;
	}

	public void set_imagePath(String item) {
		imagePath = item;
	}

	public void set_gameName(String item) {
		gameName = item;
	}

	public void set_isMaster(Boolean item) {
		isMaster = item;
	}

	public void set_score(int item) {
		score = item;
	}
	
	public void set_level(int item) {
		level = item;
	}

	public void setCharacterName(int item) {
		switch (item) {
		case 0:
			characterName = "Shrek";
			break;
		case 1:
			characterName = "Ironman";
			break;
		case 2:
			characterName = "Captain America";
			break;
		case 3:
			characterName = "Batman";
			break;
		case 4:
			characterName = "Spiderman";
			break;
		}
	}

	public void setSelectImageNum(int item) {
		selectimageNum = item;
		setCharacterName(item);
	}

	public void inc_score() {
		score++;
	}

	public void dec_score() {
		score--;
	}
}