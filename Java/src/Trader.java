import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.nevec.rjm.BigDecimalMath;


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
		} else if(ammount != 0) {
			BigDecimal tempPrice = securities[index].getBuyPrice();
			long count = securities[index].getCount();
			
			// (tempPrice*count + price*ammount) / (count + ammount)
			securities[index].setBuyPrice((tempPrice.multiply(new BigDecimal(count))
					.add(price.multiply(new BigDecimal(ammount)))).divide(new BigDecimal(count+ammount), 4, RoundingMode.HALF_UP));
			securities[index].setCount(count + 1);
		}
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
				//y = 30.1205 x+0.898795
				BigDecimal buyPortionToSell = new BigDecimal(30.1205).multiply(securities[i].getDividendRatio()).add(new BigDecimal("0.898795"));
						//BigDecimal.ONE;//new BigDecimal(0.76).multiply(BigDecimalMath.pow(BigDecimal.valueOf(Math.E), securities[i].getDividendRatio().multiply(new BigDecimal("45.6758"))));
				System.out.println("Dividends: " + securities[i].getDividendRatio() + "\n\tBuyPortion: " + buyPortionToSell + "\n\tHighest Bid: " + companies[i].getHighestBid());
				if(companies[i].getHighestBid().compareTo(securities[i].getBuyPrice().multiply(buyPortionToSell)) > 0) {
					result.add(i);
					System.out.println("SELLING " + companies[i].getTicker() + " WITH PORTION " + buyPortionToSell);
				}
			}
		}
		
		return result;
	}
	
	public long getStockCount(int index) {
		return securities[index].getCount();
	}
	
	
	public void updateDividends(BigDecimal[] dividends) {
		for(int i = 0; i < dividends.length; i++) {
			securities[i].setDividendRatio(dividends[i]);
		}
	}
}
