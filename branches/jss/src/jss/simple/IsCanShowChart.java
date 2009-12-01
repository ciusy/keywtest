/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jss.simple;

import com.mrj.person.Person;
import java.util.List;

/**
 *
 * @author ruojun
 */
public class IsCanShowChart{
    int isCanShowChart;
    List<Person> personList;

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
    public IsCanShowChart(int isCanShowChart){
        this.isCanShowChart=isCanShowChart;
    }

    public int getIsCanShowChart() {
        return isCanShowChart;
    }

    public void setIsCanShowChart(int isCanShowChart) {
        this.isCanShowChart = isCanShowChart;
    }

}
