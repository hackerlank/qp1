package org.takeback.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;

public class MyObjectMapper extends ObjectMapper
{
  public MyObjectMapper()
  {
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\MyObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */