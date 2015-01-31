import java.math.BigDecimal;
import java.util.List;


public class Company {
	private String ticker;
	private BigDecimal netWorth;
	private BigDecimal dividendRatio;
	private BigDecimal volatility;
	private List<BidAsk> bids;
	private List<BidAsk> asks;
	
	public Company(String ticker, BigDecimal netWorth, BigDecimal dividendRatio,
			BigDecimal volatility, List<BidAsk> bids, List<BidAsk> asks) {
		this.ticker = ticker;
		this.netWorth = netWorth;
		this.dividendRatio = dividendRatio;
		this.volatility = volatility;
		this.bids = bids;
		this.asks = asks;
	}
	
	public String getTicker() { return ticker; }
	public BigDecimal getNetWorth() { return netWorth; }
	public BigDecimal getDividendRatio() { return dividendRatio; }
	public BigDecimal getVolatility() { return volatility; }
	public List<BidAsk> getBids() { return bids; }
	public List<BidAsk> getAsks() { return asks; }

	public void setNetWorth(BigDecimal netWorth) { this.netWorth = netWorth; }
	public void setDividendRatio(BigDecimal dividendRatio) { this.dividendRatio = dividendRatio; }
	public void setVolatility(BigDecimal volatility) { this.volatility = volatility; }
	public void setBids(List<BidAsk> bids) { this.bids = bids; }
	public void setAsks(List<BidAsk> asks) { this.asks = asks; }
	
	public BigDecimal getLowestAsk() {
		BigDecimal result = asks.get(0).getPrice();
		for(int i = 1; i < asks.size(); i++) {
			if(result.compareTo(asks.get(i).getPrice()) > 0)
				result = asks.get(i).getPrice();
		}
		
		return result;
	}
	
	public BigDecimal getHighestBid() {
		BigDecimal result = bids.get(0).getPrice();
		for(int i = 1; i < bids.size(); i++) {
			if(result.compareTo(bids.get(i).getPrice()) < 0)
				result = bids.get(i).getPrice();
		}
		
		return result;
	}
	
}
