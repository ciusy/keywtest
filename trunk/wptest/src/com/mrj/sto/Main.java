package com.mrj.sto;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;

import com.mrj.dm.dao.HibernateUtil;
import com.mrj.dm.domain.CapitalFlow;
import com.mrj.person.Person;
import com.mrj.policy.RandomPolicy;
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
	
	public static void testSto(){
		logger.info("Entering application.");
		Map<String, Sto> stoMap = OriginalDataUtil.getAllStoMap();
		
		Person person1 = new Person(new RandomPolicy(),new BigDecimal(20000.00));
		
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
		
		try {
			Date today=sdf.parse(sdf.format(new Date()));
			person1.beginInvest(sdf.parse("01/01/2007"), today);
			logger.info(person1.getCs().getTotalAssets(today).floatValue());
		} catch (Exception e) {			
			logger.error("", e);
		}
		

	}
	
	public static void testHql(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CapitalFlow capitalFlow = new CapitalFlow();		
		capitalFlow.setUserUuid("123");
		session.save(capitalFlow);
		capitalFlow.setUserUuid("234");
		session.save(capitalFlow);
		session.getTransaction().commit();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testHql();
	}

}