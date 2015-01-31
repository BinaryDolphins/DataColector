import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;


public class StockManager {
	private StockConnectionManager conn;
	private Company[] companies; 
	
	public StockManager() {
		conn = new StockConnectionManager("binarydolphins", "what3v3r");
		
		companies = conn.getCompanies();
	}
	
	public void RandomBidding(int bids, int ammountStock) {
		for(int i = 0; true; i++) {
			Random random = new Random();
			int companyIdx = random.nextInt(companies.length);
			
			conn.bidCompany(companies[companyIdx].getTicker(),
					companies[companyIdx].getLowestAsk().add(new BigDecimal("0.05")), ammountStock);
		}
	}
}
