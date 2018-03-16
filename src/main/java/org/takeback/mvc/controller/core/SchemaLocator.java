package org.takeback.mvc.controller.core;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.takeback.core.schema.Schema;


@RestController
public class SchemaLocator
{
  @RequestMapping(value={"/**/{id}.sc"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public Schema get(@PathVariable String id)
  {
    return null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\core\SchemaLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */