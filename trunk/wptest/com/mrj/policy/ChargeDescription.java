package com.mrj.policy;

import com.mrj.sto.*;

public class ChargeDescription {
	public static final int operationType_buy=1;
	public static final int operationType_sell=-1;
	public Sto sto;
	public int operationType;
	public int operateAmount;
	public float plan_price;
}
