package com.mrj.operate.policy;

import com.mrj.person.CapitalSituation;
import com.mrj.person.ShareHolding;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.DHQ;
import com.mrj.sto.HQ;
import com.mrj.sto.Sto;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import org.apache.log4j.Logger;


/**
 *止赢止损 的操作模型
 * @author ruojun
 */


public class LastDayFinalPricePolicy extends OperatePolicy {
	public LastDayFinalPricePolicy(){
		
	}
	public LastDayFinalPricePolicy(float intrestRate,float lostRate){
		this.intrestRate=intrestRate;
		this.lostRate=lostRate;
	}

    static Logger logger = Logger.getLogger(LastDayFinalPricePolicy.class);
    private float intrestRate = 0.12f;//0.1f为默认值,止赢率
    private float lostRate=-0.10f;//止损率

    public float getIntrestRate() {
        return intrestRate;
    }

    public void setIntrestRate(float intrestRate) {
        this.intrestRate = intrestRate;
    }

    @Override
    public List<ChargeDescription> getBuyChargeDescriptionList(Calendar nextChargeDay, List<StoConfidenceValuePair> buyList) {
        BigDecimal leftMoney = getOwnerPerson().getCs().getLeftMoney();
        List<ChargeDescription> buyChargeDescriptionList = new ArrayList<ChargeDescription>();
        try {
            List<StoConfidenceValuePair> buyStoList = buyList;
            double confidence_all = 0;
            for (StoConfidenceValuePair scvp : buyStoList) {
                confidence_all += scvp.getConfidenceValue();
            }
            for (StoConfidenceValuePair scvp : buyStoList) {
                Sto sto = scvp.getSto();
                double confidence = scvp.getConfidenceValue();
                //资金分配的方法：1、首先根据信心值的比重预分配 2然后根据信心值大小决定投入多少资金
                BigDecimal allotCapital = leftMoney.multiply(new BigDecimal(confidence * confidence / confidence_all));
                float planPrice = getPlanPrice(scvp, nextChargeDay);
                if (planPrice == 0) {
                    continue;
                }
                int planMount = new BigDecimal(allotCapital.floatValue() / planPrice).intValue() / 100 * 100;
                if (planMount > 0) {
                    ChargeDescription temp = new ChargeDescription(sto, ChargeDescription.operationType_buy, planMount, planPrice);
                    buyChargeDescriptionList.add(temp);
                }
            }

        } catch (Exception e) {
            logger.error("", e);
        }
        return buyChargeDescriptionList;

    }

    @Override
    public List<ChargeDescription> getSellChargeDescriptionList(Calendar nextChargeDay, List<StoConfidenceValuePair> OwnStoList) {
        List<ChargeDescription> sellChargeDescriptionList = new ArrayList<ChargeDescription>();
        CapitalSituation cs = getOwnerPerson().getCs();
        List<ShareHolding> a = cs.getHoldingList();
        if (a != null) {
            for (ShareHolding s : a) {
                Sto sto = s.getSto();
                float lastDayPrice=getLastDayPrice(sto, nextChargeDay, "getFinalPrice");
                float costPrice=this.getOwnerPerson().getCs().getHoldingMap().get(sto.getCode()).getCostPrice();
                float planPrice=0;
                if((lastDayPrice-costPrice)/costPrice<lostRate){
                	planPrice=lastDayPrice;//以上一交易日的收盘价卖出，类似买入时以上一交易日的收盘价买入                    
                }else{
                	planPrice = s.getCostPrice() < 0 ? 0 : s.getCostPrice() * (1 + intrestRate);                    
                }
                ChargeDescription temp = new ChargeDescription(sto, ChargeDescription.operationType_sell, s.getAvailableAmountForSell(), planPrice);
                sellChargeDescriptionList.add(temp);
                
            }
        }
        return sellChargeDescriptionList;
    }

    private float getPlanPrice(StoConfidenceValuePair scvp, Calendar nextChargeDay) {
        float planPrice = 0;
        Calendar temp = Calendar.getInstance();
        temp.setTime(nextChargeDay.getTime());
        Sto sto = scvp.getSto();
        HQ hq = sto.getHq();
        while (true) {
            temp.add(Calendar.DAY_OF_YEAR, -1);
            boolean isBeforeearliestDate = false;
            Calendar earliestDate = Calendar.getInstance();
            earliestDate.setTime(hq.getEarliestDate());
            isBeforeearliestDate = temp.compareTo(earliestDate) < 0;
            if (hq.getDailyHQ(temp.getTime()) != null || isBeforeearliestDate) {
                if (hq.getDailyHQ(temp.getTime()) != null) {
                    planPrice = hq.getDailyHQ(temp.getTime()).getFinalPrice();
                }
                return planPrice;
            }
        }
    }
    
    /**
	 * 根据条件，获取nextChargeDay日之前的一天的某种价格。
	 * 
	 * @param sto
	 * @param nextChargeDay
	 * @param
	 * @return
	 */
	public float getLastDayPrice(Sto sto, Calendar nextChargeDay, String getMethodName) {
		float price = 0;
		DHQ dhq = null;
		Calendar temp = Calendar.getInstance();
		temp.setTime(nextChargeDay.getTime());
		HQ hq = sto.getHq();
		Calendar earliestDate = Calendar.getInstance();
		earliestDate.setTime(hq.getEarliestDate());
		while (true) {
			temp.add(Calendar.DAY_OF_YEAR, -1);
			boolean isBeforeearliestDate = false;
			isBeforeearliestDate = temp.compareTo(earliestDate) < 0;
			if (hq.getDailyHQ(temp.getTime()) != null || isBeforeearliestDate) {
				if (hq.getDailyHQ(temp.getTime()) != null) {
					dhq = hq.getDailyHQ(temp.getTime());
					Class clazz = dhq.getClass();
					try {
						Method m1 = clazz.getDeclaredMethod(getMethodName);
						price = (Float) m1.invoke(dhq);
					} catch (Exception e) {
						logger.error("", e);
					}

				}
				return price;
			}
		}
	}
	@Override
	public Object getInstanceWithPropertiesList(ArrayList<Properties> list) {
		// TODO Auto-generated method stub
		return null;
	}
}
