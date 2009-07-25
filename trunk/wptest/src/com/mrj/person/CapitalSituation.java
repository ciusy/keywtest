package com.mrj.person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mrj.charge.ChargeCenter;
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
		float reFloat=0f;
		reFloat+=this.leftMoney.floatValue();
		for(int i=0;i<holdingList.size();i++){
			ShareHolding temp=holdingList.get(i);
			reFloat+=temp.getAssets(date).floatValue();
		}
		BigDecimal re = new BigDecimal(reFloat);
		return re;
	}

	public int increaseSto(Sto sto, float plan_price, int operateAmount) {
		if(this.leftMoney.compareTo(new BigDecimal(operateAmount*plan_price))>0){
			this.leftMoney=this.leftMoney.subtract(new BigDecimal(operateAmount*plan_price));
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
				float preCostPrice=shtemp.getCostPrice();
				int nowMount=alreadyMount+operateAmount;
				float costPrice=(preCostPrice*alreadyMount+operateAmount*plan_price)/(nowMount*1.0f);
				shtemp.setCostPrice(costPrice);
				shtemp.hodingAmount=nowMount;
			}else{
				ShareHolding shtemp=new ShareHolding();
				shtemp.setCostPrice(plan_price);;
				shtemp.hodingAmount=operateAmount;
				shtemp.sto=sto;
				holdingList.add(shtemp);
				
			}	
			logger.info("�Ѿ������Ʊ"+sto.getName()+operateAmount+"�ɣ��ɽ��۸�Ϊ"+plan_price+"Ԫ");
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
				shtemp_SellAll.setCostPrice((shtemp_SellAll.getCostPrice()*alreadyMount-plan_price*operateAmount)/(nowMount*1.0f));
				shtemp_SellAll.availableAmountForSell-=operateAmount;
				shtemp_SellAll.hodingAmount=nowMount;		
			}
			this.leftMoney=this.leftMoney.add(new BigDecimal(operateAmount*plan_price));
			logger.info("�Ѿ�������Ʊ"+sto.getName()+operateAmount+"�ɣ��ɽ��۸�Ϊ"+plan_price+"Ԫ");
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
