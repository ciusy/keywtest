package com.mrj.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.mrj.person.ShareHolding;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;

public class DayAavAnalysePolicy extends ChoosePolicy {
	private int chargeDescription_size = 10;// 默认为3个

	private String periodStr_buy = "getPrice_" + 5 + "day";// 买入时的参考均线
	private String periodStr_sell = "getPrice_" + 5 + "day";// 卖出参考均线
	private boolean isReferenceSz = true;

	private int buyCondition = 1;// 买入方法
	public static final int BUY_CONDITION_1 = 1;//
	public static final int BUY_CONDITION_2 = 2;//
	public static final int BUY_CONDITION_3 =3;
	private int sellCondition = 1;// 卖出方法
	public static final int SELL_CONDITION_1 = 1;//
	public static final int SELL_CONDITION_2 = 2;//
	public static final int SELL_CONDITION_3 = 3;//

	public DayAavAnalysePolicy(int period) {
		if (period != 5 && period != 10 && period != 20 && period != 30 && period != 60 && period != 180)
			period = 5;
		periodStr_buy = "getPrice_" + period + "day";
		periodStr_sell = periodStr_buy;
	}

	public DayAavAnalysePolicy(int period_buy, int period_sell) {
		if (period_buy != 5 && period_buy != 10 && period_buy != 20 && period_buy != 30 && period_buy != 60 && period_buy != 180)
			period_buy = 5;
		if (period_sell != 5 && period_sell != 10 && period_sell != 20 && period_sell != 30 && period_sell != 60 && period_sell != 180)
			period_sell = 5;

		periodStr_buy = "getPrice_" + period_buy + "day";
		periodStr_sell = "getPrice_" + period_sell + "day";
	}

	public DayAavAnalysePolicy(int period_buy, int period_sell, boolean isReferenceSz, int buyCondition, int sellCondition) {
		if (period_buy != 5 && period_buy != 10 && period_buy != 20 && period_buy != 30 && period_buy != 60 && period_buy != 180)
			period_buy = 5;
		if (period_sell != 5 && period_sell != 10 && period_sell != 20 && period_sell != 30 && period_sell != 60 && period_sell != 180)
			period_sell = 5;

		periodStr_buy = "getPrice_" + period_buy + "day";
		periodStr_sell = "getPrice_" + period_sell + "day";
		this.isReferenceSz = isReferenceSz;
		this.buyCondition = buyCondition;
		this.sellCondition = sellCondition;
	}

	public DayAavAnalysePolicy() {
		// TODO Auto-generated constructor stub
	}

	private boolean canbuy(Sto sto, Calendar nextChargeDay) {
		switch (buyCondition) {
		case BUY_CONDITION_1:
			return canbuy_1(sto, nextChargeDay);
		case BUY_CONDITION_2:
			return canbuy_2(sto, nextChargeDay);
		case BUY_CONDITION_3:
			return canbuy_3(sto, nextChargeDay);

		default:
			return false;
		}
	}

	// 前三天均值小于lastDayPrice_AVGday，后三天均值大于lastDayPrice_AVGday，则买入
	private boolean canbuy_2(Sto sto, Calendar nextChargeDay) {
		float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
		float lastDayPrice_AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_buy);

		if (lastDayPrice > lastDayPrice_AVGday) {
			Calendar c2 = Calendar.getInstance();
			c2.setTime(nextChargeDay.getTime());
			c2.add(Calendar.DAY_OF_YEAR, -1);
			float lastDayPrice_2 = getLastDayFinalPrice(sto, c2);

			Calendar c3 = Calendar.getInstance();
			c3.setTime(nextChargeDay.getTime());
			c3.add(Calendar.DAY_OF_YEAR, -2);
			float lastDayPrice_3 = getLastDayFinalPrice(sto, c3);

			float avg3 = (float) ((lastDayPrice + lastDayPrice_2 + lastDayPrice_3) / 3.0);
			if (avg3 < lastDayPrice_AVGday) {
				return false;
			}
			Calendar c4 = Calendar.getInstance();
			c4.setTime(nextChargeDay.getTime());
			c4.add(Calendar.DAY_OF_YEAR, -3);
			float lastDayPrice_4 = getLastDayFinalPrice(sto, c4);

			Calendar c5 = Calendar.getInstance();
			c5.setTime(nextChargeDay.getTime());
			c5.add(Calendar.DAY_OF_YEAR, -4);
			float lastDayPrice_5 = getLastDayFinalPrice(sto, c5);

			Calendar c6 = Calendar.getInstance();
			c6.setTime(nextChargeDay.getTime());
			c6.add(Calendar.DAY_OF_YEAR, -5);
			float lastDayPrice_6 = getLastDayFinalPrice(sto, c6);

			float avgLast3 = (float) ((lastDayPrice_4 + lastDayPrice_5 + lastDayPrice_6) / 3.0);

			if (avgLast3 < lastDayPrice_AVGday) {
				return true;
			}

		}
		return false;
	}

	private boolean canbuy_1(Sto sto, Calendar nextChargeDay) {
		float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);

		float lastDayPrice_AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_buy);
		if (lastDayPrice > lastDayPrice_AVGday) {
			if ((lastDayPrice - lastDayPrice_AVGday) / lastDayPrice_AVGday > 0.02 && (lastDayPrice - lastDayPrice_AVGday) / lastDayPrice_AVGday < 0.07) {
				return true;
			}
		}
		return false;
	}
	
	private boolean canbuy_3(Sto sto, Calendar nextChargeDay) {//5avg 突破10avg buy
		//float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
		
		String firstAvg=periodStr_buy;
		String secondAvg=periodStr_sell;
		

		float lastDayPrice_5AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_buy);
		float lastDayPrice_10AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_sell);
		
		if(lastDayPrice_5AVGday<lastDayPrice_10AVGday)return false;
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(nextChargeDay.getTime());
		c1.add(Calendar.DAY_OF_YEAR, -1);
		float lastDayPrice_5AVGday_1 = getLastDayPrice(sto, c1, periodStr_buy);
		float lastDayPrice_10AVGday_1 = getLastDayPrice(sto, c1, periodStr_sell);
		
		if(lastDayPrice_5AVGday_1<lastDayPrice_10AVGday_1)return true;
		
		return false;
	}

	private boolean cansell(Sto sto, Calendar nextChargeDay) {
		switch (sellCondition) {
		case SELL_CONDITION_1:
			return cansell_1(sto, nextChargeDay);
		case SELL_CONDITION_2:
			return cansell_2(sto, nextChargeDay);
		case SELL_CONDITION_3:
			return cansell_3(sto, nextChargeDay);
		default:
			return false;
		}
	}
	
	private boolean cansell_3(Sto sto, Calendar nextChargeDay) {
		String firstAvg=periodStr_buy;
		String secondAvg=periodStr_sell;
		
		
		
		float lastDayPrice_5AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_buy);
		float lastDayPrice_10AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_sell);
		
		if(lastDayPrice_5AVGday>lastDayPrice_10AVGday)return false;
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(nextChargeDay.getTime());
		c1.add(Calendar.DAY_OF_YEAR, -1);
		float lastDayPrice_5AVGday_1 = getLastDayPrice(sto, c1, periodStr_buy);
		float lastDayPrice_10AVGday_1 = getLastDayPrice(sto, c1, periodStr_sell);
		
		if(lastDayPrice_5AVGday_1>lastDayPrice_10AVGday_1)return true;
		
		return false;
	}

	private boolean cansell_2(Sto sto, Calendar nextChargeDay) {
		float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
		float lastDayPrice_AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_sell);
		if (lastDayPrice < lastDayPrice_AVGday) {
			return true;
		}

		return false;
	}

	private boolean cansell_1(Sto sto, Calendar nextChargeDay) {

		float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
		float lastDayPrice_AVGday = getLastDayPrice(sto, nextChargeDay, periodStr_sell);
		if (lastDayPrice < lastDayPrice_AVGday) {
			return true;
		}

		return false;
	}

	@Override
	public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
		List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();
		if (isReferenceSz) {
			float sz_lastDayPrice = getLastDayFinalPrice(OriginalDataUtil.getAllStoMap().get("999999"), nextChargeDay);
			float sz_lastDayPrice_5avg = getLastDayPrice(OriginalDataUtil.getAllStoMap().get("999999"), nextChargeDay, periodStr_buy);
			if (sz_lastDayPrice < sz_lastDayPrice_5avg)
				return re;// 如果上证在5日线下方，则不买入
		}

		List<Sto> allStoList = OriginalDataUtil.getAllStoList();

		for (Sto sto : allStoList) {
			try {

				if (canbuy(sto, nextChargeDay))
					re.add(new StoConfidenceValuePair(sto, (float) (0.8f + 0.2 * Math.random())));

			} catch (Exception e) {
				logger.error("", e);
			}
		}
		StoConfidenceValuePair[] reArray = new StoConfidenceValuePair[re.size()];
		re.toArray(reArray);
		Arrays.sort(reArray);
		List<StoConfidenceValuePair> re_sort = new ArrayList<StoConfidenceValuePair>();
		re_sort = Arrays.asList(reArray);
		List<StoConfidenceValuePair> re_final = new ArrayList<StoConfidenceValuePair>();
		for (int i = re_sort.size() - 1 - chargeDescription_size; i < re_sort.size(); i++) {
			if (i < 0) {
				i = -1;
				continue;
			}
			re_final.add(re_sort.get(i));
		}
		return re_final;
	}

	@Override
	public List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay) {
		List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();
		for (ShareHolding sh : getOwnerPerson().getCs().getHoldingList()) {
			if (cansell(sh.getSto(), nextChargeDay)) {
				re.add(new StoConfidenceValuePair(sh.getSto(), 0));
			}
		}
		return re;
	}

	@Override
	public Object getInstanceWithPropertiesList(ArrayList<Properties> list) {
		DayAavAnalysePolicy re = new DayAavAnalysePolicy();
		// TODO Auto-generated method stub
		return re;
	}

}
