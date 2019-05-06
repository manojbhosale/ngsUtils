package vcfutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import htsjdk.variant.vcf.VCFHeader;

public class TestVCFreader {

	public static void main(String args[]) throws IOException {

		Path folderPath = Paths.get("C:\\Users\\manojkumar_bhosale\\Desktop\\VCF_verify\\After");

		Stream<Path> paths = Files.walk(folderPath);

		//List<Path> files = new ArrayList<>();
		List<Path> files = paths.collect(Collectors.toList());
		System.out.println(files);
		paths.close();
		for(Path f  : files) {
			if(!Files.isRegularFile(f)) {
				continue;
			}
			//File vcfOne = new File(

	//				"C:\\Users\\manojkumar_bhosale\\Desktop\\VCF_verify\\before\\pair_cnv\\result_1553069977846_VCF_4_2.vcf");
			//"D:\\SureCall\\SureCall4111\\SureCallTestRun_4111\\2ndRun\\Zips\\TVN_CNVTumor124_03Dec2018_15_52_52_677\\result_1543835738246_VCF_4_2_1544071351137_VCF_4_2.vcf");
			System.out.println(f.toFile());
			VCFFileReader vcr1 = new VCFFileReader(f.toFile(), false);
			VCFHeader header = vcr1.getFileHeader();
			long pos = 0;
			for (VariantContext vc : vcr1) {
				vc.fullyDecode(header, false);
			}
		}
	}

	public void listAllFiles(String path){
		System.out.println("In listAllfiles(String path) method");
		try(Stream<Path> paths = Files.walk(Paths.get(path))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try {
						//readContent(filePath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
