package com.mrj.sto;

import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.*;

import com.mrj.person.*;

public class OriginalDataUtil { 
	
	public static Map<String,Sto> getAllStoMap(){
		Map<String,Sto> stoMap=new HashMap<String, Sto>();
		String filePath="C:/export/";
		File[] files=new File(filePath).listFiles();
		InputStreamReader read;
		for(int i=0;i<files.length;i++){
			try {
				read = new InputStreamReader(new FileInputStream(files[i]));
				BufferedReader reader = new BufferedReader(read);
				String result="";
				int j=0;
				Sto sto=new Sto();
				HQ hq=new HQ();
				while(true){
					DHQ dhq=new DHQ();
					result=reader.readLine();
					if(result==null)break;
					System.out.println(result);
					String[] results=result.split("\\s");
					if(j==0){
						sto.setName(results[1]);
						sto.setCode(results[0]);
					}else if(j>1){
						dhq.setDate(new SimpleDateFormat("MM/dd/yy").parse(results[0]));
						dhq.setBeginPrice(Float.parseFloat(results[1]));
						dhq.setHighPrice(Float.parseFloat(results[2]));
						dhq.setLowestPrice(Float.parseFloat(results[3]));
						dhq.setFinalPrice(Float.parseFloat(results[4]));
						dhq.setChargeVolume(Long.parseLong(results[5]));
						dhq.setChargeMoney(Float.parseFloat(results[6]));
						hq.putDHQ(dhq.getDate(), dhq);
					}
					j++;
				}
				hq.calculateAllIndicator();
				sto.setHq(hq);
				stoMap.put(sto.getCode(), sto);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return stoMap;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String,Sto> stoMap=OriginalDataUtil.getAllStoMap();
		
		Person person1=new Person();
		Policy policy1=new RandomPolicy();
		person1.setPolicy(policy1);
		person1.setInitMoney(new BigDecimal(20000.00));
		try {
			person1.beginInvest(new SimpleDateFormat("MM/dd/yy").parse("01/01/2000"),new Date());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
