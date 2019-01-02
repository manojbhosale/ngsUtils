package vcfutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.Options;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder.OutputType;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public class CompareUtils {


	private static final boolean CREATE_INDEX = false;
/*
	public static void main(String args[]) throws IOException{

		
		String destinationFolder;
		File file1 = new File(args[0]);
		File file2 = new File(args[1]);
		
		if(args.length == 3){
			destinationFolder = args[2];
		}else{
			File f = new File("comparisonResults");
			if(!f.exists()){
				f.mkdir();
			}
			destinationFolder = f.getAbsolutePath();
			System.out.println(destinationFolder);
		}


		//VCFFileReader vcr1 = new VCFFileReader(new File("\\\\PTD09449\\CommonStorage\\WorkflowOutput\\NA10831_13Dec2015_19_15_31_967\\NA10831_13Dec2015_19_15_31_967.vcf"));
		//VCFFileReader vcr2 = new VCFFileReader(new File("\\\\PTD09449\\CommonStorage\\WorkflowOutput\\1_AAACATCG_L001_R1_13Dec2015_19_13_30_877\\input1.vcf"));
		VCFFileReader vcr1 = new VCFFileReader(file1);
		VCFFileReader vcr2 = new VCFFileReader(file2);


		ArrayList<VCFrecord> hmvc = new ArrayList<VCFrecord>();
		for(VariantContext vc : vcr1){

			//System.out.println(vc.getChr()+" "+vc.getStart()+" "+vc.getReference()+" "+vc.getAlternateAllele(0)+" "+vc.getPhredScaledQual()+" "+vc.getFilters()+" "+vc.getAttributes());
			String refAllele = (vc.getReference()).toString().replace("*", "");
			//System.out.println(refAllele);
			VCFrecord vcr = new VCFrecord(vc.getChr(), refAllele , (vc.getAlternateAllele(0)).toString(),  Long.valueOf(vc.getStart()),vc,"FN");
			hmvc.add(vcr);
			//System.out.println(vcr);
		}

		
		for(VCFrecord vcr : hmvc){

			System.out.println(vcr);

		}

		
		File f1 = new File(destinationFolder+"/common.vcf");
		File f2 = new File(destinationFolder+"/snpOld.vcf");
		File f3 = new File(destinationFolder+"/insOld.vcf");
		File f4 = new File(destinationFolder+"/delOld.vcf");
		File f5 = new File(destinationFolder+"/snpNew.vcf");
		File f6 = new File(destinationFolder+"/insNew.vcf");
		File f7 = new File(destinationFolder+"/delNew.vcf");
		f1.createNewFile();
		f2.createNewFile();
		f3.createNewFile();
		f4.createNewFile();
		f5.createNewFile();
		f6.createNewFile();
		f7.createNewFile();


		VCFHeader outputHeader = vcr1.getFileHeader();

		final EnumSet<Options> options = CREATE_INDEX ? EnumSet.of(Options.INDEX_ON_THE_FLY) : EnumSet.noneOf(Options.class);
		options.add(Options.ALLOW_MISSING_FIELDS_IN_HEADER);

		final VariantContextWriter pwCommon = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f1).setOptions(options).setOutputFileType(OutputType.VCF).build();
		final VariantContextWriter pwSNPOldexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f2).setOptions(options).setOutputFileType(OutputType.VCF).build();
		final VariantContextWriter pwInsOldexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f3).setOptions(options).setOutputFileType(OutputType.VCF).build();
		final VariantContextWriter pwDelOldexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f4).setOptions(options).setOutputFileType(OutputType.VCF).build();
		final VariantContextWriter pwSNPNewexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f5).setOptions(options).setOutputFileType(OutputType.VCF).build();
		final VariantContextWriter pwInsNewexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f6).setOptions(options).setOutputFileType(OutputType.VCF).build();
		final VariantContextWriter pwDelNewexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f7).setOptions(options).setOutputFileType(OutputType.VCF).build();




		pwSNPOldexclusive.writeHeader(outputHeader);
		pwInsOldexclusive.writeHeader(outputHeader);
		pwDelOldexclusive.writeHeader(outputHeader);
		pwSNPNewexclusive.writeHeader(outputHeader);
		pwInsNewexclusive.writeHeader(outputHeader);
		pwDelNewexclusive.writeHeader(outputHeader);
		pwCommon.writeHeader(outputHeader);

		
	    for (final VariantContext variantContext : sortedOutput) {
	        out.add(variantContext);
	        writeProgress.record(variantContext.getChr(), variantContext.getStart());
	    }
	    out.close();
		
		List<String> typeList = new ArrayList<String>();
		typeList.add("SNP");
		typeList.add("Insertion");
		typeList.add("Deletion");
		 


		for(VariantContext vc : vcr2){
			String refAllele = (vc.getReference()).toString().replace("*", "");

			VCFrecord vcr = new VCFrecord(vc.getChr(),refAllele, (vc.getAlternateAllele(0)).toString(),  Long.valueOf(vc.getStart()),vc,"FP");
			//System.out.println(vcr);
			if(hmvc.contains(vcr)){
				
				int temp = hmvc.indexOf(vcr);
				VCFrecord vcrc = hmvc.get(temp);
				vcrc.setValidationType("TP");
				hmvc.set(temp, vcrc);
			}else{			
				hmvc.add(vcr);

			}
		}


		for(VCFrecord vcr : hmvc){
			if(vcr.getValidationType().equals("TP")){

				pwCommon.add(vcr.getContext());

			}else if(vcr.getValidationType().equals("FP")){

				//if(vcr.getContext().isSNP()){
				if(vcr.getType().equals("SNP")){
					pwSNPNewexclusive.add(vcr.getContext());
				}else if(vcr.getType().equals("Insertion")){
					pwInsNewexclusive.add(vcr.getContext());
				}else if(vcr.getType().equals("Deletion")){
					pwDelNewexclusive.add(vcr.getContext());
				}


			}else if(vcr.getValidationType().equals("FN")){

				if(vcr.getType().equals("SNP")){
					pwSNPOldexclusive.add(vcr.getContext());
				}else if(vcr.getType().equals("Insertion")){
					pwInsOldexclusive.add(vcr.getContext());
				}else if(vcr.getType().equals("Deletion")){
					pwDelOldexclusive.add(vcr.getContext());
				}




			}

		}
		vcr1.close();
		vcr2.close();

		pwCommon.close();
		pwSNPNewexclusive.close();
		pwInsNewexclusive.close();
		pwDelNewexclusive.close();
		pwSNPOldexclusive.close();
		pwInsOldexclusive.close();
		pwDelOldexclusive.close();

		VariantContext vc = null;
		VCFrecord vr = new VCFrecord("chr1","A", "T",Long.valueOf(100) ,vc,"FP");
		VCFrecord vr1 = new VCFrecord("chr2","A", "T",Long.valueOf(100) ,vc,"FP");
		VCFrecord vr2 = new VCFrecord("chr3","A", "T",Long.valueOf(100) ,vc,"FP");
		VCFrecord vr3 = new VCFrecord("chr4","A", "T",Long.valueOf(100) ,vc,"FP");

		ArrayList<VCFrecord> hmvc1 = new ArrayList<VCFrecord>();
		hmvc1.add(vr);
		hmvc1.add(vr1);
		hmvc1.add(vr2);
		hmvc1.add(vr3);

		System.out.println(hmvc1.contains(new VCFrecord("chr4","A", "T",Long.valueOf(100) ,vc,"FP")));
	}

*/
	public ComparisonResult compareVcfs(File vcfOne, File vcfTwo) throws IOException{
	
			File f = new File("C:\\gatKAutomator\\comparisonResults");
			if(!f.exists()){
				f.mkdir();
			}
			String destinationFolder = f.getAbsolutePath();
			System.out.println(destinationFolder);
		
			VCFFileReader vcr1 = new VCFFileReader(vcfOne,false);
			VCFFileReader vcr2 = new VCFFileReader(vcfTwo,false);
			
			String fileOneName = vcfOne.getName().replaceAll("\\..*", "");
			String fileTwoName = vcfTwo.getName().replaceAll("\\..*", ""); 
			

			ArrayList<VCFrecord> hmvc = new ArrayList<VCFrecord>();
			for(VariantContext vc : vcr1){


				String refAllele = (vc.getReference()).toString().replace("*", "");

				VCFrecord vcr = new VCFrecord(vc.getChr(), refAllele , (vc.getAlternateAllele(0)).toString(),  Long.valueOf(vc.getStart()),vc,"FN");
				hmvc.add(vcr);

			}
			
			File f1 = new File(destinationFolder+"/"+fileOneName+"_common.vcf");
			File f2 = new File(destinationFolder+"/"+fileOneName+"_snpOld.vcf");
			File f3 = new File(destinationFolder+"/"+fileOneName+"_insOld.vcf");
			File f4 = new File(destinationFolder+"/"+fileOneName+"_delOld.vcf");
			File f5 = new File(destinationFolder+"/"+fileTwoName+"_snpNew.vcf");
			File f6 = new File(destinationFolder+"/"+fileTwoName+"_insNew.vcf");
			File f7 = new File(destinationFolder+"/"+fileTwoName+"_delNew.vcf");
			f1.createNewFile();
			f2.createNewFile();
			f3.createNewFile();
			f4.createNewFile();
			f5.createNewFile();
			f6.createNewFile();
			f7.createNewFile();

			VCFHeader outputHeader = vcr1.getFileHeader();
			
			final EnumSet<Options> options = CREATE_INDEX ? EnumSet.of(Options.INDEX_ON_THE_FLY) : EnumSet.noneOf(Options.class);
			options.add(Options.ALLOW_MISSING_FIELDS_IN_HEADER);

			final VariantContextWriter pwCommon = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f1).setOptions(options).setOutputFileType(OutputType.VCF).build();
			final VariantContextWriter pwSNPOldexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f2).setOptions(options).setOutputFileType(OutputType.VCF).build();
			final VariantContextWriter pwInsOldexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f3).setOptions(options).setOutputFileType(OutputType.VCF).build();
			final VariantContextWriter pwDelOldexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f4).setOptions(options).setOutputFileType(OutputType.VCF).build();
			final VariantContextWriter pwSNPNewexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f5).setOptions(options).setOutputFileType(OutputType.VCF).build();
			final VariantContextWriter pwInsNewexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f6).setOptions(options).setOutputFileType(OutputType.VCF).build();
			final VariantContextWriter pwDelNewexclusive = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f7).setOptions(options).setOutputFileType(OutputType.VCF).build();
			
			pwSNPOldexclusive.writeHeader(outputHeader);
			pwInsOldexclusive.writeHeader(outputHeader);
			pwDelOldexclusive.writeHeader(outputHeader);
			pwSNPNewexclusive.writeHeader(outputHeader);
			pwInsNewexclusive.writeHeader(outputHeader);
			pwDelNewexclusive.writeHeader(outputHeader);
			pwCommon.writeHeader(outputHeader);

			for(VariantContext vc : vcr2){
				String refAllele = (vc.getReference()).toString().replace("*", "");

				@SuppressWarnings("deprecation")
				VCFrecord vcr = new VCFrecord(vc.getChr(),refAllele, (vc.getAlternateAllele(0)).toString(),  Long.valueOf(vc.getStart()),vc,"FP");
				//System.out.println(vcr);
				if(hmvc.contains(vcr)){
					
					int temp = hmvc.indexOf(vcr);
					VCFrecord vcrc = hmvc.get(temp);
					vcrc.setValidationType("TP");
					System.out.println(vcrc.equals(vcr));
					System.out.println("HashCodes: "+vcrc.hashCode()+" "+vcr.hashCode());
					hmvc.set(temp, vcrc);
				}else{			
					hmvc.add(vcr);

				}
			}

			int commonCalls = 0;
			int newCalls = 0;
			int missedCalls = 0;
			int missedSnps = 0;
			int newSnps = 0;
			int missedInsertions = 0;
			int newInsertions = 0;
			int missedDeletions = 0;
			int newDeletions = 0;
			
			//System.out.println(pwCommon);
			for(VCFrecord vcr : hmvc){
				if(vcr.getValidationType().equals("TP")){
					//System.out.println(vcr.getContext());
					pwCommon.add(vcr.getContext());
					commonCalls++;

				}else if(vcr.getValidationType().equals("FP")){
					newCalls++;
					//if(vcr.getContext().isSNP()){
					if(vcr.getType().equals("SNP")){
						pwSNPNewexclusive.add(vcr.getContext());
						newSnps++;
					}else if(vcr.getType().equals("Insertion")){
						newInsertions++;
						pwInsNewexclusive.add(vcr.getContext());
					}else if(vcr.getType().equals("Deletion")){
						newDeletions++;
						pwDelNewexclusive.add(vcr.getContext());
					}
					
					

				}else if(vcr.getValidationType().equals("FN")){
					missedCalls++;
					if(vcr.getType().equals("SNP")){
						pwSNPOldexclusive.add(vcr.getContext());
						missedSnps++;
					}else if(vcr.getType().equals("Insertion")){
						missedInsertions++;
						pwInsOldexclusive.add(vcr.getContext());
					}else if(vcr.getType().equals("Deletion")){
						pwDelOldexclusive.add(vcr.getContext());
						missedDeletions++;
					}
				}

			}
			vcr1.close();
			vcr2.close();

			pwCommon.close();
			pwSNPNewexclusive.close();
			pwInsNewexclusive.close();
			pwDelNewexclusive.close();
			pwSNPOldexclusive.close();
			pwInsOldexclusive.close();
			pwDelOldexclusive.close();

			ComparisonResult cr = new ComparisonResult(vcfOne.getName(),vcfTwo.getName(),commonCalls, newCalls, missedCalls, missedSnps,newSnps,missedInsertions,newInsertions,missedDeletions,newDeletions);
			//System.out.println(cr);
		return cr;
	}
}
