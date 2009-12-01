package com.mrj.dm.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CapitalFlow {

    public static final String sto_buy = "证券买入";
    public static final String sto_sell = "证券卖出";
    private String capitalFlowUuid; //资金流水的主键值
    private String userUuid; //与Pesron的关联
    private String stoName;//证券名称
    private String operationType; //业务名称：证券买入、证券卖出
    private Date actionTime; // 成交日期
    private float chargePrice;//成交价格
    private int chargeAmount;//成交数量
    private BigDecimal fee; //发生金额
    private BigDecimal leftFee;//资金余额
    private BigDecimal brokerage;//手续费+印花税
    private String stoCode;//证券代码
    private String investResultUuid;//投资实验的批次号码

    public String getInvestResultUuid() {
		return investResultUuid;
	}

	public void setInvestResultUuid(String investResultUuid) {
		this.investResultUuid = investResultUuid;
	}

	public CapitalFlow(String userUuid, String stoName, String operationType, Date actionTime, float chargePrice,int chargeAmount,
            BigDecimal fee, BigDecimal leftFee, BigDecimal brokerage, String stoCode,String investResultUuid) {//fee==chargePrice*chargeAmount-brokerage
        this.userUuid = userUuid;
        this.stoName = stoName;
        this.operationType = operationType;
        this.actionTime = actionTime;
        this.chargePrice = chargePrice;
        this.chargeAmount=chargeAmount;
        this.fee=fee;
        this.leftFee=leftFee;
        this.brokerage=brokerage;
        this.stoCode=stoCode;
        this.investResultUuid=investResultUuid;
    }

    public BigDecimal getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(BigDecimal brokerage) {
        this.brokerage = brokerage;
    }

    public String getCapitalFlowUuid() {
        return capitalFlowUuid;
    }

    public void setCapitalFlowUuid(String capitalFlowUuid) {
        this.capitalFlowUuid = capitalFlowUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public String getStoCode() {
        return stoCode;
    }

    public void setStoCode(String stoCode) {
        this.stoCode = stoCode;
    }

    public int getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(int chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public float getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(float chargePrice) {
        this.chargePrice = chargePrice;
    }

    public BigDecimal getLeftFee() {
        return leftFee;
    }

    public void setLeftFee(BigDecimal leftFee) {
        this.leftFee = leftFee;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getStoName() {
        return stoName;
    }

    public void setStoName(String stoName) {
        this.stoName = stoName;
    }
}
