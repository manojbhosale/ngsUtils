package cnv_compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import liftoverutils.Interval;
import liftoverutils.IntervalNode;;

public class CnvCompare {
	
	
	public static void main(String[] args) {
		File cnvFile1 = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete\\forCNV\\test1.txt");
		File cnvFile2 = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete\\forCNV\\test2.txt");
		Path outDir = Paths.get("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete\\forCNV");
	}
	
	public static void compareCnvFilesToFolder(File cnvFile1, File cnvFile2, Path outDir) {
		CnvCompare cc = new CnvCompare();
		cc.compareCnv(cnvFile1, cnvFile2, outDir);
	}
		
	
	void compareCnv(File cnvFile1, File cnvFile2, Path outDir) {
		List<Interval<CnvInterval>> intervalList = new ArrayList<Interval<CnvInterval>>();
		File comparisonFile = outDir.resolve(cnvFile1.getName().replace(".", "_")+""+cnvFile2.getName()).toFile();
		int leftNumIntervals = 0;	
		int rightNumIntervals = 0;	
		int leftOverlap = 0;
		int rightOverlap = 0;
		int exactMatch = 0;
		int within = 0;
		int covering = 0;
		int noMatch = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader(cnvFile1));BufferedReader br1 = new BufferedReader(new FileReader(cnvFile2)); PrintWriter pw = new PrintWriter(comparisonFile)){
			String line = "";
			while((line = br1.readLine()) != null) {
				if(line.startsWith("#"))
					continue;
				leftNumIntervals++;
				String[] splitedRec = line.split("\t");
				String chr = splitedRec[0];
				int start = Integer.parseInt(splitedRec[1]);
				int stop = Integer.parseInt(splitedRec[2]);
				String type = splitedRec[6];
				CnvInterval cnvInterval = new CnvInterval(chr, start, stop, type);
				Interval<CnvInterval> interval =  new Interval<CnvInterval>(start, stop, cnvInterval); 
				intervalList.add(interval);
			}

			IntervalNode<CnvInterval> masterNode = new IntervalNode<>(intervalList);
			line = "";
			
			while((line = br.readLine()) != null) {
				if(line.startsWith("#"))
					continue;
				rightNumIntervals++;
				String[] splitedRec = line.split("\t");
				String chr = splitedRec[0];
				int start = Integer.parseInt(splitedRec[1]);
				int stop = Integer.parseInt(splitedRec[2]);
				String type = splitedRec[6];
				CnvInterval cnvInterval = new CnvInterval(chr, start, stop, type);
				Interval<CnvInterval> interval =  new Interval<CnvInterval>(start, stop, cnvInterval); 
				List<Interval<CnvInterval>> intersectList = masterNode.query(interval);
				boolean noMatchFlag = true;
				for(Interval<CnvInterval> intersect : intersectList){
					int res = getVerdict(cnvInterval, intersect.getData());
					if(res == 0) {
						exactMatch++;
						noMatchFlag = false;
					}else if(res == 1) {
						covering++;
						noMatchFlag = false;
					}else if(res == 2) {
						within++;
						noMatchFlag = false;
					}else if(res == 3) {
						leftOverlap++;
						noMatchFlag = false;
					}else if(res == 4){
						rightOverlap++;
						noMatchFlag = false;
					}
				}	
				
				if(noMatchFlag)
					noMatch++;
				
			}
			
			pw.println("File1\tFile2\t#Intervals in File1\t#Intervals in File2\tExact matches\tCovering\tWithin\tLeft overlap\tRight Overlap\tNo overlap");
			pw.println(cnvFile1+"\t"+cnvFile2+"\t"+rightNumIntervals+"\t"+leftNumIntervals+"\t"+exactMatch+"\t"+covering+"\t"+within+"\t"+leftOverlap+"\t"+rightOverlap+"\t"+noMatch);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	int getVerdict(CnvInterval inter1, CnvInterval inter2){
		if(cnvOfSameType(inter1, inter2) && isSameChr(inter1, inter2)) {
			if(inter1.getStartPosition() == inter2.getStartPosition() && inter1.getStopPosition() ==  inter2.getStopPosition()) {
				return 0; //exact match
			}else if(inter1.getStartPosition() <= inter2.getStartPosition() && inter1.getStopPosition() >= inter2.getStopPosition()) {
				return 1; //covering
			}else if(inter1.getStartPosition() >= inter2.getStartPosition() && inter1.getStopPosition() <= inter2.getStopPosition()){
				return 2; //within
			}else if(inter1.getStartPosition() <= inter2.getStartPosition() && inter1.getStopPosition() <= inter2.getStopPosition()){
				return 3; //left overlap
			}else if(inter1.getStartPosition() >= inter2.getStartPosition() && inter1.getStopPosition() >= inter2.getStopPosition()){
				return 4; //right overlap
			}
		}
		return 5;
	}
	
	boolean cnvOfSameType(CnvInterval inter1, CnvInterval inter2) {
		return (inter1.getType()).equals(inter2.getType()); 
	}
	
	boolean isSameChr(CnvInterval inter1, CnvInterval inter2) {
		return (inter1.getChromosome()).equals(inter2.getChromosome()); 
	}
	
	
	public static void readCnvFile(File cnvFile) {
		
		
		
	}

}
