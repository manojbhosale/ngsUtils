package liftoverutils;

import java.util.List;

public class Interval<Type> implements Comparable<Interval<Type>>
{

  private long start;
  private long end;
  private Type data;

  public Interval(long start, long end, Type data)
  {
      this.start = start;
      this.end = end;
      this.data = data;
  }

  public long getStart()
  {
      return start;
  }

  public void setStart(long start)
  {
      this.start = start;
  }

  public long getEnd()
  {
      return end;
  }

  public void setEnd(long end)
  {
      this.end = end;
  }

  public Type getData()
  {
      return data;
  }

  public void setData(Type data)
  {
      this.data = data;
  }

  public boolean contains(long time)
  {
      return time < end && time > start;
  }

  public boolean intersects(Interval<?> other)
  {
      return other.getEnd() >= start && other.getStart() < end;
  }

  public int compareTo(Interval<Type> other)
  {
      if (start < other.getStart())
          return -1;
      else if (start > other.getStart())
          return 1;
      else if (end < other.getEnd())
          return -1;
      else if (end > other.getEnd())
          return 1;
      else
          return 0;
  }
  
  @Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Interval<?>) {
			Interval<?> inter = (Interval<?>) arg0;
			return this.start == inter.start && this.end == inter.end;
		}
		return false ;
	}

@Override
public String toString() {
	return "Interval [start=" + start + ", end=" + end + ", data=" + data + "]";
}
  

  

}
