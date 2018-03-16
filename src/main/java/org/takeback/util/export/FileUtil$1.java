package org.takeback.util.export;

import java.io.File;
import java.io.FilenameFilter;








































































































final class FileUtil$1
  implements FilenameFilter
{
  FileUtil$1(String paramString) {}
  
  public boolean accept(File dir, String name)
  {
    String lowerName = name.toLowerCase();
    String lowerSuffix = this.val$suffix.toLowerCase();
    if (lowerName.endsWith(lowerSuffix)) {
      return true;
    }
    return false;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\export\FileUtil$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */