/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.keyplot.gp.user.dao;

import com.keyplot.gp.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author Administrator
 */
public class UserDao {
    private HibernateTemplate hibernateTemplate;

	private JdbcTemplate jdbcTemplate;

     public boolean addUser(User new_user){
        return false;
    }
}
