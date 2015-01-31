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
			checkSubscriberData();
			randomBidding(10, 5);
			List<Integer> sales = trader.stockToSell(companies);
			
			for(int i = 0; i < sales.size(); i++) {
				int companyIndex = sales.get(i);
				conn.askCompany(companies[companyIndex].getTicker(),
						companies[companyIndex].getHighestBid().subtract(new BigDecimal("0.05")), 
						trader.getStockCount(sales.get(i)));
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
					System.out.println("MSG 2: " + tokenizedMessage[2]);
					trader.addSecurity(companyIdx, new BigDecimal(tokenizedMessage[2]), Long.parseLong(tokenizedMessage[3]));
				} else if(tokenizedMessage[0].equals("SELL")) {
					trader.removeSecurity(companyIdx, Long.parseLong(tokenizedMessage[3]));
				}
			}
		}
	}
}
