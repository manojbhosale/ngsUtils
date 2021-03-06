package vcfutils;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.Options;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder.OutputType;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.omg.CORBA.SystemException;

import com.psl.automation.main.Interval;


public class VCFUtils{

	private static int uniqueSnps;
	private static int uniqueInsertions;
	private static int uniqueDeletionsCov;
	private static int uniqueDeletions;

	private static final boolean CREATE_INDEX = false;


	public static void calculateUniqueSize(File vcfPath){

		ArrayList<VCFrecord> hmvc = new ArrayList<VCFrecord>();
		ArrayList<Interval> hmvcDel = new ArrayList<Interval>();

		VCFFileReader vcr = new VCFFileReader(vcfPath);

		for(VariantContext vc : vcr){

			String refAllele = (vc.getReference()).toString().replace("*", "");

			VCFrecord vcr1 = new VCFrecord(vc.getChr(),refAllele, (vc.getAlternateAllele(0)).toString(),  Integer.valueOf(vc.getStart()),vc,"FP");
			//System.out.println(vcr);
			//System.out.println(vc.getType());
			if(vcr1.getType().equals(VariantTypes.SNP)){
				if(hmvc.contains(vcr1)){
					//int temp = hmvc.indexOf(vcr);
					//VCFrecord vcrc = hmvc.get(temp);
					//System.out.println(vcr1);
					continue;
				}else{			
					hmvc.add(vcr1);
					uniqueSnps++;
				}
			}else if(vcr1.getType().equals(VariantTypes.INSERTION)){
				if(hmvc.contains(vcr1)){
					//int temp = hmvc.indexOf(vcr);
					//VCFrecord vcrc = hmvc.get(temp);
					System.out.println(vcr1);
					continue;
				}else{			
					hmvc.add(vcr1);
					uniqueInsertions++;
				}

			}else{
				Interval delInter = null;
				try{
					delInter = VCFrecord.getIntervalrepresentation(vcr1);
				}catch(Exception e){
					e.printStackTrace();
				}
				int res = Interval.getIntersectingIntervalIndex(hmvcDel, delInter);
				if(res != -1){

					Interval intersect = hmvcDel.get(res);
					long newStart = Interval.calculateMin(delInter, intersect);
					long newStop = Interval.calculateMax(delInter, intersect);
					Interval newinterval = new Interval(delInter.getChromosome(),newStart, newStop);
					hmvcDel.get(res).setChromosome(delInter.getChromosome());
					hmvcDel.get(res).setStart(newStart);
					hmvcDel.get(res).setStop(newStop);
					//System.out.println("Inside the deletion !!!");
					System.out.println(vcr1);
				}else{
					hmvcDel.add(delInter);
				}
			}
		}

		for(Interval interDel: hmvcDel){

			uniqueDeletions++;
			uniqueDeletionsCov += (interDel.getStop()-(interDel.getStart()+1));

		}
	}




	public static void main(String args[]) throws IOException{
		File f1 = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete_1\\NA16455_Job_611_VCF_4_2.vcf");
		File f2 = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete_1\\NA16455_24Jun2019_17_50_38_382_1561390769770_VCF_4_2.vcf");
		File file1 = createTemporaryIndexedVcfFromInput(f1, "A_1");
		File file2 = createTemporaryIndexedVcfFromInput(f2, "B_2");
		List<VariantContext> compareVcfAnnotations = compareVcfAnnotations(file1, file2);
		System.out.println("Manojkuamr !!");
		if(compareVcfAnnotations.size() == 0) {
			System.out.println("Same !!!");
		}else {
			for(VariantContext vc : compareVcfAnnotations) {
				System.out.println(vc);
			}
		}
		file1.delete();
		File file3 = new File(file1.toString()+".idx");
		file2.delete();
		File file4 = new File(file2.toString()+".idx");
		file3.delete();
		file4.delete();
		
	}


	public static List<VariantContext> compareVcfAnnotations(File vcf1, File vcf2) {

		VCFFileReader vcr1 = new VCFFileReader(vcf1, true);
		VCFFileReader vcr2 = new VCFFileReader(vcf2, true);
		List<VariantContext> diffVars = new ArrayList<>();	

		for(VariantContext vc : vcr1) {

			CloseableIterator<VariantContext> query = vcr2.query(vc);
			List<VariantContext> list = query.toList();
			
			if(list.size() > 1 || list.size() == 0) {
				continue;
			}
			
			//if(query.hasNext()) {
				boolean result = compareMaps(vc.getAttributes(),list.get(0).getAttributes());
				if(result == false) {
					diffVars.add(vc);
				}
			//}

		}
		vcr1.close();
		vcr2.close();
		return diffVars; 
	}


	public static boolean compareMaps(Map<String, Object> map1,Map<String, Object> map2) {
		
		// code applicable only when key is present with different values and not when a key is totally absent from one file 
		Set<String> keySet1 = map1.keySet();
		Set<String> keySet2 = map2.keySet();
		if(keySet1.size() != keySet2.size()) {
			return false;
		}
		/*for(String key1 : keySet1) {
			if(!keySet2.contains(key1)) {
				return false;
			}
		}*/
		
		for(String key :map1.keySet()) {
			Object value1 =  map1.get(key);
			Object value2 =  map2.getOrDefault(key, "default");
			if(value1 instanceof ArrayList) {
				ArrayList<Object> li = (ArrayList<Object>) value1; 
				ArrayList<Object> li1 = (ArrayList<Object>) value2;
				int index = 0;
				for(Object ele : li) {
					if(!ele.equals(li1.get(index))) {
						return false;
					}
					index++;
				}
			}
			if(value1.equals(value2)) {
				continue;
			}else {
				return false;
			}
		}

		return true;
	}

	public static File createTemporaryIndexedVcfFromInput(final File vcfFile, final String tempFilePrefix) throws IOException {
	    final String extension;

	    if (vcfFile.getAbsolutePath().endsWith(".vcf")) extension = ".vcf";
	    else if (vcfFile.getAbsolutePath().endsWith(".vcf.gz")) extension = ".vcf.gz";
	    else
	        throw new IllegalArgumentException("couldn't find a .vcf or .vcf.gz ending for input file " + vcfFile.getAbsolutePath());
	    File directory = vcfFile.getParentFile();
	    File output = createTemporaryIndexedVcfFile(tempFilePrefix, extension,directory);
	    final VCFFileReader reader = new VCFFileReader(vcfFile, false);
		VCFHeader outputHeader = reader.getFileHeader();
		boolean CREATE_INDEX = true;
		final EnumSet<Options> options = CREATE_INDEX ? EnumSet.of(Options.INDEX_ON_THE_FLY) : EnumSet.noneOf(Options.class);
		options.add(Options.ALLOW_MISSING_FIELDS_IN_HEADER);
		final VariantContextWriter vcfWriter = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(output).setOptions(options).setOutputFileType(OutputType.VCF).build();
		vcfWriter.writeHeader(outputHeader);
	    
	   /* final VCFFileReader in = new VCFFileReader(vcfFile, false);
	    final VCFHeader header = in.getFileHeader();

	    final VariantContextWriter out = new VariantContextWriterBuilder().
	            setReferenceDictionary(header.getSequenceDictionary()).
	            setOptions(EnumSet.of(Options.INDEX_ON_THE_FLY)).
	            setOutputFile(output).setOptions(EnumSet.of(Options.ALLOW_MISSING_FIELDS_IN_HEADER)).build();
	    out.writeHeader(header);*/
	    for (final VariantContext ctx : reader) {
	    	vcfWriter.add(ctx);
	    }
	    vcfWriter.close();
	    reader.close();
	    return output;
	}
	
	public static File createTemporaryIndexedVcfFile(String prefix, String extension, File directory) {
		File tempFile = null;
		
		try {
			tempFile = File.createTempFile(prefix, extension, directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tempFile;
	}
	
}
