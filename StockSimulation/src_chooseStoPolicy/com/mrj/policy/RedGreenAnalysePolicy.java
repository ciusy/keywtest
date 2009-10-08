/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mrj.policy;

import com.mrj.policy.util.StoConfidenceValuePair;
import java.util.Calendar;
import java.util.List;

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

}
