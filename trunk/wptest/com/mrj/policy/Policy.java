package com.mrj.policy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.sto.HQ;
import com.mrj.sto.Sto;

public abstract class Policy {
	static Logger logger = Logger.getLogger(Policy.class);
	public Person ownerPerson = null;

	public Person getOwnerPerson() {
		return ownerPerson;
	}

	public void setOwnerPerson(Person p) {
		this.ownerPerson = p;
	}

	public int chargeDescription_size;// getChargePlan的返回结果不超过chargeDescription_size只

	/**
	 * CapitalAllotModel 资金分配模型
	 * 
	 * @author ruojun
	 * 
	 */
	public static enum CapitalAllotModel {
		confidenceValueModel
		// 信心值分配模型：每一个选好的股票都有一个信心值。信心值介于0~1之间。信心值关系着选好的股票要买多少的问题。
		// 根据信心值的比例分配剩余资金，然后各个股票根据分配好的资金和信心值，决定实际投入多少资金。

	};

	/**
	 * 预买入卖出价格决策模型
	 * @author ruojun
	 * 
	 */
	public static enum PlanPriceDecideModel {
		lastDayFinalPriceModel//上一交易日的收盘价模型
	}

	/**
	 * 根据begin之前的所有数据分析出begin这一天计划的操作
	 * 
	 * @param begin
	 * @return
	 */
	public abstract List<ChargeDescription> getChargePlan(Calendar begin);

	@SuppressWarnings("unchecked")
	public List<ChargeDescription> getBuyChargeDescriptionList(Object o, CapitalAllotModel caModel,PlanPriceDecideModel ppdModel,Calendar begin) {
		BigDecimal leftMoney =getOwnerPerson().getCs().getLeftMoney();
		List<ChargeDescription> buyChargeDescriptionList = new ArrayList<ChargeDescription>();
		switch (caModel) {
		case confidenceValueModel: {
			try {
				Map<Sto, Double> buyStoMap = (Map<Sto, Double>) o;
				double confidence_all=0;
				Collection c = buyStoMap.keySet();
				for (Iterator i = c.iterator(); i.hasNext();){
					confidence_all+=buyStoMap.get(i.next());
				}
				for (Iterator i = c.iterator(); i.hasNext();) {
					Sto sto=(Sto) i.next();
					double confidence=buyStoMap.get(sto);
				    BigDecimal allotCapital=leftMoney.multiply(new BigDecimal(confidence*confidence/confidence_all));
				    float planPrice=getPlanPrice(sto,ppdModel,begin);
				    if(planPrice==0)continue;
				    int planMount=new BigDecimal(allotCapital.floatValue()/planPrice).intValue()/100*100;
				    if(planMount>0){
				    	ChargeDescription temp = new ChargeDescription(sto,ChargeDescription.operationType_buy,planMount,planPrice);
						buyChargeDescriptionList.add(temp);
				    }					
				}

			} catch (Exception e) {
				logger.error("", e);
			}
			return buyChargeDescriptionList;
		}
		default:
			break;
		}
		//	

		return null;
	}
	
	

	public  List<ChargeDescription> getSellChargeDescriptionList() {
		return null;
	}


	private float getPlanPrice(Sto sto, PlanPriceDecideModel ppdModel,Calendar begin) {
		float planPrice=0;
		Calendar temp=Calendar.getInstance();
		temp.setTime(begin.getTime());
		switch (ppdModel) {
		case lastDayFinalPriceModel:
			HQ hq=sto.hq;
			while(true){
				temp.add(Calendar.DAY_OF_YEAR, -1);
				boolean isBeforeearliestDate=false;
				Calendar earliestDate=Calendar.getInstance();
				earliestDate.setTime(hq.earliestDate);
				isBeforeearliestDate=temp.compareTo(earliestDate)<0;
				if(hq.getDailyHQ(temp.getTime())!=null||isBeforeearliestDate){
					if(hq.getDailyHQ(temp.getTime())!=null){
						planPrice=hq.getDailyHQ(temp.getTime()).getFinalPrice();
					}
					return planPrice;
				}
			}

		default:
			break;
		}
		return planPrice;
	}
}
