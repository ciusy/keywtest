package com.mrj.person;

import java.math.BigDecimal;
import java.util.Date;

import com.mrj.sto.HQ;
import com.mrj.sto.Sto;

public class ShareHolding {
	Sto sto;
	int hodingAmount;
	float costPrice;
	int availableAmountForSell;
	
	float interestRate;
	
	public BigDecimal getAssets(Date date){//����dateʱ�䲢��date�����ʱ���
		BigDecimal re = new BigDecimal(0f);
		re.add(new BigDecimal(sto.getHq().getPrice(date)*hodingAmount));
		return re;
	}
	
}
