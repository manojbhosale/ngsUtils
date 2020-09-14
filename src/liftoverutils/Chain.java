package liftoverutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Chain {

	// Declare the chain elements
	long score;
	String sourceName;
	int sourceSize;
	String sourceStrand;
	int sourceStart;
	long sourceEnd;
	String targetName;
	int targetSize;
	String targetStrand;
	int targetStart;
	int targetEnd;
	String id;
	int sfrom;
	int tfrom;
	List<List<Integer>> blocks = new ArrayList<List<Integer>>();
	int size;
	int sgap;
	int tgap;


	public Chain(long score, String sourceName, int sourceSize,
			String sourceStrand, int sourceStart, long sourceEnd,
			String targetName, int targetSize, String targetStrand,
			int targetStart, int targetEnd, String id, int sfrom, int tfrom,
			List<List<Integer>> blocks, int size, int sgap, int tgap) {
		super();
		this.score = score;
		this.sourceName = sourceName;
		this.sourceSize = sourceSize;
		this.sourceStrand = sourceStrand;
		this.sourceStart = sourceStart;
		this.sourceEnd = sourceEnd;
		this.targetName = targetName;
		this.targetSize = targetSize;
		this.targetStrand = targetStrand;
		this.targetStart = targetStart;
		this.targetEnd = targetEnd;
		this.id = id;
		this.sfrom = sfrom;
		this.tfrom = tfrom;
		this.blocks = blocks;
		this.size = size;
		this.sgap = sgap;
		this.tgap = tgap;
	}

	public Chain() {

	}


	// Parse each chain and return an object for the chain with all its
	// alignment blocks
	public Chain parseChain(String header, BufferedReader br)
			throws IOException, ChainException {

		String fields[] = {};
		int plus = 1;
		int minus = -1;

		fields = header.split("\\s+");
		//parse chain header and initialize the instance variables
		if (header.startsWith("chain")) {
			//chain 20851231461 chr1 249250621 + 10000 249240621 chr1 248956422 + 10000 248946422 2
			score = Long.parseLong(fields[1]); 
			sourceName = fields[2];
			sourceSize = Integer.parseInt(fields[3]);
			sourceStrand = fields[4];
			if (!sourceStrand.equals("+")){
				throw new ChainException("Source strand in chain file must be \"+\" ");
			}
			sourceStart = Integer.parseInt(fields[5]);
			sourceEnd = Integer.parseInt(fields[6]);
			targetName = fields[7];
			targetSize = Integer.parseInt(fields[8]);
			targetStrand = fields[9];
			targetStart = Integer.parseInt(fields[10]);
			targetEnd = Integer.parseInt(fields[11]);
			id = fields.length == 12 ? "None" : fields[12];
			sfrom = sourceStart;
			tfrom = targetStart;
			if (!(targetStrand.equals("+") || targetStrand.equals("-"))){
				throw new ChainException("Target strand in chain file must be \"+\" or \"-\" at "+header);
			}
		}

		fields = br.readLine().split("\\s+");
		blocks = new ArrayList<List<Integer>>();

		//If lines are of length 3 then these are assumed to be blocks and added to the array list
		//
		//File : 365	72	72
		//Meaning : SizeOfAlignmentBlock SourceGapLength TargetGapLength

		while (fields.length == 3) {
			size = Integer.parseInt(fields[0]);
			sgap = Integer.parseInt(fields[1]);
			tgap = Integer.parseInt(fields[2]);

			ArrayList<Integer> al = new ArrayList<Integer>();
			if(targetStrand.equals("+")){
				al.add(sfrom);
				al.add(sfrom + size);
				al.add(tfrom);
				al.add(tfrom + size);
				al.add(plus);//add strand information for source
				al.add(plus);//add strand information for target
				blocks.add(al);
			}

			if(targetStrand.equals("-")){
				al.add(sfrom);
				al.add(sfrom + size);
				al.add(targetSize - (tfrom + size));
				al.add(targetSize-tfrom);
				al.add(plus);
				al.add(minus);
				blocks.add(al);
			}
			sfrom += size + sgap;
			tfrom += size + tgap;
			fields = br.readLine().split("\\s+");
		}

		//Check the invalid lines and throw an exception
		if (fields.length != 1) {
			throw new ChainException("Invalid chain file "+ header);
		}
		size = Integer.parseInt(fields[0]);
		ArrayList<Integer> al = new ArrayList<Integer>();
		if(targetStrand.equals("+")){
			al.add(sfrom);
			al.add(sfrom + size);
			al.add(tfrom);
			al.add(tfrom + size);
			al.add(plus);//add strand information for source
			al.add(plus);//add strand information for target
			blocks.add(al);
		}

		if(targetStrand.equals("-")){
			al.add(sfrom);
			al.add(sfrom + size);
			al.add(targetSize - (tfrom + size));
			al.add(targetSize-tfrom);
			al.add(plus);
			al.add(minus);
			blocks.add(al);
		}
		// If size of the chain exceeds the sourceChromosome or targetChromosome length then throw exception
		if ((sfrom + size) != sourceEnd || (tfrom + size) != targetEnd) {

			throw new ChainException("Source or target chain length exceeds expected length");

		}

		// Return the chain object with all the blocks information
		return new Chain(score, sourceName, sourceSize, sourceStrand,
				sourceStart, sourceEnd, targetName, targetSize, targetStrand,
				targetStart, targetEnd, id, sfrom, tfrom, blocks, size, sgap,
				tgap);

	}



	//Convert the queried coordinate from source build to target build
	public SortedSet<BedInterval> convertCoordinate(
			HashMap<String, IntervalTree<List<Object>>> chainIndex, String qchromosome,
			int qstart, int qstop, String qstrand) throws ChainException {

		SortedSet<BedInterval> results = new TreeSet<BedInterval>();
		BedInterval qbedInterval = new BedInterval(qchromosome,qstart,qstop,qstrand);
		
		//Query the position to the interval tree made of chains which returns the overlapping interval
		List<Interval<List<Object>>> queryResults = query(chainIndex, qchromosome, qstart, qstop);


		if(queryResults.isEmpty()){
			System.out.println("unmapped!!");
		}else{
			for(Interval<List<Object>> result : queryResults){
				results = addResults(results, result, qbedInterval);				
			}
		}
		return results;
	}


	public SortedSet<BedInterval> addResults(SortedSet<BedInterval> results, Interval<List<Object>> result, BedInterval qbedInterval) throws ChainException{

		Map<String, String> strandMap = new TreeMap<>();
		strandMap.put("+", "-");
		strandMap.put("-", "+");
		sourceStart = (int)result.getStart();
		sourceEnd = result.getEnd();
		targetName = (String) result.getData().get(4);
		targetStart = (int)result.getData().get(0);
		targetEnd = (int)result.getData().get(1);
		targetStrand =  (int)result.getData().get(3) == 1?"+":"-";
		BedInterval bi = intersectBed(qbedInterval, new BedInterval(qbedInterval.getChromosome(), (int)sourceStart, (int)sourceEnd));
		int lOffset = (int)Math.abs(bi.getStart()-(int)sourceStart);
		int size = (int)Math.abs(bi.getStop()-bi.getStart());
		int resStart = 0;
		if(targetStrand.equals("+")){
			resStart = targetStart + lOffset;
			if(qbedInterval.getStrand().equals("+")){
				results.add(new BedInterval(targetName, resStart, resStart+size, targetStrand));
			}else{
				results.add(new BedInterval(targetName, resStart, resStart+size, strandMap.get(targetStrand)));
			}

		}else if(targetStrand.equals("-")){
			resStart = targetEnd - lOffset - size;
			if(qbedInterval.getStrand().equals("+")){
				results.add(new BedInterval(targetName, resStart, resStart+size, targetStrand));
			}else{
				results.add(new BedInterval(targetName, resStart, resStart+size, strandMap.get(targetStrand)));
			}
		}else{
			throw new ChainException("Unknown Strand !!");
		}

		return results;

	}


	public static BedInterval intersectBed(BedInterval one, BedInterval two) throws ChainException{

		if(one.getStart() > one.getStop() || two.getStart() > two.getStop()){
			throw new ChainException("Start Can not be greater than stop.");
		}
		if(!one.getChromosome().equals(two.getChromosome())){
			return new BedInterval();
		}
		if(one.getStart() > two.getStop() || one.getStop() < two.getStart()){
			return new BedInterval();
		}

		return new BedInterval(one.getChromosome(),max((int)one.getStart(),(int)two.getStart()),min((int)one.getStop(),(int)two.getStop()));

	}

	public static Integer max(Integer i, Integer j){

		if(i > j){
			return i;
		}else{
			return j;
		}


	}
	public static Integer min(Integer i, Integer j){

		if(i < j){
			return i;
		}else{
			return j;
		}


	}

	public List<Chain>  liftOverChain(File chainPath) throws IOException, ChainException{

		BufferedReader br = new BufferedReader(new FileReader(chainPath));
		String line = "";
		List<Chain> chains = new ArrayList<Chain>();
		Chain cfu = new Chain();


		while((line = br.readLine())!= null){

			if(line.isEmpty() || line.startsWith("#")||line.startsWith("\n|\r")){
				continue;
			}

			if(line.startsWith("chain")){
				chains.add(cfu.parseChain(line, br));	
			}

		}
		return chains;

	}

	public HashMap<String,IntervalTree<List<Object>>> indexChains(List<Chain> chains) throws ChainException{

		HashMap<String, Integer> sourceSize = new HashMap<String, Integer>();
		HashMap<String, Integer> targetSize = new HashMap<String, Integer>();
		HashMap<String, IntervalTree<List<Object>>> chainIndex = new HashMap<String, IntervalTree<List<Object>>>();


		for(Chain c: chains){

			if(!sourceSize.containsKey(c.sourceName)){
				sourceSize.put(c.sourceName, c.sourceSize);
			}
			if(sourceSize.get(c.sourceName) != c.sourceSize)
				throw new ChainException("Source size do not match for : "+c);

			if(!targetSize.containsKey(c.targetName)){
				targetSize.put(c.targetName, c.targetSize);
			}
			if(targetSize.get(c.targetName) != c.targetSize)
				throw new ChainException("Target size do not match for : "+c);

			IntervalTree<List<Object>> tree;
			if(chainIndex.containsKey(c.sourceName)){
				tree = chainIndex.get(c.sourceName);
			}else{
				tree =  new IntervalTree<List<Object>>();
			}

			for(List<Integer> temp: c.blocks){	 

				List<Object> lo = new ArrayList<>();

				lo.add(temp.get(2));// add target start
				lo.add(temp.get(3));// add target stop
				lo.add(c);// add chain;
				lo.add(temp.get(5));// add target strand
				lo.add(c.targetName);
				
				Interval<List<Object>> i = new Interval<List<Object>>(temp.get(0),temp.get(1),lo);
				tree.addInterval(i);
			
			}

			chainIndex.put(c.sourceName, tree);
		}
		return chainIndex;
	}


	public List<Interval<List<Object>>> query(HashMap<String,IntervalTree<List<Object>>>  chainIndex ,String chromosome, int start, int stop) throws ChainException{

		if(!chainIndex.containsKey(chromosome)){
			throw new ChainException("chain file does not contain chromosome "+chromosome);
		}
		IntervalTree<List<Object>> it = chainIndex.get(chromosome);
		List<Interval<List<Object>>> results = it.get(start, stop);
		return results;
	}


}
