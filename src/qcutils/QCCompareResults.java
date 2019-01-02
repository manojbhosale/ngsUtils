package qcutils;

import java.io.File;

public class QCCompareResults {
	private File file1;
	private File file2;
	private Integer numMatch;
	private Integer numUnMatch;
	private Integer numNewMetric;
	
	public Integer getNumMatch() {
		return numMatch;
	}
	public void setNumMatch(Integer numMatch) {
		this.numMatch = numMatch;
	}
	public Integer getNumUnMatch() {
		return numUnMatch;
	}
	public void setNumUnMatch(Integer numUnMatch) {
		this.numUnMatch = numUnMatch;
	}
	public Integer getNumNewMetric() {
		return numNewMetric;
	}
	public void setNumNewMetric(Integer numNewMetric) {
		this.numNewMetric = numNewMetric;
	}
	public File getFile1() {
		return file1;
	}
	public void setFile1(File file1) {
		this.file1 = file1;
	}
	public File getFile2() {
		return file2;
	}
	public void setFile2(File file2) {
		this.file2 = file2;
	}
	public QCCompareResults(File file1, File file2, Integer numMatch,
			Integer numUnMatch, Integer numNewMetric) {
		super();
		this.file1 = file1;
		this.file2 = file2;
		this.numMatch = numMatch;
		this.numUnMatch = numUnMatch;
		this.numNewMetric = numNewMetric;
	}
	
	
	
	
	
	
	
}

