package org.takeback.core.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;
import org.takeback.util.StringValueParser;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exp.ExpressionProcessor;

public class SchemaItem
  implements Serializable
{
  private static final long serialVersionUID = 4557229734515290036L;
  private String id;
  private String name;
  private boolean pkey;
  private String strategy = "identity";
  private String type;
  private DictionaryIndicator dic;
  private Object defaultValue;
  private Integer length;
  private Integer precision;
  private Object maxValue;
  private Object minValue;
  private boolean allowBlank = true;
  private List<?> exp;
  private int displayMode = 3;
  private boolean hidden;
  private boolean update = true;
  
  private HashMap<String, Object> properties;
  

  public SchemaItem() {}
  

  public SchemaItem(String id)
  {
    this.id = id;
  }
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public Boolean isCodedValue() {
    return Boolean.valueOf(this.dic != null);
  }
  
  public Boolean isEvalValue() {
    return Boolean.valueOf(this.exp != null);
  }
  
  public boolean isAllowBlank() {
    return this.allowBlank;
  }
  
  public void setAllowBlank(boolean allowBlank) {
    this.allowBlank = allowBlank;
  }
  
  public String getType() {
    if (StringUtils.isEmpty(this.type)) {
      return "string";
    }
    return this.type;
  }
  
  private Class<?> getTypeClass() {
    return DataTypes.getTypeClass(getType());
  }
  
  public void setType(String type) {
    if (!DataTypes.isSupportType(type)) {
      throw new IllegalArgumentException("type[" + type + "] is unsupported");
    }
    this.type = StringUtils.uncapitalize(type);
  }
  
  public void setExp(List<Object> exp) {
    this.exp = exp;
  }
  
  public Integer getDisplayMode() {
    return Integer.valueOf(this.displayMode);
  }
  
  public void setDisplayMode(Integer displayMode) {
    this.displayMode = displayMode.intValue();
  }
  
  public void setDisplay(Integer displayMode) {
    setDisplayMode(displayMode);
  }
  
  public Integer getDisplay() {
    return getDisplayMode();
  }
  
  public DictionaryIndicator getDic() {
    return this.dic;
  }
  
  public void setDic(DictionaryIndicator dic) {
    this.dic = dic;
  }
  
  public boolean isPkey() {
    return this.pkey;
  }
  
  public void setPkey(boolean pkey) {
    this.pkey = pkey;
  }
  
  public String getStrategy() {
    return this.strategy;
  }
  
  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }
  
  public Integer getLength() {
    return this.length;
  }
  
  public void setLength(Integer length) {
    this.length = length;
  }
  
  public Integer getPrecision() {
    return this.precision;
  }
  
  public void setPrecision(Integer precision) {
    this.precision = precision;
  }
  
  public Object getDefaultValue() {
    if (this.defaultValue == null) {
      return this.defaultValue;
    }
    if (isCodedValue().booleanValue()) {
      HashMap<String, String> obj = new HashMap();
      String key = (String)ConversionUtils.convert(parseConfigValue(this.defaultValue), String.class);
      String text = toDisplayValue(key);
      obj.put("key", key);
      obj.put("text", text);
      return obj;
    }
    return parseConfigValue(this.defaultValue);
  }
  
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  public Object getMaxValue() {
    return parseConfigValue(this.maxValue);
  }
  
  public void setMaxValue(Object maxValue) {
    this.maxValue = maxValue;
  }
  
  public Object getMinValue() {
    return parseConfigValue(this.minValue);
  }
  
  public void setMinValue(Object minValue) {
    this.minValue = minValue;
  }
  
  private Object parseConfigValue(Object v) {
    Object val = v;
    if ((v instanceof String)) {
      val = StringValueParser.parse((String)v, getTypeClass());
    }
    else {
      val = ConversionUtils.convert(v, getTypeClass());
    }
    return val;
  }
  
  public String toDisplayValue(Object v) {
    String key = (String)ConversionUtils.convert(v, String.class);
    if ((isCodedValue().booleanValue()) && (!StringUtils.isEmpty(key))) {
      Dictionary d = (Dictionary)DictionaryController.instance().get(this.dic.getId());
      String text = "";
      if (key.indexOf(",") == -1) {
        text = d.getText(key);
      } else {
        String[] keys = key.split(",");
        StringBuffer sb = new StringBuffer();
        for (String s : keys) {
          sb.append(",").append(d.getText(s));
        }
        text = sb.substring(1);
      }
      return text;
    }
    return key;
  }
  
  public Object toPersistValue(Object source) {
    return DataTypes.toTypeValue(getType(), source);
  }
  
  public Object eval() {
    if (!isEvalValue().booleanValue()) {
      return null;
    }
    return toPersistValue(ExpressionProcessor.instance().run(this.exp));
  }
  
  public Object eval(String lang) {
    if (this.exp != null) {
      return toPersistValue(ExpressionProcessor.instance(lang).run(this.exp));
    }
    return null;
  }
  
  public void setProperty(String nm, Object v) {
    if (this.properties == null) {
      this.properties = new HashMap();
    }
    this.properties.put(nm, v);
  }
  
  public Object getProperty(String nm) {
    if (this.properties == null) {
      return null;
    }
    return this.properties.get(nm);
  }
  
  public HashMap<String, Object> getProperties() {
    if ((this.properties != null) && (this.properties.isEmpty())) {
      return null;
    }
    return this.properties;
  }
  
  public boolean hasProperty(String nm) {
    if (this.properties != null) {
      return this.properties.containsKey(nm);
    }
    return false;
  }
  
  public void setUpdate(boolean canUpdate) {
    this.update = canUpdate;
  }
  
  public boolean isUpdate() {
    if ((this.pkey) || (!this.update)) {
      return false;
    }
    return true;
  }
  
  public boolean isHidden() {
    return this.hidden;
  }
  
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\schema\SchemaItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */