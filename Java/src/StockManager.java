import java.math.BigDecimal;
import java.util.List;
import java.util.Random;


public class StockManager {
	private StockConnectionManager conn;
	private Company[] companies;
	private Trader trader;
	
	public StockManager() {
		conn = new StockConnectionManager("binarydolphins", "what3v3r");
		trader = new Trader(conn.getCash());
		
		companies = conn.getCompanies();
	}
	
	
	public void timeStepThing() {
		while(true) {
			updateDividends();
			//Bid and get what to sell
			//randomBidding(10, 5);
			buyWithSlope(5);
			
			checkSubscriberData();
			updateStockPrices();
			
			List<Integer> sales = trader.stockToSell(companies);
			
			for(int i = 0; i < sales.size(); i++) {
				int companyIndex = sales.get(i);
				conn.askCompany(companies[companyIndex].getTicker(),
						companies[companyIndex].getHighestBid().subtract(new BigDecimal("0.05")), 
						trader.getStockCount(sales.get(i)));
				System.out.println("ASKING " + companies[companyIndex].getTicker());
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(-41);
			}
		}
	}
	
	public void randomBidding(int bids, int ammountStock) {
		for(int i = 0; i < bids; i++) {
			Random random = new Random();
			int companyIdx = random.nextInt(companies.length);
			
			conn.bidCompany(companies[companyIdx].getTicker(),
					companies[companyIdx].getLowestAsk().add(new BigDecimal("0.05")), ammountStock);
		}
	}
	
	public void buyWithSlope(int ammountStock) {
		for(int i = 0; i < companies.length; i++) {
			if(companies[i].getAskSlope().compareTo(new BigDecimal("0")) > 0) {
				//System.out.println("BIDDING " + ammountStock + " SHARES FROM " + companies[i].getTicker());
				conn.bidCompany(companies[i].getTicker(),
						companies[i].getLowestAsk().add(new BigDecimal("0.05")), ammountStock);
			}
		}
	}
	
	public void checkSubscriberData() {
		String lastMessage = Subscriber.getLastMessage();
		if(lastMessage != null) {
			String[] msg = lastMessage.trim().split("\\n");
			for(int i = 0; i < msg.length; i++) {
				String[] tokenizedMessage = msg[i].trim().split(" ");
				int companyIdx = -1;
				for(int j = 0;tokenizedMessage.length >=2 && j < companies.length && companyIdx == -1; j++) {
					if(companies[j].getTicker().equals(tokenizedMessage[1]))
						companyIdx = j;
				}
				if(tokenizedMessage[0].equals("BUY")) {
					if(Long.parseLong(tokenizedMessage[3]) != 0)
						trader.addSecurity(companyIdx, new BigDecimal(tokenizedMessage[2]), Long.parseLong(tokenizedMessage[3]));
				} else if(tokenizedMessage[0].equals("SELL")) {
					if(Long.parseLong(tokenizedMessage[3]) != 0)
						trader.removeSecurity(companyIdx, Long.parseLong(tokenizedMessage[3]));
				}
			}
		}
	}
	
	public void updateDividends() {
		BigDecimal[] dividends = conn.getMyDividends();
		trader.updateDividends(dividends);
	}
	
	public void updateStockPrices() {
		companies = conn.updateCompanies(companies);
		trader.setCash(conn.getCash());
	}
}
