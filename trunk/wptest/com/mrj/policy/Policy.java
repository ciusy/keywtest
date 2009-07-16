package com.mrj.policy;

import java.util.*;

import com.mrj.person.CapitalSituation;
import com.mrj.sto.Sto;

public abstract class Policy { 
	//Hashtable<Sto,Float> choose;
	public abstract void charge(Date date,CapitalSituation cs);

	/**
	 * 根据begin之前的所有数据分析出begin这一天计划的操作
	 * @param begin
	 * @return
	 */
	public List<ChargeDescription> getChargePlan(Calendar begin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
