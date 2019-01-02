package vcfutils;

import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.GenotypesContext;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TsTvStatistics implements Runnable{

	public Double tstvRatio;
	public Integer numTransitions;
	public Integer numTransversions;
	public String anaType;

	public TsTvStatistics(String type) {
		super();
		this.tstvRatio = 0.0;
		this.numTransitions = 0;
		this.numTransversions = 0;
		this.anaType = type;

	}

	public List<String> transitions = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("AG");
			add("GA");
			add("TC");
			add("CT");
		}
	};

	public List<String> transversions = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add("AC");
			add("CA");
			add("AT");
			add("TA");
			add("GC");
			add("CG");
			add("GT");
			add("TG");
		}
	};

	public static void main(String args[]) {
		File vcfFile = new File(
				"E:\\SURECALL_VALID\\Surecall_Test_plans\\Genomeanalysis_softwares\\SnpEffect\\SnpEffectLatest\\snpEff\\1_AAACATCG_L001_R1_26Aug2016_16_50_02_350_1472212681544.vcf");
		//TsTvStatistics ts = new TsTvStatistics();
		/*
		 * System.out.println(ts.isTransition("A","T"));
		 * System.out.println(ts.isTransversion("A","T"));
		 */
		// ts.calculateTsTvRatio(vcfFile);
		/*
		 * for(VariantContext vc: vcfr){ System.out.println(vc.getContig());
		 * System
		 * .out.println(vc.getReference()+" "+vc.getAlternateAlleles()+" "+
		 * vc.ge); }
		 */
		//System.out.println(ts.calculateTsTvRatio(vcfFile));
	}

	public String[] calculateTsTvRatio(File vcfPath) {
		VCFFileReader vcfr = new VCFFileReader(vcfPath, false);
		//TsTvStatistics ts = new TsTvStatistics();

		for (VariantContext vcont : vcfr) {

			// System.out.println(vcont.getContig());
			// System.out.println(vcont.getReference()+" "+vcont.getAlternateAlleles()+" "+vcont.getGenotypes());

			if(!vcont.getType().toString().equals("SNP")){
				continue;
			}

			GenotypesContext gc = vcont.getGenotypes();
			Genotype gt = gc.get(0);

			if (this.anaType.equals("HOM")
					&& gt.getType().toString().equals("HOM_VAR")) {
				String[] st = gt.getGenotypeString().split("/");
				String ref = vcont.getReference().toString().replace("*","");
				Object[] alt = vcont.getAlternateAlleles().toArray();
				//System.out.println(ref+" "+alt[0]);
				if (isTransition(ref, alt[0].toString())) {
					numTransitions += 2;
				} else {
					numTransversions += 2;
				}

			}else if(this.anaType.equals("HET")
					&& gt.getType().toString().equals("HET")) {
				String[] st = gt.getGenotypeString().split("/");
				String ref = vcont.getReference().toString().replace("*","");
				Object[] alt = vcont.getAlternateAlleles().toArray();
				//System.out.println(ref+" "+alt[0]);
				if (isTransition(ref, alt[0].toString())) {
					numTransitions += 1;
				} else {
					numTransversions += 1;
				}
			}

			if (this.anaType.equals("BOTH")) {

				String[] st = gt.getGenotypeString().split("/");
				String ref = vcont.getReference().toString().replace("*","");
				Object[] alt = vcont.getAlternateAlleles().toArray();
				//System.out.println(ref+" "+alt[0]);
				if (gt.getType().toString().equals("HOM_VAR")) {
					if (isTransition(ref, alt[0].toString())) {
						numTransitions += 2;
					} else {
						numTransversions += 2;
					}
				} else {
					if (isTransition(ref, alt[0].toString())) {
						numTransitions += 1;
					} else {
						numTransversions += 1;
					}
				}
			}
			
			

			if(this.anaType.equals("BOTH(ONLY COUNT)")){
				String[] st = gt.getGenotypeString().split("/");
				String ref = vcont.getReference().toString().replace("*","");
				Object[] alt = vcont.getAlternateAlleles().toArray();
				//System.out.println(ref+" "+alt[0]);
			
					if (isTransition(ref, alt[0].toString())) {
						numTransitions += 1;
						System.out.println(ref+" "+alt[0].toString());
					} else {
						numTransversions += 1;
					}
				
			}
			
		}

		//System.out.println(numTransitions);
		//System.out.println(numTransversions);
		
		double tstvratio = (double) numTransitions / numTransversions;;

		String[] results = new String[]{String.format("%.3f", tstvratio), numTransitions.toString(), numTransversions.toString() };
		
		return results;
	}

	public boolean isHom(Genotype g) {

		return false;
	}

	public boolean isHet(Genotype g) {

		return false;
	}

	public boolean isTransition(String ref, String alt) {

		return transitions.contains(ref + "" + alt);

	}

	public boolean isTransversion(String ref, String alt) {

		return transversions.contains(ref + "" + alt);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
