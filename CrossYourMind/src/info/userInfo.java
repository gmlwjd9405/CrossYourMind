package info;

import java.io.Serializable;

public class userInfo implements Serializable
{
// ** STATUS ** 
	public static final int IN_LOBBY = 100;
	public static final int IN_GAME = 101;
	public static final int IN_GAME_QUESTIONER = 102;
	public static final int IN_GAME_ANSWERER = 103;

// ** VARIABLE **
	int status;
	String nickName;
	String imagePath;
	String gameName;
	boolean isMaster;
	int score;
	
// ** CONSTRUCTOR **
	public userInfo ()
	{
		nickName = "";
		gameName = "";
		isMaster = false;
		score = 0;
	}
	
	public userInfo (int _status, String _nickName, String _imagePath)
	{
		status = _status;
		nickName = _nickName;
		imagePath = _imagePath;
		gameName = "";
		isMaster = false;
		score = 0;
	}
	
// ** METHOD **
	public int get_status () { return status; }
	public String get_nickName () { return nickName; }
	public String get_imagePath () { return imagePath; }
	public String get_gameName () { return gameName; }
	public boolean get_isMaster () { return isMaster; }
	public int get_score () { return score; }
	
	public void set_status (int item) { status = item; }
	public void set_nickName (String item) { nickName = item; }
	public void set_imagePath (String item) { imagePath = item;}
	public void set_gameName (String item) { gameName = item; }
	public void set_isMaster (Boolean item) { isMaster = item; }
	public void set_score (int item) { score = item; }
	
	public void inc_score () { score++; }
	public void dec_score () { score--; }
}