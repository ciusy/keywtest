package com.mrj.person;

import java.util.*;

import com.mrj.sto.Sto;

public abstract class Policy { 
	//Hashtable<Sto,Float> choose;
	public abstract void charge(Date date,CapitalSituation cs);
	
	
}
