package org.takeback.util.task.schedule;

import java.io.PrintStream;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class Task
  implements Job
{
  public void execute(JobExecutionContext jobExecutionContext)
    throws JobExecutionException
  {
    System.out.println(new DateTime().toLocalDateTime());
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\task\schedule\Task.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */