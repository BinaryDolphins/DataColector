import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Subscriber implements Runnable {
	private String username, password;
	private static PrintWriter pout;
	private static BufferedReader bin;
	private static Socket socket;
	private static String lastMessage;
	private static boolean close;
	private Thread t;
	
	public Subscriber(String username, String password) {
		this.username = username;
		this.password = password;
		lastMessage = null;
		close = false;
	}
	
	public void open(String username, String password) {
		try {
	        socket = new Socket(StockConnectionManager.SERVER, StockConnectionManager.SOCKET);
	        pout = new PrintWriter(socket.getOutputStream());
	        bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        
	        pout.println(username + " " + password);
	        pout.println(StockConnectionManager.SUBSCRIBE);
	        pout.flush();
	        
	        String text = "";
	        String line;
	        
	        while((line = bin.readLine()) != null) {
	        	if(close) {
	        		break;
	        	}
	        	lastMessage = (line == null ? line : (lastMessage + "\n" + line));
	        }
	        
	        //return text;
    	} catch(IOException e) {
    		System.err.println("I don't even know what I should do. FYI:");
    		System.err.println(e);
    		System.exit(-42);
    	}
	}

	@Override
	public void run() {
		open(username, password);
	}
	
	public void start()
	   {
	      if (t == null)
	      {
	         t = new Thread(this, "Subscriber thread");
	         t.start();
	      }
	   }
	
	public static synchronized void setLastMessage(String msg) {
		lastMessage = msg;
	}
	
	public static synchronized String getLastMessage() {
		String msg = lastMessage;
		lastMessage = null;
		return msg;
	}
	
	public static synchronized void close() {
		close = true;
		
        pout.close();
        
        try {
            socket.close();
			bin.close();
		} catch (IOException e) {
    		System.err.println("The world probably just hates me at this point. FYI:");
			e.printStackTrace();
			System.exit(-40);
		}
	}
}
