package com.mrj.person;

import java.math.BigDecimal;
import java.util.Date;

public class Person {
	Policy policy;

	CapitalSituation cs=new CapitalSituation();

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
