package com.mrj.server;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;

import com.mrj.dm.dao.InvestResultDAO;
import com.mrj.dm.dao.PersonDao;
import com.mrj.dm.domain.InvestResult;
import com.mrj.operate.policy.DayAvgOperatePolicy;
import com.mrj.operate.policy.LastDayFinalPricePolicy;
import com.mrj.operate.policy.OperatePolicy;
import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.policy.ChoosePolicy;
import com.mrj.policy.DayAavAnalysePolicy;
import com.mrj.sto.Main;
import com.mrj.util.UUIDGenerator;
import com.mrj.util.chart.ChartUtil;

public class LetSinglePersonToInvestTask extends Task{
	public static Map<String,Float> personAverageRate=new HashMap<String,Float>();
	
	Logger logger = Logger.getLogger(LetSinglePersonToInvestTask.class);
	
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
		float rate = letPersonInvest(person, beginTime, endTime);
		sccb.call(person.getUserUuid());		
	}

	public  float letPersonInvest(Person p, String fromMMDDYYYY, String toMMDDYYYY) {
		new PersonDao().add(p);
		BigDecimal atbeginning = p.getCs().getLeftMoney();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
		String currentInvestResultUuid=UUIDGenerator.getUUID();
		p.setCurrentInvestResultUuid(currentInvestResultUuid);
		try {
			p.beginInvest(sdf.parse(fromMMDDYYYY), sdf.parse(toMMDDYYYY));
			BigDecimal atFinal=p.getCs().getTotalAssets(sdf.parse(toMMDDYYYY));
			float re = (atFinal.floatValue() - atbeginning.floatValue()) / atbeginning.floatValue();
			String reinfo = p.getUserUuid()+"-"+p.getUserId()+"-"+"赢利结果：赢利" + re + "倍";
			addAverageHashMap(p.getUserId(),re);
			logger.info(reinfo);
			InvestResult ir=new InvestResult(p.getUserUuid(),p.getUserId(),sdf.parse(fromMMDDYYYY),sdf.parse(toMMDDYYYY),atbeginning.doubleValue(),atFinal.doubleValue(),re);
			ir.setInvestResultUuid(currentInvestResultUuid);
			new InvestResultDAO().save(ir);
			return re;

		} catch (Exception e) {
			logger.error("", e);
		}
		return 1;
	}
	
	
	private  synchronized void addAverageHashMap(String userId, float re) {
		if(personAverageRate.get(userId)!=null){
			float oldRate=personAverageRate.get(userId);
			float newRate=oldRate+re;
			personAverageRate.put(userId, newRate);
		}else{
			personAverageRate.put(userId, re);
		}
		
	}

	public boolean isNeedChart() {
		return needChart;
	}

	public void setNeedChart(boolean needChart) {
		this.needChart = needChart;
	}

}
