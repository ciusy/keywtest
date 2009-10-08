package com.mrj.policy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.mrj.person.ShareHolding;
import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;

public class DayAavAnalysePolicy extends ChoosePolicy {
	private int chargeDescription_size = 10;// 默认为3个

	
	private String periodStr="getPrice_"+5+"day";
	public DayAavAnalysePolicy(int period){
		if(period!=5&&period!=10&&period!=20&&period!=30&&period!=60&&period!=180)period=5;
		periodStr="getPrice_"+period+"day";
		
	}
	

	@Override
	public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
		List<StoConfidenceValuePair> re = new ArrayList<StoConfidenceValuePair>();
		
		float sz_lastDayPrice = getLastDayFinalPrice(OriginalDataUtil.getAllStoMap().get("999999"), nextChargeDay);		
		float sz_lastDayPrice_5avg = getLastDayPrice(OriginalDataUtil.getAllStoMap().get("999999"), nextChargeDay,periodStr);
		if(sz_lastDayPrice<sz_lastDayPrice_5avg)return re;//如果上证在5日线下方，则不买入
		
		List<Sto> allStoList = OriginalDataUtil.getAllStoList();
		
		for (Sto sto : allStoList) {
			try {
				float lastDayPrice = getLastDayFinalPrice(sto, nextChargeDay);
				float lastDayPrice_5day = getLastDayPrice(sto, nextChargeDay, periodStr);
				if(lastDayPrice>lastDayPrice_5day){
					if((lastDayPrice-lastDayPrice_5day)/lastDayPrice_5day>0.02&&(lastDayPrice-lastDayPrice_5day)/lastDayPrice_5day<0.07){
						re.add(new StoConfidenceValuePair(sto, (float) (0.8f+0.2*Math.random())));
					}
				}
				
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
        	float lastDayPrice = getLastDayFinalPrice(sh.getSto(), nextChargeDay);
			float lastDayPrice_5day = getLastDayPrice(sh.getSto(), nextChargeDay, periodStr);
			if(lastDayPrice<lastDayPrice_5day){
				re.add(new StoConfidenceValuePair(sh.getSto(), 0));
			}
            
        }
        return re;
	}

}
