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

/**
 *
 * @author ruojun
 */
public class AssetDayDataDao extends Dao{
    public AssetDayDataDao(){
        
    }
     public void add(AssetDayData c) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(c);
        session.getTransaction().commit();
    }
     
     public List getAssetListByUserUuid(String uuid){
    	 DetachedCriteria query = DetachedCriteria.forClass(AssetDayData.class).add(Restrictions.eq("userUuid", uuid));    	 
    	 Session session = HibernateUtil.getSessionFactory().getCurrentSession();    	 
    	 Transaction txn = session.beginTransaction();  	 
    	 List re = query.getExecutableCriteria(session).list();   
    	 txn.commit();
    	 return re;
     }

    public List<AssetDayData> getAssetListByUserUuid(String person_uuid, String investResultUuid) {
        DetachedCriteria query = DetachedCriteria.forClass(AssetDayData.class).
                add(Restrictions.eq("userUuid", person_uuid)).
                add(Restrictions.eq("investResultUuid", investResultUuid));
    	 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    	 Transaction txn = session.beginTransaction();
    	 List re = query.getExecutableCriteria(session).list();
    	 txn.commit();
    	 return re;
    }
}
