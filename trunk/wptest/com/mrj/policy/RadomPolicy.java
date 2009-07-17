package com.mrj.policy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mrj.person.CapitalSituation;
import com.mrj.sto.OriginalDataUtil;

public class RadomPolicy extends Policy {


	@Override
	public List<ChargeDescription> getChargePlan(Calendar begin) {
		int stoSizeInAll=OriginalDataUtil.getAllStoMap().size();
		
		return null;
	}

}
