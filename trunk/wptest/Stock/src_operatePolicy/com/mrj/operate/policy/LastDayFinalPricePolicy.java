package com.mrj.operate.policy;

import com.mrj.person.CapitalSituation;
import com.mrj.person.ShareHolding;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.HQ;
import com.mrj.sto.Sto;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import org.apache.log4j.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ruojun
 */
public class LastDayFinalPricePolicy extends OperatePolicy {

    static Logger logger = Logger.getLogger(LastDayFinalPricePolicy.class);
    private float intrestRate = 0.1f;//0.1f为默认值

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
                float planPrice = s.getCostPrice() < 0 ? 0 : s.getCostPrice() * (1 + intrestRate);
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
}
