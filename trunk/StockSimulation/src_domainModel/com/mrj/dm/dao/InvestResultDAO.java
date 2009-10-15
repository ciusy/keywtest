package com.mrj.dm.dao;

import java.sql.Timestamp;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import com.mrj.dm.domain.InvestResult;

/**
 * A data access object (DAO) providing persistence and search support for
 * InvestResult entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.mrj.dm.domain.InvestResult
 * @author MyEclipse Persistence Tools
 */

public class InvestResultDAO extends BaseHibernateDAO {
	private static final Log log = LogFactory.getLog(InvestResultDAO.class);
	// property constants
	public static final String USER_UUID = "userUuid";
	public static final String USER_ID = "userId";
	public static final String BEGIN_ASSET = "beginAsset";
	public static final String END_ASSET = "endAsset";
	public static final String RATE = "rate";

	public void save(InvestResult transientInstance) {
		log.debug("saving InvestResult instance");
		try {
			getSession().beginTransaction();
			getSession().save(transientInstance);
			getSession().getTransaction().commit();
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(InvestResult persistentInstance) {
		log.debug("deleting InvestResult instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public InvestResult findById(java.lang.String id) {
		log.debug("getting InvestResult instance with id: " + id);
		try {
			InvestResult instance = (InvestResult) getSession().get("com.mrj.dm.domain.InvestResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(InvestResult instance) {
		log.debug("finding InvestResult instance by example");
		try {
			List results = getSession().createCriteria("com.mrj.dm.domain.InvestResult").add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding InvestResult instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from InvestResult as model where model." + propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserUuid(Object userUuid) {
		return findByProperty(USER_UUID, userUuid);
	}

	public List findByUserId(Object userId) {
		return findByProperty(USER_ID, userId);
	}

	public List findByBeginAsset(Object beginAsset) {
		return findByProperty(BEGIN_ASSET, beginAsset);
	}

	public List findByEndAsset(Object endAsset) {
		return findByProperty(END_ASSET, endAsset);
	}

	public List findByRate(Object rate) {
		return findByProperty(RATE, rate);
	}

	public List findAll() {
		log.debug("finding all InvestResult instances");
		try {
			String queryString = "from InvestResult";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public InvestResult merge(InvestResult detachedInstance) {
		log.debug("merging InvestResult instance");
		try {
			InvestResult result = (InvestResult) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(InvestResult instance) {
		log.debug("attaching dirty InvestResult instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(InvestResult instance) {
		log.debug("attaching clean InvestResult instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}