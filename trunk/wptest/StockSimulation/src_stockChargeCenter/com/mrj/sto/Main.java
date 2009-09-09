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
import com.mrj.dm.dao.PersonDao;
import com.mrj.dm.domain.CapitalFlow;
import com.mrj.operate.policy.LastDayFinalPricePolicy;
import com.mrj.person.CapitalSituation;
import com.mrj.person.Person;
import com.mrj.person.ShareHolding;
import com.mrj.policy.RandomPolicy;
import com.mrj.util.GlobalConstant;
import java.math.BigInteger;
import java.util.ArrayList;

public class Main {

    static Logger logger = Logger.getLogger(Main.class);
    static SimpleDateFormat  sdf = new SimpleDateFormat("MM/dd/yy");

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
        BigDecimal atbeginning= p.getCs().getLeftMoney();
        try {
            p.beginInvest(sdf.parse(fromMMDDYYYY), sdf.parse(toMMDDYYYY));
            String re="赢利结果：赢利"+(p.getCs().getTotalAssets(sdf.parse(toMMDDYYYY)).floatValue()-atbeginning.floatValue())/atbeginning.floatValue()+"倍";
            logger.info(re);
            return re;
            
        } catch (Exception e) {
            logger.error("", e);            
        }
        return null;
    }

    public static void testHql() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        CapitalFlow capitalFlow = new CapitalFlow("1234", "涓浗閾濅笟", CapitalFlow.sto_buy, new Date(), 16.83f, 300, new BigDecimal(5030.33f), new BigDecimal(5300.90f), new BigDecimal(18.67f), "601600");
        session.save(capitalFlow);
        capitalFlow.setUserUuid("234");
        session.save(capitalFlow);
        session.getTransaction().commit();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        testSto();

    }
}
