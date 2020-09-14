package vcfutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.Options;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder.OutputType;
import htsjdk.variant.vcf.VCFEncoder;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;
import htsjdk.variant.vcf.VCFUtils;

public class VCFBedIntersect {


	public static void main(String[] args) {
		Path inputVcfFile = Paths.get("C:\\Users\\manojkumar_bhosale\\OneDrive - Persistent Systems Limited\\AIO_HS2_investigation\\VCFsForIntersection\\input1_20200525183216332_snpOld.vcf");
		Path inputBedFile = Paths.get("C:\\Users\\manojkumar_bhosale\\OneDrive - Persistent Systems Limited\\AIO_HS2_investigation\\Pos_Dedup\\CAIO18_H1\\CombinedInterval.txt");
		Path outputVcfFile = Paths.get("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete_1\\");

		/*try {
			intersectBedAndVCF(inputVcfFile, inputBedFile, outputVcfFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		simpleItersect(inputVcfFile, inputBedFile, outputVcfFile);

	}

	public static void intersectBedAndVCF(Path vcfPath, Path bedPath, Path outputFolder) throws IOException {

		VCFFileReader reader = null;
		//VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		File tempFile = vcfPath.toFile();
		File file2 = VCFUtils.createTemporaryIndexedVcfFromInput(tempFile, "temp_");

		Path outputVcf = outputFolder.resolve("intersected.vcf");


		//tempFile = VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		reader = new VCFFileReader(file2); 
		


		//VCFEncoder vcfEncoder = new VCFEncoder(reader.getFileHeader(), true, true);
		//final VariantContextWriter pwCommon = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f1).setOptions(options).setOutputFileType(OutputType.VCF).build();
		VCFHeader outputHeader = reader.getFileHeader();
		boolean CREATE_INDEX = true;
		final EnumSet<Options> options = CREATE_INDEX ? EnumSet.of(Options.INDEX_ON_THE_FLY) : EnumSet.noneOf(Options.class);
		options.add(Options.ALLOW_MISSING_FIELDS_IN_HEADER);
		final VariantContextWriter vcfWriter = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(outputVcf.toFile()).setOptions(options).setOutputFileType(OutputType.VCF).build();
		vcfWriter.writeHeader(outputHeader);
		try(BufferedReader br = new BufferedReader(new FileReader(bedPath.toFile()))){
			String line = "";
			//pw.println(reader.getFileHeader());
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				CloseableIterator<VariantContext> tempIter = reader.query(splited[0],Integer.parseInt(splited[1]),Integer.parseInt(splited[2]));
//				//System.out.println(splited[0]+""+Integer.parseInt(splited[1])+""+Integer.parseInt(splited[2]);

				while(tempIter.hasNext()) {
					//pw.println(vcfEncoder.encode(tempIter.next()));
					vcfWriter.add(tempIter.next());
					//System.out.println(tempIter.next()+" Manoj");
				}
				//pw.flush();
			}

			//vcfWriter.close();

		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			reader.close();
			vcfWriter.close();
		}

		file2.delete();
		File file4 = new File(file2.toString()+".idx");
		file4.delete();



	}
	
	public static void simpleItersect(Path vcfPath, Path bedPath, Path outputFolder) {
		
		


		//tempFile = VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		


		//VCFEncoder vcfEncoder = new VCFEncoder(reader.getFileHeader(), true, true);
		//final VariantContextWriter pwCommon = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f1).setOptions(options).setOutputFileType(OutputType.VCF).build();
	
		Map<String,List<Integer>> intervalMap = new HashMap<>();
		
		try(BufferedReader br = new BufferedReader(new FileReader(bedPath.toFile()))){
			String line = "";
			//pw.println(reader.getFileHeader());
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				String chromosome = splited[0];
				Integer start = Integer.parseInt(splited[1]);
				Integer stop = Integer.parseInt(splited[2]);
				List<Integer> tempLi = new ArrayList<>();
				tempLi.add(start);
				tempLi.add(stop);
				
				if(intervalMap.containsKey(chromosome)) {
					List<Integer> intervals = intervalMap.get(chromosome);
					intervals.add(start);
					intervals.add(stop);
				}else {
					List<Integer> intervals = new ArrayList<>();
					intervals.add(start);
					intervals.add(stop);
					intervalMap.put(chromosome, intervals);
				}
			}

			//vcfWriter.close();

		}catch(IOException e) {
			e.printStackTrace();
		}
		
		VCFFileReader reader = new VCFFileReader(vcfPath,false);
		
		
		CloseableIterator<VariantContext> iterator = reader.iterator();
		int total = 0;
		int intersecting  = 0;
		while(iterator.hasNext()) {
			total++;
			VariantContext context = iterator.next();
			int pos = context.getStart();
			if(intervalMap.containsKey(context.getContig())) {
				List<Integer> intervals = intervalMap.get(context.getContig());
				for (int i = 0; i < intervals.size(); i=i+2) {
					if(pos >= intervals.get(i) && pos <= intervals.get(i+1)) {
						intersecting++;
						break;
					}
				}
			}
			
		}

		
		System.out.println(vcfPath.getFileName()+"\tTotal="+total+"\tIntersecting="+intersecting);
		
		
	}

}
