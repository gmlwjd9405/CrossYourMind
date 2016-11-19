package info;

import java.io.Serializable;

public class gameInfo implements Serializable
{
// ** STATUS **
	public static final int WATING = 100;
	public static final int PLAYING = 101;
	public static final int ROUND_NUM = 5;
	
// ** VARIABLE **
	int status;
	String gameName;
	int participants;
	int gameOrder;
	String roundAnswer;
	int roundNum;
	
// ** CONSTRUCTOR **
	public gameInfo (int _status, String _gameName, int _participants)
	{
		status = _status;
		gameName = _gameName;
		participants = _participants;
		gameOrder = 0;
		roundAnswer = "";
		roundNum = ROUND_NUM;
	}
	
// ** METHOD **
	public int get_status () { return status; }
	public String get_gameName () { return gameName; }
	public int get_participants () { return participants; }
	public String get_roundAnswer () { return roundAnswer; }
	public int get_roundNum () { return roundNum; }
	
	public void set_status (int item) { status = item; }
	public void set_gameName (String item) { gameName = item; }
	public void set_participants (int item) { participants = item; }
	public void set_roundAnswer (String item) { roundAnswer = item; }
	public void set_roundNum (int item) { roundNum = item; }
	
	public void inc_participants () { participants += 1; }
	public void dec_participants () { participants -= 1; }
}