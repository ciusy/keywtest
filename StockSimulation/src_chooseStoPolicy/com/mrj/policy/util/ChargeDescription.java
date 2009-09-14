package com.mrj.policy.util;

import com.mrj.sto.*;

public class ChargeDescription {

    public static final int operationType_buy = 1;
    public static final int operationType_sell = -1;

    public ChargeDescription(Sto sto, int operationType, int operateAmount, float plan_price) {
        this.sto = sto;
        this.operateAmount = operateAmount;
        this.operationType = operationType;
        this.plan_price = plan_price;
    }
    public Sto sto;
    public int operationType;
    public int operateAmount;
    public float plan_price;
}
