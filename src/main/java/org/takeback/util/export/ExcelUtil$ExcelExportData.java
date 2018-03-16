package org.takeback.util.export;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;






































































































































































































































































































































































































































public class ExcelUtil$ExcelExportData
{
  private LinkedHashMap<String, List<?>> dataMap;
  private String[] titles;
  private List<String[]> columnNames;
  private List<HashMap<String, String>> info;
  private String statistics;
  private List<String[]> fieldNames;
  
  public List<String[]> getFieldNames()
  {
    return this.fieldNames;
  }
  
  public void setFieldNames(List<String[]> fieldNames) {
    this.fieldNames = fieldNames;
  }
  
  public String[] getTitles() {
    return this.titles;
  }
  
  public void setTitles(String[] titles) {
    this.titles = titles;
  }
  
  public List<String[]> getColumnNames() {
    return this.columnNames;
  }
  
  public void setColumnNames(List<String[]> columnNames) {
    this.columnNames = columnNames;
  }
  
  public LinkedHashMap<String, List<?>> getDataMap() {
    return this.dataMap;
  }
  
  public void setDataMap(LinkedHashMap<String, List<?>> dataMap) {
    this.dataMap = dataMap;
  }
  
  public List<HashMap<String, String>> getInfo() {
    return this.info;
  }
  
  public void setInfo(List<HashMap<String, String>> info) {
    this.info = info;
  }
  
  public String getStatistics() {
    return this.statistics;
  }
  
  public void setStatistics(String statistics) {
    this.statistics = statistics;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\export\ExcelUtil$ExcelExportData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */