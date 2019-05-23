package liftoverutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;



public class LiftOverDriver {




	public static void main(String args[]) throws Exception{
		Chain chn = new Chain();
		//LiftChain lc = new LiftChain();

		List<Chain> chains = chn.liftOverChain(new File("E:\\SureCall4.0\\SureCall_401\\LiftOver\\testData\\DesignsComparison\\ChainFile\\hg19ToHg38.over.chain")); 

		HashMap<String,IntervalTree<List<Object>>> chainIndex = chn.indexChains(chains);
		BufferedReader br = new BufferedReader(new FileReader("\\\\agtdna02\\NGS_Data_From_Cloud\\neeli\\041503_1395942071620\\Covered.bed"));

		SortedSet<BedInterval> res = new TreeSet<>();
		//PrintWriter pw=  new PrintWriter("D:\\SureCall4.0\\SureCall_401\\LiftOver\\testData\\hg19.19to18.rand.bed");

	/*try{
			res = chn.convertCoordinate(chainIndex,"chr1", 144824682, 144824882, "+");//chr1	144824682	144824882
		}catch(ChainException e){
			e.printChainException();
		}catch(Exception e1){
			e1.printStackTrace();
		}

		for(BedInterval i : res){

			//pw.println(i.getChromosome()+"\t"+i.getStart()+"\t"+i.getStop());
			System.out.println(i.getChromosome()+"\t"+i.getStart()+"\t"+i.getStop()+"\t"+i.getStrand());

		}
*/
/*
		
		String line = "";
		while((line = br.readLine())!= null){

			line = line.trim();
			String[] test = line.split("\t");
			//System.out.println(test[2]);

			try{
				res = chn.convertCoordinate(chainIndex, test[0], Integer.parseInt(test[1]), Integer.parseInt(test[2]), "+");
			}catch(ChainException e){
				e.printChainException();
			}catch(Exception e1){
				e1.printStackTrace();
			}
			for(BedInterval i : res){

				pw.println(i.getChromosome()+"\t"+i.getStart()+"\t"+i.getStop()+"\t"+i.getStrand());

			}

		}	

		 */
		//br.close();
		//pw.close();
		
		LiftOverDriver lod = new LiftOverDriver();
		lod.liftBed(chainIndex, new File("\\\\agtdna02\\NGS_Data_From_Cloud\\neeli\\041503_1395942071620\\Covered.bed"),new File("\\\\agtdna02\\NGS_Data_From_Cloud\\neeli\\041503_1395942071620\\CoveredManoj.bed"));
	
		
	}


	public void liftBed(HashMap<String,IntervalTree<List<Object>>> chainIndex,File inbed, File outBed) throws Exception{
		boolean reportMultiple = false;
		Integer mergingThreshold = 2000;
		double inputRemapRatio = 0.95;
		PrintWriter pw = new PrintWriter(outBed);
		BufferedReader br = new BufferedReader(new FileReader(inbed));
		Chain chn = new Chain();
		PrintWriter pwfail = new PrintWriter(outBed.getParent()+"\\"+outBed.getName()+".failed");

		String line = "";
		while((line = br.readLine())!= null){

			if(line.startsWith("#") || line.startsWith("track") || line.startsWith("browser"))
				continue;

			line = line.trim();
			String[] splited = line.split("\t");

			String strand = "+";

			if(splited.length < 3)
				throw new Exception("less than 3 fileds in BED. Skipped.");
			String chromosome = splited[0]; 
			Integer start = Integer.parseInt(splited[1]);
			Integer stop = Integer.parseInt(splited[2]);

			if(start > stop){
				throw new Exception("Start greater than stop. Skipped.");
			}

			//deal with bed less than 12 columns
			int strandIndex = 0;
			if(splited.length < 12){

				for(String str : splited){				
					if(str.equals("+") || str.equals("-")){
						strand = str; //change default strand information if present in input file
						break;
					}
					strandIndex++;
				}

				SortedSet<BedInterval> res = new TreeSet<>();
				
				res = chn.convertCoordinate(chainIndex, chromosome, start, stop, strand);

				String selectedCombi = selectStrandAndChromosome(res);

				String[] chrStrand = selectedCombi.split("\t");

				
				
				SortedSet<BedInterval> filteredByChrStrand = new TreeSet<BedInterval>();
				
				for(BedInterval i: res){
					
					if(i.getChromosome().equals(chrStrand[0]) && i.getStrand().equals(chrStrand[1])){
						
						filteredByChrStrand.add(i);
						
					}
					
				}
				
				if(filteredByChrStrand.isEmpty()){
					System.out.println("unmapped!!");
					continue;
				}
				
				SortedSet<BedInterval> nearestResult = getNearestIntervals(filteredByChrStrand, mergingThreshold);

				Integer queryBaseSize = stop - start;
				Integer remapBaseSize = 0; 
				for(BedInterval inter : nearestResult){
					if(inter.getChromosome().equals(chrStrand[0]) && inter.getStrand().equals(chrStrand[1])){	
						remapBaseSize = (int)(remapBaseSize + inter.getStop() - inter.getStart());
					}
				}

				double remapRatio = (double)remapBaseSize/queryBaseSize;


				if(remapRatio < inputRemapRatio){

					pwfail.println(line);
					System.out.println(line+"\t"+remapRatio+"\t"+remapBaseSize+"\t"+queryBaseSize);
					continue;

				}
				pw.println(line);
				//System.out.println(line+"\t"+remapRatio+"\t"+remapBaseSize+"\t"+queryBaseSize);
				if(reportMultiple){

					for(BedInterval inter : nearestResult){
						if(inter.getChromosome().equals(chrStrand[0]) && inter.getStrand().equals(chrStrand[1])){
							pw.print(inter.getChromosome()+"\t"+inter.getStart()+"\t"+inter.getStop()+"\t"+inter.getStrand());
							for(int i = 3; i < splited.length ; i++){
								if(i == strandIndex){
									pw.print("\t"+inter.getStrand());
								}else{
									pw.print("\t"+splited[i]);
								}
							}
						}
						pw.print("\n");

					}
				}else{

					/*List<BedInterval> plus = new ArrayList<>();
					List<BedInterval> minus = new ArrayList<>();
					// segregate strand
					for(BedInterval i : res){
						if(i.getStrand().equals("+")){
							plus.add(i);
						}else if(i.getStrand().equals("-")){
							minus.add(i);
						}else{
							throw new ChainException("Invalid strand!!");
						}
					}
					 */
					
					/*if(res.isEmpty()){
						System.out.println("Unmapped");
						continue;
					}*/
					
					
					//System.out.println(nearestResult.);
					/*if(nearestResult.isEmpty()){
						continue;
					}
					*/
					BedInterval firstInterval = new BedInterval();
					BedInterval lastInterval = new BedInterval();
					try{
						firstInterval = nearestResult.first();
						lastInterval = nearestResult.last();
					}catch(NoSuchElementException e){
						System.out.println(filteredByChrStrand.first().getStart());
					}
					
					Integer combinedStart = (int)firstInterval.getStart();
					Integer combinedStop = (int)lastInterval.getStop();

					pw.print(firstInterval.getChromosome()+"\t"+combinedStart+"\t"+combinedStop+"\t"+firstInterval.getStrand());
					for(int i = 3; i < splited.length ; i++){
						if(i == strandIndex){
							pw.print("\t"+firstInterval.getStrand());
						}else{
							pw.print("\t"+splited[i]);
						}
					}
					pw.print("\n");
				}
			}
		}
		pw.close();
		pwfail.close();
	}


	public String selectStrandAndChromosome(SortedSet<BedInterval> res){

		Map<String, Integer> chrStrandMap = new HashMap<>();
		String selectedCombi = "";
		for(BedInterval i : res){

			String tempStr = i.getChromosome()+"\t"+i.getStrand();


			if(chrStrandMap.containsKey(tempStr)){

				Integer size = chrStrandMap.get(tempStr);
				Integer newIntervalSize = (int) (i.getStop() - i.getStart());
				size = size + newIntervalSize;

				chrStrandMap.put(tempStr, size);

			}else{
				Integer newIntervalSize = (int) (i.getStop() - i.getStart());
				chrStrandMap.put(tempStr,newIntervalSize);
			}


			Integer oldSize = 0;

			//List<Integer> allSizes = chrStrandMap.values();


			for(String combi : chrStrandMap.keySet()){
				Integer newSize = chrStrandMap.get(combi);
				if(newSize > oldSize){
					oldSize = newSize;
				}
			}



			for(String combi : chrStrandMap.keySet()){
				if(chrStrandMap.get(combi) == oldSize){
					selectedCombi = combi;
				}
			}

			/*	if(chrStrandMap.containsKey(i.getStrand())){
				Map<String,Integer> chrMap = chrStrandMap.get(i.getStrand());
				if(chrMap.containsKey(i.getChromosome())){
					Integer size = chrMap.get(i.getChromosome());
					Integer newSize = i.getStop() - i.getStart();
					size = size + newSize;
					chrMap.put(i.getChromosome(), size);
				}else{
					Integer newSize = i.getStop() - i.getStart();
					chrMap.put(i.getChromosome(), newSize);
				}
			}else{
				Map<String,Integer> chrMap = new HashMap<>();
				Integer newSize = i.getStop() - i.getStart();
				chrMap.put(i.getChromosome(),newSize);
				chrStrandMap.put(i.getStrand(),chrMap);
			}
			 */	

		}
		return selectedCombi;

	}
	
	
	public SortedSet<BedInterval> getNearestIntervals(SortedSet<BedInterval> res, Integer mergingThreshold){
		
		
		 SortedSet<BedInterval> resultIntervals = new TreeSet<BedInterval>();
		 
		 if(res.size() == 1){
			 return res;
		 }
		 
		 BedInterval first = res.first();
		 
		 int counter = 0;
		 
		 for(BedInterval one : res){
			 if(counter == 0){
				 counter++;
				 continue; 
			 }
			 if((one.getStart() - first.getStop()) < mergingThreshold){
				 
				 resultIntervals.add(one);
				 resultIntervals.add(first);
				 
			 }else{
				 resultIntervals = new TreeSet<BedInterval>();
			 }
			 
			 first = one;
			 
		 }
		 
		 if(resultIntervals.isEmpty()){
			 resultIntervals.add(res.last());
		 }
		 
		return resultIntervals;
		
	}

}