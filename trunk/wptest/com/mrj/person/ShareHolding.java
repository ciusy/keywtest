package com.mrj.person;

import java.math.BigDecimal;
import java.util.Date;

import com.mrj.sto.HQ;

public class ShareHolding {
	HQ hq;
	int hodingAmount;
	int costPrice;
	int availableAmountForSell;
	
	float interestRate;
	
	public BigDecimal getAssets(Date date){//����dateʱ�䲢��date�����ʱ���
		BigDecimal re = new BigDecimal(0f);
		re.add(new BigDecimal(hq.getPrice(date)*hodingAmount));
		return re;
	}
	
}
