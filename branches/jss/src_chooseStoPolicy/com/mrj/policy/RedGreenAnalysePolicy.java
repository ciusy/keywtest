/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class RedGreenAnalysePolicy extends ChoosePolicy{

    @Override
    public List<StoConfidenceValuePair> getBuyList(Calendar nextChargeDay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<StoConfidenceValuePair> getOwnStoList(Calendar nextChargeDay) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public Object getInstanceWithPropertiesList(ArrayList<Properties> list) {
		// TODO Auto-generated method stub
		return null;
	}

    
    public static Policy getInstance(String fullargs) {
        return new RedGreenAnalysePolicy();
    }

    @Override
    public String toString() {
        return RedGreenAnalysePolicy.class.getName();
    }

    @Override
    public String getFullargs() {
        return "";
    }
}
