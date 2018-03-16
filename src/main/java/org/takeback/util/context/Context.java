package org.takeback.util.context;

import java.util.HashMap;
import java.util.Map;
import org.takeback.util.BeanUtils;
import org.takeback.util.converter.ConversionUtils;

public class Context extends HashMap<String, Object>
{
  private static final long serialVersionUID = 7821961495108859198L;
  public static final String APP_CONTEXT = "$applicationContext";
  public static final String WEB_SESSION = "$webSession";
  public static final String HTTP_REQUEST = "$httpRequest";
  public static final String HTTP_RESPONSE = "$httpResponse";
  public static final String DB_SESSION = "$dbSession";
  public static final String FROM_DOMAIN = "$fromDomain";
  public static final String CLIENT_IP_ADDRESS = "$clientIpAddress";
  public static final String USER_ROLE_TOKEN = "$urt";
  public static final String REQUEST_APPNODE_DEEP = "$requestAppNodeDeep";
  public static final String REQUEST_UNIT_DEEP = "$requestUnitDeep";
  public static final String EXP_BEAN = "$exp";
  public static final String ENTITY_CONTEXT = "$r";
  public static final String QUERY_CONTEXT = "$q";
  public static final String TENANT_ID = "$tenantId";
  public static final String RPC_INVOKE_HEADERS = "$rpcInvokeHeaders";
  public static final String UID = "$uid";
  public static final String INVITOR = "$invitor";
  private static String topCtxName;
  private static Context topCtx;
  
  public static Context instance()
  {
    return topCtx;
  }
  
  public Context(String name, Context ctx) {
    topCtxName = name;
    topCtx = ctx;
    put(topCtxName, topCtx);
  }
  
  public Context() {
    if (topCtx != null) {
      put(topCtxName, topCtx);
    }
  }
  
  public Context(Map<String, Object> m) {
    super(m);
  }
  
  public Object get(Object key)
  {
    if (containsKey(key)) {
      return super.get(key);
    }
    try {
      return BeanUtils.getProperty(this, (String)key);
    } catch (Exception e) {}
    return null;
  }
  

  public void set(String key, Object v)
  {
    try
    {
      BeanUtils.setProperty(this, key, v);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
  
  public <T> T get(Object key, Class<T> type) {
    Object result = get(key);
    return (T)ConversionUtils.convert(result, type);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\context\Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */