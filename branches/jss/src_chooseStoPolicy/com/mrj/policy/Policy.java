package com.mrj.policy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.mrj.sto.DHQ;
import com.mrj.sto.HQ;
import com.mrj.sto.Sto;

public abstract class Policy implements initWithProperties{
	
	
	
	static Logger logger = Logger.getLogger(Policy.class);
	
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
		begin.add(Calendar.DAY_OF_YEAR, -7);
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

	
	/**
	 * 拟合的函数
	 * 实现未定
	 * @param beforeArray
	 * @return
	 */
	public float getFittingResult(List<Float> beforeArray) {//
		return getFittingResult2(beforeArray);		
	}
	
	
	/**
	 * 拟合的函数的第一种实现
	 * 目前的实现：直接使用最后一天涨跌幅作用在最后一天的收盘价上，作为拟合的结果
	 * @param beforeArray
	 * @return
	 */
	public float getFittingResult1(List<Float> beforeArray) {//
		
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
	 * 拟合的函数的第二种实现
	 * 目前的实现：//目前的实现：趋势平均值+涨跌幅平均值
	 * @param beforeArray
	 * @return
	 */
	public float getFittingResult2(List<Float> beforeArray) {//
		
		if (beforeArray.size() < 3) {
			if (beforeArray.size() - 1 < 0) {
				return 0;
			}
			return beforeArray.get(beforeArray.size() - 1);
		}
		
		
		//目前的实现：趋势平均值+涨跌幅平均值
		float re=beforeArray.get(beforeArray.size() - 1);		
		float[] cha_array=new float[beforeArray.size()-1];//涨跌幅
		for(int i=0;i<beforeArray.size()-1;i++){
			cha_array[i]=(beforeArray.get(i+1)-beforeArray.get(i))/beforeArray.get(i);
		}	
		float[] trend_array=new float[cha_array.length-1];//趋势
		for(int i=0;i<cha_array.length-1;i++){
			trend_array[i]=cha_array[i+1]-cha_array[i];
		}
		float trend_avg=0;//趋势平均值
		for(int i=0;i<trend_array.length;i++){
			trend_avg+=trend_array[i];
		}
		trend_avg=(float) (trend_avg/(trend_array.length*1.0));
		float cha_avg=0;
		for(int i=0;i<cha_array.length;i++){
			cha_avg+=cha_array[i];
		}
		cha_avg=(float)(cha_avg/cha_array.length*1.0);
		re=trend_avg+cha_avg;
		if(re==0)return beforeArray.get(beforeArray.size() - 1)*1;//如果没有算出值就返回最后一个值
		if(re<-0.1)return (float)(beforeArray.get(beforeArray.size() - 1)*(1-0.1));//如果太差返回跌停
		if(re>0.1)return (float)(beforeArray.get(beforeArray.size() - 1)*(1+0.1));//如果太好返回涨停
		else{
			return (float)(beforeArray.get(beforeArray.size() - 1)*(1+re));
		}
	}
	
	
	

	

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
						Object re = m1.invoke(dhq);
                        if(re.getClass().equals(Long.class)){
                            price =((Long) m1.invoke(dhq)).floatValue();
                        }else{
                            price =(Float) m1.invoke(dhq);
                        }                        
					} catch (Exception e) {
						logger.error("", e);
					}

				}
				return price;
			}
		}
	}

	public abstract Object getInstanceWithPropertiesList(ArrayList<Properties> list) ;

       // public abstract  Policy getInstance(String fullargs);
        public abstract String getFullargs();
       
}


