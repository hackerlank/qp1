package org.takeback.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.dao.exception.DaoException;

@org.springframework.stereotype.Repository("baseDao")
public class BaseDAO implements IBaseDAO
{
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(BaseDAO.class);
  

  private static final int DEFAULT_PAGE_SIZE = 10;
  
  private static final int DEFAULT_PAGE_NO = 1;
  
  @Autowired
  protected SessionFactory sessionFactory;
  

  public Session getSession()
  {
    return this.sessionFactory.getCurrentSession();
  }
  




  public Session getNewSession()
  {
    return this.sessionFactory.openSession();
  }
  






  public <T> void save(Class<T> cls, T t)
  {
    log.debug("saving {} instance", cls.getSimpleName());
    try {
      getSession().save(t);
    } catch (DaoException re) {
      log.error("saving {} instance failed", cls.getSimpleName(), re);
      throw re;
    }
  }
  






  public <T> void delete(Class<T> cls, T t)
  {
    log.debug("deleting {} instance", cls.getSimpleName());
    try {
      getSession().delete(t);
    } catch (DaoException re) {
      log.error("deleting {} instance failed", cls.getSimpleName(), re);
      throw re;
    }
  }
  
  public <T> void delete(String entityName, Serializable id) {
    log.debug("deleting {} instance", entityName);
    try {
      getSession().delete(entityName, get(entityName, id));
    } catch (DaoException re) {
      log.error("deleting {} instance failed", entityName, re);
      throw re;
    }
  }
  






  public <T> void update(Class<T> cls, T t)
  {
    log.debug("updating {} instance", cls.getSimpleName());
    try {
      getSession().update(t);
    } catch (DaoException re) {
      log.error("updating {} instance failed", cls.getSimpleName(), re);
      throw re;
    }
  }
  






  public <T> void merge(Class<T> cls, T t)
  {
    log.debug("merge {} instance", cls.getSimpleName());
    try {
      getSession().merge(t);
    } catch (DaoException re) {
      log.error("merge {} instance failed", cls.getSimpleName(), re);
      throw re;
    }
  }
  






  public <T> void saveOrUpdate(Class<T> cls, T t)
  {
    log.debug("saving or updating {} instance", cls.getSimpleName());
    try {
      getSession().saveOrUpdate(t);
    } catch (DaoException re) {
      log.error("saving or updating {} instance failed", cls.getSimpleName(), re);
      throw re;
    }
  }
  







  public <T> T get(Class<T> cls, Serializable id)
  {
    log.debug("getting {} instance with id {}", cls.getSimpleName(), id);
    try {
      return (T)getSession().get(cls, id);
    }
    catch (DaoException re) {
      log.error("getting {} instance with id {} failed", new Object[] { cls.getSimpleName(), id, re });
      throw re;
    }
  }
  
  public <T> T get(String entityName, Serializable id) {
    log.debug("getting {} instance with id {}", entityName, id);
    try {
      return (T)getSession().get(entityName, id);
    }
    catch (DaoException re) {
      log.error("getting {} instance with id {} failed", new Object[] { entityName, id, re });
      throw re;
    }
  }
  







  public <T> T getUnique(String hql, Object... pramas)
  {
    log.debug("get unique result with hql {}, with params {}", hql, pramas);
    try {
      Query q = getSession().createQuery(hql);
      for (int i = 0; i < pramas.length; i++) {
        q.setParameter(i, pramas[i]);
      }
      return (T)q.uniqueResult();
    }
    catch (DaoException re) {
      log.error("get unique result with hql {}, with params {}", new Object[] { hql, pramas, re });
      throw re;
    }
  }
  








  public <T> T getUnique(Class<T> cls, String propertyName, Object value)
  {
    log.debug("finding {} instance with property: {}, value: {}", new Object[] { cls.getSimpleName(), propertyName, value });
    try {
      Query queryObject = processHql(cls.getSimpleName(), propertyName, value, null);
      return (T)queryObject.uniqueResult();
    } catch (DaoException re) {
      log.error("finding {} instance with property: {}, value: {} falied", new Object[] { cls.getSimpleName(), propertyName, value, re });
      throw re;
    }
  }
  






  public <T> T getUniqueByProps(Class<T> cls, Map<String, Object> props)
  {
    log.debug("finding {} instance with properties: {}", cls.getSimpleName(), props);
    try {
      Query queryObject = processHql(cls.getSimpleName(), props, null);
      return (T)queryObject.uniqueResult();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} falied", new Object[] { cls.getSimpleName(), props, re });
      throw re;
    }
  }
  






  public int executeUpdate(String hql, Object[] pramas)
  {
    log.debug("executeUpdate with hql {}, with params {}", hql, pramas);
    try {
      Query q = getSession().createQuery(hql);
      for (int i = 0; i < pramas.length; i++) {
        q.setParameter(i, pramas[i]);
      }
      return q.executeUpdate();
    } catch (DaoException re) {
      log.error("executeUpdate with hql {}, with params {}", new Object[] { hql, pramas, re });
      throw re;
    }
  }
  
  public int executeUpdate(String hql, Map<String, Object> pramas) {
    log.debug("executeUpdate with hql {}, with params {}", hql, pramas);
    try {
      Query q = getSession().createQuery(hql);
      for (String k : pramas.keySet()) {
        q.setParameter(k, pramas.get(k));
      }
      return q.executeUpdate();
    } catch (DaoException re) {
      log.error("executeUpdate with hql {}, with params {}", new Object[] { hql, pramas, re });
      throw re;
    }
  }
  
  public <T> List<T> findByProperty(Class<T> cls, String propertyName, Object value) {
    log.debug("finding {} instance with property: {}, value: {}", new Object[] { cls.getSimpleName(), propertyName, value });
    try {
      Query queryObject = processHql(cls.getSimpleName(), propertyName, value, null);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with property: {}, value: {} falied", new Object[] { cls.getSimpleName(), propertyName, value, re });
      throw re;
    }
  }
  
  public <T> List<T> findByProperty(Class<T> cls, String propertyName, Object value, String orderInfo)
  {
    log.debug("finding {} instance with property: {}, value: {}", new Object[] { cls.getSimpleName(), propertyName, value });
    try {
      Query queryObject = processHql(cls.getSimpleName(), propertyName, value, orderInfo);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with property: {}, value: {} falied", new Object[] { cls.getSimpleName(), propertyName, value, re });
      throw re;
    }
  }
  
  public <T> List<T> findByExample(Class<T> cls, T instance)
  {
    log.debug("finding {} instance by example {}", cls.getSimpleName(), instance);
    try {
      List<T> results = getSession().createCriteria(cls).add(org.hibernate.criterion.Example.create(instance)).list();
      log.debug("find by example successful, result size: " + results.size());
      return results;
    } catch (DaoException re) {
      log.error("finding {} instance by example {} falied", new Object[] { cls.getSimpleName(), instance, re });
      throw re;
    }
  }
  
  public <T> List<T> findByProperties(Class<T> cls, Map<String, Object> properties)
  {
    log.debug("finding {} instance with properties: {}", cls.getSimpleName(), properties);
    try {
      Query queryObject = processHql(cls.getSimpleName(), properties, null);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} failed", new Object[] { cls.getSimpleName(), properties, re });
      throw re;
    }
  }
  
  public <T> List<T> findByProperties(Class<T> cls, Map<String, Object> properties, String orderInfo)
  {
    log.debug("finding {} instance with properties: {}", cls.getSimpleName(), properties);
    try {
      Query queryObject = processHql(cls.getSimpleName(), properties, orderInfo);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} failed", new Object[] { cls.getSimpleName(), properties, re });
      throw re;
    }
  }
  
  public <T> List<T> find(String entityName, Map<String, Object> properties, int pageSize, int pageNo, String orderInfo) {
    log.debug("finding {} instance with properties: {} ,order: {}, pageSize: {}, pageNo: {}", new Object[] { entityName, properties, orderInfo, Integer.valueOf(pageSize), Integer.valueOf(pageNo) });
    try {
      Query queryObject = processHql(entityName, properties, orderInfo);
      pageSize = pageSize < 1 ? 10 : pageSize;
      pageNo = pageNo < 1 ? 1 : pageNo;
      queryObject.setFirstResult(pageSize * (pageNo - 1)).setMaxResults(pageSize);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} ,order: {}, pageSize: {}, pageNo: {}", new Object[] { entityName, properties, orderInfo, Integer.valueOf(pageSize), Integer.valueOf(pageNo), re });
      throw re;
    }
  }
  
  public <T> List<T> find(Class<T> cls, Map<String, Object> properties, int pageSize, int pageNo, String orderInfo)
  {
    return find(cls.getSimpleName(), properties, pageSize, pageNo, orderInfo);
  }
  

  public <T> List<T> findByProperties(Class<T> cls, Map<String, Object> properties, String... columns)
  {
    log.debug("finding {} instance with properties: {}", cls.getSimpleName(), properties);
    try {
      StringBuilder queryString = new StringBuilder("select ");
      int columnsize = columns.length;
      for (String column : columns) {
        queryString.append("model.").append(column);
        if (columnsize > 1) queryString.append(",");
        columnsize--;
      }
      
      queryString.append(" from ").append(cls.getSimpleName()).append(" as model");
      queryString.append(" where ");
      int size = properties.size();
      Object values = new ArrayList(size);
      for (String prop : properties.keySet()) {
        queryString.append("model.").append(prop).append("= ?");
        ((List)values).add(properties.get(prop));
        if (size > 1) {
          queryString.append(" and ");
        }
        size--;
      }
      Query queryObject = getSession().createQuery(queryString.toString());
      for (int i = 0; i < ((List)values).size(); i++) {
        queryObject.setParameter(i, ((List)values).get(i));
      }
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} failed", new Object[] { cls.getSimpleName(), properties, re });
      throw re;
    }
  }
  
  public <T> List<T> find(Class<T> cls, Map<String, Object> properties, int pageSize, int pageNo, String orderInfo, String... columns)
  {
    log.debug("finding {} instance with properties: {} ,order: {}, pageSize: {}, pageNo: {}", new Object[] { cls.getSimpleName(), properties, orderInfo, Integer.valueOf(pageSize), Integer.valueOf(pageNo) });
    try {
      StringBuilder queryString = new StringBuilder("select ");
      int columnsize = columns.length;
      for (String column : columns) {
        queryString.append("model.").append(column);
        if (columnsize > 1) queryString.append(",");
        columnsize--;
      }
      queryString.append(" from ").append(cls.getSimpleName()).append(" as model");
      Object values = null;
      int size; if ((properties != null) && (properties.size() > 0)) {
        queryString.append(" where ");
        size = properties.size();
        values = new ArrayList(size);
        for (String prop : properties.keySet()) {
          queryString.append("model.").append(prop).append("= ?");
          ((List)values).add(properties.get(prop));
          if (size > 1) {
            queryString.append(" and ");
          }
          size--;
        }
      }
      if (!StringUtils.isEmpty(orderInfo)) {
        queryString.append(" order by ").append(orderInfo);
      }
      Query queryObject = getSession().createQuery(queryString.toString());
      if (values != null) {
        for (int i = 0; i < ((List)values).size(); i++) {
          queryObject.setParameter(i, ((List)values).get(i));
        }
      }
      pageSize = pageSize < 1 ? 10 : pageSize;
      pageNo = pageNo < 1 ? 1 : pageNo;
      queryObject.setFirstResult(pageSize * (pageNo - 1)).setMaxResults(pageSize);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} ,order: {}, pageSize: {}, pageNo: {}", new Object[] { cls.getSimpleName(), properties, orderInfo, Integer.valueOf(pageSize), Integer.valueOf(pageNo), re });
      throw re;
    }
  }
  
  public <T> List<T> findByHql(String hql) {
    return findByHql(hql, null);
  }
  
  public <T> List<T> findByHql(String hql, Map<String, Object> properties) {
    Query query = getSession().createQuery(hql);
    if ((properties != null) && (!properties.isEmpty())) {
      for (String key : properties.keySet()) {
        Object obj = properties.get(key);
        setParameter(query, key, obj);
      }
    }
    return query.list();
  }
  
  public <T> List<T> findByHqlPaging(String hql, int pagesize, int pageno) {
    return getSession().createQuery(hql).setFirstResult(pagesize * (pageno - 1)).setMaxResults(pagesize).list();
  }
  
  public <T> List<T> findByHqlPaging(String hql, Object[] params, int pagesize, int pageno) {
    Query query = getSession().createQuery(hql);
    if (params.length > 0) {
      int i = 0;
      for (Object param : params) {
        query.setParameter(i, param);
        i++;
      }
    }
    query.setFirstResult(pagesize * (pageno - 1));
    query.setMaxResults(pagesize);
    return query.list();
  }
  
  public <T> List<T> findByHqlPaging(String hql, Map<String, Object> map, int pagesize, int pageno) {
    Query query = getSession().createQuery(hql);
    if ((map != null) && (!map.isEmpty())) {
      for (String key : map.keySet()) {
        Object obj = map.get(key);
        setParameter(query, key, obj);
      }
    }
    if (pagesize > -1) {
      query.setFirstResult(pagesize * (pageno - 1));
      query.setMaxResults(pagesize);
    }
    return query.list();
  }
  
  public <T> long count(Class<T> cls, Map<String, Object> properties) {
    StringBuilder queryString = new StringBuilder("SELECT COUNT(*) FROM ").append(cls.getSimpleName()).append(" AS MODEL");
    int size; if ((properties != null) && (properties.size() > 0)) {
      size = properties.size();
      queryString.append(" WHERE ");
      for (String prop : properties.keySet()) {
        queryString.append("MODEL.").append(prop).append("= :").append(prop);
        if (size > 1) {
          queryString.append(" AND ");
        }
        size--;
      }
    }
    Query query = getSession().createQuery(queryString.toString());
    if (properties != null) {
      for (String k : properties.keySet()) {
        query.setParameter(k, properties.get(k));
      }
    }
    return ((Long)query.uniqueResult()).longValue();
  }
  
  public long count(String hql, Map<String, Object> properties) {
    Query query = getSession().createQuery(hql);
    if ((properties != null) && (!properties.isEmpty())) {
      for (String key : properties.keySet()) {
        Object obj = properties.get(key);
        setParameter(query, key, obj);
      }
    }
    return ((Long)query.uniqueResult()).longValue();
  }
  
  public long countByHql(String hql, Object... params) {
    Query query = getSession().createQuery(hql);
    int i = 0;
    for (Object p : params) {
      query.setParameter(i, p);
      i++;
    }
    return ((Long)query.uniqueResult()).longValue();
  }
  
  public <T> List<T> query(String entityName, String filter, int pageSize, int pageNo, String orderInfo) {
    log.debug("finding {} instance with properties: {} ,order: {}, pageSize: {}, pageNo: {}", new Object[] { entityName, filter, orderInfo, Integer.valueOf(pageSize), Integer.valueOf(pageNo) });
    try {
      Query queryObject = weaveHql(null, entityName, filter, orderInfo, new Object[0]);
      pageSize = pageSize < 1 ? 10 : pageSize;
      pageNo = pageNo < 1 ? 1 : pageNo;
      queryObject.setFirstResult(pageSize * (pageNo - 1)).setMaxResults(pageSize);
      return queryObject.list();
    } catch (DaoException re) {
      log.error("finding {} instance with properties: {} ,order: {}, pageSize: {}, pageNo: {}", new Object[] { entityName, filter, orderInfo, Integer.valueOf(pageSize), Integer.valueOf(pageNo), re });
      throw re;
    }
  }
  
  public long totalSize(String entity, String filter) {
    Query query = weaveHql("SELECT COUNT(1)", entity, filter, null, new Object[0]);
    return ((Long)query.uniqueResult()).longValue();
  }
  








  public Query weaveHql(String queryColumns, String entity, String filter, String orderInfo, Object... params)
  {
    StringBuilder queryString = new StringBuilder();
    if (!StringUtils.isEmpty(queryColumns)) {
      queryString.append(queryColumns).append(" ");
    }
    queryString.append("FROM ").append(entity).append(" AS a");
    if (!StringUtils.isEmpty(filter)) {
      queryString.append(" WHERE ").append(filter);
    }
    if (!StringUtils.isEmpty(orderInfo)) {
      queryString.append(" ORDER BY ").append(orderInfo);
    }
    Query query = getSession().createQuery(queryString.toString());
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i, params[i]);
    }
    return query;
  }
  








  protected Query processHql(String entity, String property, Object value, String orderInfo)
  {
    Map<String, Object> properties = new HashMap();
    properties.put(property, value);
    return processHql(entity, properties, orderInfo);
  }
  







  protected Query processHql(String entity, Map<String, Object> properties, String orderInfo)
  {
    StringBuilder queryString = new StringBuilder("FROM ").append(entity).append(" AS MODEL");
    int size; if (properties != null) {
      size = properties.size();
      if (size > 0) {
        queryString.append(" WHERE ");
        for (String prop : properties.keySet()) {
          queryString.append("MODEL.").append(prop).append("= :").append(prop);
          if (size > 1) {
            queryString.append(" AND ");
          }
          size--;
        }
      }
    }
    if (!StringUtils.isEmpty(orderInfo)) {
      queryString.append(" ORDER BY ").append(orderInfo);
    }
    Query query = getSession().createQuery(queryString.toString());
    if (properties != null) {
      for (String k : properties.keySet()) {
        query.setParameter(k, properties.get(k));
      }
    }
    return query;
  }
  






  private void setParameter(Query query, String name, Object value)
  {
    if ((value instanceof Collection)) {
      query.setParameterList(name, (Collection)value);
    } else if ((value instanceof Object[])) {
      query.setParameterList(name, (Object[])value);
    } else {
      query.setParameter(name, value);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\dao\BaseDAO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */