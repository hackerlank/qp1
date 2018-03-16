package org.takeback.core.dictionary;

import java.io.Serializable;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;



















public class CodeRule
  implements Serializable
{
  private String codeDefine;
  private int[] layerLens;
  private String[] define;
  private HashMap<Integer, Integer> layersMapping;
  private int count;
  private int maxLen;
  
  public CodeRule(String cd)
  {
    this.codeDefine = cd;
    this.define = cd.split(",");
    this.count = this.define.length;
    this.layerLens = new int[this.count];
    this.layersMapping = new HashMap();
    int i = 0;
    for (String s : this.define) {
      int len = Integer.parseInt(s);
      this.maxLen += len;
      if (i == 0) {
        this.layerLens[i] = len;
        this.layersMapping.put(Integer.valueOf(len), Integer.valueOf(i));
      }
      else {
        this.layerLens[i] = (this.layerLens[(i - 1)] + len);
        this.layersMapping.put(Integer.valueOf(this.layerLens[(i - 1)] + len), Integer.valueOf(i));
      }
      i++;
    }
  }
  
  public int getLayerCount() {
    return this.count;
  }
  
  public int getLayerLength(int i) {
    if (i < this.count) {
      return this.layerLens[i];
    }
    return 0;
  }
  
  public boolean isLeaf(String key) {
    return key.length() == this.maxLen;
  }
  
  public int indexOfLayer(String key) {
    if (StringUtils.isEmpty(key)) {
      return -1;
    }
    int keySize = key.length();
    if (this.layersMapping.containsKey(Integer.valueOf(keySize))) {
      return ((Integer)this.layersMapping.get(Integer.valueOf(keySize))).intValue();
    }
    return -1;
  }
  
  public String getParentKey(String key) {
    if (StringUtils.isEmpty(key)) {
      return "";
    }
    int index = indexOfLayer(key);
    if (index < 1) {
      return "";
    }
    index--;
    return key.substring(0, this.layerLens[index]);
  }
  
  public int getNextLength(int length) {
    for (int i = 0; i < this.layerLens.length; i++) {
      if (this.layerLens[i] == length) {
        if (i == this.layerLens.length - 1) {
          return this.layerLens[i];
        }
        return this.layerLens[(i + 1)];
      }
    }
    return -1;
  }
  
  public int getParentLength(int length) {
    for (int i = 0; i < this.layerLens.length; i++) {
      if (this.layerLens[i] == length) {
        if (i == 0) {
          return this.layerLens[i];
        }
        return this.layerLens[(i - 1)];
      }
    }
    return -1;
  }
  
  public String toString() {
    return this.codeDefine;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\CodeRule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */