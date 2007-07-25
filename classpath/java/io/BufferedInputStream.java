package java.io;

public class BufferedInputStream extends InputStream {
  private final InputStream in;
  private final byte[] buffer;
  private int position;
  private int limit;

  public BufferedInputStream(InputStream in, int size) {
    this.in = in;
    this.buffer = new byte[size];
  }
  
  public BufferedInputStream(InputStream in) {
    this(in, 32);
  }

  private void fill() throws IOException {
    position = 0;
    limit = in.read(buffer);
  }

  public int read() throws IOException {
    if (position >= limit) {
      fill();
      if (limit == -1) {
        return -1;
      }
    }

    return buffer[position++];
  }

  public int read(byte[] b, int offset, int length) throws IOException {
    int count = 0;

    if (position < limit) {
      int remaining = limit - position;
      if (remaining > length) {
        remaining = length;
      }

      System.arraycopy(buffer, position, b, offset, remaining);

      count += remaining;
      position += remaining;
      offset += remaining;
      length -= remaining;
    }

    if (length > 0) {
      int c = in.read(b, offset, length);
      if (c == -1) {
        if (count == 0) {
          count = -1;
        }
      } else {
        count += c;
      }
    }

    return count;
  }

  public void close() throws IOException {
    in.close();
  }
}