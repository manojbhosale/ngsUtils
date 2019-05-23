package liftoverutils;

import java.util.List;

public class IntervalUtils {
	
	public static Interval<String> mergeOverlappingIntervals(Interval<String> one, Interval<String> two) throws Exception {
		
		return new Interval<String>(min(one.getStart(),two.getStart()),max(one.getEnd(),two.getEnd()),"");
	}
	
	public static Interval<String> intersectBed(Interval<String> one, Interval<String> two) throws Exception{

		return new Interval<String>(max(one.getStart(),two.getStart()),min(one.getEnd(),two.getEnd()),"");

	}

	public static Interval<String> intersectIntervals(List<Interval<String>> intervals) {
		Interval<String> first = intervals.get(0);
		for(Interval<String> inter : intervals) {
			 try {
				first = intersectBed(first, inter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return first;
	
  }
	
  public static Interval<String> mergeIntervals(List<Interval<String>> intervals) {
		Interval<String> first = intervals.get(0);
		for(Interval<String> inter : intervals) {
			 try {
				first = mergeOverlappingIntervals(first, inter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return first;
	
  }
  
  public static long max(long i, long j){

		if(i > j){
			return i;
		}else{
			return j;
		}


	}
	public static long min(long i, long j){

		if(i < j){
			return i;
		}else{
			return j;
		}


	}


}
