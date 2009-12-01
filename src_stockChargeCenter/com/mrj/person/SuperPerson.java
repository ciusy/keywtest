package com.mrj.person;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mrj.charge.ChargeCenter;
import com.mrj.operate.policy.OperatePolicy;
import com.mrj.policy.ChoosePolicy;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.sto.OriginalDataUtil;

public class SuperPerson extends Person {
	private Calendar nextChargeDay;
	private List<String> pUuidList;//person uuid
	private int period=10;//周期
	
	public SuperPerson(int period,List<String> pUuidList){
		this.period=period;
		this.pUuidList=pUuidList;
	}

	public void beginInvest(Date beginDate, Date endDate) {
		nextChargeDay = Calendar.getInstance();
		Calendar begin = nextChargeDay;
		begin.setTime(beginDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		while (begin.compareTo(end) <= 0) {
			if (OriginalDataUtil.isChargeDate(begin.getTime())) {
				doDailyInvest(begin);
			}
			begin.add(Calendar.DAY_OF_YEAR, 1);
		}
	}

	public void doDailyInvest(Calendar nextChargeDay) {

		availableAmountForSellProcess();// 初始化可卖数量
		List<ChargeDescription> sto_chargePlan = getOperatePolicy().getChargePlan(nextChargeDay);
		ChargeCenter cc = new ChargeCenter();
		if (sto_chargePlan.size() != 0) {
			logger.debug(nextChargeDay.getTime() + "的投资日记:");
		}
		for (ChargeDescription cd : sto_chargePlan) {
			cc.charge(this, cd, nextChargeDay.getTime());
		}
		insertCurrentDateAsset(nextChargeDay);
	}

	public ChoosePolicy getPolicy() {		
		return policy;
	}

	public OperatePolicy getOperatePolicy() {
		return operatePolicy;
	}

}
