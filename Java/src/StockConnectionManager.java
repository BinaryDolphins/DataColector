import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class StockConnectionManager {
    /*****************************************************
     ****************** COMMAND CONSTANTS ****************
     *****************************************************/
     /*
      * Commands to get your information
      */

     public static final String MY_CASH = "MY_CASH";
     public static final String MY_SECURITIES = "MY_SECURITIES";
     public static final String MY_ORDERS = "MY_ORDERS";

     /*
      * Commands to get company and stock information
      */
     public static final String SECURITIES = "SECURITIES";
     public static final String ORDERS = "ORDERS"; // ORDERS <ticker>

     /*
      * Bid and ask actions. Actual actions
      */
     // Bid is buy stock
     public static final String BID = "BID"; //BID <ticker> <price> <shares>
     // Ask is sell stock
     public static final String ASK = "ASK"; //ASK <ticker> <price> <shares>
     
     public static final String CLEAR_BID = "CLEAR_BID"; //CLEAR_BID <ticker>
     public static final String CLEAR_ASK = "CLEAR_ASK"; //CLEAR_ASK <ticker>

     /*
      * Subscription for the ticker and bids
      */
     public static final String SUBSCRIBE = "SUBSCRIBE"; //SUBSCRIBE
     public static final String UNSUBSCRIBE = "UNSUBSCIBE";

     // Close the connection
     public static final String CLOSE_CONNECTION = "CLOSE_CONNECTION";



    /******************************************************
     ************************* CODE ***********************
     ******************************************************/

    public String username, password;
    public static final String SERVER = "codebb.cloudapp.net";
    public static final int SOCKET = 17429;
    
    private Subscriber subscriber; 


    public StockConnectionManager(String username, String password)  {
        this.username = username;
        this.password = password;
        this.subscriber = new Subscriber(username, password);
        this.subscriber.start();
    }

    private String sendCommand(String command, String params) {
    	try {
	        Socket socket = new Socket(SERVER, SOCKET);
	        PrintWriter pout = new PrintWriter(socket.getOutputStream());
	        BufferedReader bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        
	        pout.println(username + " " + password);
	        String commandString = command + " " + (params == null ? "" : params);
	        pout.println(commandString);
	        pout.println(CLOSE_CONNECTION);
	        pout.flush();
	        
	        String text = "";
	        String line;
	        
	        while((line = bin.readLine()) != null) {
	        	System.out.println("Line: "+line);
	        	text += line + "\n";
	        }
	
	        pout.close();
	        bin.close();
	        return text;
    	} catch(IOException e) {
    		System.err.println("I don't even know what I should do. FYI:");
    		System.err.println(e);
    		System.exit(-42);
    	}
    	
    	return null;
    }

    public BigDecimal getCash() {
    	String result = sendCommand(MY_CASH, null);
    	System.out.println(result);
    	return new BigDecimal(result.trim().split(" ")[1]);
    }

    public String[] getTickers()  {
        List<String> listResult = new ArrayList<String>();
        String[] result;
    	String text = sendCommand(SECURITIES, null);
        text = text.trim();
        String[] tokens = text.split(" ");
        
        for(int i = 1; i < tokens.length; i += 4)
        	listResult.add(tokens[i]);
        
        result = new String[listResult.size()];
        listResult.toArray(result);
        
        return result;
    }
    
    // Uses securities to get most of a company's information (excluding ticker name)
    public String[] getTickerInfo(String ticker)  {
        String[] result = new String[3];
    	String text = sendCommand(SECURITIES, null);
        text = text.trim();
        String[] tokens = text.split(" ");
        int companyIndex = -1;
        
        for(int i = 1; i < tokens.length && companyIndex == -1; i += 4) {
        	if(tokens[i].equals(ticker))
        		companyIndex = i;
        }
        
        if(companyIndex != -1) {
        	result[0] = tokens[companyIndex + 1];
        	result[1] = tokens[companyIndex + 2];
        	result[2] = tokens[companyIndex + 3];
        	return result;
        }
        
        return null;
    }
    
    public String[] getOrderString(String ticker) {
    	String text = sendCommand(ORDERS, ticker);
    	String trimmedText = text.trim();
    	return trimmedText.substring(trimmedText.indexOf(" ") + 1, trimmedText.length() - 1).split(" ");
    }
    
    public Company[] getCompanies()  {
    	String[] tickers = getTickers();
    	Company[] companies = new Company[tickers.length];
    	
    	for(int i = 0; i < tickers.length; i++) {
        	//Get each company order information (ask and bid prices and stock count)
    		String[] order = getOrderString(tickers[i]);
    		List<BidAsk> bids = new ArrayList<BidAsk>();
    		List<BidAsk> asks = new ArrayList<BidAsk>();
    		
    		for(int j = 0; j < order.length / 4; j++) {
    			BidAsk bidAsk = new BidAsk(new BigDecimal(order[4*j + 2]), Long.parseLong(order[4*j + 3]));
    			if(order[4*j].equals("BID"))
    				bids.add(bidAsk);
    			else
    				asks.add(bidAsk);
    		}
    		
    		//Get company info from securities command
    		String[] info = getTickerInfo(tickers[i]);
    		BigDecimal netWorth = new BigDecimal(info[0]);
    		BigDecimal dividend = new BigDecimal(info[1]);
    		BigDecimal volatility = new BigDecimal(info[2]);
    		
    		companies[i] = new Company(tickers[i], netWorth, dividend, volatility, bids, asks);
    	}
    	
    	return companies;
    }
    
    public String bidCompany(String ticker, BigDecimal price, long stocks) {
    	String result = sendCommand(BID, ticker + " " + price.toString() + " " + stocks);
    	return result;
    }
    
    public String askCompany(String ticker, BigDecimal price, long stocks) {
    	String result = sendCommand(ASK, ticker + " " + price.toString() + " " + stocks);
    	return result;
    }
    
}
