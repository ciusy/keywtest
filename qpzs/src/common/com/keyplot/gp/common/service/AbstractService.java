/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.keyplot.gp.common.service;

import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author Administrator
 */
public abstract class AbstractService {

    protected HibernateTemplate hibernateTemplate;
    protected JdbcTemplate jdbcTemplate;
    protected int jdbcBatchSize;

    public void setJdbcBatchSize(int jdbcBatchSize) {
        this.jdbcBatchSize = jdbcBatchSize;
    }

    protected abstract Class getEntity();

    public void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public void setDataSource(DataSource source) {
        jdbcTemplate = new JdbcTemplate(source);
    }

    /**
	 * 添加对象
	 *
	 * @param entity
	 *            对象实体
	 */
	public void add(final Object entity) {
		hibernateTemplate.save(entity);
	}
/**
	 * 更新对象
	 *
	 * @param entity
	 *            对象实体
	 */
	public void update(final Object entity) {
		hibernateTemplate.update(entity);
	}   
}
