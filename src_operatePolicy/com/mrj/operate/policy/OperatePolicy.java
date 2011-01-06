/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.operate.policy;

import com.mrj.person.Person;
import com.mrj.policy.Policy;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.policy.util.StoConfidenceValuePair;
import java.util.*;

/**
 *操作策略中解决两个问题：
 * 1、如何买
 * 2、如何卖
 *
 *  至于买什么和卖什么，由ChoosePolicy解决
 * @author ruojun
 */
public abstract class OperatePolicy  extends Policy{
    private float buyOperatePeriod;//买入操作周期:多少天操作一次
    private float sellOperatePeriod;//卖出操作周期:多少天操作一次

    //买入参数
    private float lowestBuyConfidenceValue;//最低买入信心值，如果信心值低于这个值，则不买入

    //卖出参数
    private float lowestSellConfidenceValue;//最低卖出信心值，如果信心值低于这个值，则全部卖出
    //如果信心值在两者之间，则根据其位置可以计算出卖出的数量
    private float highestSellConfidenceValue;//最高卖出信心值，如果信心值高于这个值，则不卖出

    public Person ownerPerson = null;

    public Person getOwnerPerson() {
        return ownerPerson;
    }

    public void setOwnerPerson(Person p) {
        this.ownerPerson = p;
    }

    public abstract List<ChargeDescription> getBuyChargeDescriptionList(Calendar nextChargeDay, List<StoConfidenceValuePair> buyList);

    public abstract List<ChargeDescription> getSellChargeDescriptionList(Calendar nextChargeDay, List<StoConfidenceValuePair> OwnStoList);

    /**
     * 根据begin之前的所有数据分析出begin这一天计划的操作
     *
     * @param begin
     * @return
     */
    public List<ChargeDescription> getChargePlan(Calendar nextChargeDay) {
        List<ChargeDescription> buyChargeDescriptionList = getBuyChargeDescriptionList(nextChargeDay,this.getOwnerPerson().getPolicy().getBuyList(nextChargeDay));
        List<ChargeDescription> sellChargeDescriptionList = getSellChargeDescriptionList(nextChargeDay,this.getOwnerPerson().getPolicy().getOwnStoList(nextChargeDay));
        List<ChargeDescription> chargePlanDescriptionList = buyChargeDescriptionList;
        for (ChargeDescription c : sellChargeDescriptionList) {
            chargePlanDescriptionList.add(c);
        }
        return chargePlanDescriptionList;
    }
}
