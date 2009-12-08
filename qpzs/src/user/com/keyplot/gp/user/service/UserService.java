/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.keyplot.gp.user.service;

import com.keyplot.gp.common.service.AbstractService;
import com.keyplot.gp.user.dao.UserDao;
import com.keyplot.gp.user.domain.User;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UserService extends AbstractService{
    UserDao userDao=new UserDao();
    public boolean addUser(User new_user){
        add(new_user);
        return false;
    }

    public User checkUser(String email, String password) {
    User u=findByUserEmail(email);
    if(u!=null&&!password.equals(u.getPassword())){
        u=null;
    }
    return u;
    }

    @Override
    protected Class getEntity() {
        return User.class;
    }

    private User findByUserEmail(String email) {
        List l = this.hibernateTemplate.find("from User user where user.userEmail=?", email);
        if (l.size() > 0) {
            return (User) l.get(0);
        }
        return null;

    }
}
