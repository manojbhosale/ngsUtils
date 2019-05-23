package liftoverutils;

import java.util.HashMap;
import java.util.Map;

public class BedInterval implements Comparable<BedInterval>{

	private String chromosome;
	private long start;
	private long stop;
	private String strand;
	public static Map<String, Integer> chrMap;
	static {
		chrMap = new HashMap<>();
		
		chrMap.put("chr1", 1);
		chrMap.put("chr2", 2);
		chrMap.put("chr3", 3);
		chrMap.put("chr4", 4);
		chrMap.put("chr5", 5);
		chrMap.put("chr6", 6);
		chrMap.put("chr7", 7);
		chrMap.put("chr8", 8);
		chrMap.put("chr9", 9);
		chrMap.put("chr10", 10);
		chrMap.put("chr11", 11);
		chrMap.put("chr12", 12);
		chrMap.put("chr13", 13);
		chrMap.put("chr14", 14);
		chrMap.put("chr15", 15);
		chrMap.put("chr16", 16);
		chrMap.put("chr17", 17);
		chrMap.put("chr18", 18);
		chrMap.put("chr19", 19);
		chrMap.put("chr20", 20);
		chrMap.put("chr21", 21);
		chrMap.put("chr22", 22);
		chrMap.put("chrX", 23);
		chrMap.put("chrY", 24);
		chrMap.put("chrM", 25);
		
		
	}

	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public BedInterval(String chromosome, long start, long stop, String strand) {
		super();
		this.chromosome = chromosome;
		this.start = start;
		this.stop = stop;
		this.strand = strand;
	}
	public BedInterval(String chromosome, long start, long stop) {
		super();
		this.chromosome = chromosome;
		this.start = start;
		this.stop = stop;
	}
	public String getChromosome() {
		return chromosome;
	}
	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getStop() {
		return stop;
	}
	public void setStop(long stop) {
		this.stop = stop;
	}
	public BedInterval() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof BedInterval) {
			BedInterval inter = (BedInterval) arg0;
			if(this.chromosome == inter.getChromosome() && this.start == inter.getStart() && this.stop == inter.getStop()) {
				return true;
			}
		}
		return false;
	}

	public boolean intersects(Object arg0) {
		if(arg0 instanceof BedInterval) {
			BedInterval inter = (BedInterval) arg0;

			if(!inter.getChromosome().equals(this.getChromosome())){
				return false;
			}
			if(inter.getStart() > this.getStop() || inter.getStop() < this.getStart()){
				return false;
			}

		}
		return true;
	}

	public static BedInterval mergeOverlappingIntervals(BedInterval one, BedInterval two) throws Exception {

		if(one.getStart() > one.getStop() || two.getStart() > two.getStop()){
			throw new Exception("Start Can not be greater than stop.");
		}
		if(!one.getChromosome().equals(two.getChromosome())){
			return new BedInterval();
		}
		if(one.getStart() > two.getStop() || one.getStop() < two.getStart()){
			return new BedInterval();
		}
		if(!one.intersects(two)) {
			return new BedInterval();
		}

		return new BedInterval(one.getChromosome(),min(one.getStart(),two.getStart()),max(one.getStop(),two.getStop()));
	}

	public static BedInterval intersectBed(BedInterval one, BedInterval two) throws Exception{

		if(one.getStart() > one.getStop() || two.getStart() > two.getStop()){
			throw new Exception("Start Can not be greater than stop.");
		}
		if(!one.getChromosome().equals(two.getChromosome())){
			return new BedInterval();
		}
		if(one.getStart() > two.getStop() || one.getStop() < two.getStart()){
			return new BedInterval();
		}

		return new BedInterval(one.getChromosome(),max(one.getStart(),two.getStart()),min(one.getStop(),two.getStop()));

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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return chromosome+"\t"+start+"\t"+stop;
	}


	@Override
	public int compareTo(BedInterval other)
	{ 
		if(chromosome.equals(other.chromosome)) {
			if (start < other.getStart())
				return -1;
			else if (start > other.getStart())
				return 1;
			else if (stop < other.getStop())
				return -1;
			else if (stop > other.getStop())
				return 1;
			else
				return 0;
		}
		
		if(chromosome.startsWith("chr")) {
			return chrMap.get(chromosome).compareTo(chrMap.get(other.chromosome));
		}
		return chromosome.compareTo(other.chromosome);
	}
	
	 public boolean intersects(BedInterval other)
	  {
	      return other.getStop() > start && other.getStart() < stop;
	  }

	 public Interval<String> getBedInterval() {
			return new Interval<String>(this.start,this.stop,"");
		}

}
