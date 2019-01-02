package vcfutils;

import java.io.File;
import java.io.IOException;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

public class TestVCFreader {
	
	public static void main(String args[]) throws IOException{
		
		//TabixIndexCreator tic = new TabixIndexCreator(TabixFormat.VCF);
		//PrintWriter pw = new PrintWriter("C:\\Users\\manojkumar_bhosale\\Desktop\\ResultsComparison\\test.vcf.idx");
		
		File vcfOne = new File("C:\\Users\\manojkumar_bhosale\\Desktop\\CAIO17_A1_25Apr2018_14_54_15_529_1524648763832_VCF_4_2.vcf");
		VCFFileReader vcr1 = new VCFFileReader(vcfOne,false);
		long pos = 0;
		for(VariantContext vc : vcr1){		
			//tic.addFeature(vc, pos++);
			//System.out.println(vc.getReference()+"\t"+vc.getChr()+" "+vc.getStart()+"\t"+vc.getEnd()+"\t"+vc.getAlleles());
			System.out.println(vc.getGenotype(2).getGenotypeString());
		}
	//	pw.print(tic);
		//pw.close();
		
	}

}
