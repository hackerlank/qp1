package org.takeback.chat.entity;

import java.util.Date;
import java.util.UUID;

public class Message
{
  private String msgId;
  private String type = "TXT";
  private Integer sender;
  private String nickName;
  private String headImg;
  private Object content;
  private Date msgTime;
  private String cmd;
  public static final String TXT = "TXT";
  public static final String RED = "RED";
  public static final String IMG = "IMG";
  public static final String VOC = "VOC";
  public static final String ORD = "ORD";
  public static final String CMD = "CMD";
  public static final String TXT_SYS = "TXT_SYS";
  public static final String TXT_ALERT = "TXT_ALERT";
  public static final String RED_SYS = "RED_SYS";
  public static final String PC_MSG = "PC_MSG";
  
  public Message()
  {
    this.msgId = UUID.randomUUID().toString().replace("-", "");
  }
  
  public Message(String type, Integer sender, Object content) {
    this();
    
    this.sender = sender;
    this.content = content;
  }
  
  public Message(String cmd, Object cmdContent) {
    this("ORD", Integer.valueOf(0), cmdContent);
    this.cmd = cmd;
  }
  
  public String getMsgId() {
    return this.msgId;
  }
  
  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }
  
  public String getType() {
    return this.type;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  public Integer getSender() {
    return this.sender;
  }
  
  public void setSender(Integer sender) {
    this.sender = sender;
  }
  
  public String getNickName() {
    return this.nickName;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
  
  public String getHeadImg() {
    return this.headImg;
  }
  
  public void setHeadImg(String headImg) {
    this.headImg = headImg;
  }
  
  public Object getContent() {
    return this.content;
  }
  
  public void setContent(Object content) {
    this.content = content;
  }
  
  public Date getMsgTime() {
    if (this.msgTime == null) {
      this.msgTime = new Date();
    }
    return this.msgTime;
  }
  
  public void setMsgTime(Date msgTime) {
    this.msgTime = msgTime;
  }
  
  public String getCmd() {
    return this.cmd;
  }
  
  public void setCmd(String cmd) {
    this.cmd = cmd;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\entity\Message.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */