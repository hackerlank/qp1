package org.takeback.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.takeback.mvc.ResponseUtils;
import org.takeback.util.ApplicationContextHolder;
import org.takeback.util.export.ExcelUtil.ExcelExportData;

@Controller
@RequestMapping({"/**/file"})
public class ResourceController
{
  private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
  
  private static final String SERVICE = "service";
  private static final String METHOD = "method";
  private static final String FILENAME = "filename";
  private String fileDirectory = "/resources/upload";
  
  @RequestMapping(value={"upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ModelAndView upload(@RequestParam("file") CommonsMultipartFile file, @RequestParam(required=false) String filePath, HttpServletRequest request)
  {
    Object uid = WebUtils.getSessionAttribute(request, "$uid");
    if (uid == null) {
      return ResponseUtils.jsonView(403, "not login");
    }
    
    if (file.getSize() == 0L) {
      return ResponseUtils.jsonView(404, "filesize is 0");
    }
    

    String oFilename = file.getOriginalFilename();
    String suffix = oFilename.contains(".") ? oFilename.substring(oFilename.lastIndexOf(".")) : "";
    String deskName = org.apache.commons.lang3.StringUtils.join(new java.io.Serializable[] { Long.valueOf(System.currentTimeMillis()), suffix });
    File directory = new File(request.getSession().getServletContext().getRealPath(org.springframework.util.StringUtils.isEmpty(filePath) ? this.fileDirectory : filePath));
    if (!directory.exists()) {
      directory.mkdirs();
    }
    
    HashMap map = new HashMap();
    map.put("filename", deskName);
    
    if ((float)file.getSize() < 10485.0F) {
      map.put("size", this.decimalFormat.format((float)file.getSize() / 1024.0F) + "KB");
    } else {
      map.put("size", this.decimalFormat.format((float)file.getSize() / 1024.0F / 1024.0F) + "MB");
    }
    try {
      File deskFile = new File(directory.getAbsolutePath() + "/" + deskName);
      file.transferTo(deskFile);
      return ResponseUtils.jsonView(com.google.common.collect.ImmutableMap.of("code", Integer.valueOf(200), "success", Boolean.valueOf(true), "body", map));
    } catch (IOException e) {}
    return ResponseUtils.jsonView(500, "failure", deskName);
  }
  





  @RequestMapping(value={"download"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public void download(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) {}
  





  @RequestMapping(value={"exportExcel"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public ModelAndView exportExcel(HttpServletRequest request, HttpServletResponse response)
  {
    String uid = (String)WebUtils.getSessionAttribute(request, "$uid");
    Long urt = (Long)WebUtils.getSessionAttribute(request, "$urt");
    if ((uid == null) || (urt == null)) {
      return ResponseUtils.jsonView(401, "notLogon");
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    Map properties = ParameterMap2Map(request.getParameterMap());
    
    if ((org.springframework.util.StringUtils.isEmpty(properties.get("service"))) || (org.springframework.util.StringUtils.isEmpty(properties.get("method"))) || 
      (org.springframework.util.StringUtils.isEmpty(properties.get("filename")))) {
      return ResponseUtils.jsonView(402, "missing service or method.");
    }
    
    String service = properties.get("service").toString();
    String method = properties.get("method").toString();
    String filename = properties.get("filename").toString();
    
    if (!ApplicationContextHolder.containBean(service)) {
      return ResponseUtils.jsonView(403, "service is not defined in spring.");
    }
    
    response.reset();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("content-disposition", "attachment;filename=" + filename + sdf.format(new Date()) + ".xls");
    OutputStream fOut = null;
    try {
      fOut = response.getOutputStream();
    } catch (IOException e) {
      return ResponseUtils.jsonView(404, "get OutputStream error!");
    }
    
    properties.remove("service");
    properties.remove("method");
    properties.remove("filename");
    
    LinkedHashMap<String, java.util.List<?>> datamap = new LinkedHashMap();
    Object s = ApplicationContextHolder.getBean(service);
    Method m = null;
    Object r = null;
    try {
      m = s.getClass().getDeclaredMethod(method, new Class[] { Map.class });
      r = m.invoke(s, new Object[] { properties });
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(405, "导出文件出错！" + e.getMessage());
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(406, "导出文件出错！" + e.getMessage());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return ResponseUtils.jsonView(407, "导出文件出错！" + e.getMessage());
    }
    
    ExcelUtil.ExcelExportData setInfo = (ExcelUtil.ExcelExportData)org.takeback.util.converter.ConversionUtils.convert(r, ExcelUtil.ExcelExportData.class);
    try
    {
      fOut.write(org.takeback.util.export.ExcelUtil.export2Stream(setInfo).toByteArray());
      fOut.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public Map ParameterMap2Map(Map properties) {
    Map returnMap = new HashMap();
    Iterator entries = properties.entrySet().iterator();
    
    String name = "";
    String value = "";
    while (entries.hasNext()) {
      Map.Entry entry = (Map.Entry)entries.next();
      name = (String)entry.getKey();
      Object valueObj = entry.getValue();
      if (null == valueObj) {
        value = "";
      } else if ((valueObj instanceof String[])) {
        String[] values = (String[])valueObj;
        for (int i = 0; i < values.length; i++) {
          value = values[i] + ",";
        }
        value = value.substring(0, value.length() - 1);
      } else {
        value = valueObj.toString();
      }
      returnMap.put(name, value);
    }
    return returnMap;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\ResourceController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */