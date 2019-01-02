package hsutils;

import htsjdk.samtools.DefaultSAMRecordFactory;
import htsjdk.samtools.SAMProgramRecord;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMValidationError;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;
import htsjdk.samtools.ValidationStringency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BAMParser implements Callable{

	private File bamPath;
	private String[] results;


	public BAMParser(File path){
		bamPath = path;
	}
	
	public BAMParser() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) throws MalformedURLException,
	FileNotFoundException, IOException {
		File bamFile = null;

		if(args.length == 1){
			bamFile = new File(args[0]);
		}else{
			System.out.println("User error: Please specify BAM file path !!");
		}


		//	File bamFile = new File(
		//			"E:\\NGS_DATA\\Barcode\\ValidationData\\MolecularBarcodeTestData\\test_w_extras_R1_01Aug2016_18_40_51_175_reHeader_Sorted__01Aug2016_18_40_51_175.bam");
		SamReader reader = null;
		BAMParser bp = new BAMParser(bamFile);

		// Open the SAM file for reading
		reader = bp.openSamExamples(bamFile);

		// Get the histogram data from header
		Integer freInt[] = bp.getHistogramData(reader);

		//System.out.printf("Percent duplicate molecules: %.3f %% \n",bp.getPercentDuplicates(reader));
		System.out.printf("Mean amplification level: %.3f \n",bp.getMeanAmplificationLevel(freInt));
		System.out.printf("Median amplification level: %d \n",bp.getMedianAmplificationLevel(freInt));


		reader.close();
	}

	public String[] getMetrics(File bamPath) throws MalformedURLException{
		SamReader reader = null;
		BAMParser bp = new BAMParser(bamPath);

		// Open the SAM file for reading
		reader = bp.openSamExamples(bamPath);

		// Get the histogram data from header
		Integer freInt[] = bp.getHistogramData(reader);
		String[] result = new String[3]; 
		//System.out.printf("Percent duplicate molecules: %.3f %% \n",bp.getPercentDuplicates(reader));
		//System.out.printf("Mean amplification level: %.3f \n",bp.getMeanAmplificationLevel(freInt));
		//System.out.printf("Median amplification level: %d \n",bp.getMedianAmplificationLevel(freInt));

		result[0] = String.format("%.3f %%", bp.getPercentDuplicates(reader));
		result[1] = String.format("%.3f", bp.getMeanAmplificationLevel(freInt));
		result[2] = String.format("%d", bp.getMedianAmplificationLevel(freInt));

		return result;

	}

	public SamReader openSamExamples(File filePath)
			throws MalformedURLException {

		/**
		 * With different reader options
		 */
		final SamReader readerFromConfiguredFactory = SamReaderFactory.make()
				.enable(SamReaderFactory.Option.DONT_MEMORY_MAP_INDEX)
				.validationStringency(ValidationStringency.SILENT)
				.samRecordFactory(DefaultSAMRecordFactory.getInstance())
				.open(filePath);

		return readerFromConfiguredFactory;

	}

	public Double getPercentDuplicates(SamReader reader) {

		int count = 0;
		long mergedSum = 0;
		long totalReads = 0;
		int numMerged = 0;
		long rowCount = 0;
		for (SAMRecord record : reader) {
			//			/System.out.println(record);
			
			
			List<SAMValidationError> valErrs = record.isValid();
			
			if (valErrs != null) {
				boolean okErr = true;
				for (Iterator iter=valErrs.iterator(); iter.hasNext(); ) {
					SAMValidationError err = (SAMValidationError) iter.next();
					if (err.getMessage().indexOf("SAMRecord not found in header") > 0) {
						okErr = okErr & true;
					} else {
						okErr = okErr & false;
					}
				}
				if (!okErr) {
					continue;
				}
			}
			
			if (record.getAttribute("XA") != null ) {
				if (((String)record.getAttribute("XA")).indexOf("chr") > -1) {
					//rowCount++;
					continue;

				}
			}
			
			if (record.getNotPrimaryAlignmentFlag()) {  // had to separate it out from above because of ion torrent data
			//	rowCount++;
				continue;
			}
			
			Object XI = record.getAttribute("XI");
			rowCount++;
			if(XI != null && XI instanceof Integer){
				totalReads = totalReads + (Integer)XI;
				
				Object al = record.getAttribute("al");
				if(null != al && al instanceof String){
					String alStr = (String) al;
					
					Pattern p = Pattern.compile("XI\\:i\\:(\\d+)");
					Matcher m = p.matcher(alStr);
					
					while(m.find()){
						int i  = Integer.parseInt(m.group(1));
						totalReads = totalReads + i;
					}
					
					
				}
				
			}else{
				++totalReads;
			}
			
			
			
			
		}

		// percent calculation
		Double percent = ((double) (totalReads - rowCount) / totalReads) * 100;
		//System.out.println(totalReads+" "+rowCount);
		return percent;

	}

	public Integer[] getHistogramData(SamReader reader) {

		List<SAMProgramRecord> l = reader.getFileHeader().getProgramRecords();
		List<String> freqStr = new ArrayList<String>();
		for (SAMProgramRecord sr : l) {
			if (sr.getAttribute("HG") != null) {
				String one = sr.getAttribute("HG");
				one = one.replace("[", "");
				one = one.replace("]", "");
				String[] test = one.split(",");
				for (int i = 0; i < test.length; i++) {
					freqStr.add(test[i]);
				}
			}
		}

		Integer[] freInt = new Integer[freqStr.size()];

		for (int i = 0; i < freqStr.size(); i++) {
			freInt[i] = Integer.parseInt(freqStr.get(i));
		}
		return freInt;

	}

	public Double getMeanAmplificationLevel(Integer[] inArr) {

		List<Integer> inList = Arrays.asList(inArr);

		Integer freqSum = inList.stream().mapToInt(e -> e).sum();
		Integer freqByLevel[] = new Integer[inArr.length];
		int count = 1;
		for (int i = 0; i < inList.size(); i++) {
			freqByLevel[i] = count * inList.get(i);
			count++;
		}
		Integer freqLevelSum = sum(freqByLevel);


		Double meanLevel = ((double) freqLevelSum / freqSum);

		return meanLevel;
	}

	public Integer getMedianAmplificationLevel(Integer[] inArr) {

		List<Integer> inList = Arrays.asList(inArr);

		Integer freqSum = inList.stream().mapToInt(e -> e).sum();

		Integer middleVal = freqSum / 2;
		Integer cumSum = 0, median = 0;

		for (int i = 0; i < inArr.length; i++) {
			if (cumSum > middleVal) {
				break;
			}
			cumSum += inArr[i];
			median++;
		}

		return median;
	}

	public Integer sum(Integer[] in) {

		Integer sum = 0;

		for (int i = 0; i < in.length; i++) {
			sum += in[i];
		}

		return sum;
	}

	public void printArray(Object[] in) {

		for (int i = 0; i < in.length; i++) {
			System.out.print(in[i] + " ");
		}
	}

	public String[] getResults() {
		return results;
	}

	public void setResults(String[] results) {
		this.results = results;
	}

	/*@Override
	public void run() {
		// TODO Auto-generated method stub

		results = new String[3];
		try {
			results = getMetrics(bamPath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	@Override
	public String[] call() throws Exception {
		// TODO Auto-generated method stub
		results = new String[3];
		try {
			results = getMetrics(bamPath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

}
