package org.takeback.core.resource;

import java.io.IOException;
import org.springframework.core.io.Resource;

public abstract interface RemoteResourceLoader
{
  public abstract Resource load(String paramString, boolean paramBoolean)
    throws IOException;
  
  public abstract Resource load(String paramString)
    throws IOException;
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\resource\RemoteResourceLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */