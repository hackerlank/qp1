package org.takeback.util.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takeback.util.exception.CodedBaseRuntimeException;


public class HttpClientUtils
{
  private static final Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
  

  public static String encode = "UTF-8";
  





  private static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
  private static CloseableHttpClient httpClient = httpClientBuilder.setConnectionTimeToLive(20L, TimeUnit.SECONDS).build();
  
  public static String get(String url)
  {
    HttpGet method = new HttpGet(url);
    try {
      HttpResponse httpResponse = httpClient.execute(method);
      int code = httpResponse.getStatusLine().getStatusCode();
      if (200 == code) {
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity, encode);
      }
      throw new CodedBaseRuntimeException(code, "http response error code " + code);
    }
    catch (IOException e) {
      throw new CodedBaseRuntimeException(500, "execute get method [" + url + "] failed", e);
    } finally {
      method.releaseConnection();
    }
  }
  





  public static String post(String url, Map<String, String> params)
  {
    HttpPost method = new HttpPost(url);
    method.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    String k; if ((params != null) || (params.size() > 0)) {
      List<NameValuePair> nvps = new ArrayList();
      Set<String> set = params.keySet();
      for (Iterator localIterator = set.iterator(); localIterator.hasNext();) { k = (String)localIterator.next();
        nvps.add(new BasicNameValuePair(k, (String)params.get(k)));
      }
      try {
        method.setEntity(new UrlEncodedFormEntity(nvps, encode));
      } catch (UnsupportedEncodingException e) {
        throw new CodedBaseRuntimeException(505, "http parameters encode failed");
      }
    }
    try {
      HttpResponse httpResponse = httpClient.execute(method);
      int code = httpResponse.getStatusLine().getStatusCode();
      if (200 == code) {
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity);
      }
      throw new CodedBaseRuntimeException(code, "http response error code " + code);
    }
    catch (IOException e) {
      throw new CodedBaseRuntimeException(500, "execute post method [" + url + "] failed");
    } finally {
      method.releaseConnection();
    }
  }
  
  public static void release() {
    try {
      httpClient.close();
    } catch (IOException e) {
      log.error("httpClient close failed.", e);
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\httpclient\HttpClientUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */