package org.takeback.mvc.controller.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryController;
import org.takeback.core.dictionary.DictionaryItem;

@RestController
public class DicLocator
{
  @org.springframework.web.bind.annotation.RequestMapping(value={"/**/{id}.dc"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public List<DictionaryItem> get(@PathVariable String id, @RequestParam(required=false) String node, @RequestParam(required=false, defaultValue="3") Integer sliceType, @RequestParam(required=false) String query, @RequestParam(value="filter", required=false) String filter)
  {
    Dictionary dic = (Dictionary)DictionaryController.instance().get(id);
    if (dic == null) {
      return null;
    }
    if (("root".equals(node)) || ("0".equals(node))) {
      node = null;
    }
    List<DictionaryItem> items = dic.getSlice(node, sliceType.intValue(), query);
    if (!StringUtils.isEmpty(filter)) {
      Map<String, String> map = (Map)org.takeback.util.JSONUtils.parse(filter, Map.class);
      List<DictionaryItem> filterItems = new ArrayList();
      for (DictionaryItem item : items) {
        boolean fit = true;
        for (String k : map.keySet()) {
          String v = (String)map.get(k);
          if (!v.equals(item.getProperty(k))) {
            fit = false;
            break;
          }
        }
        if (fit) {
          filterItems.add(item);
        }
      }
      return filterItems;
    }
    return items;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\mvc\controller\core\DicLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */