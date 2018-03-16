package org.takeback.util.kvstore.support;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.impl.Iq80DBFactory;

public class LevelDBStore implements org.takeback.util.kvstore.KVStore
{
  private String name = getClass().getSimpleName();
  private Options options;
  private File databaseDir;
  private DB db;
  private final Lock connectingLock = new java.util.concurrent.locks.ReentrantLock();
  private AtomicBoolean initedStatus = new AtomicBoolean(false);
  
  public LevelDBStore(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void start()
  {
    this.connectingLock.lock();
    try {
      if (!this.initedStatus.get()) {
        this.databaseDir = new File(System.getProperty("user.home") + File.separator + "leveldb" + File.separator + this.name);
        this.options = new Options().createIfMissing(true).compressionType(org.iq80.leveldb.CompressionType.NONE);
        this.db = Iq80DBFactory.factory.open(this.databaseDir, this.options);
        this.initedStatus.set(true);
      }
    } catch (IOException e) {
      throw new IllegalStateException("init db error.", e);
    } finally {
      this.connectingLock.unlock();
    }
  }
  
  public DB db() {
    return this.db;
  }
  
  public void close()
  {
    try {
      if (this.db != null) {
        this.db.close();
        this.initedStatus.set(false);
      }
    } catch (IOException e) {
      throw new IllegalStateException("close db error.", e);
    }
  }
  
  private void checkStatus() {
    if (!this.initedStatus.get()) {
      throw new IllegalStateException("Need to start the store at first.");
    }
  }
  
  public void put(String k, String v)
  {
    checkStatus();
    this.db.put(Iq80DBFactory.bytes(k), Iq80DBFactory.bytes(v));
  }
  
  public void put(byte[] k, byte[] v)
  {
    checkStatus();
    this.db.put(k, v);
  }
  
  public String get(String k)
  {
    checkStatus();
    return Iq80DBFactory.asString(this.db.get(Iq80DBFactory.bytes(k)));
  }
  
  public byte[] get(byte[] k)
  {
    checkStatus();
    return this.db.get(k);
  }
  
  public Map.Entry<byte[], byte[]> seekFirst()
  {
    checkStatus();
    ReadOptions ro = new ReadOptions();
    DBIterator it = this.db.iterator(ro);
    it.seekToFirst();
    if (it.hasNext()) {
      Map.Entry<byte[], byte[]> e = it.peekNext();
      return e;
    }
    return null;
  }
  
  public void remove(String k)
  {
    checkStatus();
    this.db.delete(Iq80DBFactory.bytes(k));
  }
  
  public void remove(byte[] k)
  {
    checkStatus();
    this.db.delete(k);
  }
  
  public void removes(List<String> ks)
  {
    checkStatus();
    WriteBatch batch = null;
    try {
      batch = this.db.createWriteBatch();
      for (String k : ks) {
        batch.delete(Iq80DBFactory.bytes(k));
      }
      this.db.write(batch); return;
    } finally {
      try {
        if (batch != null)
          batch.close();
      } catch (IOException e) {
        throw new IllegalStateException("Error occurs when close batch.", e);
      }
    }
  }
  
  public void removesByByte(List<byte[]> ks)
  {
    checkStatus();
    WriteBatch batch = null;
    try {
      batch = this.db.createWriteBatch();
      for (byte[] k : ks) {
        batch.delete(k);
      }
      this.db.write(batch); return;
    } finally {
      try {
        if (batch != null)
          batch.close();
      } catch (IOException e) {
        throw new IllegalStateException("Error occurs when close batch.", e);
      }
    }
  }
  
  public void puts(Map<String, String> kv)
  {
    checkStatus();
    WriteBatch batch = null;
    try {
      batch = this.db.createWriteBatch();
      for (String k : kv.keySet()) {
        String v = (String)kv.get(k);
        batch.put(Iq80DBFactory.bytes(k), Iq80DBFactory.bytes(v));
      }
      this.db.write(batch); return;
    } finally {
      try {
        if (batch != null)
          batch.close();
      } catch (IOException e) {
        throw new IllegalStateException("Error occurs when close batch.", e);
      }
    }
  }
  
  public void putsByByte(Map<byte[], byte[]> kv)
  {
    checkStatus();
    WriteBatch batch = null;
    try {
      batch = this.db.createWriteBatch();
      for (byte[] k : kv.keySet()) {
        byte[] v = (byte[])kv.get(k);
        batch.put(k, v);
      }
      this.db.write(batch); return;
    } finally {
      try {
        if (batch != null)
          batch.close();
      } catch (IOException e) {
        throw new IllegalStateException("Error occurs when close batch.", e);
      }
    }
  }
  
  public Map<String, String> gets()
  {
    checkStatus();
    DBIterator iterator = null;
    try {
      iterator = this.db.iterator();
      Map<String, String> map = new java.util.HashMap();
      Map.Entry<byte[], byte[]> e; for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
        e = iterator.peekNext();
        map.put(Iq80DBFactory.asString((byte[])e.getKey()), Iq80DBFactory.asString((byte[])e.getValue()));
      }
      return map;
    } finally {
      try {
        if (iterator != null)
          iterator.close();
      } catch (IOException e) {
        throw new IllegalStateException("Error occurs when close iterator.", e);
      }
    }
  }
  
  public Map<byte[], byte[]> getsByByte()
  {
    checkStatus();
    DBIterator iterator = null;
    try {
      iterator = this.db.iterator();
      Map<byte[], byte[]> map = new java.util.HashMap();
      Map.Entry<byte[], byte[]> e; for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
        e = iterator.peekNext();
        map.put(e.getKey(), e.getValue());
      }
      return map;
    } finally {
      try {
        if (iterator != null)
          iterator.close();
      } catch (IOException e) {
        throw new IllegalStateException("Error occurs when close iterator.", e);
      }
    }
  }
  
  public boolean containsKey(String k)
  {
    return containsKey(Iq80DBFactory.bytes(k));
  }
  
  public boolean containsKey(byte[] k)
  {
    checkStatus();
    return this.db.get(k) != null;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\kvstore\support\LevelDBStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */