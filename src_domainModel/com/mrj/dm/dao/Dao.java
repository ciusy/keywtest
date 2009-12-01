/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrj.dm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public class Dao {

    public void add(Object c) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(c);
        session.getTransaction().commit();
    }

    public void clearData() throws SQLException {
        String[] sqlarray = {"delete from asset_day_data",
            "delete from capital_flow",
            "delete from person",
            "delete from invest_result"};


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Connection con = session.connection();
        for (String sql : sqlarray) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
        }
        session.getTransaction().commit();

    }
}
