package org.takeback.chat.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takeback.core.service.MyListService;
import org.takeback.dao.BaseDAO;
import org.takeback.util.converter.ConversionUtils;
import org.takeback.util.exception.CodedBaseRuntimeException;
import org.takeback.util.exp.ExpressionProcessor;

@Service("inoutReportService")
public class InoutReportService
  extends MyListService
{
  @Transactional(readOnly=true)
  public Map<String, Object> list(Map<String, Object> req)
  {
    String entityName = (String)req.get(ENTITYNAME);
    if (StringUtils.isEmpty(entityName)) {
      throw new CodedBaseRuntimeException(404, "missing entityName");
    }
    int limit = ((Integer)req.get(LIMIT)).intValue();
    int page = ((Integer)req.get(PAGE)).intValue();
    List<?> cnd = (List)ConversionUtils.convert(req.get(CND), List.class);
    String filter = null;
    if (cnd != null) {
      filter = ExpressionProcessor.instance().toString(cnd);
    }
    String orderInfo = (String)req.get(ORDERINFO);
    List<?> ls = this.dao.query(entityName, filter, limit, page, orderInfo);
    afterList(ls);
    long count = this.dao.totalSize(entityName, filter);
    Map<String, Object> result = new HashMap();
    result.put("totalSize", Long.valueOf(count));
    result.put("body", ls);
    return result;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\service\admin\InoutReportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */