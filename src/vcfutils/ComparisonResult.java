package vcfutils;

public class ComparisonResult {
	
	private String file1;
	private String file2;
	private int commonCalls;
	private int newCalls;
	private int missedCalls;
	private int missedSnps = 0;
	private int newSnps;
	private int missedInsertions;
	private int newInsertions;
	private int missedDeletions;
	private int newDeletions;
	
	public String getFile1() {
		return file1;
	}
	public void setFile1(String file1) {
		this.file1 = file1;
	}
	public String getFile2() {
		return file2;
	}
	public void setFile2(String file2) {
		this.file2 = file2;
	}
	public int getCommonCalls() {
		return commonCalls;
	}
	public void setCommonCalls(int commonCalls) {
		this.commonCalls = commonCalls;
	}
	public int getNewCalls() {
		return newCalls;
	}
	public void setNewCalls(int newCalls) {
		this.newCalls = newCalls;
	}
	public int getMissedCalls() {
		return missedCalls;
	}
	public void setMissedCalls(int missedCalls) {
		this.missedCalls = missedCalls;
	}
	public int getNewSnps() {
		return newSnps;
	}
	public void setNewSnps(int newSnps) {
		this.newSnps = newSnps;
	}
	public int getMissedInsertions() {
		return missedInsertions;
	}
	public void setMissedInsertions(int missedInsertions) {
		this.missedInsertions = missedInsertions;
	}
	public int getNewInsertions() {
		return newInsertions;
	}
	public void setNewInsertions(int newInsertions) {
		this.newInsertions = newInsertions;
	}
	public int getMissedDeletions() {
		return missedDeletions;
	}
	public void setMissedDeletions(int missedDeletions) {
		this.missedDeletions = missedDeletions;
	}
	public int getNewDeletions() {
		return newDeletions;
	}
	public void setNewDeletions(int newDeletions) {
		this.newDeletions = newDeletions;
	}
		
	public int getMissedSnps() {
		return missedSnps;
	}
	public void setMissedSnps(int missedSnps) {
		this.missedSnps = missedSnps;
	}
	public ComparisonResult(String file1, String file2, int commonCalls,
			int newCalls, int missedCalls, int missedSnps, int newSnps,
			int missedInsertions, int newInsertions, int missedDeletions,
			int newDeletions) {
		super();
		this.file1 = file1;
		this.file2 = file2;
		this.commonCalls = commonCalls;
		this.newCalls = newCalls;
		this.missedCalls = missedCalls;
		this.missedSnps = missedSnps;
		this.newSnps = newSnps;
		this.missedInsertions = missedInsertions;
		this.newInsertions = newInsertions;
		this.missedDeletions = missedDeletions;
		this.newDeletions = newDeletions;
	}
	
	
	@Override
	public String toString() {
		return "ComparisonResult [file1=" + file1 + ", file2=" + file2
				+ ", commonCalls=" + commonCalls + ", newCalls=" + newCalls
				+ ", missedCalls=" + missedCalls + ", missedSnps=" + missedSnps
				+ ", newSnps=" + newSnps + ", missedInsertions="
				+ missedInsertions + ", newInsertions=" + newInsertions
				+ ", missedDeletions=" + missedDeletions + ", newDeletions="
				+ newDeletions + "]";
	}
	
	
	
	
}
