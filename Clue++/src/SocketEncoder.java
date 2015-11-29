import java.io.DataOutputStream;
import java.io.IOException;

public class SocketEncoder
{
	private DataOutputStream out;
	
	public SocketEncoder(DataOutputStream out)
	{
		this.out = out;
	}
	
	public synchronized void writeInt(int i) throws IOException
	{		
		out.writeInt(i);
	}
	
	public synchronized void writeString(String string) throws IOException
	{		
		out.writeInt(string.length());
		out.writeChars(string);
	}
		
	public synchronized void flush() throws IOException
	{
		out.flush();
	}
}