package com.mrj.operate.policy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mrj.person.CapitalSituation;
import com.mrj.person.ShareHolding;
import com.mrj.policy.Policy;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.Sto;

/**
 * n日均线操作模型 低于n日线卖，高于m日线买
 * 
 * @author ruojun
 * 
 */
public class DayAvgOperatePolicy extends OperatePolicy {
	static Logger logger = Logger.getLogger(DayAvgOperatePolicy.class);

	@Override
	public List<ChargeDescription> getBuyChargeDescriptionList(
			Calendar nextChargeDay, List<StoConfidenceValuePair> buyList) {
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
				// 资金分配的方法：1、首先根据信心值的比重预分配 2然后根据信心值大小决定投入多少资金
				BigDecimal allotCapital = leftMoney.multiply(new BigDecimal(
						confidence * confidence / confidence_all));
				float planPrice = getLastDayFinalPrice(scvp.getSto(),nextChargeDay);
				if (planPrice == 0) {
					continue;
				}
				int planMount = new BigDecimal(allotCapital.floatValue()
						/ planPrice).intValue() / 100 * 100;
				if (planMount > 0) {
					ChargeDescription temp = new ChargeDescription(sto,
							ChargeDescription.operationType_buy, planMount,
							planPrice);
					buyChargeDescriptionList.add(temp);
				}
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		return buyChargeDescriptionList;
	}

	@Override
	public List<ChargeDescription> getSellChargeDescriptionList(
			Calendar nextChargeDay, List<StoConfidenceValuePair> ownStoList) {
		List<ChargeDescription> sellChargeDescriptionList = new ArrayList<ChargeDescription>();
		
		CapitalSituation cs = getOwnerPerson().getCs();
		
		for (StoConfidenceValuePair scvp : ownStoList) {
			ShareHolding s= cs.getHoldingMap().get(scvp.getSto().getCode());
			float planPrice=getLastDayFinalPrice(scvp.getSto(),nextChargeDay);
			 ChargeDescription temp = new ChargeDescription(scvp.getSto(), ChargeDescription.operationType_sell, s.getAvailableAmountForSell(), planPrice);
			sellChargeDescriptionList.add(temp);
		}
		return sellChargeDescriptionList;
	}

	@Override
	public Object getInstanceWithPropertiesList(ArrayList<Properties> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
