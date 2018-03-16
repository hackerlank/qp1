package org.takeback.util.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XMLHelper
{
  private static final String CHARSET = "UTF-8";
  
  public static Document createDocument()
  {
    return org.dom4j.DocumentHelper.createDocument();
  }
  
  public static Document getDocument(String fileName) throws org.dom4j.DocumentException, IOException {
    return getDocument(new File(fileName));
  }
  
  public static Document getDocument(File file) throws org.dom4j.DocumentException, IOException {
    return getDocument(new java.io.FileInputStream(file));
  }
  
  public static Document getDocument(java.io.InputStream ins) throws org.dom4j.DocumentException, IOException {
    org.dom4j.io.SAXReader oReader = new org.dom4j.io.SAXReader();
    try {
      return oReader.read(ins);
    } finally {
      ins.close();
    }
  }
  
  public static void putDocument(OutputStream outs, Document doc) throws IOException {
    OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer = null;
    try {
      format.setEncoding("UTF-8");
      writer = new XMLWriter(outs, format);
      writer.setEscapeText(false);
      writer.write(doc);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
  
  public static void putDocument(OutputStream outs, String doc) throws IOException {
    OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer = null;
    try {
      format.setEncoding("UTF-8");
      writer = new XMLWriter(outs, format);
      writer.setEscapeText(false);
      writer.write(doc);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
  
  public static void putDocument(File file, Document doc) throws IOException {
    OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer = null;
    try {
      format.setEncoding("UTF-8");
      writer = new XMLWriter(new java.io.FileOutputStream(file), format);
      writer.setEscapeText(true);
      writer.write(doc);
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\xml\XMLHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */