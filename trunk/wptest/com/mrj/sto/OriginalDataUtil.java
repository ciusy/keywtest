package com.mrj.sto;

import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.*;

import org.apache.log4j.Logger;

import com.mrj.person.*;
import com.mrj.util.GlobalConstant;

public class OriginalDataUtil {
	static Logger logger = Logger.getLogger(OriginalDataUtil.class);
	static Map<String, Sto> stoMap = null;
	static List<Sto> stoList = null;

	public static Map<String, Sto> getAllStoMap() {
		if (stoMap != null)
			return stoMap;
		else {
			stoMap = new HashMap<String, Sto>();
			String filePath = GlobalConstant.ExportFilePath;
			File[] files = new File(filePath).listFiles();
			InputStreamReader read;
			for (int i = 0; i < files.length; i++) {
				try {
					read = new InputStreamReader(new FileInputStream(files[i]));
					BufferedReader reader = new BufferedReader(read);
					String result = "";
					int j = 0;
					Sto sto = new Sto();
					HQ hq = new HQ();
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
							dhq.setDate(new SimpleDateFormat("MM/dd/yy")
									.parse(results[0]));
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
					logger.error("", ex);
				}
			}
			return stoMap;
		}

	}

	public static List<Sto> getAllStoList() {
		if (stoList == null) {
			stoList= new ArrayList<Sto>();
			Collection c=getAllStoMap().values();
			for(Iterator i=c.iterator() ; i.hasNext() ; ){
				stoList.add((Sto)i.next());
			}
		}

		return stoList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(Sto sto:getAllStoList()){
			logger.info(sto.code);
		}

	}

}
