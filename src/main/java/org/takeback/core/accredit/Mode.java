package org.takeback.core.accredit;

import java.io.PrintStream;
import java.io.Serializable;

public class Mode implements Serializable
{
  private static final long serialVersionUID = 8406591497388596746L;
  public static final int ACCESSIBLE_FLAG = 1;
  public static final int CREATEABLE_FLAG = 2;
  public static final int UPDATEABLE_FLAG = 4;
  public static final int REMOVEABLE_FLAG = 8;
  public static final Mode FullAccessMode = new Mode(15);
  public static final Mode NoneAccessMode = new Mode(0);
  private int value;
  private boolean accessible;
  private boolean creatable;
  private boolean updatable;
  private boolean removable;
  
  public static Mode parseFromInt(int v)
  {
    if (v == 0) {
      return NoneAccessMode;
    }
    if ((v & 0xF) == 15) {
      return FullAccessMode;
    }
    
    Mode mode = new Mode();
    mode.setValue(Integer.valueOf(v));
    return mode;
  }
  

  public Mode() {}
  

  public Mode(int v)
  {
    setValue(Integer.valueOf(v));
  }
  
  public Mode(String v) {
    setValue(Integer.valueOf(v, 2));
  }
  
  public Integer getValue() {
    return Integer.valueOf(this.value);
  }
  
  public void setValue(Integer v) {
    this.value = v.intValue();
    this.accessible = ((this.value & 0x1) == 1);
    this.creatable = ((this.value & 0x2) == 2);
    this.updatable = ((this.value & 0x4) == 4);
    this.removable = ((this.value & 0x8) == 8);
  }
  
  public boolean isAccessible() {
    return this.accessible;
  }
  
  public boolean isCreatable() {
    return this.creatable;
  }
  
  public boolean isUpdatable() {
    return this.updatable;
  }
  
  public boolean isRemovable() {
    return this.removable;
  }
  
  public boolean equals(Object o)
  {
    if (!o.getClass().equals(getClass())) {
      return false;
    }
    return ((Mode)o).getValue().equals(Integer.valueOf(this.value));
  }
  
  public static void main(String[] args) {
    System.out.println(FullAccessMode.getValue());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\accredit\Mode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */