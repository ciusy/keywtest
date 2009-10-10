package com.mrj.server;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.mrj.operate.policy.DayAvgOperatePolicy;
import com.mrj.operate.policy.LastDayFinalPricePolicy;
import com.mrj.operate.policy.OperatePolicy;
import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.policy.ChoosePolicy;
import com.mrj.policy.DayAavAnalysePolicy;
import com.mrj.sto.Main;
import com.mrj.util.chart.ChartUtil;

public class LetSinglePersonToInvestTask extends Task{
	String beginTime = "01/01/2007";
	String endTime = "09/30/2009";
	ChoosePolicy cp;
	OperatePolicy op;
	CapitalSituation cs;
	
	boolean needChart=false;
	
	public LetSinglePersonToInvestTask(String beginTime,String endTime,ChoosePolicy cp,OperatePolicy op,CapitalSituation cs){
		this.beginTime=beginTime;
		this.endTime=endTime;
		this.cp=cp;
		this.op=op;
		this.cs=cs;
	}
	
	@Override
	public void dotask() {		
		Person person = new Person(cp, op, cs);
		//Person person1 = new Person(new DayAavAnalysePolicy(60,10), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		
		
		float rate = Main.letPersonInvest(person, beginTime, endTime);	
		
		String[] personUuidArray = new String[0];
		personUuidArray[0] = person.getUserUuid();
		
		if(needChart){
			ChartUtil.showAssetChart(personUuidArray);
		}
		
		
	}

	public boolean isNeedChart() {
		return needChart;
	}

	public void setNeedChart(boolean needChart) {
		this.needChart = needChart;
	}

}
