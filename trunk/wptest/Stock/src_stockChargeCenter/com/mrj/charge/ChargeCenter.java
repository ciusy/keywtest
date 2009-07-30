package com.mrj.charge;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mrj.person.Person;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.sto.DHQ;
import com.mrj.sto.Sto;

public class ChargeCenter {

    static Logger logger = Logger.getLogger(ChargeCenter.class);
    float brokerageRate = 2.5f / 1000.0f;
    float stampRate = 1f / 1000.0f;
    public static final int charge_succuss = 1;
    public static final int charge_fail = 2;

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
                    return p.getCs().increaseSto(sto, plan_price, operateAmount,date);
                } else {
                    return ChargeCenter.charge_fail;
                }
            } else {
                if (plan_price <= dhq.getHighPrice()) {
                    if (plan_price <= dhq.getLowestPrice()) {
                        plan_price = dhq.getLowestPrice();
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
