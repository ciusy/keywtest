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
 * ���ѡ��chargeDescription_sizeֻ��Ʊ���롣Ӯ��intrestRate����ȫ������
 * ���ѡ���Ƿ�����й�Ʊ������
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
		//ÿһ��ѡ�õĹ�Ʊ����һ������ֵ������ֵ����0~1֮�䡣����ֵ��ϵ��ѡ�õĹ�ƱҪ����ٵ����⡣
		//��������ֵ�ı�������ʣ���ʽ�Ȼ�������Ʊ���ݷ���õ��ʽ������ֵ������ʵ��Ͷ������ʽ�		
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
			float planPrice=s.getCostPrice()<0?0:s.getCostPrice()*(1+0.1f);//��10%������۸�������������0Ԫ����(������ͼ�����)��
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
