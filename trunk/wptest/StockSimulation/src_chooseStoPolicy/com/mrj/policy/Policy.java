package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.sto.DHQ;
import com.mrj.sto.HQ;
import com.mrj.sto.Sto;

/**
 * 选个策略应该包括两个方面： 1、推荐买那些股票，每只股票的信心值是多少 2、分析已有股票的信心值
 * 
 * 如果很有信心就是信心值为1；反之为0；0～0.5 为预期是下跌；0.5～1为预期为上涨
 * 
 * @author ruojun
 */
public abstract class Policy {
	static Logger logger = Logger.getLogger(Policy.class);
	public Person ownerPerson = null;

	public Person getOwnerPerson() {
		return ownerPerson;
	}

	public void setOwnerPerson(Person p) {
		this.ownerPerson = p;
	}

	public float getFinalPriceFittingResult(Calendar nextChargeDay, Sto sto) {
		List beforeList = new ArrayList();
		Calendar begin = Calendar.getInstance();
		begin.setTime(sto.getHq().getEarliestDate());
		HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
		while (begin.compareTo(nextChargeDay) <= 0) {
			if (map.get(begin.getTime()) != null) {
				beforeList.add(map.get(begin.getTime()).getFinalPrice());
			}
			begin.add(Calendar.DAY_OF_YEAR, 1);
		}

		return getFittingResult(beforeList);
	}

	public float getAverage5PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if(sto.getAverage5PriceFittingResultMap().get(nextChargeDay)==null){
			List beforeList = new ArrayList();
			Calendar begin = Calendar.getInstance();
			begin.setTime(sto.getHq().getEarliestDate());
			HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
			while (begin.compareTo(nextChargeDay) <= 0) {
				if (map.get(begin.getTime()) != null) {
					beforeList.add(map.get(begin.getTime()).getPrice_5day());
				}
				begin.add(Calendar.DAY_OF_YEAR, 1);
			}
			sto.getAverage5PriceFittingResultMap().put(nextChargeDay, getFittingResult(beforeList)) ;
		}
		
		return sto.getAverage5PriceFittingResultMap().get(nextChargeDay);
		
	}

	public float getAverage10PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if(sto.getAverage10PriceFittingResultMap().get(nextChargeDay)==null){
			List beforeList = new ArrayList();
			Calendar begin = Calendar.getInstance();
			begin.setTime(sto.getHq().getEarliestDate());
			HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
			while (begin.compareTo(nextChargeDay) <= 0) {
				if (map.get(begin.getTime()) != null) {
					beforeList.add(map.get(begin.getTime()).getPrice_10day());
				}
				begin.add(Calendar.DAY_OF_YEAR, 1);
			}
			sto.getAverage10PriceFittingResultMap().put(nextChargeDay, getFittingResult(beforeList)) ;
		}
		
		return sto.getAverage10PriceFittingResultMap().get(nextChargeDay);
		
	}

	public float getAverage20PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if(sto.getAverage20PriceFittingResultMap().get(nextChargeDay)==null){
			List beforeList = new ArrayList();
			Calendar begin = Calendar.getInstance();
			begin.setTime(sto.getHq().getEarliestDate());
			HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
			while (begin.compareTo(nextChargeDay) <= 0) {
				if (map.get(begin.getTime()) != null) {
					beforeList.add(map.get(begin.getTime()).getPrice_20day());
				}
				begin.add(Calendar.DAY_OF_YEAR, 1);
			}
			sto.getAverage20PriceFittingResultMap().put(nextChargeDay, getFittingResult(beforeList)) ;
		}
		
		return sto.getAverage20PriceFittingResultMap().get(nextChargeDay);
		
	}

	public float getAverage30PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if(sto.getAverage30PriceFittingResultMap().get(nextChargeDay)==null){
			List beforeList = new ArrayList();
			Calendar begin = Calendar.getInstance();
			begin.setTime(sto.getHq().getEarliestDate());
			HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
			while (begin.compareTo(nextChargeDay) <= 0) {
				if (map.get(begin.getTime()) != null) {
					beforeList.add(map.get(begin.getTime()).getPrice_30day());
				}
				begin.add(Calendar.DAY_OF_YEAR, 1);
			}
			sto.getAverage30PriceFittingResultMap().put(nextChargeDay, getFittingResult(beforeList)) ;
		}
		
		return sto.getAverage30PriceFittingResultMap().get(nextChargeDay);
		
	}

	public float getAverage60PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if(sto.getAverage60PriceFittingResultMap().get(nextChargeDay)==null){
			List beforeList = new ArrayList();
			Calendar begin = Calendar.getInstance();
			begin.setTime(sto.getHq().getEarliestDate());
			HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
			while (begin.compareTo(nextChargeDay) <= 0) {
				if (map.get(begin.getTime()) != null) {
					beforeList.add(map.get(begin.getTime()).getPrice_60day());
				}
				begin.add(Calendar.DAY_OF_YEAR, 1);
			}
			sto.getAverage60PriceFittingResultMap().put(nextChargeDay, getFittingResult(beforeList)) ;
		}
		
		return sto.getAverage60PriceFittingResultMap().get(nextChargeDay);
		
	}

	public float getAverage180PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if(sto.getAverage180PriceFittingResultMap().get(nextChargeDay)==null){
			List beforeList = new ArrayList();
			Calendar begin = Calendar.getInstance();
			begin.setTime(sto.getHq().getEarliestDate());
			HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
			while (begin.compareTo(nextChargeDay) <= 0) {
				if (map.get(begin.getTime()) != null) {
					beforeList.add(map.get(begin.getTime()).getPrice_180day());
				}
				begin.add(Calendar.DAY_OF_YEAR, 1);
			}
			sto.getAverage180PriceFittingResultMap().put(nextChargeDay, getFittingResult(beforeList)) ;
		}
		
		return sto.getAverage180PriceFittingResultMap().get(nextChargeDay);
		
		
	}

	public float getFittingResult(List<Float> beforeArray) {
		// TODO 拟合函数未完成
		if (beforeArray.size() < 3) {
			if (beforeArray.size() - 1 < 0) {
				return 0;
			}
			return beforeArray.get(beforeArray.size() - 1);
		}
		float re = (beforeArray.get(beforeArray.size() - 1) - beforeArray.get(beforeArray.size() - 2))
				/ beforeArray.get(beforeArray.size() - 2);

		return re;
	}

	/**
	 * 给出对一些股票的信息值List
	 * 
	 * @param nextChargeDay
	 * @return
	 */
	public abstract List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay);

	/**
	 * 给出已有仓位中股票的信心值List
	 * 
	 * @param nextChargeDay
	 *            下一交易日
	 * @return
	 */
	public abstract List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay);

	public float getLastDayFinalPrice(Sto sto, Calendar nextChargeDay) {
		float planPrice = 0;
		Calendar temp = Calendar.getInstance();
		temp.setTime(nextChargeDay.getTime());
		HQ hq = sto.getHq();
		while (true) {
			temp.add(Calendar.DAY_OF_YEAR, -1);
			boolean isBeforeearliestDate = false;
			Calendar earliestDate = Calendar.getInstance();
			earliestDate.setTime(hq.getEarliestDate());
			isBeforeearliestDate = temp.compareTo(earliestDate) < 0;
			if (hq.getDailyHQ(temp.getTime()) != null || isBeforeearliestDate) {
				if (hq.getDailyHQ(temp.getTime()) != null) {
					planPrice = hq.getDailyHQ(temp.getTime()).getFinalPrice();
				}
				return planPrice;
			}
		}
	}
}
