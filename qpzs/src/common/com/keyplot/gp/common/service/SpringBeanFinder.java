/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.keyplot.gp.common.service;
import javax.servlet.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

@SuppressWarnings("serial")
public class SpringBeanFinder {
  
	private static ApplicationContext appctx=new XmlWebApplicationContext() ;

	/**
	 * 通过bean名获取spring的bean对象
	 *
	 * @param beanName
	 *            bean名
	 * @return 装载的bean对象
	 */
	public static Object findBean(String beanName) {
		return appctx.getBean(beanName);
	}

	public static void setAppctx(ApplicationContext appctx1) {
		appctx = appctx1;
	}
}
