package org.takeback.chat.store.user;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.takeback.chat.entity.PubUser;
import org.takeback.chat.service.UserService;
import org.takeback.util.BeanUtils;
import org.takeback.util.exception.CodedBaseException;


public class DefaultUserStore
  implements UserStore
{
  @Autowired
  private UserService userService;
  private LoadingCache<Integer, User> store;
  
  public void init()
  {
    this.store = CacheBuilder.newBuilder().expireAfterAccess(4L, TimeUnit.HOURS).build(new CacheLoader()
    {
      public User load(Integer uid) throws Exception {
        PubUser user = (PubUser)DefaultUserStore.this.userService.get(PubUser.class, uid);
        if (user != null) {
          return (User)BeanUtils.map(user, User.class);
        }
        throw new CodedBaseException(530, "user " + uid + " not exists");
      }
    });
  }
  
  public long size()
  {
    return this.store.size();
  }
  
  public User get(Serializable uid)
  {
    try {
      return (User)this.store.get((Integer)uid);
    } catch (ExecutionException e) {}
    return null;
  }
  

  public void reload(Serializable uid)
  {
    this.store.invalidate(uid);
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\chat\store\user\DefaultUserStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */