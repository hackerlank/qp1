package org.takeback.chat.service.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.mvc.listener.SessionListener;
import org.takeback.util.exception.CodedBaseRuntimeException;

@org.springframework.stereotype.Service("onlineUserService")
public class OnlineUserService extends org.takeback.core.service.MyListServiceInt
{
  @Transactional(readOnly=true)
  public Map<String, Object> list(Map<String, Object> req)
  {
    String entityName = (String)req.get(ENTITYNAME);
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    int limit = ((Integer)req.get(LIMIT)).intValue();
    int page = ((Integer)req.get(PAGE)).intValue();
    
    Map<Integer, List<String>> users = SessionListener.getUsers();
    Set<Integer> keyset = users.keySet();
    Integer start = Integer.valueOf((page - 1) * limit);
    Integer end = Integer.valueOf(start.intValue() + limit);
    Iterator itr = keyset.iterator();
    Integer idx = Integer.valueOf(-1);
    StringBuffer keys = new StringBuffer();
    while (itr.hasNext()) {
      Integer localInteger1 = idx;Integer localInteger2 = idx = Integer.valueOf(idx.intValue() + 1);
      if (idx.intValue() < start.intValue()) {
        itr.next();
      }
      else {
        keys.append(itr.next()).append(",");
        if (idx.intValue() + 1 >= end.intValue()) {
          break;
        }
      }
    }
    Object result = new HashMap();
    if (keys.length() < 1) {
      ((Map)result).put("totalSize", Integer.valueOf(0));
      ((Map)result).put("body", new ArrayList());
      return (Map<String, Object>)result;
    }
    String key = keys.substring(0, keys.length() - 1);
    String hql = "from PubUser where id in(" + key + ")";
    List<org.takeback.chat.entity.PubUser> ls = this.dao.findByHql(hql);
    long count = users.size();
    ((Map)result).put("totalSize", Long.valueOf(count));
    ((Map)result).put("body", ls);
    return (Map<String, Object>)result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\OnlineUserService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */