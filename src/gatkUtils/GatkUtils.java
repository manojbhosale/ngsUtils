package gatkUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GatkUtils {
	
	public static File logFile = new File("D:\\SureCall\\SureCall4111\\GATKcomparison\\Autommation\\gatkProcess.log");
	public static Path configPath = Paths.get("D:\\SureCall\\SureCall4111\\GATKcomparison\\Autommation\\config.json");
	//public static Path javaPath = Paths.get("java");
	public static Path gatkPath = Paths.get("D:\\SureCall\\SureCall4111\\GATKcomparison\\tools\\GenomeAnalysisTK.jar");
	public static Path picardPath = Paths.get("D:\\SureCall\\SureCall4111\\GATKcomparison\\tools\\picard.jar");
	public static String maxHeapSpace = "-Xmx4g";
	public static Path inputBam = Paths.get("D:\\SureCall\\SureCall4111\\GATKcomparison\\L004_I003_chr22_child.bam");
	public static Path referencePath = Paths.get("D:\\SureCall\\SURECALL_VALID\\Surecall_Test_plans\\Genomeanalysis_softwares\\IGV\\IGVTools\\hg19\\hg19.fasta");
	public static Path targetBedFile = Paths.get("D:\\SureCall\\SureCall4111\\GATKcomparison\\targets_subset_Exons.bed");
	
	
	
	public static void main(String args[]) throws IOException {
		readConfigJson(Paths.get("D:\\SureCall\\SureCall4111\\GATKcomparison\\Autommation\\config.json"));
		if(logFile.createNewFile()) {
			System.out.println("Created file successfully !!"); 
		}
		
		Thread th = new Thread(new GatkThread());
		
		th.start();
	}
	
	
	public static JSONArray readConfigJson(Path configPath) {
		
		 JSONParser jsonParser = new JSONParser();
		 JSONArray gatkSteps = null;
	        try (FileReader reader = new FileReader(configPath.toFile()))
	        {
	            //Read JSON file
	            Object obj = jsonParser.parse(reader);
	 
	            gatkSteps = (JSONArray) obj;
//	            System.out.println(gatkSteps);
	             
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
		
		return gatkSteps;
	}

}
