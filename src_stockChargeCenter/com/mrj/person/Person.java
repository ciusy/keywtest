package com.mrj.person;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.logging.Level;
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
    protected CapitalSituation cs;
    protected String userId;
    protected String currentInvestResultUuid;//当前投资的结果Uuid

    public void clearCurrentInvestResult(){
        currentInvestResultUuid="";
        cs=null;
    }


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

    public Person(ChoosePolicy policy, OperatePolicy operatePolicy, CapitalSituation cs) {
        this.userUuid = UUIDGenerator.getUUID();
        this.setPolicy(policy);
        this.setOperatePolicy(operatePolicy);
        this.setCs(cs);
    }

    public Person() {
    }

    
private String choosePolicyName,choosePolicyArgs,  operatePolicyName,  operatePolicyArgs;

    public String getOperatePolicyArgs() {
        return operatePolicyArgs;
    }

    public void setOperatePolicyArgs(String operatePolicyArgs) {
        this.operatePolicyArgs = operatePolicyArgs;
    }

    public String getChoosePolicyArgs() {
        return choosePolicyArgs;
    }

    public void setChoosePolicyArgs(String choosePolicyArgs) {
        this.choosePolicyArgs = choosePolicyArgs;
    }

    public String getChoosePolicyName() {
        return choosePolicyName;
    }

    public void setChoosePolicyName(String choosePolicyName) {
        this.choosePolicyName = choosePolicyName;
    }

    public String getOperatePolicyName() {
        return operatePolicyName;
    }

    public void setOperatePolicyName(String operatePolicyName) {
        this.operatePolicyName = operatePolicyName;
    }

   

    public static Person getInstance(String userUuid,String name, String choosePolicyName, String choosePolicyArgs, String operatePolicyName, String operatePolicyArgs, CapitalSituation cs) {
        try {
        Person p=null;
            ChoosePolicy cp = (ChoosePolicy) Class.forName(choosePolicyName).getDeclaredMethod("getInstance", String.class).invoke(null, choosePolicyArgs);
            OperatePolicy op = (OperatePolicy) Class.forName(operatePolicyName).getDeclaredMethod("getInstance", String.class).invoke(null,operatePolicyArgs);
            p= new Person(name, cp, op, cs);
            p.setUserUuid(userUuid);
            return p;
        } catch (Exception ex) {
            logger.error("", ex);
            return null;
        }

    }

    public Person(String userId, ChoosePolicy policy, OperatePolicy operatePolicy, CapitalSituation cs) {
        this(policy, operatePolicy, cs);
        this.userId = userId;
        //String choosePolicyName,choosePolicyArgs,  operatePolicyName,  operatePolicyArgs;
        this.choosePolicyName=policy.getClass().getSimpleName();
        this.choosePolicyArgs=policy.getFullargs();
        this.operatePolicyName=operatePolicy.getClass().getSimpleName();
        this.operatePolicyArgs=operatePolicy.getFullargs();
    }

    public void beginInvest(Date beginDate, Date endDate) {
        Calendar begin = Calendar.getInstance();
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
        AssetDayDataDao addao = new AssetDayDataDao();
        AssetDayData asset = new AssetDayData(this.userUuid, nextChargeDay.getTime(), getCs().getTotalAssets(nextChargeDay.getTime()).doubleValue());
        asset.setInvestResultUuid(getCurrentInvestResultUuid());
        if (getCs().getTotalAssets(nextChargeDay.getTime()).doubleValue() < 0) {
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

    private float currentInvestRate=1;

    public float getCurrentInvestRate() {
        return currentInvestRate;
    }

    public void setCurrentInvestRate(float currentInvestRate) {
        this.currentInvestRate = currentInvestRate;
    }


    public static void main(String[] args) {
        Date date = new Date();
        Calendar begin = Calendar.getInstance();
        begin.setTime(date);
        begin.add(Calendar.DAY_OF_MONTH, 30);
        System.out.println(new SimpleDateFormat("MM/dd/yy").format(begin.getTime()));

    }
}
