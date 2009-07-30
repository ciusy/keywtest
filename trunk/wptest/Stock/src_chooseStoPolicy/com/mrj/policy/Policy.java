package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.sto.HQ;
import com.mrj.sto.Sto;
/**
 * 策略应该包括两个方面：
 * (染色体)-(基因)、(基因)
 * 1、如何选择买入股票——随机、价格拟合预测、成交量拟合预测选股、大盘指数走势分析、行业走势
 *
 *
 * 1、选股策略
 * 单方面的策略：随机选股、价格拟合预测选股、成交量拟合预测选股、
 * 2、操作策略
 *  买入时机：随机选股买入、价格拟合预测选股
 *  买卖时机：赢利点卖出、止损点卖出、根据高低价格预测波段买入卖出
 *  价位设置测量：前一天收盘价买入（卖出）、市价买入（卖出）、最低（高）价拟合预测买入（卖出）
 *  买卖数量：
 *
 *
 * 仅给出一定的买入股票推荐信心值和已有股票的信心值
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
         * 给出对一些股票的信息值List
         * @param nextChargeDay
         * @return
         */
        public abstract List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay);
        /**
         * 给出已有仓位中股票的信心值List
         * @param nextChargeDay 下一交易日
         * @return
         */
        public abstract List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay);

	
}
