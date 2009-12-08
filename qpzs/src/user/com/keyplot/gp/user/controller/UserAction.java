/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.keyplot.gp.user.controller;

import com.keyplot.gp.common.service.SpringBeanFinder;
import com.keyplot.gp.user.domain.User;
import com.keyplot.gp.user.service.UserService;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.DispatchActionSupport;

/**
 *
 * @author Administrator
 */
public class UserAction extends DispatchActionSupport {
    //private  ApplicationContext context=getWebApplicationContext();
    // public UserService userService=(UserService) context.getBean("userService");

    public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("register");
    }

    public ActionForward reg_sub(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        WebApplicationContext ctx = getWebApplicationContext();
        UserService userService = (UserService) ctx.getBean("userService");
        String userEmail = request.getParameter("email");
        String password = request.getParameter("passwd");
        String password2 = request.getParameter("passwd2");
        String nickName = request.getParameter("nickname");

        if (!checkNewUser(userEmail, password, password2, nickName)) {
            return mapping.findForward("reg_failure");
        }
        User user = new User();
        user.setUserEmail(userEmail);
        user.setName("");
        user.setPassword(password);
        user.setNickname(nickName);
        try {
            userService.add(user);
        } catch (Exception e) {
            return mapping.findForward("reg_failure");
        }
        return mapping.findForward("reg_success");
    }

    public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        WebApplicationContext ctx = getWebApplicationContext();
        UserService userService = (UserService) ctx.getBean("userService");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = null;
        if ((user = userService.checkUser(email, password)) != null) {
            request.getSession().setAttribute("user", user);
            return mapping.findForward("login_success");
        }
        return mapping.findForward("login_failure");
    }

    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.invalidate();
        return mapping.findForward("logout");
    }

    private boolean checkNewUser(String userEmail, String password, String password2, String nickName) {
        return true;
    }
}
