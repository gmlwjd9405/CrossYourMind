package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import info.ProgressInfo;
import info.UserInfo;

public class ServerClient extends Thread
{
// ** VARIABLE **
	// Connects to the common server
	ALPHAserver server;
	// For connection
	Socket s;
	ObjectOutputStream out;
	boolean outLock;
	ObjectInputStream in;
	UserInfo ui;
   
// ** CONSTRUCTOR **
	public ServerClient(Socket s, ALPHAserver server)
	{
		this.s = s;
		this.server = server;
		ui = new UserInfo ();
		
		try
		{	
			out = new ObjectOutputStream (s.getOutputStream ());
			outLock = true;
			in = new ObjectInputStream (s.getInputStream ());
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

// ** METHOD **
   @Override
   public void run ()
   {
      try
      {
    	  loop: while(true)
    	  {
        	 ProgressInfo pi = (ProgressInfo)in.readObject();
        	 switch (pi.get_status())
        	 {
        	 	case ProgressInfo.USER_ACCEPT: // When user tries to enter the game
        	 	{
        	 		String s = pi.get_chat ();
        	 		if (server.checkDuplicateUser (s))
        	 		{//duplicate
        	 			ProgressInfo pi_ack = new ProgressInfo ();
        	 			pi_ack.set_status (ProgressInfo.USER_DUPLICATE); // The typed nickname is already in use
        	 			lockedWrite (pi_ack);
        	 		}
        	 		else
        	 		{//accept
        	 			ui.set_nickName (s);
        	 			ui.set_imagePath (pi.get_imagePath ());
        	 			ui.set_status (UserInfo.IN_LOBBY);
        	 			//System.out.println (s);
        	 			server.lobbyGameAllUpdate ();
        	 			server.lobbyUserAllUpdate ();
        	 			ProgressInfo pi_ack = new ProgressInfo ();
        	 			pi_ack.set_status (ProgressInfo.USER_APPROVE);
        	 			pi_ack.set_chat (pi.get_chat ());
        	 			pi_ack.set_imagePath (pi.get_imagePath ());
        	 			lockedWrite (pi_ack);
        	 			server.printUsers ();
        	 		}
        	 		break;
        	 	}
        	 	case ProgressInfo.CHAT_LOBBY: // When user types chat in lobby
        	 	{
        	 		String s = pi.get_chat ();
        	 		server.set_RLC (ui.get_nickName () + ": " + s);
        	 		server.lobbyChatUpdateAll ();
        	 		break;
        	 	}
        	 	case ProgressInfo.EXIT_LOBBY: // When user exits lobby
        	 	{
        	 		ui.set_nickName ("");
        	 		server.lobbyUserAllUpdate ();
        	 		server.printUsers ();
        	 		break;
        	 	}
        	 	case ProgressInfo.CREATE_GAME_TRY: // When user tries to create a game
        	 	{
        	 		String s = pi.get_chat ();
        	 		if (server.checkDuplicateGame (s))
        	 		{
        	 			System.out.println ("CREATE_GAME_DENIED");
        	 			ProgressInfo pi_ack = new ProgressInfo ();
        	 			pi_ack.set_status (ProgressInfo.CREATE_GAME_DENIED);
        	 			lockedWrite (pi_ack);
        	 		}
        	 		else
        	 		{
        	 			System.out.println ("CREATE_GAME_APPROVE");
        	 			server.newGame (pi.get_chat ());
            	 		server.lobbyGameAllUpdate ();
            	 		ui.set_status (UserInfo.IN_GAME);
            	 		ui.set_gameName (pi.get_chat ());
            	 		ui.set_isMaster (true);
            	 		ProgressInfo pi_ack = new ProgressInfo ();
            	 		pi_ack.set_status (ProgressInfo.CREATE_GAME_APPROVE);
            	 		pi_ack.set_usersGame(server.getUsersGame (pi.get_chat ()));
            	 		lockedWrite (pi_ack);
        	 			server.lobbyUserAllUpdate ();
        	 		}
        	 		break;
        	 	}
        	 	case ProgressInfo.JOIN_GAME_TRY: // When user tries to join a game
        	 	{
        	 		System.out.println ("JOIN_GAME_TRY");
        	 		if (server.checkFull (pi.get_chat ()))
        	 		{
        	 			ProgressInfo pi_ack = new ProgressInfo ();
        	 			pi_ack.set_status (ProgressInfo.JOIN_GAME_DENIED);
        	 			lockedWrite (pi_ack);
        	 		}
        	 		else
        	 		{
        	 			ui.set_status (UserInfo.IN_GAME);
        	 			ui.set_isMaster(false);
            	 		ui.set_gameName (pi.get_chat ());
        	 			ProgressInfo pi_ack = new ProgressInfo ();
        	 			pi_ack.set_status (ProgressInfo.JOIN_GAME_APPROVE);
        	 			pi_ack.set_chat (pi.get_chat ());
        	 			pi_ack.set_usersGame (server.getUsersGame (pi.get_chat ()));
        	 			lockedWrite (pi_ack);
        	 		}
        	 		break;
        	 	}
        	 	case ProgressInfo.JOIN_GAME: // When user succeeds to join a game
        	 	{
        	 		System.out.println ("JOIN_GAME");
        	 		server.userJoinGame (ui.get_nickName (), ui.get_gameName ());
        	 		server.lobbyUserAllUpdate ();
        	 		break;
        	 	}
        	 	case ProgressInfo.EXIT_GAME: // When user exits the game
        	 	{
        	 		System.out.println ("EXIT_GAME");
        	 		String gameName = ui.get_gameName();
        	 		ui.set_gameName("");
        	 		ui.set_status (UserInfo.IN_LOBBY);
        	 		server.userExitGame (gameName);
        	 		server.lobbyUserAllUpdate ();
        	 		server.gameUserAllUpdate (gameName);
        	 		server.printGi ();
        	 		
        	 		break;
        	 	}
        	 	case ProgressInfo.START_TRY: // When users tries to start a game
        	 	{
        	 		System.out.println ("START_TRY");
        	 		pi = new ProgressInfo ();
        	 		
        	 		if (ui.get_isMaster ())
        	 		{
        	 			if (server.startAvailable (ui.get_gameName ()))
        	 				server.startGameAll (ui.get_gameName ());
        	 			else
        	 				pi.set_status (ProgressInfo.START_DENIED_NUM);
        	 		}
        	 		else
        	 			pi.set_status (ProgressInfo.START_DENIED_MASTER);
        	 		lockedWrite (pi);
        	 		break;
        	 	}
        	 	case ProgressInfo.DRAW: // When questioner draws to canvas
        	 	{
        	 		System.out.println ("DRAW");
        	 		server.drawBroadcast (ui.get_gameName (), pi.get_pList ());
        	 		break;
        	 	}
        	 	case ProgressInfo.SELECT_CLEAR: // When user selects clear button
        	 	{
        	 		server.clearBroadcast (ui.get_gameName ());
        	 		break;
        	 	}
        	 	case ProgressInfo.SELECT_ERASER: // When user selects eraser button
        	 	{
        	 		server.eraserBroadcast (ui.get_gameName ());
        	 		break;
        	 	}
        	 	case ProgressInfo.SELECT_COLOR: // When user selects color button
        	 	{
        	 		server.colorBroadcast (ui.get_gameName (), pi.get_drawColor ());
        	 		break;
        	 	}
        	 	case ProgressInfo.TIMER_EXPIRE: // When the round timer becomes zero in playing game
        	 	{
        	 		//System.out.println ("TIMER_EXPIRE: " + ui.get_nickName ());
        	 		server.timerExpireBroadcast (ui.get_gameName (), ui.get_nickName());
        	 		break;
        	 	}
        	 	case ProgressInfo.CHAT_GAME: // When user types chat in game
        	 	{
        	 		System.out.println ("CHAT_GAME");
        	 		server.checkAnswer (ui.get_gameName (), ui.get_nickName (), pi.get_chat ());
        	 		break;
        	 	}
        	 	case ProgressInfo.EXIT_ENTRY: // When user exits in entry panel: Closes the connection and destroy data structures
        	 	{
        	 		System.out.println ("EXIT_ENTRY");
        	 		ProgressInfo pi_ack = new ProgressInfo ();
        	 		pi_ack.set_status (ProgressInfo.EXIT_APPROVE);
        	 		lockedWrite (pi_ack);
        	 		server.exitUser (s);
        	 		break loop;
        	 	}
        	 	default:
        	 	{
        	 		break;
        	 	}
        	 }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   	// INPUT: progressInfo
	// OUTPUT: null
	// Objective:
	// When serverClient try to send progressInfo to client, we should prevent race condition of output stream
    // Race condition can occur when timer broadcasting function is executed
   public void lockedWrite (ProgressInfo pi)
   {
	   //System.out.println ("@@Before while");
	   while (!outLock) { System.out.println ("@In while"); }
	   //System.out.println ("@@After while");
	   outLock = false;
	   try
	   {
		   out.writeObject (pi);
		   out.flush ();
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace ();
	   }
	   outLock = true;
   }
 
   //Get methods
   public UserInfo getUserInfo ()
   {
	   return ui;
   }
   
   public int getUserStatus ()
   {
	   return ui.get_status ();
   }
   
   public String getUserNickname ()
   {
	   return ui.get_nickName ();
   }
   
   public boolean userInLobby ()
   {
	   return ui.get_status() == UserInfo.IN_LOBBY;
   }
   
   public Socket getSocket ()
   {
	   return s;
   }
   
    // INPUT: null
	// OUTPUT: null
	// Objective:
    // When user try to exit game, server should disconnect socket and stream to client
   public void exitClient ()
   {
	   try
	   {
		   in.close ();
		   out.close ();
		   s.close ();
	   }
	   catch (Exception e)
	   {
		   e.printStackTrace ();
	   }
   }
}