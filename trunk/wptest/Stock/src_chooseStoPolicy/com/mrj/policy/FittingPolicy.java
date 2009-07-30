/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.sto.DHQ;
import com.mrj.sto.OriginalDataUtil;
import com.mrj.sto.Sto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *拟合预测分析
 * @author Administrator
 */
public class FittingPolicy extends Policy {

    private float sH_Composite_Index_FittingResult;//上证综合指数拟合结果
    private float sS_Composition_Index_FittingResult;//深圳成分指数拟合结果

    @Override
    public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private float getSH_Composite_Index_FittingResult(Calendar nextChargeDay) {
        return getFittingResult(nextChargeDay, OriginalDataUtil.getAllStoMap().get("999999"));//上证指数的代码
    }

     private float getSS_Composition_Index_FittingResult(Calendar nextChargeDay) {
        return getFittingResult(nextChargeDay, OriginalDataUtil.getAllStoMap().get("399001"));//上证指数的代码
    }


    private float getFittingResult(Calendar nextChargeDay, Sto sto) {
        List beforeList = new ArrayList();
        Calendar begin = Calendar.getInstance();
        begin.setTime(sto.getHq().getEarliestDate());
        HashMap<Date, DHQ> map = sto.getHq().getDhqMap();
        while (begin.compareTo(nextChargeDay) <= 0) {
            beforeList.add(map.get(begin.getTime()).getFinalPrice());
            begin.add(Calendar.DAY_OF_YEAR, 1);
        }
        return getFittingResult(beforeList);
    }

    private float getFittingResult(List beforeArray) {
        float re = 0;
        throw new UnsupportedOperationException("Not supported yet.");
        //return re;
    }
}
