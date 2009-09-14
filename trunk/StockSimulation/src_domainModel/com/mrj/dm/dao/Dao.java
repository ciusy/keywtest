/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mrj.dm.dao;

import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public  class Dao {
     public void add(Object c) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(c);
        session.getTransaction().commit();
    }
}
