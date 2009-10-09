package com.mrj.sto;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;

import com.mrj.dm.dao.HibernateUtil;
import com.mrj.dm.dao.PersonDao;
import com.mrj.dm.domain.CapitalFlow;
import com.mrj.operate.policy.DayAvgOperatePolicy;
import com.mrj.operate.policy.LastDayFinalPricePolicy;
import com.mrj.operate.policy.OperatePolicy;
import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.policy.FittingPolicy;
import com.mrj.policy.DayAavAnalysePolicy;
import com.mrj.policy.ChoosePolicy;
import com.mrj.policy.RandomPolicy;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.util.GlobalConstant;
import com.mrj.util.chart.ChartUtil;

import java.math.BigInteger;
import java.util.ArrayList;

public class Main {

	static Logger logger = Logger.getLogger(Main.class);

	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

	static {

		String installPath = GlobalConstant.ProjectPath;
		installPath += "src_stockChargeCenter\\";
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
			OriginalDataUtil.getAllStoMap();
		} catch (Exception e) {
			logger.error("", e);
		}

	}

	public static void testSto() {
		logger.info("Entering application.");
		Person person1 = new Person(new RandomPolicy(), new LastDayFinalPricePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(20000f)));
		new PersonDao().add(person1);
		try {
			Date today = sdf.parse(sdf.format(new Date()));
			person1.beginInvest(sdf.parse("03/01/2008"), today);
			logger.info(person1.getCs().getTotalAssets(today).floatValue());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static String testPersonInvest(Person p, String fromMMDDYYYY, String toMMDDYYYY) {
		new PersonDao().add(p);
		BigDecimal atbeginning = p.getCs().getLeftMoney();
		try {
			p.beginInvest(sdf.parse(fromMMDDYYYY), sdf.parse(toMMDDYYYY));
			String re = "赢利结果：赢利" + (p.getCs().getTotalAssets(sdf.parse(toMMDDYYYY)).floatValue() - atbeginning.floatValue()) / atbeginning.floatValue() + "倍";
			logger.info(re);
			return re;

		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

	public static float letPersonInvest(Person p, String fromMMDDYYYY, String toMMDDYYYY) {
		new PersonDao().add(p);
		BigDecimal atbeginning = p.getCs().getLeftMoney();
		try {
			p.beginInvest(sdf.parse(fromMMDDYYYY), sdf.parse(toMMDDYYYY));
			float re = (p.getCs().getTotalAssets(sdf.parse(toMMDDYYYY)).floatValue() - atbeginning.floatValue()) / atbeginning.floatValue();
			String reinfo = "赢利结果：赢利" + re + "倍";
			logger.info(reinfo);
			return re;

		} catch (Exception e) {
			logger.error("", e);
		}
		return 1;
	}

	public static void testHql() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		CapitalFlow capitalFlow = new CapitalFlow("1234", "中国铝业", CapitalFlow.sto_buy, new Date(), 16.83f, 300, new BigDecimal(5030.33f), new BigDecimal(5300.90f), new BigDecimal(18.67f), "601600");
		session.save(capitalFlow);
		capitalFlow.setUserUuid("234");
		session.save(capitalFlow);
		session.getTransaction().commit();
	}

	public static void tellmeHowtoInvestOnSomeDay(String dateMMDDYYYY, ChoosePolicy policy, OperatePolicy operatePolicy, CapitalSituation cs) {
		Person p = new Person(policy, operatePolicy, cs);
		BigDecimal atbeginning = p.getCs().getLeftMoney();
		try {
			Date date = sdf.parse(dateMMDDYYYY);
			Calendar begin = Calendar.getInstance();
			begin.setTime(date);
			List<ChargeDescription> list = p.getOperatePolicy().getChargePlan(begin);
			System.out.println(dateMMDDYYYY + "的操作策略：");
			for (ChargeDescription cd : list) {
				System.out.print(cd.operationType == ChargeDescription.operationType_buy ? "买入【" : "卖出" + "【");
				System.out.print(cd.sto.getName() + cd.sto.getCode() + "】 ");
				System.out.print(cd.operateAmount + "股，操作价格【");
				System.out.println(cd.plan_price + "】元");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static void testLastDayFinalPricePolicyWithRandomPolicy() {
		String beginTime = "06/12/2007";
		String endTime = "09/15/2009";
		int n = 500;

		// List<Person> plist=new ArrayList<Person>();
		String[] personUuidArray = new String[n];

		float maxWinRate = -10;
		float goodintrestRate = -2;
		float goodlostRate = -2;
		for (int i = 0; i < n; i++) {
			float intrestRate = (float) Math.random();
			float lostRate = (float) Math.random();
			Person person = new Person(new RandomPolicy(), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
			// plist.add(person);
			float rate = Main.letPersonInvest(person, beginTime, endTime);
			if (rate > maxWinRate) {
				maxWinRate = rate;
				goodintrestRate = intrestRate;
				goodlostRate = lostRate;
			}
			personUuidArray[i] = person.getUserUuid();
		}
		logger.error(maxWinRate);
		logger.error(goodintrestRate);
		logger.error(goodlostRate);

		ChartUtil.showAssetChart(personUuidArray);

	}

	public static void testFittingePolicy() {
		String beginTime = "06/12/2007";
		String endTime = "09/15/2009";

		String[] personUuidArray = new String[1];

		float maxWinRate = -10;
		float goodintrestRate = -2;
		float goodlostRate = -2;

		float intrestRate = (float) 0.15;
		float lostRate = (float) 0.1;
		Person person = new Person(new FittingPolicy(), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));

		float rate = Main.letPersonInvest(person, beginTime, endTime);
		maxWinRate = rate;
		goodintrestRate = intrestRate;
		goodlostRate = lostRate;
		personUuidArray[0] = person.getUserUuid();

		logger.info(maxWinRate);
		logger.info(goodintrestRate);
		logger.info(goodlostRate);

		ChartUtil.showAssetChart(personUuidArray);

	}
	@SuppressWarnings("unused")
	public static void testFiveAavAnalysePolicy() {
		String beginTime = "01/01/2007";
		String endTime = "09/30/2009";

		String[] personUuidArray = new String[8];


		float intrestRate = (float) 0.12;
		float lostRate = (float) 0.07;
		Person person = new Person(new DayAavAnalysePolicy(60,5), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		Person person1 = new Person(new DayAavAnalysePolicy(60,10), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		Person person2 = new Person(new DayAavAnalysePolicy(60,20), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		Person person3 = new Person(new DayAavAnalysePolicy(60,60), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		

		
		Person person4 = new Person(new DayAavAnalysePolicy(60,5), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		Person person5 = new Person(new DayAavAnalysePolicy(60,10), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		Person person6 = new Person(new DayAavAnalysePolicy(60,20), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		Person person7 = new Person(new DayAavAnalysePolicy(60,60), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		
		
		float rate = Main.letPersonInvest(person, beginTime, endTime);
		float rate1 = Main.letPersonInvest(person1, beginTime, endTime);
		float rate2 = Main.letPersonInvest(person2, beginTime, endTime);
		float rate3 = Main.letPersonInvest(person3, beginTime, endTime);
		
		float rate4 = Main.letPersonInvest(person4, beginTime, endTime);
		float rate5 = Main.letPersonInvest(person5, beginTime, endTime);
		float rate6 = Main.letPersonInvest(person6, beginTime, endTime);
		float rate7 = Main.letPersonInvest(person7, beginTime, endTime);
		
		
		personUuidArray[0] = person.getUserUuid();
		personUuidArray[1]=person1.getUserUuid();
		personUuidArray[2]=person2.getUserUuid();
		personUuidArray[3] = person3.getUserUuid();
		personUuidArray[4]=person4.getUserUuid();
		personUuidArray[5] = person5.getUserUuid();
		personUuidArray[6]=person6.getUserUuid();
		personUuidArray[7] = person7.getUserUuid();

		ChartUtil.showAssetChart(personUuidArray);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * String beginTime="06/12/2007"; String endTime="09/15/2009"; int
		 * trytimes=10; for(int i=0;i<trytimes;i++){ Person person1 = new
		 * Person(new RandomPolicy(), new LastDayFinalPricePolicy(), new
		 * CapitalSituation(new ArrayList<ShareHolding>(), new
		 * BigDecimal(30000f))); Main.testPersonInvest(person1, "09/15/1999",
		 * "09/15/2009"); }
		 * 
		 * 
		 * Main.tellmeHowtoInvestOnSomeDay("09/11/2009",new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * Main.tellmeHowtoInvestOnSomeDay("09/12/2009",new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * Main.tellmeHowtoInvestOnSomeDay("09/13/2009",new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * Main.tellmeHowtoInvestOnSomeDay("09/14/2009",new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * Main.tellmeHowtoInvestOnSomeDay("09/15/2009",new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * Main.tellmeHowtoInvestOnSomeDay("09/16/2009",new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * 
		 * //Person person1 = new Person(new FittingPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * //Main.testPersonInvest(person1, beginTime, endTime); Person person2
		 * = new Person(new RandomPolicy(), new LastDayFinalPricePolicy(), new
		 * CapitalSituation(new ArrayList<ShareHolding>(), new
		 * BigDecimal(30000f))); Main.testPersonInvest(person2, beginTime,
		 * endTime); Person person3 = new Person(new RandomPolicy(), new
		 * LastDayFinalPricePolicy(), new CapitalSituation(new
		 * ArrayList<ShareHolding>(), new BigDecimal(30000f)));
		 * Main.testPersonInvest(person3, beginTime, endTime); Person person4 =
		 * new Person(new RandomPolicy(), new LastDayFinalPricePolicy(), new
		 * CapitalSituation(new ArrayList<ShareHolding>(), new
		 * BigDecimal(30000f))); Main.testPersonInvest(person4, beginTime,
		 * endTime); ChartUtil.showAssetChart(new
		 * String[]{person2.getUserUuid(),
		 * person3.getUserUuid(),person4.getUserUuid()});
		 */

		//testLastDayFinalPricePolicyWithRandomPolicy();
		//testFittingePolicy();
		testFiveAavAnalysePolicy();

	}
}
