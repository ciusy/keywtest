/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.dm.dao;

import org.hibernate.Session;

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
}
