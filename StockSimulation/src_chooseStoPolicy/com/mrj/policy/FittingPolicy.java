/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.policy;

import com.mrj.person.ShareHolding;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.DHQ;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *拟合预测分析
 * @author Administrator
 */
public class FittingPolicy extends Policy {

    private float sH_Composite_Index_FittingResult_rate = 0.05f;//上证综合指数拟合结果所占比例
    private float sS_Composition_Index_FittingResult_rate = 0.05f;//深圳成分指数拟合结果所占比例
    private float finalPrice_FittingResult_rate = 0.3f;//收盘价拟合结果所占比例
    private float average5Price_FittingResult_rate = 0.3f;//5日均线拟合结果所占比例
    private float average10Price_FittingResult_rate = 0.2f;//10日均线拟合结果所占比例
    private float average20Price_FittingResult_rate = 0.1f;//20日均线拟合结果所占比例
    private float average30Price_FittingResult_rate = 0.05f;//30日均线拟合结果所占比例
    private float average60Price_FittingResult_rate = 0.025f;//60日均线拟合结果所占比例
    private float average180Price_FittingResult_rate = 0.025f;//180日均线拟合结果所占比例
    private float buyifConfidenceGreaterThanThisValue=0.6f;

    @Override
    public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
        List<Sto> allStoList = OriginalDataUtil.getAllStoList();
        List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();
        for (Sto sto : allStoList) {
        	try{
            	float a=getCompositeConfidenceValue(nextChargeDay, sto);
            	if(a>=buyifConfidenceGreaterThanThisValue){//仅推荐信息值大于0.8f的股票
            		re.add(new StoConfidenceValuePair(sto, a));
            	}   
        	}catch(Exception e){
        		logger.error("",e);
        	}   
        }
        StoConfidenceValuePair[] reArray = new StoConfidenceValuePair[re.size()];
        re.toArray(reArray);
        Arrays.sort(reArray);
        List<StoConfidenceValuePair> re1 = new ArrayList<StoConfidenceValuePair>();
        re1 = Arrays.asList(reArray);
        return re;
    }

    @Override
    public List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay) {
        List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();
        for (ShareHolding sh : getOwnerPerson().getCs().getHoldingList()) {
            re.add(new StoConfidenceValuePair(sh.getSto(), getCompositeConfidenceValue(nextChargeDay, sh.getSto())));
        }
        return re;
    }

    private float getCompositeConfidenceValue(Calendar nextChargeDay, Sto sto) {//key confidence; value rate
        Map<Float, Float> confidenceVlaueRateMap = new HashMap<Float, Float>();
       
        //lastDayPrice-confidence
        confidenceVlaueRateMap.put(getlastDayPriceConfidenceValue(nextChargeDay, sto), this.finalPrice_FittingResult_rate);
        // confidenceVlaueRateMap.put(getSH_Composite_Index_FittingResult(nextChargeDay), this.sH_Composite_Index_FittingResult_rate);
        // confidenceVlaueRateMap.put(getSS_Composition_Index_FittingResult(nextChargeDay), this.sS_Composition_Index_FittingResult_rate);
        confidenceVlaueRateMap.put(getAverage5PriceConfidenceValue(nextChargeDay, sto), this.average5Price_FittingResult_rate);
        confidenceVlaueRateMap.put(getAverage10PriceConfidenceValue(nextChargeDay, sto), this.average10Price_FittingResult_rate);
        confidenceVlaueRateMap.put(getAverage20PriceConfidenceValue(nextChargeDay, sto), this.average20Price_FittingResult_rate);
        confidenceVlaueRateMap.put(getAverage30PriceConfidenceValue(nextChargeDay, sto), this.average30Price_FittingResult_rate);
        confidenceVlaueRateMap.put(getAverage60PriceConfidenceValue(nextChargeDay, sto), this.average60Price_FittingResult_rate);
        confidenceVlaueRateMap.put(getAverage180PriceConfidenceValue(nextChargeDay, sto), this.average180Price_FittingResult_rate);

        //大盘指数-confidence

        float allConfidence = 0;
        float allRate = 0;
        for (float f : confidenceVlaueRateMap.keySet()) {
            allRate += confidenceVlaueRateMap.get(f);
        }
        for (float f : confidenceVlaueRateMap.keySet()) {
            allConfidence += f * (confidenceVlaueRateMap.get(f) / allRate);
        }
        return allConfidence;
    }

   
    private float formatConfidenceValue(float re){
    	float r=(float)(re/2.0+0.5);
    	 if (r > 1) {
             r = 1;
         }
         if (r < 0) {
             r = 0;
         }
         return r;
    }
    
    /**
     * 获取单一收盘价预测的信心值
     * @param nextChargeDay
     * @param sto
     * @return
     */
    
    
    private float getlastDayPriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
    	float fittingResult = getFinalPriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);        
        return formatConfidenceValue(re) ;
    }

    private float getAverage5PriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
        float fittingResult = getAverage5PriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayPrice(sto, nextChargeDay, "getPrice_5day");
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);
        return formatConfidenceValue(re) ;
    }

    private float getAverage10PriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
    	float fittingResult = getAverage10PriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayPrice(sto, nextChargeDay, "getPrice_10day");
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);
        return formatConfidenceValue(re) ;
    }

    private float getAverage20PriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
    	float fittingResult = getAverage20PriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayPrice(sto, nextChargeDay, "getPrice_20day");
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);
        return formatConfidenceValue(re) ;
    }

    private float getAverage30PriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
    	float fittingResult = getAverage30PriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayPrice(sto, nextChargeDay, "getPrice_30day");
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);
        return formatConfidenceValue(re) ;
    }

    private float getAverage60PriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
    	float fittingResult = getAverage60PriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayPrice(sto, nextChargeDay, "getPrice_60day");
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);
        return formatConfidenceValue(re) ;
    }

    private float getAverage180PriceConfidenceValue(Calendar nextChargeDay, Sto sto) {
    	float fittingResult = getAverage180PriceFittingResult(nextChargeDay, sto);
        float lastDayPrice = getLastDayPrice(sto, nextChargeDay, "getPrice_180day");
        float re = (float) ((fittingResult - lastDayPrice) / lastDayPrice);
        return formatConfidenceValue(re) ;
    }

    private float getSH_Composite_Index_FittingResult(Calendar nextChargeDay) {
        return getFinalPriceFittingResult(nextChargeDay, OriginalDataUtil.getAllStoMap().get("999999"));//上证指数的代码
    }

    private float getSS_Composition_Index_FittingResult(Calendar nextChargeDay) {
        return getFinalPriceFittingResult(nextChargeDay, OriginalDataUtil.getAllStoMap().get("399001"));//上证指数的代码
    }

   
}
