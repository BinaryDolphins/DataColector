import java.math.BigDecimal;


public class BidAsk {
	BigDecimal price;
	long stock;
	
	public BidAsk(BigDecimal price, long stock) {
		this.price = price;
		this.stock = stock;
	}

	public BigDecimal getPrice() { return price; }
	public long getStock() { return stock; }
	
	public void setPrice(BigDecimal price) { this.price = price; }
	public void setStock(long stock) { this.stock = stock; }
}
