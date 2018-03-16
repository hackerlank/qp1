package org.takeback.util.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileUtil
{
  public static String currentWorkDir = System.getProperty("user.dir") + "\\";
  







  public static String leftPad(String str, int length, char ch)
  {
    if (str.length() >= length) {
      return str;
    }
    char[] chs = new char[length];
    java.util.Arrays.fill(chs, ch);
    char[] src = str.toCharArray();
    System.arraycopy(src, 0, chs, length - src.length, src.length);
    return new String(chs);
  }
  







  public static boolean delete(String fileName)
  {
    boolean result = false;
    File f = new File(fileName);
    if (f.exists()) {
      result = f.delete();
    }
    else {
      result = true;
    }
    return result;
  }
  





  public static ArrayList<File> getAllFiles(String dirPath)
  {
    File dir = new File(dirPath);
    
    ArrayList<File> files = new ArrayList();
    
    if (dir.isDirectory()) {
      File[] fileArr = dir.listFiles();
      for (int i = 0; i < fileArr.length; i++) {
        File f = fileArr[i];
        if (f.isFile()) {
          files.add(f);
        } else {
          files.addAll(getAllFiles(f.getPath()));
        }
      }
    }
    return files;
  }
  





  public static ArrayList<File> getDirFiles(String dirPath)
  {
    File path = new File(dirPath);
    File[] fileArr = path.listFiles();
    ArrayList<File> files = new ArrayList();
    
    for (File f : fileArr) {
      if (f.isFile()) {
        files.add(f);
      }
    }
    return files;
  }
  









  public static ArrayList<File> getDirFiles(String dirPath, String suffix)
  {
    File path = new File(dirPath);
    File[] fileArr = path.listFiles(new java.io.FilenameFilter() {
      public boolean accept(File dir, String name) {
        String lowerName = name.toLowerCase();
        String lowerSuffix = this.val$suffix.toLowerCase();
        if (lowerName.endsWith(lowerSuffix)) {
          return true;
        }
        return false;
      }
      
    });
    ArrayList<File> files = new ArrayList();
    
    for (File f : fileArr) {
      if (f.isFile()) {
        files.add(f);
      }
    }
    return files;
  }
  






  public static String read(String fileName)
    throws IOException
  {
    File f = new File(fileName);
    FileInputStream fs = new FileInputStream(f);
    String result = null;
    byte[] b = new byte[fs.available()];
    fs.read(b);
    fs.close();
    result = new String(b);
    return result;
  }
  









  public static boolean write(String fileName, String fileContent)
    throws IOException
  {
    return write(fileName, fileContent, true, true);
  }
  













  public static boolean write(String fileName, String fileContent, boolean autoCreateDir, boolean autoOverwrite)
    throws IOException
  {
    return write(fileName, fileContent.getBytes(), autoCreateDir, autoOverwrite);
  }
  














  public static boolean write(String fileName, byte[] contentBytes, boolean autoCreateDir, boolean autoOverwrite)
    throws IOException
  {
    boolean result = false;
    if (autoCreateDir) {
      createDirs(fileName);
    }
    if (autoOverwrite) {
      delete(fileName);
    }
    File f = new File(fileName);
    FileOutputStream fs = new FileOutputStream(f);
    fs.write(contentBytes);
    fs.flush();
    fs.close();
    result = true;
    return result;
  }
  







  public static boolean append(String fileName, String fileContent)
    throws IOException
  {
    boolean result = false;
    File f = new File(fileName);
    if (f.exists()) {
      RandomAccessFile rFile = new RandomAccessFile(f, "rw");
      byte[] b = fileContent.getBytes();
      long originLen = f.length();
      rFile.setLength(originLen + b.length);
      rFile.seek(originLen);
      rFile.write(b);
      rFile.close();
    }
    result = true;
    return result;
  }
  









  public List<String> splitBySize(String fileName, int byteSize)
    throws IOException
  {
    List<String> parts = new ArrayList();
    File file = new File(fileName);
    int count = (int)Math.ceil(file.length() / byteSize);
    int countLen = (count + "").length();
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(count, count * 3, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue(count * 2));
    


    for (int i = 0; i < count; i++)
    {
      String partFileName = file.getPath() + "." + leftPad(new StringBuilder().append(i + 1).append("").toString(), countLen, '0') + ".part";
      threadPool.execute(new SplitRunnable(byteSize, i * byteSize, partFileName, file));
      
      parts.add(partFileName);
    }
    return parts;
  }
  












  public void mergePartFiles(String dirPath, String partFileSuffix, int partFileSize, String mergeFileName)
    throws IOException
  {
    ArrayList<File> partFiles = getDirFiles(dirPath, partFileSuffix);
    
    java.util.Collections.sort(partFiles, new FileComparator(null));
    
    RandomAccessFile randomAccessFile = new RandomAccessFile(mergeFileName, "rw");
    
    randomAccessFile.setLength(partFileSize * (partFiles.size() - 1) + 
      ((File)partFiles.get(partFiles.size() - 1)).length());
    randomAccessFile.close();
    


    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(partFiles.size(), partFiles.size() * 3, 1L, TimeUnit.SECONDS, new ArrayBlockingQueue(partFiles.size() * 2));
    
    for (int i = 0; i < partFiles.size(); i++) {
      threadPool.execute(new MergeRunnable(i * partFileSize, mergeFileName, 
        (File)partFiles.get(i)));
    }
  }
  

  private class FileComparator
    implements java.util.Comparator<File>
  {
    private FileComparator() {}
    

    public int compare(File o1, File o2)
    {
      return o1.getName().compareToIgnoreCase(o2.getName());
    }
  }
  





  public static void createDirs(String filePath)
  {
    File file = new File(filePath);
    File parent = file.getParentFile();
    if ((parent != null) && (!parent.exists())) {
      parent.mkdirs();
    }
  }
  

  private class SplitRunnable
    implements Runnable
  {
    int byteSize;
    
    String partFileName;
    
    File originFile;
    
    int startPos;
    

    public SplitRunnable(int byteSize, int startPos, String partFileName, File originFile)
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
  

  private class MergeRunnable
    implements Runnable
  {
    long startPos;
    
    String mergeFileName;
    
    File partFile;
    
    public MergeRunnable(long startPos, String mergeFileName, File partFile)
    {
      this.startPos = startPos;
      this.mergeFileName = mergeFileName;
      this.partFile = partFile;
    }
    
    public void run()
    {
      try {
        RandomAccessFile rFile = new RandomAccessFile(this.mergeFileName, "rw");
        rFile.seek(this.startPos);
        FileInputStream fs = new FileInputStream(this.partFile);
        byte[] b = new byte[fs.available()];
        fs.read(b);
        fs.close();
        rFile.write(b);
        rFile.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\export\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */