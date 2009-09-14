package com.mrj.dm.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * AssetDayData entity. @author MyEclipse Persistence Tools
 */

public class AssetDayData implements java.io.Serializable {

	// Fields

	private String assetDayDataUuid;
	private String userUuid;
	private Date dateTime;
	private Double assetValue;

	// Constructors

	/** default constructor */
	public AssetDayData() {
	}

	
	/** full constructor */
	public AssetDayData(String userUuid, Date dateTime, Double assetValue) {
		this.userUuid = userUuid;
		this.dateTime = dateTime;
		this.assetValue = assetValue;
	}

	// Property accessors

	public String getAssetDayDataUuid() {
		return this.assetDayDataUuid;
	}

	public void setAssetDayDataUuid(String assetDayDataUuid) {
		this.assetDayDataUuid = assetDayDataUuid;
	}

	public String getUserUuid() {
		return this.userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Double getAssetValue() {
		return this.assetValue;
	}

	public void setAssetValue(Double assetValue) {
		this.assetValue = assetValue;
	}

}