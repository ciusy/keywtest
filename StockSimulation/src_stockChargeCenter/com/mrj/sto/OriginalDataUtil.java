package com.mrj.sto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mrj.util.GlobalConstant;

public class OriginalDataUtil {
	static Logger logger = Logger.getLogger(OriginalDataUtil.class);
	static Map<String, Sto> stoMap = null;
	static List<Sto> stoList = null;

	public static Map<String, Sto> getAllStoMap() {
		if (stoMap != null)
			return stoMap;
		else {
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
			stoMap = new HashMap<String, Sto>();
			String filePath = GlobalConstant.ExportFilePath;
			File[] files = new File(filePath).listFiles();
			InputStreamReader read;
			for (int i = 0; i < files.length; i++) {
				try {
					logger.info("读取第"+(i+1)+"个文件");
					read = new InputStreamReader(new FileInputStream(files[i]),"GBK");
					BufferedReader reader = new BufferedReader(read);
					String result = "";
					int j = 0;
					Sto sto = new Sto();
					HQ hq = new HQ();
					boolean flag_isSetEarliestDate = false;
					while (true) {
						DHQ dhq = new DHQ();
						result = reader.readLine();
						
						if (result == null)
							break;
						String[] results = result.split("\\s");
						if (j == 0) {
							sto.setName(results[1]);
							sto.setCode(results[0]);
						} else if (j > 1) {
							Date temp=sdf.parse(results[0]);
							if (!flag_isSetEarliestDate) {
								hq.setEarliestDate(temp);
								flag_isSetEarliestDate=true;
							}
							dhq.setDate(temp);
							dhq.setBeginPrice(Float.parseFloat(results[1]));
							dhq.setHighPrice(Float.parseFloat(results[2]));
							dhq.setLowestPrice(Float.parseFloat(results[3]));
							dhq.setFinalPrice(Float.parseFloat(results[4]));
							dhq.setChargeVolume(Long.parseLong(results[5]));
							dhq.setChargeMoney(Float.parseFloat(results[6]));
							hq.putDHQ(dhq.getDate(), dhq);
							hq.setLatestDate(temp);
						}
						j++;
					}
					hq.calculateAllIndicator();
					sto.setHq(hq);
					if(hq.getEarliestDate()!=null){
						stoMap.put(sto.getCode(), sto);
					}
					read.close();
					reader.close();
				} catch (Exception ex) {
					logger.error("", ex);
				}
			}
			return stoMap;
		}

	}

	@SuppressWarnings("unchecked")
	public static List<Sto> getAllStoList() {
		if (stoList == null) {
			stoList = new ArrayList<Sto>();
			Collection c = getAllStoMap().values();
			for (Iterator i = c.iterator(); i.hasNext();) {
				stoList.add((Sto) i.next());
			}
		}

		return stoList;
	}
	
	public static boolean isChargeDate(Sto sto,Date date){
		if(sto.getHq().getDailyHQ(date)!=null){
			return true;
		}
		return false;
	}
	
	public static boolean isChargeDate(Date date){
		if(getAllStoMap().get("999999").getHq().getDailyHQ(date)!=null){
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (Sto sto : getAllStoList()) {
			logger.info(sto.getCode());
		}

	}

}
