package com.mrj.sto;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;


public class Main {
	static Logger logger = Logger.getLogger(Main.class);
	static {
	
		String installPath = "E:\\DevelopeStudio\\workspace\\wptest";
		Properties props = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(installPath+"/log4j.properties");
			props.load(fis);
			fis.close();
			String s = props.getProperty("log4j.appender.MRJ_FILE.File");
			if(s != null) {
				s = s.replace("${log4j.root}/", installPath+"/log/");
				props.setProperty("log4j.appender.MRJ_FILE.File", s);
			}
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			logger.error("",e);
		}
		
	}	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Entering application.");
		logger.info("Entering application.");

	}

}
