package vcfutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;

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
		Path inputVcfFile = Paths.get("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete_1\\EL_SCS_v6s_XT_Ver_121817_11_S11_R1.fastq_183_VCF_4_2.vcf");
		Path inputBedFile = Paths.get("D:\\ToDelete\\VCF_test\\SureSelect_Trio_TestDesign_1_AllTracks_covered.bed");
		Path outputVcfFile = Paths.get("C:\\Users\\manojkumar_bhosale\\Desktop\\ToDelete_1\\");

		try {
			intersectBedAndVCF(inputVcfFile, inputBedFile, outputVcfFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void intersectBedAndVCF(Path vcfPath, Path bedPath, Path outputFolder) throws IOException {

		VCFFileReader reader = null;
		//VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		File tempFile = vcfPath.toFile();

		Path outputVcf = outputFolder.resolve("intersected.vcf");


		//tempFile = VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		reader = new VCFFileReader(tempFile); 
		


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






	}

}
