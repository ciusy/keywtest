/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.operate.policy;

import com.mrj.person.Person;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.policy.util.StoConfidenceValuePair;
import java.util.*;

/**
 *
 * @author ruojun
 */
public abstract class OperatePolicy {

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
