package org.takeback.util.io;

import java.io.IOException;
import java.io.InputStream;




public class UnsafeByteArrayInputStream
  extends InputStream
{
  protected byte[] mData;
  protected int mPosition;
  protected int mLimit;
  protected int mMark = 0;
  
  public UnsafeByteArrayInputStream(byte[] buf) {
    this(buf, 0, buf.length);
  }
  
  public UnsafeByteArrayInputStream(byte[] buf, int offset) {
    this(buf, offset, buf.length - offset);
  }
  
  public UnsafeByteArrayInputStream(byte[] buf, int offset, int length) {
    this.mData = buf;
    this.mPosition = (this.mMark = offset);
    this.mLimit = Math.min(offset + length, buf.length);
  }
  
  public int read() {
    return this.mPosition < this.mLimit ? this.mData[(this.mPosition++)] & 0xFF : -1;
  }
  
  public int read(byte[] b, int off, int len) {
    if (b == null)
      throw new NullPointerException();
    if ((off < 0) || (len < 0) || (len > b.length - off))
      throw new IndexOutOfBoundsException();
    if (this.mPosition >= this.mLimit)
      return -1;
    if (this.mPosition + len > this.mLimit)
      len = this.mLimit - this.mPosition;
    if (len <= 0)
      return 0;
    System.arraycopy(this.mData, this.mPosition, b, off, len);
    this.mPosition += len;
    return len;
  }
  
  public long skip(long len) {
    if (this.mPosition + len > this.mLimit)
      len = this.mLimit - this.mPosition;
    if (len <= 0L)
      return 0L;
    this.mPosition = ((int)(this.mPosition + len));
    return len;
  }
  
  public int available() {
    return this.mLimit - this.mPosition;
  }
  
  public boolean markSupported() {
    return true;
  }
  
  public void mark(int readAheadLimit) {
    this.mMark = this.mPosition;
  }
  
  public void reset() {
    this.mPosition = this.mMark;
  }
  
  public void close() throws IOException
  {}
  
  public int position() {
    return this.mPosition;
  }
  
  public void position(int newPosition) {
    this.mPosition = newPosition;
  }
  
  public int size() {
    return this.mData == null ? 0 : this.mData.length;
  }
}


/* Location:              E:\apache-tomcat-8\apache-tomcat-8\webapps\ROOT\WEB-INF\classes\!\org\takeback\util\io\UnsafeByteArrayInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */