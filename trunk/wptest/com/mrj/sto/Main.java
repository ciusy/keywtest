package com.mrj.sto;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mrj.person.Person;
import com.mrj.person.RandomPolicy;
import com.mrj.policy.Policy;
import com.mrj.util.GlobalConstant;

public class Main {
	static Logger logger = Logger.getLogger(Main.class);
	static {

		String installPath = GlobalConstant.ProjectPath;
		Properties props = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(installPath + "/log4j.properties");
			props.load(fis);
			fis.close();
			String s = props.getProperty("log4j.appender.MRJ_FILE.File");
			if (s != null) {
				s = s.replace("${log4j.root}/", installPath + "/log/");
				props.setProperty("log4j.appender.MRJ_FILE.File", s);
			}
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Entering application.");
		Map<String, Sto> stoMap = OriginalDataUtil.getAllStoMap();
		
		Person person1 = new Person();
		Policy policy1 = new RandomPolicy();
		person1.setPolicy(policy1);
		person1.setInitMoney(new BigDecimal(20000.00));
		try {
			person1.beginInvest(new SimpleDateFormat("MM/dd/yy")
					.parse("01/01/2000"), new Date());
		} catch (Exception e) {			
			logger.error("", e);
		}
		logger.info(person1.getCs().getTotalAssets(new Date()).floatValue());

	}

}
