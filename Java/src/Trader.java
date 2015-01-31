import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Trader {
	private Security[] securities;
	private BigDecimal cash;
	
	
	public Trader(BigDecimal cash) {
		securities = new Security[10];
		for(int i = 0; i < securities.length; i++)
			securities[i] = new Security(0, BigDecimal.ZERO, BigDecimal.ZERO);
		this.cash = cash;
	}
	
	public void addSecurity(int index, BigDecimal price, long ammount) {
		if(securities[index].getCount() == 0) {
			securities[index].setBuyPrice(price);
			securities[index].setCount(ammount);
		} else {
			BigDecimal tempPrice = securities[index].getBuyPrice();
			long count = securities[index].getCount();
			
			// (tempPrice*count + price*ammount) / (count + ammount)
			securities[index].setBuyPrice((tempPrice.multiply(new BigDecimal(count))
					.add(price.multiply(new BigDecimal(ammount)))).divide(new BigDecimal(count+ammount)));
			securities[index].setCount(count + 1);
		}
		System.out.println();
	}
	
	public void removeSecurity(int index, long ammount) {
		securities[index].setCount(securities[index].getCount() - ammount);
	}
	
	public void setCash(BigDecimal cash) { this.cash = cash; }
	public BigDecimal getCash() { return cash; }
	
	
	
	public List<Integer> stockToSell(Company[] companies) {
		List<Integer> result = new ArrayList<Integer>();
		for(int i = 0; i < companies.length; i++) {
			if(securities[i].getCount() != 0) {
				if(companies[i].getHighestBid().compareTo(securities[i].getBuyPrice()) > 0) {
					result.add(i);
				}
			}
		}
		
		return result;
	}
	
	public long getStockCount(int index) {
		return securities[index].getCount();
	}
}
