package liftoverutils;

public class BedInterval implements Comparable<BedInterval>{

	private String chromosome;
	private int start;
	private int stop;
	private String strand;
	
	public String getStrand() {
		return strand;
	}
	public void setStrand(String strand) {
		this.strand = strand;
	}
	public BedInterval(String chromosome, int start, int stop, String strand) {
		super();
		this.chromosome = chromosome;
		this.start = start;
		this.stop = stop;
		this.strand = strand;
	}
	public BedInterval(String chromosome, int start, int stop) {
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
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	public BedInterval() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int compareTo(BedInterval other)
	  {
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

}
