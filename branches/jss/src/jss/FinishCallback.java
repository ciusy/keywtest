/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jss;

import com.mrj.person.Person;
import com.mrj.server.CallBack;
import com.mrj.server.LetSinglePersonToInvestTask;
import com.mrj.util.chart.ChartUtil;
import java.util.*;
import javax.swing.JLabel;
import jss.simple.IsCanShowChart;
import org.apache.log4j.Logger;

/**
 *
 * @author ruojun
 */
class FinishCallback extends CallBack {

    static Logger logger = Logger.getLogger(FinishCallback.class);
    int size;
    List<Person> personList;
    IsCanShowChart isCanShowChart=null;
    private JLabel robotInvestTestJlabel;

    public Object callbackNotifyObject;

    public Object getCallbackNotifyObject() {
        return callbackNotifyObject;
    }

    public void setCallbackNotifyObject(Object callbackNotifyObject) {
        this.callbackNotifyObject = callbackNotifyObject;
    }

    public FinishCallback(int size,List<Person> personList,IsCanShowChart isCanShowChart) {
        this.size = size;
        this.personList = personList;
        this.isCanShowChart=isCanShowChart;
    }

    FinishCallback(int size, List<Person> personList, JLabel robotInvestTestJlabel,IsCanShowChart isCanShowChart) {
        this(size, personList, isCanShowChart);
        this.robotInvestTestJlabel=robotInvestTestJlabel;
    }

    private synchronized void push(Person person) {
        if (size > personList.size()) {
            personList.add(person);
        }
        robotInvestTestJlabel.setText("测试"+person.getUserId()+"完成");


        if (personList.size() == size) {
            isCanShowChart.setIsCanShowChart(1);
            isCanShowChart.setPersonList(personList);
            robotInvestTestJlabel.setText("本轮测试完成!");
            if(callbackNotifyObject!=null){
                synchronized(callbackNotifyObject){
                   callbackNotifyObject.notifyAll();
                }
                
            }
        }

    }
  
//    private void printlnPersonAverageRate() {
//        Map<String, Float> personAverageRate = LetSinglePersonToInvestTask.personAverageRate;
//        Set<String> setString = personAverageRate.keySet();
//        float maxRate = 0;
//        String maxRatePerson = "";
//        for (String a : setString) {
//            logger.info(a + ":" + personAverageRate.get(a));
//            if (personAverageRate.get(a) > maxRate) {
//                maxRate = personAverageRate.get(a);
//                maxRatePerson = a;
//            }
//        }
//
//        //Main.tellmeHowtoInvestOnSomeDay(today, policy, operatePolicy, cs)
//    }

    @Override
    public void call(Object o) {
        push((Person) o);
    }
}
