package com.mrj.person;

import java.math.BigDecimal;
import java.util.*;

public class CapitalSituation {
	BigDecimal leftMoney;
	List<ShareHolding> holdingList = new ArrayList<ShareHolding>();

	public BigDecimal getLeftMoney() {
		return leftMoney;
	}

	public void setLeftMoney(BigDecimal leftMoney) {
		this.leftMoney = leftMoney;
	}

	public List<ShareHolding> getHoldingList() {
		return holdingList;
	}

	public void setHoldingList(List<ShareHolding> holdingList) {
		this.holdingList = holdingList;
	}

	public BigDecimal getTotalAssets(Date date) {
		BigDecimal re = new BigDecimal(0f);
		re.add(this.leftMoney);
		for(int i=0;i<holdingList.size();i++){
			ShareHolding temp=holdingList.get(i);
			re.add(temp.getAssets(date));
		}
		return re;
	}

}
