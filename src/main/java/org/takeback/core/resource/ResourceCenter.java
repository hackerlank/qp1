package org.takeback.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.takeback.util.ApplicationContextHolder;

public class ResourceCenter implements org.springframework.context.ResourceLoaderAware
{
  private static ResourceLoader loader = new org.springframework.core.io.DefaultResourceLoader();
  private static RemoteResourceLoader remoteLoader;
  
  public static Resource load(String path) throws IOException {
    Resource r = loader.getResource("classpath:" + path);
    if (r.exists()) {
      return r;
    }
    r = loader.getResource(path);
    if (r.exists()) {
      return r;
    }
    if (remoteLoader != null) {
      return remoteLoader.load(path, !ApplicationContextHolder.isDevMode());
    }
    throw new FileNotFoundException("file not found:" + path);
  }
  
  public static Resource load(String pathPrefix, String path) throws IOException
  {
    Resource r = loader.getResource(pathPrefix + path);
    if (r.exists()) {
      return r;
    }
    throw new FileNotFoundException("file not found:" + path);
  }
  
















  public static void write(Resource r, OutputStream output)
    throws IOException
  {
    String protocol = r.getURL().getProtocol();
    boolean isFileSystem = protocol.startsWith("file");
    
    if ((ApplicationContextHolder.isDevMode()) && (isFileSystem)) {
      File f = r.getFile();
      InputStream input = new java.io.FileInputStream(f);
      try {
        org.takeback.util.io.IOUtils.write(input, output);
      } finally {
        input.close();
      }
    } else {
      InputStream input = r.getInputStream();
      try {
        org.takeback.util.io.IOUtils.write(input, output);
      } finally {
        input.close();
      }
    }
  }
  
  public void setResourceLoader(ResourceLoader appContextLoader)
  {
    loader = appContextLoader;
  }
  
  public void setRemoteResourceLoader(RemoteResourceLoader loader) {
    remoteLoader = loader;
  }
  
  public static String getAbstractClassPath() throws URISyntaxException {
    return new File(loader.getClassLoader().getResource("").toURI()).getAbsolutePath();
  }
  
  public static String getAbstractClassPath(String path) throws URISyntaxException {
    return org.apache.commons.lang3.StringUtils.join(new String[] { getAbstractClassPath(), "/", path });
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\resource\ResourceCenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */