package com.mrj.sto;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.jfree.chart.axis.PeriodAxis;

import com.mrj.dm.dao.HibernateUtil;
import com.mrj.dm.dao.PersonDao;
import com.mrj.dm.domain.CapitalFlow;
import com.mrj.operate.policy.DayAvgOperatePolicy;
import com.mrj.operate.policy.LastDayFinalPricePolicy;
import com.mrj.operate.policy.OperatePolicy;
import com.mrj.person.CapitalSituation;
import com.mrj.person.CapitalSituationFactory;
import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.person.SuperPerson;
import com.mrj.policy.FittingPolicy;
import com.mrj.policy.DayAavAnalysePolicy;
import com.mrj.policy.ChoosePolicy;
import com.mrj.policy.RandomPolicy;
import com.mrj.policy.util.ChargeDescription;
import com.mrj.server.CallBack;
import com.mrj.server.LetSinglePersonToInvestTask;
import com.mrj.server.TaskDispatcher;
import com.mrj.util.GlobalConstant;
import com.mrj.util.UUIDGenerator;
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

	public static ArrayList<Float> letPersonListInvest(List<Person> plist, String fromMMDDYYYY, String toMMDDYYYY) {
		ArrayList<Float> re = new ArrayList<Float>();
		for (Person p : plist) {
			float rate = letPersonInvest(p, fromMMDDYYYY, toMMDDYYYY);
			re.add(rate);
		}
		return re;
	}
	
	
	public static void letPersonListInvest_multyThread(List<Person> plist, String fromMMDDYYYY, String toMMDDYYYY) {
		TaskDispatcher tp=TaskDispatcher.getInstance();
		List<String> pUuidList=new ArrayList<String>();
		ShowChartCallback sccb=new ShowChartCallback(plist.size(),pUuidList);
		
		new Thread(TaskDispatcher.getInstance()).start();		
		for (Person p : plist) {
			tp.addTask(new LetSinglePersonToInvestTask(fromMMDDYYYY,toMMDDYYYY,p,sccb));
		}
	}
	
	

	public static float letPersonInvest(Person p, String fromMMDDYYYY, String toMMDDYYYY) {
		new PersonDao().add(p);
		BigDecimal atbeginning = p.getCs().getLeftMoney();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
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
		CapitalFlow capitalFlow = new CapitalFlow("1234", "中国铝业", CapitalFlow.sto_buy, new Date(), 16.83f, 300, new BigDecimal(5030.33f), new BigDecimal(5300.90f), new BigDecimal(
				18.67f), "601600",UUIDGenerator.getUUID());
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
			Person person = new Person(new RandomPolicy(), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(
					30000f)));
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
		Person person = new Person(new FittingPolicy(), new LastDayFinalPricePolicy(intrestRate, -lostRate), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(
				30000f)));

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
	
	
	static String beginTime = "10/12/2009";//1320	
	static String endTime="11/12/2009";//2894
	
	@SuppressWarnings("unused")
	public static void testAavAnalysePolicy1() {
		
		float intrestRate = (float) 0.12;
		float lostRate = (float) 0.07;
		float beginAsset=300000f;
		ArrayList<Person> plist = new ArrayList<Person>();
		for(int j=0;j<5;j++){
			
		
		/*	plist.add(new Person("m1",new DayAavAnalysePolicy(10, 180,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));//p12
			
			plist.add(new Person("m2",new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.6f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));//p28
			
			plist.add(new Person("m3",new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));//p28
			
			plist.add(new Person("m4",new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.1f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));//p28
		*/	
			plist.add(new Person("y5",new DayAavAnalysePolicy(5, 10,false,3,3), new LastDayFinalPricePolicy(0.1f,-0.60f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
			
		}
		
		letPersonListInvest_multyThread(plist, beginTime, endTime);

	}
	
	
	
	
	
	@SuppressWarnings("unused")
	public static void testAavAnalysePolicy() {
		
		float intrestRate = (float) 0.12;
		float lostRate = (float) 0.07;
		float beginAsset=300000f;
		ArrayList<Person> plist = new ArrayList<Person>();
		for(int j=0;j<3;j++){
			int i=1;
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(180, 180,true,2,1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(60, 60,true,2,1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(30, 30,true,2,1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(20, 20,true,2,1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 10,true,2,1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 5,true,2,1), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 180,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 60,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 30,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 20,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 10,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
			
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 180,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));//p12
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 60,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 30,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 20,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 5,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
			
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(180, 180,true,2,1), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(60, 60,true,2,1), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(30, 30,true,2,1), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(20, 20,true,2,1), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 10,true,2,1), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 5,true,2,1), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 180,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 60,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 30,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 20,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(5, 10,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
			
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));//p28
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 60,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 30,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 20,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			plist.add(new Person("p"+i++,new DayAavAnalysePolicy(10, 5,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset))));
			
		}
		
		letPersonListInvest_multyThread(plist, beginTime, endTime);

	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testAavAnalysePolicy();
		float beginAsset=300000f;
                String today="11/27/2009";
                String filePath = "C:\\Users\\ruojun\\Documents\\20091127 资金股份查询.txt";
                tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(5, 10,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset)));

		tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(10,20,false,3,3),new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset)));
                tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(10,20,false,3,3),new LastDayFinalPricePolicy(0.12f,-0.10f), CapitalSituationFactory.getInstanceFromRealWorld(filePath));
		
		
		tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(10, 180,false,3,3), new DayAvgOperatePolicy(), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset)));
                tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(10, 180,false,3,3), new DayAvgOperatePolicy(), CapitalSituationFactory.getInstanceFromRealWorld(filePath));
                tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), new CapitalSituation(new ArrayList<ShareHolding>(), new BigDecimal(beginAsset)));
                tellmeHowtoInvestOnSomeDay(today, new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f), CapitalSituationFactory.getInstanceFromRealWorld(filePath));
		
            /**
             * p28	com.mrj.policy.DayAavAnalysePolicy:10,180,false,3,3,com.mrj.operate.policy.LastDayFinalPricePolicy:0.12,-0.1		0.25269076
            
		String filePath = "C:\\Users\\ruojun\\Documents\\20091124 资金股份查询.txt";
		tellmeHowtoInvestOnSomeDay("11/23/2009",new DayAavAnalysePolicy(10, 180,false,3,3), new LastDayFinalPricePolicy(0.12f,-0.10f),CapitalSituationFactory.getInstanceFromRealWorld(filePath));
 */
              }
	
	 
	
	
}
class ShowChartCallback extends CallBack{
	static Logger logger = Logger.getLogger(ShowChartCallback.class);
	int size;List<String> pUuidList;
	public ShowChartCallback(int size,List<String> pUuidList){
		this.size=size;this.pUuidList=pUuidList;
	}
	private synchronized void push(String uuid){
		if(size>pUuidList.size()){
			pUuidList.add(uuid);
		}
		if(pUuidList.size()==size){
			String[] uuidArray=new String[size] ;
			ChartUtil.showAssetChart(pUuidList.toArray(uuidArray));
			
			printlnPersonAverageRate();
			
		/*	SuperPerson sp=new SuperPerson(10,pUuidList);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
			try {
				sp.beginInvest(sdf.parse(Main.beginTime), sdf.parse(Main.beginTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			*/
			//ChartUtil.showEarnAbilityInPeriodChart(pUuidList,7);
		}
			
	}

	String today="10/30/2009";
	private void printlnPersonAverageRate() {
		Map<String,Float> personAverageRate=LetSinglePersonToInvestTask.personAverageRate;
		Set<String> setString=personAverageRate.keySet();
		float maxRate=0;
		String maxRatePerson="";
		for(String a:setString){
			logger.info(a+":"+personAverageRate.get(a));
			if(personAverageRate.get(a)>maxRate){
				maxRate=personAverageRate.get(a);
				maxRatePerson=a;
			}
		}
		
		//Main.tellmeHowtoInvestOnSomeDay(today, policy, operatePolicy, cs)
	}
	@Override
	public void call(Object o) {
		push(((Person)o).getUserUuid());
	}
	
}