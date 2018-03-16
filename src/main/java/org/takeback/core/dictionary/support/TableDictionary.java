package org.takeback.core.dictionary.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.takeback.core.dictionary.CodeRule;
import org.takeback.core.dictionary.Dictionary;
import org.takeback.core.dictionary.DictionaryItem;
import org.takeback.util.ApplicationContextHolder;
import org.takeback.util.exp.ExpressionProcessor;
import org.takeback.util.exp.exception.ExprException;

public class TableDictionary extends Dictionary
{
  private static final long serialVersionUID = -997610255379902104L;
  private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TableDictionary.class);
  protected CodeRule codeRule;
  protected String parent;
  protected String entityName;
  protected String keyField = "id";
  protected String textField = "text";
  protected String sortField;
  protected boolean ignoreSearchFieldExPrefix;
  protected String filter;
  protected String where = "";
  protected String iconCls;
  protected boolean supportRemote = true;
  protected String queryType = "Query";
  protected String searchFieldType = "string";
  protected HashSet<String> folders;
  protected LinkedHashMap<String, String> propFields;
  protected boolean distinct = false;
  
  private String sessionFactory = "sessionFactory";
  
  public void init()
  {
    if ((!this.queryOnly) && (this.supportRemote)) {
      List<DictionaryItem> ls = initAllItems();
      for (DictionaryItem di : ls) {
        addItem(di);
      }
      if (!StringUtils.isEmpty(this.parent)) {
        initNodeToFolder(ls);
      }
    }
  }
  
  public List<DictionaryItem> initAllItems() {
    List<DictionaryItem> ls = new ArrayList();
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      String sql = spellSql();
      Query q = createQuery(ss, sql);
      List<Object[]> records = q.list();
      for (it = records.iterator(); it.hasNext();) {
        Object[] r = (Object[])it.next();
        DictionaryItem dictionaryItem = parseDicItem(r);
        ls.add(dictionaryItem);
      }
    } catch (Exception e) { Iterator<Object[]> it;
      LOGGER.error("init dic[{}] items failed, db error \r ", this.id, e);
    } finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return ls;
  }
  
  public List<DictionaryItem> getSlice(String parentKey, int sliceType, String query)
  {
    if (StringUtils.isEmpty(query)) {
      switch (sliceType) {
      case 0: 
        return getAllItems(parentKey);
      case 1: 
        return getAllLeaf(parentKey);
      case 3: 
        return getAllChild(parentKey);
      }
      return null;
    }
    
    return query(parentKey, query);
  }
  








  public List<DictionaryItem> query(String parentKey, String qs)
  {
    List<DictionaryItem> ls = new ArrayList();
    String curSF = this.ignoreSearchFieldExPrefix ? this.searchFieldEx : this.searchField;
    qs = qs.toLowerCase();
    if (qs.charAt(0) == this.searchKeySymbol) {
      curSF = this.keyField;
      qs = qs.substring(1);
    }
    else if (qs.charAt(0) == this.searchExSymbol) {
      curSF = this.searchFieldEx;
      qs = qs.substring(1);
    }
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      StringBuffer condition = new StringBuffer("lower(").append(curSF).append(")");
      if (qs.startsWith("=")) {
        qs = qs.substring(1);
        if ((curSF.equals(this.searchField)) && (org.takeback.core.schema.DataTypes.isNumberType(this.searchFieldType))) {
          condition = new StringBuffer(curSF).append("=").append(qs);
        } else {
          condition.append("='").append(qs).append("'");
        }
      }
      else {
        condition.append(" like '").append(qs).append("%'");
      }
      if (!StringUtils.isEmpty(parentKey))
      {
        condition.append(" and substring(").append(this.searchField).append(",1,").append(parentKey.length()).append(")='").append(parentKey).append("'");
      }
      String sql = spellSql(condition.toString());
      Query q = createQuery(ss, sql);
      List<Object[]> records = q.list();
      int rowCount = records.size();
      for (int i = 0; i < rowCount; i++) {
        Object[] r = (Object[])records.get(i);
        DictionaryItem di = parseDicItem(r);
        ls.add(di);
      }
    }
    catch (Exception e) {
      LOGGER.error("query failed from table dic:{}", this.id, e);
    }
    finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return ls;
  }
  
  protected List<DictionaryItem> getAllItemsFromCacheList(String parentKey) {
    List<DictionaryItem> list = itemsList();
    if (StringUtils.isEmpty(parentKey)) {
      return list;
    }
    List<DictionaryItem> ls = new ArrayList();
    for (DictionaryItem di : list) {
      String p = (String)di.getProperty("parent");
      if ((!StringUtils.isEmpty(p)) && (p.contains(parentKey))) {
        ls.add(di);
      }
    }
    return ls;
  }
  
  protected List<DictionaryItem> getAllChildFromCacheList(String parentKey) {
    return getAllChildFromCacheList(parentKey, itemsList());
  }
  
  protected List<DictionaryItem> getAllChildFromCacheList(String parentKey, List<DictionaryItem> itemList) {
    if ((this.codeRule == null) && (StringUtils.isEmpty(this.parent))) {
      return itemList;
    }
    List<DictionaryItem> ls = new ArrayList();
    int firstCodeLength; if (StringUtils.isEmpty(parentKey)) {
      if (this.codeRule != null) {
        firstCodeLength = this.codeRule.getLayerLength(0);
        for (DictionaryItem di : itemList) {
          if (di.getKey().length() == firstCodeLength) {
            ls.add(di);
          }
        }
      }
      else if (!StringUtils.isEmpty(this.parent)) {
        for (DictionaryItem di : itemList) {
          String p = (String)di.getProperty("parent");
          if ((StringUtils.isEmpty(p)) || (getItem(p) == null)) {
            ls.add(di);
          }
        }
      }
      
      return ls;
    }
    for (DictionaryItem di : itemList) {
      String p = (String)di.getProperty("parent");
      if ((!StringUtils.isEmpty(p)) && (parentKey.equals(p))) {
        ls.add(di);
      }
    }
    return ls;
  }
  
  protected List<DictionaryItem> getAllLeafFromCacheList(String parentKey) {
    List<DictionaryItem> list = itemsList();
    if (StringUtils.isEmpty(parentKey)) {
      return list;
    }
    List<DictionaryItem> ls = new ArrayList();
    for (DictionaryItem di : list) {
      String p = (String)di.getProperty("parent");
      String f = (String)di.getProperty("folder");
      if ((StringUtils.isEmpty(f)) && (!StringUtils.isEmpty(p)) && (p.contains(parentKey))) {
        ls.add(di);
      }
    }
    return ls;
  }
  





  protected List<DictionaryItem> getAllLeaf(String parentKey)
  {
    if (!this.queryOnly) {
      checkItems();
      return getAllLeafFromCacheList(parentKey);
    }
    return initAllItems();
  }
  
  protected List<DictionaryItem> getAllItems(String parentKey) {
    if (!this.queryOnly) {
      checkItems();
      return getAllItemsFromCacheList(parentKey);
    }
    List<DictionaryItem> ls = new ArrayList();
    if (this.codeRule == null) {
      return ls;
    }
    if (StringUtils.isEmpty(parentKey)) {
      ls.addAll(initAllItems());
    }
    else {
      int curLayer = this.codeRule.indexOfLayer(parentKey);
      if ((curLayer == -1) || (curLayer == this.codeRule.getLayerCount() - 1)) {
        return ls;
      }
      int curLen = this.codeRule.getLayerLength(curLayer);
      String condition = "substring(" + this.keyField + ",1,:curLen)=:parentKey";
      String sql = spellSql(condition);
      SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
      Session ss = null;
      try {
        ss = sf.openSession();
        Query q = createQuery(ss, sql);
        q.setInteger("curLen", curLen);
        q.setString("parentKey", parentKey);
        List<Object[]> records = q.list();
        for (Object[] r : records) {
          ls.add(parseDicItem(r));
        }
      }
      catch (Exception e) {
        LOGGER.error("Get dictionary {} item failed.", this.id, e);
      }
      finally {
        if (ss.isOpen()) {
          ss.close();
        }
      }
    }
    
    return ls;
  }
  
  protected List<DictionaryItem> getAllChild(String parentKey) {
    if (!this.queryOnly) {
      checkItems();
      return getAllChildFromCacheList(parentKey);
    }
    if (this.codeRule == null) {
      if (!StringUtils.isEmpty(this.parent)) {
        if (StringUtils.isEmpty(parentKey)) {
          synchronized (this) {
            List<DictionaryItem> itemList = initAllItems();
            initNodeToFolder(itemList);
            List<DictionaryItem> ls = getAllChildFromCacheList(parentKey, itemList);
            return ls;
          }
        }
        return getItemsFromDBByParentKey(parentKey);
      }
      
      return initAllItems();
    }
    List<DictionaryItem> ls = new ArrayList();
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = null;
      String sql; if (StringUtils.isEmpty(parentKey)) {
        String condition = "length(" + this.keyField + ")=:len";
        sql = spellSql(condition);
        q = createQuery(ss, sql);
        q.setInteger("len", this.codeRule.getLayerLength(0));
      }
      else {
        int curLayer = this.codeRule.indexOfLayer(parentKey);
        if ((curLayer == -1) || (curLayer == this.codeRule.getLayerCount() - 1)) {
          return ls;
        }
        curLen = this.codeRule.getLayerLength(curLayer);
        int nextLen = this.codeRule.getLayerLength(curLayer + 1);
        
        String condition = "length(" + this.keyField + ")=:nextLen and substring(" + this.keyField + ",1,:curLen)=:parentKey";
        String sql = spellSql(condition);
        q = createQuery(ss, sql);
        q.setInteger("curLen", curLen);
        q.setInteger("nextLen", nextLen);
        q.setString("parentKey", parentKey);
      }
      List<Object[]> records = q.list();
      for (Object[] r : records) {
        ls.add(parseDicItem(r));
      }
    } catch (Exception e) {
      int curLen;
      LOGGER.error("Failed to initialize dictionary {}.", this.id, e);
    }
    finally {
      if (ss.isOpen()) {
        ss.close();
      }
    }
    return ls;
  }
  
  protected String spellSql() {
    return spellSql(null);
  }
  
  protected String spellSql(String condition) {
    StringBuffer props = new StringBuffer();
    if (this.propFields != null) {
      Set<String> fields = this.propFields.keySet();
      for (Object fld : fields) {
        props.append(",").append(fld);
      }
    }
    StringBuffer sql = new StringBuffer("select ").append(this.distinct ? "distinct " : "");
    sql.append(this.keyField).append(",").append(this.textField).append(props);
    if (!StringUtils.isEmpty(this.parent)) {
      sql.append(",").append(this.parent);
    }
    sql.append(" from ").append(this.entityName);
    if (this.queryOnly) {
      try {
        setFilter(this.filter);
      }
      catch (ExprException localExprException1) {}
    }
    
    String cnd = this.where;
    if (!StringUtils.isEmpty(condition)) {
      cnd = this.where + " and ";
      cnd = cnd + condition;
    }
    sql.append(cnd);
    if (this.sortField != null) {
      sql.append(" order by ").append(this.sortField);
    }
    return sql.toString();
  }
  
  protected DictionaryItem parseDicItem(Object[] r) {
    String key = String.valueOf(r[0]);
    String text = String.valueOf(r[1]);
    DictionaryItem dictionaryItem = new DictionaryItem(key, text);
    int i; if (this.propFields != null) {
      Set<String> ps = this.propFields.keySet();
      i = 2;
      for (String p : ps) {
        if (r[i] != null) {
          dictionaryItem.setProperty(StringUtils.isEmpty((CharSequence)this.propFields.get(p)) ? p : (String)this.propFields.get(p), r[i]);
        }
        i++;
      }
    }
    if (!StringUtils.isEmpty(this.iconCls)) {
      dictionaryItem.setProperty("iconCls", this.iconCls);
    }
    if (this.codeRule != null) {
      String parentKey = this.codeRule.getParentKey(key);
      dictionaryItem.setProperty("parent", parentKey);
      if (this.codeRule.isLeaf(key)) {
        dictionaryItem.setLeaf(true);
      }
    }
    else if (!StringUtils.isEmpty(this.parent)) {
      String parentKey = r[(r.length - 1)] == null ? null : String.valueOf(r[(r.length - 1)]);
      if (!StringUtils.isEmpty(parentKey)) {
        dictionaryItem.setProperty("parent", parentKey);
      }
    } else {
      dictionaryItem.setLeaf(true);
    }
    
    return dictionaryItem;
  }
  

















  public List<DictionaryItem> getItemFromDBByText(String text)
  {
    List<DictionaryItem> dicItemList = null;
    String con = this.textField + "=:text";
    String sql = spellSql(con);
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = createQuery(ss, sql);
      q.setString("text", text);
      List<Object[]> l = q.list();
      if (l.size() > 0) {
        dicItemList = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
          dicItemList.add(parseDicItem((Object[])l.get(i)));
        }
      }
    } catch (Exception e) {
      LOGGER.error("get {} dicItem by text[" + text + "] failed.", this.id, e);
    } finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return dicItemList;
  }
  
  public DictionaryItem getItemFromDB(String key) {
    DictionaryItem dictionaryItem = null;
    String con = this.keyField + "=:key";
    String sql = spellSql(con);
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = createQuery(ss, sql);
      q.setString("key", key);
      List<Object[]> l = q.list();
      if (l.size() > 0) {
        dictionaryItem = parseDicItem((Object[])l.get(0));
      }
    } catch (Exception e) {
      LOGGER.error("get {} dicItem by key[" + key + "] failed.", this.id, e);
    } finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return dictionaryItem;
  }
  
  public List<DictionaryItem> getItemsFromDB(String keys) {
    List<DictionaryItem> list = new ArrayList();
    String con = this.keyField + " in (" + keys + ")";
    String sql = spellSql(con);
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = createQuery(ss, sql);
      List<Object[]> l = q.list();
      if (l.size() > 0) {
        for (Object[] o : l) {
          DictionaryItem dictionaryItem = parseDicItem(o);
          list.add(dictionaryItem);
        }
      }
    } catch (Exception e) {
      LOGGER.error("get {} dicItems by keys[" + keys + "] failed.", this.id, e);
    } finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return list;
  }
  
  public List<DictionaryItem> getItemsFromDBByParentKey(String parentKey) {
    List<DictionaryItem> list = new ArrayList();
    String p = this.parent;
    int index = this.parent.toLowerCase().lastIndexOf(" as ");
    if (index > -1) {
      p = this.parent.substring(0, index);
    }
    String con = p + "='" + parentKey + "'";
    String sql = spellSql(con);
    SessionFactory sf = (SessionFactory)ApplicationContextHolder.getBean(this.sessionFactory, SessionFactory.class);
    Session ss = null;
    try {
      ss = sf.openSession();
      Query q = createQuery(ss, sql);
      List<Object[]> l = q.list();
      if (l.size() > 0) {
        for (Object[] o : l) {
          DictionaryItem dictionaryItem = parseDicItem(o);
          
          if (!this.folders.contains(dictionaryItem.getKey())) {
            dictionaryItem.setLeaf(true);
          }
          list.add(dictionaryItem);
        }
      }
    } catch (Exception e) {
      LOGGER.error("get {} dicItems by parentKey[" + parentKey + "] failed.", this.id, e);
    } finally {
      if ((ss != null) && (ss.isOpen())) {
        ss.close();
      }
    }
    return list;
  }
  
  private Query createQuery(Session ss, String q) {
    if ("SQLQuery".equals(this.queryType)) {
      return ss.createSQLQuery(q);
    }
    return ss.createQuery(q);
  }
  

  public List<String> getKey(String text)
  {
    if (!this.queryOnly) {
      checkItems();
      return super.getKey(text);
    }
    List<String> list = new ArrayList();
    List<DictionaryItem> li = getItemFromDBByText(text);
    if (li != null) {
      for (DictionaryItem d : li) {
        list.add(d.getKey());
      }
    }
    return list;
  }
  

  public String getText(String key)
  {
    if (!this.queryOnly) {
      DictionaryItem di = getItem(key);
      return di == null ? "" : di.getText();
    }
    DictionaryItem di = getItemFromDB(key);
    if (di != null) {
      return di.getText();
    }
    return "";
  }
  
  public String getWholeText(String key, int includeParentMinLen)
  {
    StringBuffer text = new StringBuffer();
    StringBuffer sb = new StringBuffer();
    sb.append(",'").append(key).append("'");
    String pkey = this.codeRule.getParentKey(key);
    while ((!StringUtils.isEmpty(pkey)) && (pkey.length() >= includeParentMinLen)) {
      sb.append(",'").append(pkey).append("'");
      pkey = this.codeRule.getParentKey(pkey);
    }
    List<DictionaryItem> list = getItemsFromDB(sb.substring(1));
    java.util.Collections.sort(list, new java.util.Comparator() {
      public int compare(DictionaryItem d1, DictionaryItem d2) {
        if (d1.getKey().length() > d2.getKey().length()) {
          return 1;
        }
        return -1;
      }
    });
    for (int i = 0; i < list.size(); i++) {
      DictionaryItem di = (DictionaryItem)list.get(i);
      if (i + 1 < list.size()) {
        DictionaryItem dd = (DictionaryItem)list.get(i + 1);
        if (dd.getText().contains(di.getText())) {
          di.setProperty("___removeText___", "1");
        }
      }
    }
    for (int i = 0; i < list.size(); i++) {
      DictionaryItem di = (DictionaryItem)list.get(i);
      if (!di.getProperty("___removeText___").equals("1")) {
        text.append(di.getText());
      }
    }
    return text.toString();
  }
  
  public boolean hasCodeRule() {
    return this.codeRule != null;
  }
  
  private void initNodeToFolder(List<DictionaryItem> list) {
    this.folders = new HashSet();
    for (Iterator<DictionaryItem> it = list.iterator(); it.hasNext();) {
      DictionaryItem di = (DictionaryItem)it.next();
      String p = (String)di.getProperty("parent");
      DictionaryItem pdi = getItem(p);
      if (pdi != null) {
        this.folders.add(pdi.getKey());
      }
    }
    for (DictionaryItem dictionaryItem : list) {
      if (!this.folders.contains(dictionaryItem.getKey())) {
        dictionaryItem.setLeaf(true);
      }
    }
  }
  
  public DictionaryItem getItem(String key)
  {
    if (!this.queryOnly) {
      checkItems();
      return super.getItem(key);
    }
    return getItemFromDB(key);
  }
  
  private void checkItems()
  {
    if ((!this.supportRemote) && (getItems().size() == 0)) {
      List<DictionaryItem> ls = initAllItems();
      for (DictionaryItem di : ls) {
        addItem(di);
      }
      if (!StringUtils.isEmpty(this.parent)) {
        initNodeToFolder(ls);
      }
    }
  }
  
  public void setParent(String parent) {
    this.parent = parent;
  }
  
  public void setSearchFieldType(String searchFieldType) {
    this.searchFieldType = searchFieldType;
  }
  
  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }
  
  public String getEntityName() {
    return this.entityName;
  }
  
  public String getParent() { return this.parent; }
  
  public String getKeyField()
  {
    return this.keyField;
  }
  
  public String getTextField() {
    return this.textField;
  }
  
  public String getSortField() {
    return this.sortField;
  }
  
  public String getIconCls() {
    return this.iconCls;
  }
  
  public String getFilter() {
    return this.filter;
  }
  
  @Deprecated
  public void setEntry(String entry) {
    setEntityName(entry);
  }
  
  @Deprecated
  public String getEntry() {
    return getEntityName();
  }
  
  public void setKeyField(String keyField) {
    this.keyField = keyField;
  }
  
  public void setTextField(String textField) {
    this.textField = textField;
  }
  
  public void setSortField(String sortField) {
    this.sortField = sortField;
  }
  
  public void setIgnoreSearchFieldExPrefix(boolean ignoreSearchFieldExPrefix) {
    this.ignoreSearchFieldExPrefix = ignoreSearchFieldExPrefix;
  }
  
  public void setIconCls(String iconCls) {
    this.iconCls = iconCls;
  }
  
  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }
  
  public boolean isSupportRemote() {
    return this.supportRemote;
  }
  
  public void setSupportRemote(boolean supportRemote) {
    this.supportRemote = supportRemote;
  }
  
  public boolean isDistinct() {
    return this.distinct;
  }
  
  public void setDistinct(boolean distinct) {
    this.distinct = distinct;
  }
  
  public void setCodeRule(String sCodeRule) {
    if (!StringUtils.isEmpty(sCodeRule)) {
      this.codeRule = new CodeRule(sCodeRule);
    }
  }
  
  public void setFilter(String filter) throws ExprException {
    if (!StringUtils.isEmpty(filter)) {
      this.filter = filter;
      List<?> exp = (List)org.takeback.util.JSONUtils.parse(filter, List.class);
      this.where = (" where " + ExpressionProcessor.instance().toString(exp));
    }
  }
  
  public void setPropField(String nm, String v) {
    if (this.propFields == null) {
      this.propFields = new LinkedHashMap();
    }
    this.propFields.put(nm, v);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\core\dictionary\support\TableDictionary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */