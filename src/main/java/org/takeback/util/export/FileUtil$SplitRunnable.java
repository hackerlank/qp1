package org.takeback.util.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;















































































































































































































































































































































class FileUtil$SplitRunnable
  implements Runnable
{
  int byteSize;
  String partFileName;
  File originFile;
  int startPos;
  
  public FileUtil$SplitRunnable(FileUtil paramFileUtil, int byteSize, int startPos, String partFileName, File originFile)
  {
    this.startPos = startPos;
    this.byteSize = byteSize;
    this.partFileName = partFileName;
    this.originFile = originFile;
  }
  
  public void run()
  {
    try
    {
      RandomAccessFile rFile = new RandomAccessFile(this.originFile, "r");
      byte[] b = new byte[this.byteSize];
      rFile.seek(this.startPos);
      int s = rFile.read(b);
      OutputStream os = new FileOutputStream(this.partFileName);
      os.write(b, 0, s);
      os.flush();
      os.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\export\FileUtil$SplitRunnable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */