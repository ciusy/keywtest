package com.mrj.person;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mrj.charge.ChargeCenter;
import com.mrj.dm.dao.AssetDayDataDao;
import com.mrj.dm.dao.PersonDao;
import com.mrj.dm.domain.AssetDayData;
import com.mrj.operate.policy.OperatePolicy;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.policy.ChoosePolicy;
import com.mrj.sto.Main;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.util.UUIDGenerator;

/**
 *
 * @author ruojun
 */
public class Person {

    static Logger logger = Logger.getLogger(Person.class);
    protected String userUuid;
    protected ChoosePolicy policy;
    protected OperatePolicy operatePolicy;   
    protected CapitalSituation cs ;
    protected String userId;

    
    protected String currentInvestResultUuid;//当前投资的批次号
	

    public String getCurrentInvestResultUuid() {
		return currentInvestResultUuid;
	}

	public void setCurrentInvestResultUuid(String currentInvestResultUuid) {
		this.currentInvestResultUuid = currentInvestResultUuid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Person(ChoosePolicy policy, OperatePolicy operatePolicy,CapitalSituation cs) {
        this.userUuid=UUIDGenerator.getUUID();
        this.setPolicy(policy);
        this.setOperatePolicy(operatePolicy);
        this.setCs(cs);
    }
	
	public Person(){
		
	}
	
	public Person(String userId,ChoosePolicy policy, OperatePolicy operatePolicy,CapitalSituation cs) {
       this(policy,operatePolicy,cs);
       this.userId=userId;
    }

    public void beginInvest(Date beginDate, Date endDate) {
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        while (begin.compareTo(end) <= 0) {
        	if(OriginalDataUtil.isChargeDate(begin.getTime())){
        		doDailyInvest(begin);
        	}
            begin.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    public void doDailyInvest(Calendar nextChargeDay) {
    	
        availableAmountForSellProcess();//初始化可卖数量
        List<ChargeDescription> sto_chargePlan = operatePolicy.getChargePlan(nextChargeDay);
        ChargeCenter cc = new ChargeCenter();
        if (sto_chargePlan.size() != 0) {
            logger.debug(nextChargeDay.getTime() + "的投资日记:");
        }
        for (ChargeDescription cd : sto_chargePlan) {
            cc.charge(this, cd, nextChargeDay.getTime());
        }
        insertCurrentDateAsset(nextChargeDay);
    }

    protected void insertCurrentDateAsset(Calendar nextChargeDay) {
    	AssetDayDataDao addao=new AssetDayDataDao(); 
    	AssetDayData asset=new AssetDayData(this.userUuid,nextChargeDay.getTime(),getCs().getTotalAssets(nextChargeDay.getTime()).doubleValue());
    	asset.setInvestResultUuid(getCurrentInvestResultUuid());
    	if(getCs().getTotalAssets(nextChargeDay.getTime()).doubleValue()<0){
    		System.out.println("error");
    	}
    	addao.add(asset);
	}

	protected void availableAmountForSellProcess() {
        cs.availableAmountForSellProcess();
    }

    public OperatePolicy getOperatePolicy() {
        return operatePolicy;
    }

    public void setOperatePolicy(OperatePolicy operatePolicy) {
        this.operatePolicy = operatePolicy;
        this.operatePolicy.setOwnerPerson(this);
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }



    public CapitalSituation getCs() {
        return cs;
    }

    public void setCs(CapitalSituation cs) {
        this.cs = cs;
        this.cs.setOwnerPerson(this);
    }

     public ChoosePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(ChoosePolicy po) {
        this.policy = po;
        this.policy.setOwnerPerson(this);

    }

    
    
    
    public static void main(String[] args) {
        Date date = new Date();
        Calendar begin = Calendar.getInstance();
        begin.setTime(date);
        begin.add(Calendar.DAY_OF_MONTH, 30);
        System.out.println(new SimpleDateFormat("MM/dd/yy").format(begin.getTime()));

    }
}
