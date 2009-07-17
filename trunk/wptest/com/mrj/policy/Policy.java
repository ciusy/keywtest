package com.mrj.policy;

import java.util.*;

import com.mrj.person.CapitalSituation;
import com.mrj.sto.Sto;

public abstract class Policy { 
	public int chargeDescription_size;// getChargePlan的返回结果不超过chargeDescription_size只
	
	/**
	 * 根据begin之前的所有数据分析出begin这一天计划的操作
	 * @param begin
	 * @return
	 */
	public abstract List<ChargeDescription> getChargePlan(Calendar begin);
	
	
}
