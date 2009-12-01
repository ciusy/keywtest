package com.mrj.charge;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mrj.person.Person;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.sto.DHQ;
import com.mrj.sto.Sto;

public class ChargeCenter {

    static Logger logger = Logger.getLogger(ChargeCenter.class);
    public static final float brokerageRate = 2.5f / 1000.0f;
    public static final float stampRate = 1f / 1000.0f;
    public static final int charge_succuss = 1;
    public static final int charge_fail = 2;
    public static final float minimumBrokerage=6f;//最低的手续费+印花税=6元
    public ChargeCenter() {
    }

    public synchronized int charge(Person p, ChargeDescription cd, Date date) {
        Sto sto = cd.sto;
        int operationType = cd.operationType;
        int operateAmount = cd.operateAmount;
        float plan_price = cd.plan_price;
        DHQ dhq = sto.getHq().getDailyHQ(date);
        if (dhq != null) {
            if (operationType == ChargeDescription.operationType_buy) {
                if (plan_price >= dhq.getLowestPrice()) {
                    if (plan_price >= dhq.getHighPrice()) {
                        plan_price = dhq.getHighPrice();
                    }
                    if(plan_price>=dhq.getBeginPrice()){//如果设置的买入价格比开盘价格高，则会以开盘价买入
                    	plan_price = dhq.getBeginPrice();
                    }
                    return p.getCs().increaseSto(sto, plan_price, operateAmount,date);
                } else {
                    return ChargeCenter.charge_fail;
                }
            } else {
                if (plan_price <= dhq.getHighPrice()) {
                    if (plan_price <= dhq.getLowestPrice()) {
                        plan_price = dhq.getLowestPrice();
                    }
                    if(plan_price<=dhq.getBeginPrice()){//如果设置的卖出价格比开盘价格低，则会以开盘价卖出
                    	plan_price = dhq.getBeginPrice();
                    }
                    return p.getCs().reduceSto(sto, plan_price, operateAmount,date);
                } else {
                    return ChargeCenter.charge_fail;
                }
            }
        } else {
            return ChargeCenter.charge_fail;
        }



    }
}
