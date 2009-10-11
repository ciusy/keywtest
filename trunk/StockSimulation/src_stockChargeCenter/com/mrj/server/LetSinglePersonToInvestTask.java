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
	/*ChoosePolicy cp;
	OperatePolicy op;
	CapitalSituation cs;*/
	
	Person person;
	
	boolean needChart=false;
	CallBack sccb;
	/*public LetSinglePersonToInvestTask(String beginTime,String endTime,ChoosePolicy cp,OperatePolicy op,CapitalSituation cs){
		this.beginTime=beginTime;
		this.endTime=endTime;
		this.cp=cp;
		this.op=op;
		this.cs=cs;
	}*/
	
	public LetSinglePersonToInvestTask(String beginTime,String endTime,Person person){
		this.beginTime=beginTime;
		this.endTime=endTime;
		this.person=person;
	}
	
	public LetSinglePersonToInvestTask(String fromMMDDYYYY, String toMMDDYYYY, Person p, CallBack sccb) {
		this(fromMMDDYYYY,toMMDDYYYY,p);
		this.sccb=sccb;
	}

	@Override
	public void dotask() {
		float rate = Main.letPersonInvest(person, beginTime, endTime);
		sccb.call(person.getUserUuid());
		/*person.setRate(rate);
		String[] personUuidArray = new String[1];
		personUuidArray[0] = person.getUserUuid();
		
		if(needChart){
			ChartUtil.showAssetChart(personUuidArray);
		}*/
		
		
	}

	public boolean isNeedChart() {
		return needChart;
	}

	public void setNeedChart(boolean needChart) {
		this.needChart = needChart;
	}

}
