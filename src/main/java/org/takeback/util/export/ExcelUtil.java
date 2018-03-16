package org.takeback.util.export;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;




public class ExcelUtil
{
  private static HSSFWorkbook wb;
  private static CellStyle titleStyle;
  private static Font titleFont;
  private static CellStyle dateStyle;
  private static Font dateFont;
  private static CellStyle headStyle;
  private static Font headFont;
  private static CellStyle contentStyle;
  private static Font contentFont;
  private static CellStyle infoStyle;
  
  public static boolean export2File(ExcelExportData setInfo, String outputExcelFileName)
    throws Exception
  {
    return FileUtil.write(outputExcelFileName, export2ByteArray(setInfo), true, true);
  }
  







  public static byte[] export2ByteArray(ExcelExportData setInfo)
    throws Exception
  {
    return export2Stream(setInfo).toByteArray();
  }
  






  public static ByteArrayOutputStream export2Stream(ExcelExportData setInfo)
    throws Exception
  {
    init();
    
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    Set<Map.Entry<String, List<?>>> set = setInfo.getDataMap().entrySet();
    String[] sheetNames = new String[setInfo.getDataMap().size()];
    int sheetNameNum = 0;
    for (Map.Entry<String, List<?>> entry : set) {
      sheetNames[sheetNameNum] = ((String)entry.getKey());
      sheetNameNum++;
    }
    HSSFSheet[] sheets = getSheets(setInfo.getDataMap().size(), sheetNames);
    int sheetNum = 0;
    for (Map.Entry<String, List<?>> entry : set)
    {
      List<?> objs = (List)entry.getValue();
      

      createTableTitleRow(setInfo, sheets, sheetNum);
      




      if ((setInfo.getInfo() != null) && (setInfo.getInfo().size() != 0)) {
        createTableInfoRow(setInfo, sheets, sheetNum);
      }
      

      creatTableHeadRow(setInfo, sheets, sheetNum);
      

      String[] fieldNames = (String[])setInfo.getFieldNames().get(sheetNum);
      
      int rowNum = 4;
      for (Object obj : objs) {
        HSSFRow contentRow = sheets[sheetNum].createRow(rowNum);
        contentRow.setHeight((short)300);
        HSSFCell[] cells = getCells(contentRow, 
          ((String[])setInfo.getFieldNames().get(sheetNum)).length);
        int cellNum = 1;
        if (fieldNames != null) {
          for (int num = 0; num < fieldNames.length; num++) {
            Object value = null;
            if ((obj instanceof Map)) {
              value = ((Map)obj).get(fieldNames[num]);
            } else {
              value = ReflectionUtil.invokeGetterMethod(obj, fieldNames[num]);
            }
            

            cells[cellNum].setCellValue(value == null ? "" : value
              .toString());
            cellNum++;
          }
        }
        rowNum++;
      }
      

      if (StringUtils.isNotEmpty(setInfo.getStatistics())) {
        creatTableStatisticsRow(setInfo, sheets, sheetNum, rowNum);
      }
      
      adjustColumnSize(sheets, sheetNum, fieldNames);
      sheetNum++;
    }
    wb.write(outputStream);
    return outputStream;
  }
  


  private static void init()
  {
    wb = new HSSFWorkbook();
    
    titleFont = wb.createFont();
    titleStyle = wb.createCellStyle();
    dateStyle = wb.createCellStyle();
    dateFont = wb.createFont();
    headStyle = wb.createCellStyle();
    headFont = wb.createFont();
    contentStyle = wb.createCellStyle();
    contentFont = wb.createFont();
    infoStyle = wb.createCellStyle();
    
    initTitleCellStyle();
    initTitleFont();
    initDateCellStyle();
    initDateFont();
    initHeadCellStyle();
    initHeadFont();
    initContentCellStyle();
    initContentFont();
    initInfoCellStyle();
  }
  



  private static void adjustColumnSize(HSSFSheet[] sheets, int sheetNum, String[] fieldNames)
  {
    for (int i = 0; i < fieldNames.length + 1; i++) {
      sheets[sheetNum].autoSizeColumn(i, true);
    }
  }
  




  private static void createTableTitleRow(ExcelExportData setInfo, HSSFSheet[] sheets, int sheetNum)
  {
    CellRangeAddress titleRange = new CellRangeAddress(0, 0, 0, ((String[])setInfo.getFieldNames().get(sheetNum)).length);
    sheets[sheetNum].addMergedRegion(titleRange);
    HSSFRow titleRow = sheets[sheetNum].createRow(0);
    titleRow.setHeight((short)800);
    HSSFCell titleCell = titleRow.createCell(0);
    titleCell.setCellStyle(titleStyle);
    titleCell.setCellValue(setInfo.getTitles()[sheetNum]);
  }
  




  private static void createTableDateRow(ExcelExportData setInfo, HSSFSheet[] sheets, int sheetNum)
  {
    CellRangeAddress dateRange = new CellRangeAddress(1, 1, 0, ((String[])setInfo.getFieldNames().get(sheetNum)).length);
    sheets[sheetNum].addMergedRegion(dateRange);
    HSSFRow dateRow = sheets[sheetNum].createRow(1);
    dateRow.setHeight((short)350);
    HSSFCell dateCell = dateRow.createCell(0);
    dateCell.setCellStyle(dateStyle);
    dateCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd")
      .format(new Date()));
  }
  





  private static void createTableInfoRow(ExcelExportData setInfo, HSSFSheet[] sheets, int sheetNum)
  {
    HSSFRow dateRow = sheets[sheetNum].createRow(1);
    
    for (int i = 0; i < 6; i++) {
      if (i == 3) {
        dateRow = sheets[sheetNum].createRow(2);
      }
      for (int j = 0; j < 2; j++) {
        CellRangeAddress dateRange = new CellRangeAddress((i + 3) / 3, (i + 3) / 3, i % 3 * 4 + j, i % 3 * 4 + j * 3);
        sheets[sheetNum].addMergedRegion(dateRange);
        dateRow.setHeight((short)600);
        HSSFCell dateCell = dateRow.createCell(i % 3 * 4 + j);
        dateCell.setCellValue((String)((HashMap)setInfo.getInfo().get(i)).get(j == 0 ? "text" : "value"));
        dateCell.setCellStyle(infoStyle);
      }
    }
  }
  






  private static void creatTableHeadRow(ExcelExportData setInfo, HSSFSheet[] sheets, int sheetNum)
  {
    HSSFRow headRow = sheets[sheetNum].createRow(3);
    headRow.setHeight((short)350);
    
    HSSFCell snCell = headRow.createCell(0);
    snCell.setCellStyle(headStyle);
    snCell.setCellValue("序号");
    
    int num = 1; for (int len = ((String[])setInfo.getColumnNames().get(sheetNum)).length; num <= len; num++) {
      HSSFCell headCell = headRow.createCell(num);
      headCell.setCellStyle(headStyle);
      headCell.setCellValue(((String[])setInfo.getColumnNames().get(sheetNum))[(num - 1)]);
    }
  }
  


  private static HSSFSheet[] getSheets(int num, String[] names)
  {
    HSSFSheet[] sheets = new HSSFSheet[num];
    for (int i = 0; i < num; i++) {
      sheets[i] = wb.createSheet(names[i]);
    }
    return sheets;
  }
  


  private static HSSFCell[] getCells(HSSFRow contentRow, int num)
  {
    HSSFCell[] cells = new HSSFCell[num + 1];
    
    int i = 0; for (int len = cells.length; i < len; i++) {
      cells[i] = contentRow.createCell(i);
      cells[i].setCellStyle(contentStyle);
    }
    
    cells[0].setCellValue(contentRow.getRowNum() - 3);
    return cells;
  }
  




  private static void creatTableStatisticsRow(ExcelExportData setInfo, HSSFSheet[] sheets, int sheetNum, int num)
  {
    CellRangeAddress dateRange = new CellRangeAddress(num, num, 0, ((String[])setInfo.getFieldNames().get(sheetNum)).length);
    
    sheets[sheetNum].addMergedRegion(dateRange);
    
    HSSFRow dateRow = sheets[sheetNum].createRow(num);
    dateRow.setHeight((short)400);
    HSSFCell dateCell = dateRow.createCell(0);
    dateCell.setCellStyle(infoStyle);
    dateCell.setCellValue(setInfo.getStatistics());
  }
  


  private static void initTitleCellStyle()
  {
    titleStyle.setAlignment((short)2);
    titleStyle.setVerticalAlignment((short)1);
    titleStyle.setFont(titleFont);
    titleStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
  }
  


  private static void initDateCellStyle()
  {
    dateStyle.setAlignment((short)6);
    dateStyle.setVerticalAlignment((short)1);
    dateStyle.setFont(dateFont);
    dateStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
  }
  


  private static void initHeadCellStyle()
  {
    headStyle.setAlignment((short)2);
    headStyle.setVerticalAlignment((short)1);
    headStyle.setFont(headFont);
    headStyle.setFillBackgroundColor(IndexedColors.YELLOW.index);
    headStyle.setBorderTop((short)2);
    headStyle.setBorderBottom((short)1);
    headStyle.setBorderLeft((short)1);
    headStyle.setBorderRight((short)1);
    headStyle.setTopBorderColor(IndexedColors.BLUE.index);
    headStyle.setBottomBorderColor(IndexedColors.BLUE.index);
    headStyle.setLeftBorderColor(IndexedColors.BLUE.index);
    headStyle.setRightBorderColor(IndexedColors.BLUE.index);
  }
  
  private static void initInfoCellStyle()
  {
    infoStyle.setAlignment((short)2);
    infoStyle.setVerticalAlignment((short)1);
    infoStyle.setFont(headFont);
    infoStyle.setFillBackgroundColor(IndexedColors.SKY_BLUE.index);
    infoStyle.setWrapText(true);
  }
  


  private static void initContentCellStyle()
  {
    contentStyle.setAlignment((short)2);
    contentStyle.setVerticalAlignment((short)1);
    contentStyle.setFont(contentFont);
    contentStyle.setBorderTop((short)1);
    contentStyle.setBorderBottom((short)1);
    contentStyle.setBorderLeft((short)1);
    contentStyle.setBorderRight((short)1);
    contentStyle.setTopBorderColor(IndexedColors.BLUE.index);
    contentStyle.setBottomBorderColor(IndexedColors.BLUE.index);
    contentStyle.setLeftBorderColor(IndexedColors.BLUE.index);
    contentStyle.setRightBorderColor(IndexedColors.BLUE.index);
    contentStyle.setWrapText(true);
  }
  


  private static void initTitleFont()
  {
    titleFont.setFontName("华文楷体");
    titleFont.setFontHeightInPoints((short)20);
    titleFont.setBoldweight((short)700);
    titleFont.setCharSet((byte)1);
    titleFont.setColor(IndexedColors.BLUE_GREY.index);
  }
  


  private static void initDateFont()
  {
    dateFont.setFontName("隶书");
    dateFont.setFontHeightInPoints((short)10);
    dateFont.setBoldweight((short)700);
    dateFont.setCharSet((byte)1);
    dateFont.setColor(IndexedColors.BLUE_GREY.index);
  }
  


  private static void initHeadFont()
  {
    headFont.setFontName("宋体");
    headFont.setFontHeightInPoints((short)10);
    headFont.setBoldweight((short)700);
    headFont.setCharSet((byte)1);
    headFont.setColor(IndexedColors.BLUE_GREY.index);
  }
  


  private static void initContentFont()
  {
    contentFont.setFontName("宋体");
    contentFont.setFontHeightInPoints((short)10);
    contentFont.setBoldweight((short)400);
    contentFont.setCharSet((byte)1);
    contentFont.setColor(IndexedColors.BLUE_GREY.index);
  }
  




  public static class ExcelExportData
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
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\export\ExcelUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */