package com.mrj.person;

import java.math.BigDecimal;
import java.util.Date;

import com.mrj.sto.Sto;

public class ShareHolding {
	Sto sto;
	public Sto getSto() {
		return sto;
	}

	public void setSto(Sto sto) {
		this.sto = sto;
	}

	public int getHodingAmount() {
		return hodingAmount;
	}

	public void setHodingAmount(int hodingAmount) {
		this.hodingAmount = hodingAmount;
	}

	public float getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(float costPrice) {
		this.costPrice = costPrice;
	}

	public int getAvailableAmountForSell() {
		return availableAmountForSell;
	}

	public void setAvailableAmountForSell(int availableAmountForSell) {
		this.availableAmountForSell = availableAmountForSell;
	}

	int hodingAmount;
	private float costPrice;
	int availableAmountForSell;
	
	float interestRate;
	
	public BigDecimal getAssets(Date date){//早于date时间并里date最近的时间点
		BigDecimal re = new BigDecimal(0f);
		re=re.add(new BigDecimal(sto.getHq().getPrice(date)*hodingAmount));
		return re;
	}
	
	public float getInterestRate(float currentPrice){//相对于currentPrice而言的利润率
		if(costPrice<=0)return 100;//负成本时返回利润率100
		return (currentPrice-costPrice)/costPrice;
		
	}
	
}
