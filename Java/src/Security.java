import java.math.BigDecimal;


public class Security {
	private long count;
	private BigDecimal buyPrice;
	private BigDecimal dividendRatio;
	
	public Security(long count, BigDecimal buyPrice, BigDecimal dividendRatio) {
		this.count = count;
		this.buyPrice = buyPrice;
		this.dividendRatio = dividendRatio;
	}

	public long getCount() { return count; }
	public BigDecimal getBuyPrice() { return buyPrice; }
	public BigDecimal getDividendRatio() { return dividendRatio; }

	public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }
	public void setCount(long count) { this.count = count; }
	public void setDividendRatio(BigDecimal dividendRatio) { this.dividendRatio = dividendRatio; }
	
}
