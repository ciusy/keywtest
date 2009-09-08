package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;
import com.mrj.person.ShareHolding;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.mrj.sto.OriginalDataUtil;
/**
 * 随机的给出一些股票的信心值和已有股票的信心值
 * @author ruojun
 */
public class RandomPolicy extends Policy {

    static Logger logger = Logger.getLogger(RandomPolicy.class);
    private int chargeDescription_size = 3;//默认为3个
    private float isUseRandomConfidenceValue=1;//如果isUseRandomConfidenceValue>0.5,则使用随机值，否则使用固定值constantConfidenceValue
    private float constantConfidenceValue=1;

    public float getConstantConfidenceValue() {
        return constantConfidenceValue;
    }

    public void setConstantConfidenceValue(float constantConfidenceValue) {
        this.constantConfidenceValue = constantConfidenceValue;
    }

    public float getIsUseRandomConfidenceValue() {
        return isUseRandomConfidenceValue;
    }

    public void setIsUseRandomConfidenceValue(float isUseRandomConfidenceValue) {
        this.isUseRandomConfidenceValue = isUseRandomConfidenceValue;
    }

    public int getChargeDescription_size() {
        return chargeDescription_size;
    }

    public void setChargeDescription_size(int chargeDescription_size) {
        this.chargeDescription_size = chargeDescription_size;
    }

    public static void main(String[] args) {
        int stoSizeInAll = 300;
        double ram = java.lang.Math.random();
        int i = new BigDecimal(ram * stoSizeInAll).intValue();
        logger.info(i);
    }

    @Override
    public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
        int stoSizeInAll = OriginalDataUtil.getAllStoList().size();
        List<StoConfidenceValuePair> buyStoList = new ArrayList<StoConfidenceValuePair>();
        for (int i = 0; i < chargeDescription_size; i++) {
            StoConfidenceValuePair temp = new StoConfidenceValuePair(OriginalDataUtil.getAllStoList().get(new BigDecimal(Math.random() * stoSizeInAll).intValue()), getConfidenceValue());
            buyStoList.add(temp);
        }
        return buyStoList;
    }

    @Override
    public List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay) {
        List<StoConfidenceValuePair> OwnStoList = new ArrayList<StoConfidenceValuePair>();
        List<ShareHolding> ownStoList=this.getOwnerPerson().getCs().getHoldingList();
        for(ShareHolding sh:ownStoList){
            OwnStoList.add(new StoConfidenceValuePair(sh.getSto(),getConfidenceValue()));
        }
        return OwnStoList;
    }

    private float getConfidenceValue() {
        if (isUseRandomConfidenceValue > 0.5) {
            return (float) Math.random();
        } else {
            return getConstantConfidenceValue();
        }
    }
}
