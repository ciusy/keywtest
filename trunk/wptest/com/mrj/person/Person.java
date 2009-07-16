package com.mrj.person;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mrj.charge.ChargeCenter;
import com.mrj.policy.ChargeDescription;
import com.mrj.policy.Policy;

public class Person {
	Policy policy;

	CapitalSituation cs = new CapitalSituation();

	public CapitalSituation getCs() {
		return cs;
	}

	public void setCs(CapitalSituation cs) {
		this.cs = cs;
	}

	public void setPolicy(Policy po) {
		this.policy = po;

	}

	public void setInitMoney(BigDecimal l) {
		cs.leftMoney = l;

	}

	public void beginInvest(Date beginDate, Date endDate) {
		Calendar begin = Calendar.getInstance();
		begin.setTime(beginDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		while (begin.compareTo(end) <= 0) {
			doDailyInvest(begin);
			begin.add(Calendar.DAY_OF_YEAR, 1);
		}
	}

	private void doDailyInvest(Calendar begin) {
		availableAmountForSellProcess();//每日交易前先使得修改可卖数量
		List<ChargeDescription> sto_tobuy_array=policy.getChargePlan(begin);
		ChargeCenter cc=new ChargeCenter();
		for(ChargeDescription cd:sto_tobuy_array){
			cc.charge(this, cd, begin.getTime());
		}
	}

	private void availableAmountForSellProcess() {
		cs.availableAmountForSellProcess();
	}

	public static void main(String[] args) {
		Date date = new Date();
		Calendar begin = Calendar.getInstance();
		begin.setTime(date);
		begin.add(Calendar.DAY_OF_MONTH, 30);
		System.out.println(new SimpleDateFormat("MM/dd/yy").format(begin
				.getTime()));

	}

}
