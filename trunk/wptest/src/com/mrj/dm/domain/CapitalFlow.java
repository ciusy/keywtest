package com.mrj.dm.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CapitalFlow {
	private String capitalFlowUuid; // (主键)记录UUID

	private String userUuid; // 用户uuID

	private String operationType; // 操作类型

	private Date beginTime; // 操作时间点

	private String stoCode;

	private int operateAmount;

	private float plan_price;

	private BigDecimal leftfee;// 剩余金额

	private BigDecimal fee; // 发生操作设计金额
	private String stoName;

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

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getStoCode() {
		return stoCode;
	}

	public void setStoCode(String stoCode) {
		this.stoCode = stoCode;
	}

	public int getOperateAmount() {
		return operateAmount;
	}

	public void setOperateAmount(int operateAmount) {
		this.operateAmount = operateAmount;
	}

	public float getPlan_price() {
		return plan_price;
	}

	public void setPlan_price(float plan_price) {
		this.plan_price = plan_price;
	}

	public BigDecimal getLeftfee() {
		return leftfee;
	}

	public void setLeftfee(BigDecimal leftfee) {
		this.leftfee = leftfee;
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
