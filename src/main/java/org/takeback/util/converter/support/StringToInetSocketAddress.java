package org.takeback.util.converter.support;

import java.net.InetSocketAddress;
import org.springframework.core.convert.converter.Converter;
import org.takeback.util.NetUtils;

public class StringToInetSocketAddress
  implements Converter<String, InetSocketAddress>
{
  public InetSocketAddress convert(String source)
  {
    return NetUtils.toAddress(source);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\converter\support\StringToInetSocketAddress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */