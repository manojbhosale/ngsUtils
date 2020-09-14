package cnv_compare;

public class CnvInterval {
	
	private String chromosome;
	private int startPosition;
	private int stopPosition;
	private String type;
	private float logRatio;
	private int copyNumber;
	
	public CnvInterval(String chromosome, int startPosition, int stopPosition, String type, float logRatio,
			int copyNumber) {
		super();
		this.chromosome = chromosome;
		this.startPosition = startPosition;
		this.stopPosition = stopPosition;
		this.type = type;
		this.logRatio = logRatio;
		this.copyNumber = copyNumber;
	}
	
	public CnvInterval(String chromosome, int startPosition, int stopPosition, String type) {
		super();
		this.chromosome = chromosome.toUpperCase();
		this.startPosition = startPosition;
		this.stopPosition = stopPosition;
		this.type = type.toUpperCase(); 
		this.logRatio = 0;
		this.copyNumber = 0;
	}
	
	public CnvInterval(String chromosome, int startPosition, int stopPosition) {
		super();
		this.chromosome = chromosome;
		this.startPosition = startPosition;
		this.stopPosition = stopPosition;
		this.type = "";
		this.logRatio = 0;
		this.copyNumber = 0;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getStopPosition() {
		return stopPosition;
	}

	public void setStopPosition(int stopPosition) {
		this.stopPosition = stopPosition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public float getLogRatio() {
		return logRatio;
	}

	public void setLogRatio(float logRatio) {
		this.logRatio = logRatio;
	}

	public int getCopyNumber() {
		return copyNumber;
	}

	public void setCopyNumber(int copyNumber) {
		this.copyNumber = copyNumber;
	}
	
}
