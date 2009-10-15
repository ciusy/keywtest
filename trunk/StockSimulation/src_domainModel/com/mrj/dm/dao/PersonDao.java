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

/**
 *
 * @author Administrator
 */
public class PersonDao extends Dao{
	
	 public Person getPersonByUserUuid(String uuid){
    	 DetachedCriteria query = DetachedCriteria.forClass(Person.class).add(Restrictions.eq("userUuid", uuid));    	 
    	 Session session = HibernateUtil.getSessionFactory().getCurrentSession();    	 
    	 Transaction txn = session.beginTransaction();  	 
    	 List list = query.getExecutableCriteria(session).list();
    	 Person re=null;
    	 if(list!=null&&list.size()>0){
    		 re=(Person)list.get(0);
    	 }    		 
    	 txn.commit();
    	 return re;
     }
	 
	 public Person getPersonByUserId(String userId){
    	 DetachedCriteria query = DetachedCriteria.forClass(Person.class).add(Restrictions.eq("userId", userId));    	 
    	 Session session = HibernateUtil.getSessionFactory().getCurrentSession();    	 
    	 Transaction txn = session.beginTransaction();  	 
    	 List list = query.getExecutableCriteria(session).list();
    	 Person re=null;
    	 if(list!=null&&list.size()>0){
    		 re=(Person)list.get(0);
    	 }    		 
    	 txn.commit();
    	 return re;
     }

}
