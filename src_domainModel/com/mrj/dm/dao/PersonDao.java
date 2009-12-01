/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.dm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mrj.dm.domain.AssetDayData;
import com.mrj.person.Person;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.hibernate.criterion.Order;
import org.hibernate.tool.stat.BeanTableModel;

/**
 *
 * @author Administrator
 */
public class PersonDao extends Dao {

    public Person getPersonByUserUuid(String uuid) {
        DetachedCriteria query = DetachedCriteria.forClass(Person.class).add(Restrictions.eq("userUuid", uuid));
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction txn = session.beginTransaction();
        List list = query.getExecutableCriteria(session).list();
        Person re = null;
        if (list != null && list.size() > 0) {
            re = (Person) list.get(0);
        }
        txn.commit();
        return re;
    }

    public Person getPersonByUserId(String userId) {
        DetachedCriteria query = DetachedCriteria.forClass(Person.class).add(Restrictions.eq("userId", userId));
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction txn = session.beginTransaction();
        List list = query.getExecutableCriteria(session).list();
        Person re = null;
        if (list != null && list.size() > 0) {
            re = (Person) list.get(0);
        }
        txn.commit();
        return re;
    }

    public ArrayList<Person> getPersonList() {
        DetachedCriteria query = DetachedCriteria.forClass(Person.class);

        ArrayList<Person> re = new ArrayList<Person>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction txn = session.beginTransaction();
        List list = query.getExecutableCriteria(session).addOrder(Order.desc("userId")).list();

        if (list != null && list.size() > 0) {
            for (Object o : list) {
                re.add((Person) o);
            }
        }
        txn.commit();
        return re;
    }

    public void addIfNotExist(Person p) {
       if(this.getPersonByUserId(p.getUserId())==null){
           add(p);
       }
    }
}
