package org.takeback.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileAuthPassport
{
  MobileAuthPassportType value() default MobileAuthPassportType.APP;
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\annotation\MobileAuthPassport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */