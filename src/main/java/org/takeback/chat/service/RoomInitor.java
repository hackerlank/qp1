package org.takeback.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;





@Component
public class RoomInitor
  implements ApplicationContextAware
{
  @Autowired
  RoomService service;
  
  @Transactional(rollbackFor={Throwable.class})
  public void setApplicationContext(ApplicationContext var1)
  {
    this.service.initRoomStatus();
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\RoomInitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */