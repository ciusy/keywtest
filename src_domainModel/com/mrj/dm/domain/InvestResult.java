package com.mrj.dm.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * InvestResult entity. @author MyEclipse Persistence Tools
 */

public class InvestResult implements java.io.Serializable {

	// Fields

	private String investResultUuid;
	private String userUuid;
	private String userId;
	private Date fromdate;
	private Date todate;
	private Double beginAsset;
	private Double endAsset;
	private Float rate;

	// Constructors

	/** default constructor */
	public InvestResult() {
	}

	/** full constructor */
	public InvestResult(String userUuid, String userId, Date fromdate, Date todate, Double beginAsset, Double endAsset, Float rate) {
		this.userUuid = userUuid;
		this.userId = userId;
		this.fromdate = fromdate;
		this.todate = todate;
		this.beginAsset = beginAsset;
		this.endAsset = endAsset;
		this.rate = rate;
	}

	// Property accessors

	public String getInvestResultUuid() {
		return this.investResultUuid;
	}

	public void setInvestResultUuid(String investResultUuid) {
		this.investResultUuid = investResultUuid;
	}

	public String getUserUuid() {
		return this.userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getFromdate() {
		return this.fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return this.todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public Double getBeginAsset() {
		return this.beginAsset;
	}

	public void setBeginAsset(Double beginAsset) {
		this.beginAsset = beginAsset;
	}

	public Double getEndAsset() {
		return this.endAsset;
	}

	public void setEndAsset(Double endAsset) {
		this.endAsset = endAsset;
	}

	public Float getRate() {
		return this.rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

}