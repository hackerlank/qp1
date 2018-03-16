package org.takeback.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

public class IOUtils
{
  private static final int BUFFER_SIZE = 8192;
  
  public static long write(InputStream is, OutputStream os) throws IOException
  {
    return write(is, os, 8192);
  }
  











  public static long write(InputStream is, OutputStream os, int bufferSize)
    throws IOException
  {
    long total = 0L;
    byte[] buff = new byte[bufferSize];
    while (is.available() > 0) {
      int read = is.read(buff, 0, buff.length);
      if (read > 0) {
        os.write(buff, 0, read);
        total += read;
      }
    }
    return total;
  }
  






  public static String read(Reader reader)
    throws IOException
  {
    StringWriter writer = new StringWriter();
    try {
      write(reader, writer);
      return writer.getBuffer().toString();
    } finally {
      writer.close();
    }
  }
  







  public static long write(Writer writer, String string)
    throws IOException
  {
    Reader reader = new StringReader(string);
    try {
      return write(reader, writer);
    } finally {
      reader.close();
    }
  }
  








  public static long write(Reader reader, Writer writer)
    throws IOException
  {
    return write(reader, writer, 8192);
  }
  











  public static long write(Reader reader, Writer writer, int bufferSize)
    throws IOException
  {
    long total = 0L;
    char[] buf = new char[' '];
    int read; while ((read = reader.read(buf)) != -1) {
      writer.write(buf, 0, read);
      total += read;
    }
    return total;
  }
  






  public static String[] readLines(File file)
    throws IOException
  {
    if ((file == null) || (!file.exists()) || (!file.canRead())) {
      return new String[0];
    }
    return readLines(new FileInputStream(file));
  }
  






  public static String[] readLines(InputStream is)
    throws IOException
  {
    List<String> lines = new java.util.ArrayList();
    BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(is));
    try {
      String line;
      while ((line = reader.readLine()) != null)
        lines.add(line);
      return (String[])lines.toArray(new String[0]);
    } finally {
      reader.close();
    }
  }
  







  public static void writeLines(OutputStream os, String[] lines)
    throws IOException
  {
    PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
    try {
      for (String line : lines)
        writer.println(line);
      writer.flush();
    } finally {
      writer.close();
    }
  }
  







  public static void writeLines(File file, String[] lines)
    throws IOException
  {
    if (file == null)
      throw new IOException("File is null.");
    writeLines(new FileOutputStream(file), lines);
  }
  







  public static void appendLines(File file, String[] lines)
    throws IOException
  {
    if (file == null)
      throw new IOException("File is null.");
    writeLines(new FileOutputStream(file, true), lines);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\io\IOUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */