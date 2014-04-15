import java.net.*; 
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Server extends Thread{
	private Socket clientSocket;
	private static HashSet<String> names = new HashSet<String>();
	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
	private static HashMap<String, PrintWriter> userMap = new HashMap<String, PrintWriter>();
	
	
	private static final int PORT = 9001;
	
	public static void main(String[] args) throws IOException{ 
		ServerSocket listener = new ServerSocket(PORT);
        try {
        	while (true){
                 System.out.println ("Waiting for a Connection");
                 new Handler(listener.accept()).start();
            }
        }
        catch(IOException e){
        	System.err.println(e);
        }
		finally {
			listener.close();
		}
	}
	
	
	
	private static class Handler extends Thread {
		private String clientName;
	    private Socket socket;
	    private BufferedReader in;
	    private PrintWriter out;
	    
	    public Handler(Socket socket){
	    	this.socket = socket;
	    }
	    
	    public void run(){
	    	try{
	    		
	    		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    		out = new PrintWriter(socket.getOutputStream(), true);
	    		
	    		while (true){
	    			out.println("Please enter username: ");
	    			clientName = in.readLine();
	    			if(clientName == null)
	    				return;
	    			synchronized (names){
	    				if(!userMap.containsKey(clientName)){
	    					userMap.put(clientName, out);
	    					break;
	    				}
	    			}
	    		}
	    		
	    		out.println("UserName accepted!");
	    		
	    		while(true){
	    			String input = in.readLine();
	    			if(input == null){
	    				return;
	    			}
	    			// Get a set of the entries
	    		      Set set = userMap.entrySet();
	    		      // Get an iterator
	    		      Iterator i = set.iterator();
	    		      // Spam out message
	    		      while(i.hasNext()) {
	    		         Map.Entry me = (Map.Entry)i.next();
	    		         ((PrintWriter)me.getValue()).println("Message: " + clientName + ": "+input);
	    		      }
	    		}
	    		
	    	}
	    	catch(IOException e){
	    		System.out.println("Getting here");
	    		System.err.println(e);
	    	}
	    	finally{
	    		/*if(names != null){
	    			names.remove(clientName);
	    		}
	    		if(out != null){
	    			writers.remove(out);
	    		}*/
	    		userMap.remove(clientName);
	    		try {
	    			socket.close();
	    		}
	    		catch (IOException e){
	    			System.err.println("Could not close socket!");
	    		}
	    	}
	    }
	    
	}
	
	
	
}