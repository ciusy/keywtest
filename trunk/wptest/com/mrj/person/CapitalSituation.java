package com.mrj.person;

import java.math.BigDecimal;
import java.util.*;

import org.apache.log4j.Logger;

import com.mrj.charge.ChargeCenter;
import com.mrj.sto.Main;
import com.mrj.sto.Sto;

public class CapitalSituation {
	static Logger logger = Logger.getLogger(CapitalSituation.class);
	
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

	public int increaseSto(Sto sto, float plan_price, int operateAmount) {
		if(this.leftMoney.compareTo(new BigDecimal(operateAmount*plan_price))>0){
			this.leftMoney.subtract(new BigDecimal(operateAmount*plan_price));
			boolean isAlreadyOwn=false;
			int i=0;
			for(;i<holdingList.size();i++){
				ShareHolding shtemp=holdingList.get(i);
				if(shtemp.sto.code.equals(sto.code)){
					isAlreadyOwn=true;
					break;
				}
			}
			if(isAlreadyOwn){
				ShareHolding shtemp=holdingList.get(i);
				int alreadyMount=shtemp.hodingAmount;
				float preCostPrice=shtemp.costPrice;
				int nowMount=alreadyMount+operateAmount;
				float costPrice=(preCostPrice*alreadyMount+operateAmount*plan_price)/(nowMount*1.0f);
				shtemp.costPrice=costPrice;
				shtemp.hodingAmount=nowMount;
			}else{
				ShareHolding shtemp=new ShareHolding();
				shtemp.costPrice=plan_price;
				shtemp.hodingAmount=operateAmount;
				shtemp.sto=sto;
				holdingList.add(shtemp);
				
			}			
			return ChargeCenter.charge_succuss;
		}else{
			logger.error("����������Ҫ��Ĺ�Ʊ");
			return ChargeCenter.charge_fail;
		}
		
	}

	/**
	 * ������Ʊ��
	 * @param sto
	 * @param plan_price
	 * @param operateAmount����Ӧ�ñ�֤��������С����������
	 */
	public int reduceSto(Sto sto, float plan_price, int operateAmount) {
		ShareHolding shtemp_SellAll=null;
		boolean flag_isHodingThisSto=false;
		for(ShareHolding shtemp:holdingList){
			if(shtemp.sto.code.equals(sto.code)){
				if(shtemp.availableAmountForSell<operateAmount){
					logger.error("Ҫ�������Ĺ�Ʊ�������ڵ�ǰ������Ʊ������");
					return ChargeCenter.charge_fail;
				}else{
					flag_isHodingThisSto=true;
					shtemp_SellAll=shtemp;
					break;					
				}
			}
		}
		if(flag_isHodingThisSto){
			int alreadyMount=shtemp_SellAll.hodingAmount;					
			int nowMount=alreadyMount-operateAmount;
			if(nowMount==0){
				holdingList.remove(shtemp_SellAll);
			}else{
				shtemp_SellAll.costPrice=(shtemp_SellAll.costPrice*alreadyMount-plan_price*operateAmount)/(nowMount*1.0f);
				shtemp_SellAll.availableAmountForSell-=operateAmount;
				shtemp_SellAll.hodingAmount=nowMount;		
			}
			this.leftMoney.add(new BigDecimal(operateAmount*plan_price));
			return ChargeCenter.charge_succuss;
		}else{
			logger.error("Ҫ�������Ĺ�Ʊ���ڵ�ǰ���й�Ʊ��");
			return ChargeCenter.charge_fail;
		}
		
		
		
		
		
	}

	public void availableAmountForSellProcess() {
		for(ShareHolding shtemp:holdingList){
			shtemp.availableAmountForSell=shtemp.hodingAmount;
		}
		
	}

}
