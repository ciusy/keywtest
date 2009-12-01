package com.mrj.sto;

import java.util.*;

public class Sto {
	private String name;
	private String code;
	private HQ hq;

	private Map<Calendar,Float> average5PriceFittingResultMap=new HashMap<Calendar,Float>();
	private Map<Calendar,Float> average10PriceFittingResultMap=new HashMap<Calendar,Float>();
	private Map<Calendar,Float> average20PriceFittingResultMap=new HashMap<Calendar,Float>();
	private Map<Calendar,Float> average30PriceFittingResultMap=new HashMap<Calendar,Float>();
	private Map<Calendar,Float> average60PriceFittingResultMap=new HashMap<Calendar,Float>();
	private Map<Calendar,Float> average180PriceFittingResultMap=new HashMap<Calendar,Float>();
	
	
	public Map<Calendar, Float> getAverage5PriceFittingResultMap() {
		return average5PriceFittingResultMap;
	}

	public void setAverage5PriceFittingResultMap(Map<Calendar, Float> average5PriceFittingResultMap) {
		this.average5PriceFittingResultMap = average5PriceFittingResultMap;
	}

	public Map<Calendar, Float> getAverage10PriceFittingResultMap() {
		return average10PriceFittingResultMap;
	}

	public void setAverage10PriceFittingResultMap(Map<Calendar, Float> average10PriceFittingResultMap) {
		this.average10PriceFittingResultMap = average10PriceFittingResultMap;
	}

	public Map<Calendar, Float> getAverage20PriceFittingResultMap() {
		return average20PriceFittingResultMap;
	}

	public void setAverage20PriceFittingResultMap(Map<Calendar, Float> average20PriceFittingResultMap) {
		this.average20PriceFittingResultMap = average20PriceFittingResultMap;
	}

	public Map<Calendar, Float> getAverage30PriceFittingResultMap() {
		return average30PriceFittingResultMap;
	}

	public void setAverage30PriceFittingResultMap(Map<Calendar, Float> average30PriceFittingResultMap) {
		this.average30PriceFittingResultMap = average30PriceFittingResultMap;
	}

	public Map<Calendar, Float> getAverage60PriceFittingResultMap() {
		return average60PriceFittingResultMap;
	}

	public void setAverage60PriceFittingResultMap(Map<Calendar, Float> average60PriceFittingResultMap) {
		this.average60PriceFittingResultMap = average60PriceFittingResultMap;
	}

	public Map<Calendar, Float> getAverage180PriceFittingResultMap() {
		return average180PriceFittingResultMap;
	}

	public void setAverage180PriceFittingResultMap(Map<Calendar, Float> average180PriceFittingResultMap) {
		this.average180PriceFittingResultMap = average180PriceFittingResultMap;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HQ getHq() {
		return hq;
	}

	public void setHq(HQ hq) {
		this.hq = hq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
