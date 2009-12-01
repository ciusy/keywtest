package com.mrj.person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import org.apache.log4j.Logger;

import com.mrj.sto.OriginalDataUtil;

public class CapitalSituationFactory {
	static Logger logger = Logger.getLogger(CapitalSituationFactory.class);

	public static CapitalSituation getInstanceFromRealWorld(String fileFullName) {
		ArrayList<ShareHolding> shList = new ArrayList<ShareHolding>();
		CapitalSituation re = new CapitalSituation(shList, new BigDecimal(0f));

		/*
		 * 获取用户 目录的方法 FileSystemView fsv = FileSystemView.getFileSystemView();
		 * 
		 * fsv.getHomeDirectory();
		 */

		InputStreamReader read;
		try {
			int j = 0;
			read = new InputStreamReader(new FileInputStream(fileFullName), "GBK");
			BufferedReader reader = new BufferedReader(read);
			String result = "";
			while (true) {
				result = reader.readLine();
				j++;
				if (result == null)
					break;
				String[] results = result.split("\\s+");

				if (j == 1) {
					// 人民币: 余额:486.00 可用
					String leftMongey = results[1];
					leftMongey = leftMongey.substring(3);
					re.setLeftMoney(new BigDecimal(Float.parseFloat(leftMongey)));
				}
				if (j < 5)
					continue;

				ShareHolding sh = new ShareHolding();
				/*消除类似”张裕    A“等，股票名称中有空格的问题*/
				int i = 0;				
				while (true) {
					try {
						Integer.parseInt(results[1 + i]);
						break;
					} catch (NumberFormatException e) {
						i++;
					}
				}

				sh.setSto(OriginalDataUtil.getAllStoMap().get(results[10 + i]));
				sh.setCostPrice(Float.parseFloat(results[3 + i]));
				sh.setHodingAmount(Integer.parseInt(results[1 + i]));
				sh.setAvailableAmountForSell(Integer.parseInt(results[2 + i]));
				shList.add(sh);

			}

		} catch (Exception ex) {
			logger.error("", ex);
		}

		return re;
	}

	public static void main(String[] args) {
		String filePath = "C:\\Users\\ruojun\\Documents\\20091029 资金股份查询.txt";
		logger.info(getInstanceFromRealWorld(filePath));
	}

}
