package vcfutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder;
import htsjdk.variant.variantcontext.writer.VariantContextWriterBuilder.OutputType;
import htsjdk.variant.vcf.VCFEncoder;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFUtils;

public class VCFBedIntersect {


	public static void main(String[] args) {
		Path inputVcfFile = Paths.get("D:\\DelData\\NA12878.vcf");
		Path inputBedFile = Paths.get("D:\\DelData\\SSAllExonV5covered.bed");
		Path outputVcfFile = Paths.get("D:\\DelData\\result.vcf");

		try {
			intersectBedAndVCF(inputVcfFile, inputBedFile, outputVcfFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void intersectBedAndVCF(Path vcfPath, Path bedPath, Path outputVcf) throws IOException {

		VCFFileReader reader = null;
		//VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		File tempFile = vcfPath.toFile();




		//tempFile = VCFUtils.createTemporaryIndexedVcfFromInput(vcfPath.toFile(), "temp");
		reader = new VCFFileReader(tempFile);


		VCFEncoder vcfEncoder = new VCFEncoder(reader.getFileHeader(), true, true);
		//final VariantContextWriter pwCommon = new VariantContextWriterBuilder().setReferenceDictionary(outputHeader.getSequenceDictionary()).setOutputFile(f1).setOptions(options).setOutputFileType(OutputType.VCF).build();

		try(BufferedReader br = new BufferedReader(new FileReader(bedPath.toFile()));PrintWriter pw = new PrintWriter(outputVcf.toFile())){
			String line = "";
			pw.println(reader.getFileHeader());
			while((line = br.readLine()) != null) {
				if(line.startsWith("#")) {
					continue;
				}
				String[] splited = line.split("\t");
				CloseableIterator<VariantContext> tempIter = reader.query(splited[0],Integer.parseInt(splited[1]),Integer.parseInt(splited[2]));
//				/System.out.println(splited[0]+""+Integer.parseInt(splited[1])+""+Integer.parseInt(splited[2]);

				while(tempIter.hasNext()) {
					pw.println(vcfEncoder.encode(tempIter.next()));
					//System.out.println(tempIter.next()+" Manoj");
				}
				pw.flush();
			}



		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			reader.close();
		}






	}

}
