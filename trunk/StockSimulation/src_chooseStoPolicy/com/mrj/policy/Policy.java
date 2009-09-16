package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;

import java.lang.reflect.Method;
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

	/**
	 * 获取某种价格的拟合值
	 * 
	 * @param nextChargeDay
	 * @param sto
	 * @param getMethodName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public float getPriceFittingResult(Calendar nextChargeDay, Sto sto, String getMethodName) {// 这个方法需要研究拟合预测后修改。目前仅对60天数据进行拟合
		List beforeList = new ArrayList();
		DHQ dhq = new DHQ();
		Class clazz = dhq.getClass();
		Method m1 = null;
		try {
			m1 = clazz.getDeclaredMethod(getMethodName);
		} catch (Exception e) {
			logger.error("", e);
		}
		Calendar begin = Calendar.getInstance();
		begin.setTime(nextChargeDay.getTime());
		begin.add(Calendar.DAY_OF_YEAR, -10);
		HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
		while (begin.compareTo(nextChargeDay) < 0) {
			if (map.get(begin.getTime()) != null) {
				dhq = map.get(begin.getTime());
				try {
					beforeList.add((Float) m1.invoke(dhq));
				} catch (Exception e) {
					logger.error("", e);
				}
			}
			begin.add(Calendar.DAY_OF_YEAR, 1);
		}

		return getFittingResult(beforeList);
	}

	public float getFinalPriceFittingResult(Calendar nextChargeDay, Sto sto) {// 这个方法需要研究拟合预测后修改。目前仅对60天数据进行拟合
		return getPriceFittingResult(nextChargeDay,sto,"getFinalPrice");		
	}

	public float getAverage5PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if (sto.getAverage5PriceFittingResultMap().get(nextChargeDay) == null) {			
			sto.getAverage5PriceFittingResultMap().put(nextChargeDay, getPriceFittingResult(nextChargeDay,sto,"getPrice_5day"));
		}
		return sto.getAverage5PriceFittingResultMap().get(nextChargeDay);
	}

	public float getAverage10PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if (sto.getAverage10PriceFittingResultMap().get(nextChargeDay) == null) {			
			sto.getAverage10PriceFittingResultMap().put(nextChargeDay, getPriceFittingResult(nextChargeDay,sto,"getPrice_10day"));
		}
		return sto.getAverage10PriceFittingResultMap().get(nextChargeDay);
	}

	public float getAverage20PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if (sto.getAverage20PriceFittingResultMap().get(nextChargeDay) == null) {			
			sto.getAverage20PriceFittingResultMap().put(nextChargeDay, getPriceFittingResult(nextChargeDay,sto,"getPrice_20day"));
		}
		return sto.getAverage20PriceFittingResultMap().get(nextChargeDay);
	}

	public float getAverage30PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if (sto.getAverage30PriceFittingResultMap().get(nextChargeDay) == null) {			
			sto.getAverage30PriceFittingResultMap().put(nextChargeDay, getPriceFittingResult(nextChargeDay,sto,"getPrice_30day"));
		}
		return sto.getAverage30PriceFittingResultMap().get(nextChargeDay);
	}

	public float getAverage60PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if (sto.getAverage60PriceFittingResultMap().get(nextChargeDay) == null) {			
			sto.getAverage60PriceFittingResultMap().put(nextChargeDay, getPriceFittingResult(nextChargeDay,sto,"getPrice_60day"));
		}
		return sto.getAverage60PriceFittingResultMap().get(nextChargeDay);
	}

	public float getAverage180PriceFittingResult(Calendar nextChargeDay, Sto sto) {
		if (sto.getAverage180PriceFittingResultMap().get(nextChargeDay) == null) {			
			sto.getAverage180PriceFittingResultMap().put(nextChargeDay, getPriceFittingResult(nextChargeDay,sto,"getPrice_180day"));
		}
		return sto.getAverage180PriceFittingResultMap().get(nextChargeDay);
	}

	public float getFittingResult(List<Float> beforeArray) {
		// TODO 拟合函数未完成，需要研究然后再完成。目前的实现：直接使用最后一天涨跌幅作用在最后一天的收盘价上，作为拟合的结果
		if (beforeArray.size() < 3) {
			if (beforeArray.size() - 1 < 0) {
				return 0;
			}
			return beforeArray.get(beforeArray.size() - 1);
		}
		float re = (beforeArray.get(beforeArray.size() - 1) - beforeArray.get(beforeArray.size() - 2)) / beforeArray.get(beforeArray.size() - 2);
		re = (1 + re) * (beforeArray.get(beforeArray.size() - 1));

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
		/*
		 * float finalPrice = 0; Calendar temp = Calendar.getInstance();
		 * temp.setTime(nextChargeDay.getTime()); HQ hq = sto.getHq(); Calendar
		 * earliestDate = Calendar.getInstance();
		 * earliestDate.setTime(hq.getEarliestDate()); while (true) {
		 * temp.add(Calendar.DAY_OF_YEAR, -1); boolean isBeforeearliestDate =
		 * false; isBeforeearliestDate = temp.compareTo(earliestDate) < 0; if
		 * (hq.getDailyHQ(temp.getTime()) != null || isBeforeearliestDate) { if
		 * (hq.getDailyHQ(temp.getTime()) != null) { finalPrice =
		 * hq.getDailyHQ(temp.getTime()).getFinalPrice(); } return finalPrice; }
		 * }
		 */
		return getLastDayPrice(sto, nextChargeDay, "getFinalPrice");
	}

	/**
	 * 根据条件，获取nextChargeDay日之前的一天的某种价格。
	 * 
	 * @param sto
	 * @param nextChargeDay
	 * @param
	 * @return
	 */
	public float getLastDayPrice(Sto sto, Calendar nextChargeDay, String getMethodName) {
		float price = 0;
		DHQ dhq = null;
		Calendar temp = Calendar.getInstance();
		temp.setTime(nextChargeDay.getTime());
		HQ hq = sto.getHq();
		Calendar earliestDate = Calendar.getInstance();
		earliestDate.setTime(hq.getEarliestDate());
		while (true) {
			temp.add(Calendar.DAY_OF_YEAR, -1);
			boolean isBeforeearliestDate = false;
			isBeforeearliestDate = temp.compareTo(earliestDate) < 0;
			if (hq.getDailyHQ(temp.getTime()) != null || isBeforeearliestDate) {
				if (hq.getDailyHQ(temp.getTime()) != null) {
					dhq = hq.getDailyHQ(temp.getTime());
					Class clazz = dhq.getClass();
					try {
						Method m1 = clazz.getDeclaredMethod(getMethodName);
						price = (Float) m1.invoke(dhq);
					} catch (Exception e) {
						logger.error("", e);
					}

				}
				return price;
			}
		}
	}
}
