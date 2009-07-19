package com.mrj.policy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mrj.person.CapitalSituation;
import com.mrj.person.ShareHolding;
import com.mrj.sto.HQ;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;

/**
 * 随机选择chargeDescription_size只股票买入。赢利intrestRate卖出全部可卖
 * 随机选择是否对已有股票卖出。
 * @author ruojun
 *
 */
public class RandomPolicy extends Policy {
	
	
	static Logger logger = Logger.getLogger(RandomPolicy.class);
	public int chargeDescription_size=3;
	public float intrestRate=0.1f;

	@Override
	public List<ChargeDescription> getChargePlan(Calendar begin) {
		int stoSizeInAll=OriginalDataUtil.getAllStoList().size();		
		Map<Sto,Double> buyStoMap=new HashMap<Sto,Double>();
		//每一个选好的股票都有一个信心值。信心值介于0~1之间。信心值关系着选好的股票要买多少的问题。
		//根据信心值的比例分配剩余资金，然后各个股票根据分配好的资金和信心值，决定实际投入多少资金。		
		for(int i=0;i<chargeDescription_size;i++){
			buyStoMap.put(OriginalDataUtil.getAllStoList().get(new BigDecimal(Math.random()*stoSizeInAll).intValue()), Math.random());
		}
		
		List<ChargeDescription> buyChargeDescriptionList= getBuyChargeDescriptionList(buyStoMap,CapitalAllotModel.confidenceValueModel,PlanPriceDecideModel.lastDayFinalPriceModel,begin);
		List<ChargeDescription> sellChargeDescriptionList= getSellChargeDescriptionList(begin);
		List<ChargeDescription> chargePlanDescriptionList=buyChargeDescriptionList;
		for(ChargeDescription c:sellChargeDescriptionList){
			chargePlanDescriptionList.add(c);
		}
		return chargePlanDescriptionList; 
	}
	
	public  List<ChargeDescription> getSellChargeDescriptionList(Calendar begin) {
		List<ChargeDescription> sellChargeDescriptionList = new ArrayList<ChargeDescription>();
		CapitalSituation cs=getOwnerPerson().getCs();
		List<ShareHolding>a=cs.getHoldingList();
		for(ShareHolding s:a){
			Sto sto=s.getSto();
			float planPrice=s.getCostPrice()<0?0:s.getCostPrice()*(1+0.1f);//以10%的利润价格卖出。或者以0元卖出(即以最低价卖出)。
			ChargeDescription temp = new ChargeDescription(sto,ChargeDescription.operationType_sell,s.getAvailableAmountForSell(),planPrice);
			sellChargeDescriptionList.add(temp);
		}
		return sellChargeDescriptionList;
		
	}

	public static void main(String[] args){
		int stoSizeInAll=300;
		double ram=java.lang.Math.random();
		int i=new BigDecimal(ram*stoSizeInAll).intValue();
		logger.info(i);
	}

}
