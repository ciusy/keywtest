package com.mrj.policy;

import java.util.*;

import com.mrj.person.CapitalSituation;
import com.mrj.sto.Sto;

public abstract class Policy { 
	public int chargeDescription_size;// getChargePlan�ķ��ؽ��������chargeDescription_sizeֻ
	
	/**
	 * ����begin֮ǰ���������ݷ�����begin��һ��ƻ��Ĳ���
	 * @param begin
	 * @return
	 */
	public abstract List<ChargeDescription> getChargePlan(Calendar begin);
	
	
}
