import java.io.DataInputStream;
import java.io.IOException;

public class SocketDecoder
{
	private DataInputStream in;
	
	public SocketDecoder(DataInputStream dataInputStream)
	{
		this.in = dataInputStream;
	}
	
	public String readString() throws IOException
	{
		int length = in.readInt();
		StringBuilder string = new StringBuilder("");
		
		for(int x=0; x<length; x++)
			string.append(in.readChar());
		
		return string.toString();
	}
	
	public int readInt() throws IOException
	{
		return in.readInt();		
	}	
}
