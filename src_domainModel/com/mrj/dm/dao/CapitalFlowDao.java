/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.dm.dao;

import com.mrj.dm.domain.CapitalFlow;
import java.math.BigDecimal;
import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author ruojun
 */
public class CapitalFlowDao extends Dao{
    public CapitalFlowDao(){
        
    }
     public void add(CapitalFlow c) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(c);
        session.getTransaction().commit();
    }
}
