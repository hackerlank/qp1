package org.takeback.chat.store.user;

import com.google.common.cache.CacheLoader;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.UserService;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseException;












class DefaultUserStore$1
  extends CacheLoader<Integer, User>
{
  DefaultUserStore$1(DefaultUserStore this$0) {}
  
  public User load(Integer uid)
    throws Exception
  {
    PubUser user = (PubUser)DefaultUserStore.access$000(this.this$0).get(PubUser.class, uid);
    if (user != null) {
      return (User)BeanUtils.map(user, User.class);
    }
    throw new CodedBaseException(530, "user " + uid + " not exists");
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\user\DefaultUserStore$1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */