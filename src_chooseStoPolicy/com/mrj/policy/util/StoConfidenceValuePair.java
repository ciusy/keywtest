/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mrj.policy.util;

import com.mrj.sto.Sto;

/**
 *由chooseStoPolicy返回的股票与信心值组成的一个对象
 * @author ruojun
 */
public class StoConfidenceValuePair implements Comparable<StoConfidenceValuePair>{

    public StoConfidenceValuePair(Sto sto,float confidenceValue){
        this.sto=sto;
        this.confidenceValue=confidenceValue;
    }

    private Sto sto;
    /**
     * 信心值在[0,1]之间
     */
    private float confidenceValue;

    public float getConfidenceValue() {
        return confidenceValue;
    }

    public void setConfidenceValue(float confidenceValue) {
        this.confidenceValue = confidenceValue;
    }

    public Sto getSto() {
        return sto;
    }

    public void setSto(Sto sto) {
        this.sto = sto;
    }


    public int compareTo(StoConfidenceValuePair o) {
       float thisVal = this.getConfidenceValue();
	float anotherVal = o.getConfidenceValue();
	return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
    }

}
