package com.mrj.person;

import java.math.BigDecimal;
import java.util.Date;

import com.mrj.policy.Policy;

public class Person {
	Policy policy;

	CapitalSituation cs=new CapitalSituation();

	public CapitalSituation getCs() {
		return cs;
	}

	public void setCs(CapitalSituation cs) {
		this.cs = cs;
	}

	public void setPolicy(Policy po) {
		this.policy=po;

	}

	public void setInitMoney(BigDecimal l) {
		cs.leftMoney=l;

	}

	public void beginInvest(Date beginDate, Date endDate) {
		// TODO Auto-generated method stub
		
	}

}
