package org.takeback.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.dao.BaseDAO;

@Service("baseService")
public class BaseService
{
  @Autowired
  protected BaseDAO dao;
  
  @Transactional(readOnly=true)
  public <T> List<T> findByHql(String hql, Map<String, Object> properties, int pageSize, int pageNo)
  {
    return this.dao.findByHqlPaging(hql, properties, pageSize, pageNo);
  }
  
  @Transactional(readOnly=true)
  public <T> List<T> find(Class<T> cls, Map<String, Object> properties, int pageSize, int pageNo, String orderInfo) {
    return this.dao.find(cls, properties, pageSize, pageNo, orderInfo);
  }
  
  @Transactional(readOnly=true)
  public <T> List<T> findByProperties(Class<T> cls, Map<String, Object> properties) {
    return this.dao.findByProperties(cls, properties);
  }
  
  @Transactional(readOnly=true)
  public <T> List<T> findByExample(Class<T> cls, T instance) {
    return this.dao.findByExample(cls, instance);
  }
  
  @Transactional(readOnly=true)
  public <T> List<T> findByProperty(Class<T> cls, String propertyName, Object value) {
    return this.dao.findByProperty(cls, propertyName, value);
  }
  
  @Transactional(readOnly=true)
  public <T> List<T> findByProperty(Class<T> cls, String propertyName, Object value, String orderInfo) {
    return this.dao.findByProperty(cls, propertyName, value, orderInfo);
  }
  
  @Transactional(readOnly=true)
  public <T> T get(Class<T> cls, Serializable id) {
    return (T)this.dao.get(cls, id);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public <T> void update(Class<T> cls, T t) {
    this.dao.update(cls, t);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public <T> void merge(Class<T> cls, T t) {
    this.dao.merge(cls, t);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public <T> void delete(Class<T> cls, T t) {
    this.dao.delete(cls, t);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public <T> void save(Class<T> cls, T t) {
    this.dao.save(cls, t);
  }
  
  @Transactional(rollbackFor={Throwable.class})
  public int executeUpdate(String hql, Map<String, Object> pramas) {
    return this.dao.executeUpdate(hql, pramas);
  }
  
  @Transactional(readOnly=true)
  public <T> long count(Class<T> cls, Map<String, Object> properties) {
    return this.dao.count(cls, properties);
  }
  
  @Transactional(readOnly=true)
  public long count(String hql, Map<String, Object> map) {
    return this.dao.count(hql, map);
  }
  
  @Transactional(readOnly=true)
  public <T> T getUnique(Class<T> cls, String propertyName, Object value) {
    return (T)this.dao.getUnique(cls, propertyName, value);
  }
  
  @Transactional(readOnly=true)
  public <T> Map entityPagingByProperties(Class<T> cls, Map<String, Object> properties, int pageSize, int pageNo, String orderInfo) {
    List<T> list = this.dao.find(cls, properties, pageSize, pageNo, orderInfo);
    long total = 0L;
    if ((list != null) && (list.size() > 0)) {
      total = this.dao.count(cls, properties);
    }
    
    HashMap result = new HashMap();
    result.put("data", list);
    result.put("total", Long.valueOf(total));
    return result;
  }
  
  @Transactional(readOnly=true)
  public <T> Map hqlPagingByProperties(String hql, Map<String, Object> properties, int pageSize, int pageNo, String orderInfo) {
    List<T> list = this.dao.findByHqlPaging(
      StringUtils.isNotEmpty(orderInfo) ? hql + " order by " + orderInfo : hql, properties, pageSize, pageNo);
    
    long total = 0L;
    if ((list != null) && (list.size() > 0)) {
      if (hql.indexOf("select ") != -1) {
        total = this.dao.count("select count(*) " + hql.substring(hql.indexOf(" from ")), properties);
      } else {
        total = this.dao.count("select count(*) " + hql, properties);
      }
    }
    
    HashMap result = new HashMap();
    result.put("data", list);
    result.put("total", Long.valueOf(total));
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\service\BaseService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */