package drawing;

import java.io.Serializable;

public class UserPoint implements Serializable
{
	int pointX;
	int pointY;
	
	public UserPoint (int x, int y)
	{
		pointX = x;
		pointY = y;
	}
	
	public int get_pointX () { return pointX; }
	public int get_pointY () { return pointY; }
	
	public void set_pointX (int item) { pointX = item; }
	public void set_pointY (int item) { pointY = item; }
}