package vcfutils;

import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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
	
	}
	
	
}
