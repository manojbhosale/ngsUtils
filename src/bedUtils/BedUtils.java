package bedUtils;
import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import liftoverutils.*;

public class BedUtils {

	public static void main(String[] args) {

		File f = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\ForIntervalMerging\\DEF_662-1322126769L_AllTracks_amplicons.bed");
		File f1 = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\d\\CancerAll-In-OneLung_hg38Mut.bed");
		//mergeBedFiles(f,null);
		Set<BedInterval> intersectBedFiles = mergeBedFiles(f);

		intersectBedFiles.forEach(System.out::println);

	}

	public static Set<BedInterval> intersectBedFiles(File one, File two){


		//read a bed file record by record
		//query another files tree with record
		// intersect the resulting inervals each other
		//add the interval to resutl

		Map<String, IntervalTree<String>> bedMapTwo = getIntervalTree(two);
		Map<String, IntervalTree<String>> bedMap1 = getIntervalTree(one);

		
		Set<BedInterval> result1 = mergeSingleFile(bedMap1, one);


		Set<BedInterval> result = new TreeSet<>();

			for(BedInterval inter : result1) {
				

				IntervalTree<String> intervalTree = bedMapTwo.get(inter.getChromosome());
				List<Interval<String>> intervals = intervalTree.getIntervals(inter.getStart(), inter.getStop());
				if(!intervals.isEmpty()) {
					intervals.add(new Interval<String>(inter.getStart(), inter.getStop(),""));
					Interval<String> mergedInterval = IntervalUtils.intersectIntervals(intervals);
					result.add(new BedInterval(inter.getChromosome(),mergedInterval.getStart(),mergedInterval.getEnd()));
				}
			}



		return result;


	}

	public static Set<BedInterval> mergeBedFiles(File one){

		Set<BedInterval> result1 = new TreeSet<BedInterval>();
		Set<BedInterval> result = new TreeSet<BedInterval>();
		Map<String, IntervalTree<String>> bedMap = getIntervalTree(one);
		result1 = mergeSingleFile(bedMap, one);
		boolean checkFirst = true;
		BedInterval first = null;
		for(BedInterval inter : result1) {
			
			if(checkFirst == true) {
				first = inter;
				checkFirst = false;
				continue;
			}
			
			if(first.intersects(inter)) {
				try {
					inter = BedInterval.mergeOverlappingIntervals(first, inter);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				first = inter;
			}else {
				result.add(first);
				first = inter;
			}
			
		}
		result.add(first);

		return result;

	}
	
	 

	public static Set<BedInterval> mergeBedFiles(File one, File two){

		Set<BedInterval> result = new TreeSet<>();
		//Map<String, IntervalTree<String>> bedMap12 = getIntervalTree(one,two);
		Map<String, IntervalTree<String>> bedMap1 = getIntervalTree(one);
		Map<String, IntervalTree<String>> bedMap2 = getIntervalTree(two);

		Set<BedInterval> result1 = mergeSingleFile(bedMap1, one);
		Set<BedInterval> result2 = mergeSingleFile(bedMap2, two);

		result1.addAll(result2);
		
		for(BedInterval interval : result1) {
			
			IntervalTree<String> intervalTree = bedMap1.get(interval.getChromosome());
			List<Interval<String>> interList = intervalTree.get(interval.getStart(), interval.getStop());
			if(interList.isEmpty()) {
				result.add(interval);
				System.out.println("Manoj");
				continue;
			}
			Interval<String> mergedInterval = IntervalUtils.mergeIntervals(interList);
			result.add(new BedInterval(interval.getChromosome(),mergedInterval.getStart(),mergedInterval.getEnd()));

		}
		

		return result;

	}


	static Set<BedInterval> mergeSingleFile(Map<String, IntervalTree<String>> bedMap, File one) {
		Set<BedInterval> result = new TreeSet<>();
		try(BufferedReader br = new BufferedReader(new FileReader(one))){

			String line= "";
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				String chromosome = splited[0];
				long start = Integer.parseInt(splited[1]);
				long end = Integer.parseInt(splited[2]);

				BedInterval interval = new BedInterval(chromosome,start,end);
				IntervalTree<String> intervalTree = bedMap.get(chromosome);
				List<Interval<String>> intervals = intervalTree.getIntervals(start, end);
				//System.out.println(interval+" : "+ intervals);
				if(!intervals.isEmpty()) {
					Interval<String> mergedInterval = IntervalUtils.mergeIntervals(intervals);
					result.add(new BedInterval(chromosome,mergedInterval.getStart(),mergedInterval.getEnd()));
				}
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;	

	}

	public BedInterval mergeIntervals(List<BedInterval> intervals) {
		BedInterval first = intervals.get(0);
		for(BedInterval inter : intervals) {
			try {
				first = BedInterval.mergeOverlappingIntervals(first, inter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return first;
	}

	static void sortBedFile(File bedFile) {



	}

	static void addBedInterval(Map<String, IntervalTree<String>> bedMap, BedInterval interval) {

		if(bedMap.containsKey(interval.getChromosome())) {
			IntervalTree<String> intervalTree = bedMap.get(interval.getChromosome());
			Interval<String> inter = new Interval(interval.getStart(), interval.getStop(),"");
			intervalTree.addInterval(inter);
		}else {
			IntervalTree<String> intervalTree = new IntervalTree<>();
			Interval<String> inter = new Interval(interval.getStart(), interval.getStop(),"");
			intervalTree.addInterval(inter);
			bedMap.put(interval.getChromosome(), intervalTree);
		}		
	}

	public static Map<String, IntervalTree<String>> getIntervalTree(File bedFile){
		Map<String, IntervalTree<String>> bedMap = new HashMap<>();
		try(BufferedReader br = new BufferedReader(new FileReader(bedFile))){

			String line= "";
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				String chromosome = splited[0];
				long start = Integer.parseInt(splited[1]);
				long end = Integer.parseInt(splited[2]);

				BedInterval interval = new BedInterval(chromosome,start,end);
				addBedInterval(bedMap, interval);


			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bedMap;		

	}

	public static Map<String, IntervalTree<String>> getIntervalTree(File bedFile1, File bedFile2){
		Map<String, IntervalTree<String>> bedMap = new HashMap<>();
		try(BufferedReader br = new BufferedReader(new FileReader(bedFile1));BufferedReader br1 = new BufferedReader(new FileReader(bedFile2))){

			String line= "";
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				String chromosome = splited[0];
				long start = Integer.parseInt(splited[1]);
				long end = Integer.parseInt(splited[2]);

				BedInterval interval = new BedInterval(chromosome,start,end);
				addBedInterval(bedMap, interval);


			}

			while((line = br1.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				String chromosome = splited[0];
				long start = Integer.parseInt(splited[1]);
				long end = Integer.parseInt(splited[2]);

				BedInterval interval = new BedInterval(chromosome,start,end);
				addBedInterval(bedMap, interval);


			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bedMap;		

	}


}
