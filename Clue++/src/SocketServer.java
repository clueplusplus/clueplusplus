import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class SocketServer implements Runnable
{
	// This holds the list of people connected to the server.
	private ArrayList<SocketServerConnection> connections = new ArrayList<SocketServerConnection>(0);
	
	private Thread thread;
	private int port = 1234;
	
	private ServerSocket listener;
	
	private boolean running = false;
	
	private Game game;
	
	public SocketServer()
	{
		game = Game.getInstance();
	}
	
	public void startServer(int port)
	{
		this.port = port;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		try 
		{
			listener = new ServerSocket(port);
		
			System.out.println("Socket Listener Running");
			
			while(!listener.isClosed())
			{
				running = true;
				Socket socket = listener.accept();
				
				System.out.println("Connection From " + socket.getRemoteSocketAddress());
				
				SocketServerConnection c = new SocketServerConnection(this);
				synchronized(connections)
				{
					connections.add(c);
					
					if(connections.size() == 1)
						c.firstPlayer = true;
				}		
				c.startThread(socket);
			}
			
            listener.close();
        }
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		running = false;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void stop()
	{
		if(!running) return;
		
		try {
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendStartGame()
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendStartGame();
			}
		}
	}
	public synchronized void sendStartTurn(String characterWhosTurnItIs)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendStartTurn(characterWhosTurnItIs);
			}
		}
	}
	public synchronized void sendNotifyMove(String characterMoving, String location)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendNotifyMove(characterMoving, location);
			}
		}
	}
	public synchronized void sendSuggestionNotification(
			String suggestingCharacter,
			String characterToRespond,
			String suggestionCharacter,
			String suggestionRoom,
			String suggestionWeapon)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendSuggestionNotification(	suggestingCharacter,
												characterToRespond,
												suggestionCharacter,
												suggestionRoom,
												suggestionWeapon);
			}
		}
	}
	public synchronized void sendForwardResponseToSuggestion(String characterResponding, String card)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendForwardResponseToSuggestion(characterResponding, card);
			}
		}
	}
	public synchronized void sendSuggestionRoundComplete()
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendSuggestionRoundComplete();
			}
		}
	}
	public synchronized void sendAccusationMade(
			String accusingCharacter,
			String accusationCharacter,
			String accusationRoom,
			String accusationWeapon,
			String accuracy)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendAccusationMade(	accusingCharacter,
										accusationCharacter,
										accusationRoom,
										accusationWeapon,
										accuracy);
			}
		}
	}
	public synchronized void sendEndGame(String why)
	{
		synchronized(connections)
		{
			for(SocketServerConnection c : connections)
			{
				c.sendEndGame(why);
			}
		}
	}
	
	
	

}
