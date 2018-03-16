package org.takeback.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.takeback.chat.service.RedService;

@Controller("redController")
@RequestMapping({"/red"})
public class RedController
{
  @Autowired
  private RedService redService;
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\controller\RedController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */