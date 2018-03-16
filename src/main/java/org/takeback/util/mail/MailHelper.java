package org.takeback.util.mail;

import java.io.PrintStream;
import java.util.Date;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.takeback.util.exception.CodedBaseRuntimeException;


public class MailHelper
{
  @Autowired
  private JavaMailSenderImpl mailSender;
  private static MailHelper mailHelper;
  
  private MailHelper()
  {
    mailHelper = this;
  }
  
  public static MailHelper instance() {
    return mailHelper;
  }
  


  public void sendHtmlMail(String title, String content, String receiver)
  {
    sendHtmlMail(new Mail(title, content, receiver));
  }
  
  public void sendHtmlMail(Mail mail) {
    if (this.mailSender == null) {
      throw new IllegalArgumentException("mailSender is null, did not define this in spring as a bean ?");
    }
    
    MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
    try {
      mimeMessageHelper.setSentDate(new Date());
      mimeMessageHelper.setFrom(this.mailSender.getUsername());
      mimeMessageHelper.setTo(mail.getReceiver());
      mimeMessageHelper.setSubject(mail.getTitle());
      mimeMessageHelper.setText(mail.getContent(), true);
      this.mailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new CodedBaseRuntimeException("send mail to " + mail.getReceiver() + "failed");
    }
  }
  




  public void send(Mail mail)
  {
    System.out.println(this.mailSender);
    if (this.mailSender == null) {
      throw new IllegalArgumentException("mailSender is null, did not define this in spring as a bean ?");
    }
    SimpleMailMessage smm = new SimpleMailMessage();
    smm.setSentDate(new Date());
    smm.setFrom(this.mailSender.getUsername());
    smm.setTo(mail.getReceiver());
    smm.setSubject(mail.getTitle());
    smm.setText(mail.getContent());
    this.mailSender.send(smm);
  }
  
  public void send(String title, String content, String receiver) {
    send(new Mail(title, content, receiver));
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\mail\MailHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */