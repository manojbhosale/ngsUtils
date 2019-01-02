package hsutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class BamSorter implements Runnable{
	
	public static String sortError = "";
	private File file;
	private String replaced;
	private Logger log4jLog;
	private Thread t;
	private String threadName="Sorting";
	
	public BamSorter(File file, String replaced, Logger log4jLog){
		
		this.file = file;
		this.replaced = replaced;
		this.log4jLog = log4jLog;
	
	}
	
	

	public void run() {
		// TODO Auto-generated method stub
		int errorFlag = 0;
		Process p = null;
		String s;
		try{
			p = Runtime.getRuntime().exec("java -jar E:/SURECALL_VALID/Surecall_Test_plans/Genomeanalysis_softwares/picard/picard-tools-1.130/picard-tools-1.130/picard.jar SortSam INPUT="+file.getAbsolutePath()+" OUTPUT="+file.getParent()+"/"+replaced+" SORT_ORDER=coordinate");


			BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));

			// read the output from the command
			// System.out.println("Here is the standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				//log.append(s+"\n");
				// log4jLog.debug("s");
				//System.out.println(s);

			}

			// read any errors from the attempted command
			// System.out.println("Here is the standard error of the command (if any):\n");
			String errorString="Unknown Error !!";
			while ((s = stdError.readLine()) != null) {
				//log.append(s+"\n");
				log4jLog.debug(s);
				System.out.println(s);
				if(Pattern.compile(Pattern.quote(" error "), Pattern.CASE_INSENSITIVE).matcher(s).find()){
					errorFlag = 1;	
					sortError = s;
				}
			}
			
		}catch(IOException e){

			e.printStackTrace();
		}
		
	}
	
	 public void start ()
	   {
	      System.out.println("Starting " +  threadName );
	      if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }

}
