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
public abstract class ChoosePolicy extends Policy{
	static Logger logger = Logger.getLogger(ChoosePolicy.class);

	public Person ownerPerson = null;

	public Person getOwnerPerson() {
		return ownerPerson;
	}

	public void setOwnerPerson(Person p) {
		this.ownerPerson = p;
	}
	
	/**
	 * 给出对一些股票的信息值List
     *
	 * 
	 * @param nextChargeDay
	 * @return
	 */
	public abstract List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay);

	/**
	 * 给出已有仓位中股票的信心值List
     *
     * 许多情况下，如果由ChoosePolicy 来决定卖什么的话，这个方法也相当于卖什么getSellList
	 * 
	 * @param nextChargeDay
	 *            下一交易日
	 * @return
	 */
	public abstract List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay);

        

      
}
