package gatkUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GatkThread implements Runnable {

	@Override
	public void run() {

		JSONArray stepsArray = GatkUtils.readConfigJson(GatkUtils.configPath);
		Iterator iter = stepsArray.iterator();

		String inputFileNameNoExt = FilenameUtils.getBaseName(GatkUtils.inputBam.getFileName().toString());
		Path inputFileLocation = GatkUtils.inputBam.getParent();
		String origInputFileName = GatkUtils.inputBam.getFileName().toString();
		System.out.println(inputFileNameNoExt);
		System.out.println(inputFileLocation);
		System.out.println(origInputFileName);
		// run sort on input file
		String stepFileName = inputFileNameNoExt + ".sorted.bam";
		String listFileName = "";
		String recalGrpFile = "";

		String command = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe " + GatkUtils.maxHeapSpace + " -jar "
				+ GatkUtils.picardPath + " SortSam " + " SO=coordinate " + " I=" + GatkUtils.inputBam + " O="
				+ inputFileLocation.resolve(stepFileName);
		// System.out.println(command);

		Runtime r = Runtime.getRuntime();

		try {
			Process p2 = r.exec(command);

			StreamGobbler errGobler = new StreamGobbler(p2.getErrorStream(), "ERROR");
			StreamGobbler opGobler = new StreamGobbler(p2.getInputStream(), "OUTPUT");
			errGobler.start();
			opGobler.start();

			p2.waitFor();

		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (iter.hasNext()) {
			JSONObject obj1 = (JSONObject) iter.next();
			System.out.println(obj1.keySet().contains("MarkDuplicates") && ((JSONObject)obj1.get("MarkDuplicates")).values().contains("true") );

			if (obj1.keySet().contains("MarkDuplicates")  && ((JSONObject)obj1.get("MarkDuplicates")).values().contains("true")) {

				String newStepFileName = inputFileLocation.resolve(FilenameUtils.getBaseName(stepFileName)).toString()
						+ ".mdup.bam";
				String mdupCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe " + GatkUtils.maxHeapSpace
						+ " -jar " + GatkUtils.picardPath + " MarkDuplicates " + " I="
						+ inputFileLocation.resolve(stepFileName) + " O=" + inputFileLocation.resolve(newStepFileName)
						+ " M=" + inputFileLocation.resolve(newStepFileName + ".metricFile")
						+ " VALIDATION_STRINGENCY=LENIENT";
				System.out.println(mdupCommand);

				runAndGobbleCommand(mdupCommand, r);

				stepFileName = newStepFileName;
			} else if (obj1.keySet().contains("BuildBamIndex") && ((JSONObject)obj1.get("BuildBamIndex")).values().contains("true")) {
				String bamIndexCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.picardPath + " BuildBamIndex " + " I="
						+ inputFileLocation.resolve(stepFileName);
				System.out.println(bamIndexCommand);

				runAndGobbleCommand(bamIndexCommand, r);
			} else if (obj1.keySet().contains("RealignerTargetCreator") && ((JSONObject)obj1.get("RealignerTargetCreator")).values().contains("true")) {
				listFileName = inputFileLocation.resolve(FilenameUtils.getBaseName(stepFileName)).toString() + ".list";
				String realignerCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.gatkPath + " -T RealignerTargetCreator "
						+ " -R " + GatkUtils.referencePath + " -I " + inputFileLocation.resolve(stepFileName) + " -o "
						+ inputFileLocation.resolve(listFileName);
				System.out.println(realignerCommand);
				runAndGobbleCommand(realignerCommand, r);

			} else if (obj1.keySet().contains("IndelRealigner") && ((JSONObject)obj1.get("IndelRealigner")).values().contains("true")) {
				String newStepFileName = inputFileLocation.resolve(FilenameUtils.getBaseName(stepFileName)).toString()
						+ ".realigned.bam";
				String indelRealignerCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.gatkPath + " -T IndelRealigner  " + " -R "
						+ GatkUtils.referencePath + " -I " + inputFileLocation.resolve(stepFileName) + " -o "
						+ inputFileLocation.resolve(newStepFileName) + " -targetIntervals "
						+ inputFileLocation.resolve(listFileName);
				System.out.println(indelRealignerCommand);
				runAndGobbleCommand(indelRealignerCommand, r);
				stepFileName = newStepFileName;
			}

			else if (obj1.keySet().contains("FixMateInformation") && ((JSONObject)obj1.get("FixMateInformation")).values().contains("true")) {
				// java -Xmx1g -jar
				// tools\picard-tools-1.82\picard-tools-1.82\FixMateInformation.jar
				// INPUT=%sampleName%.sorted.realigned.bam
				// OUTPUT=%sampleName%.sorted.realigned.fixed.bam SO=coordinate
				// VALIDATION_STRINGENCY=LENIENT CREATE_INDEX=true
				String newStepFileName = inputFileLocation.resolve(FilenameUtils.getBaseName(stepFileName)).toString()
						+ ".fixed.bam";
				String mateFixCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe " + GatkUtils.maxHeapSpace
						+ " -jar " + GatkUtils.picardPath + " FixMateInformation " + " I="
						+ inputFileLocation.resolve(stepFileName) + " O=" + inputFileLocation.resolve(newStepFileName)
						+ " VALIDATION_STRINGENCY=LENIENT";

				runAndGobbleCommand(mateFixCommand, r);

				stepFileName = newStepFileName;
				
				String bamIndexCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.picardPath + " BuildBamIndex " + " I="
						+ inputFileLocation.resolve(stepFileName);
				System.out.println(bamIndexCommand);
				runAndGobbleCommand(bamIndexCommand, r);


			} else if (obj1.keySet().contains("BaseRecalibrator") && ((JSONObject)obj1.get("BaseRecalibrator")).values().contains("true")) {
				recalGrpFile = inputFileLocation.resolve(FilenameUtils.getBaseName(stepFileName)).toString()
						+ ".recal.grp";
				String baseRecalCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.gatkPath + " -T BaseRecalibrator  " + " -R "
						+ GatkUtils.referencePath + " -I " + inputFileLocation.resolve(stepFileName) + " -o "
						+ inputFileLocation.resolve(recalGrpFile) + " -bqsrBAQGOP 40  ";
				runAndGobbleCommand(baseRecalCommand, r);

			} else if (obj1.keySet().contains("PrintReads") && ((JSONObject)obj1.get("PrintReads")).values().contains("true")) {
				// java -Xmx1g -jar tools\GenomeAnalysisTK.jar -T PrintReads -R %genomeRef% -I
				// %sampleName%.sorted.realigned.fixed.bam -BQSR %sampleName%.recal.grp -o
				// %sampleName%.sorted.realigned.fixed.bqsr.bam
				String newStepFile = inputFileLocation.resolve(FilenameUtils.getBaseName(stepFileName)).toString()
						+ ".bqsr.bam";
				String printReadCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.gatkPath + " -T PrintReads  " + " -R "
						+ GatkUtils.referencePath + " -I " + inputFileLocation.resolve(stepFileName) + " -o "
						+ inputFileLocation.resolve(newStepFile) + " -BQSR " + inputFileLocation.resolve(recalGrpFile);

				runAndGobbleCommand(printReadCommand, r);

				stepFileName = newStepFile;
			} else if (obj1.keySet().contains("UnifiedGenotyper") && ((JSONObject)obj1.get("UnifiedGenotyper")).values().contains("true")) {
				// java -Xmx1g -jar tools\GenomeAnalysisTK.jar -R %genomeRef% -T
				// UnifiedGenotyper -I %sampleName%.sorted.realigned.fixed.bqsr.bam -mbq 13 -glm
				// BOTH -indelGCP 20 -indelGOP 40 -o %sampleName%_snps.raw.vcf -L %targetBed%
				String resultVcf = inputFileLocation.resolve(inputFileNameNoExt + ".raw.vcf").toString();
				String UnifiedGenotyperCommand = "C:\\Program Files\\Java\\jdk1.8.0_192\\bin\\java.exe "
						+ GatkUtils.maxHeapSpace + " -jar " + GatkUtils.gatkPath + " -T UnifiedGenotyper  " + " -R "
						+ GatkUtils.referencePath + " -I " + inputFileLocation.resolve(stepFileName) + " -o "
						+ inputFileLocation.resolve(resultVcf) 
						+ "  -mbq 13 -glm BOTH -indelGCP 20 -indelGOP 40 " + " -L " + GatkUtils.targetBedFile;
				if(obj1.keySet().contains("BaseRecalibrator") && ((JSONObject)obj1.get("BaseRecalibrator")).values().contains("true")) {
					UnifiedGenotyperCommand = UnifiedGenotyperCommand+" -BQSR " + inputFileLocation.resolve(recalGrpFile);
				}
				
				runAndGobbleCommand(UnifiedGenotyperCommand, r);

			}

		}

	}

	public void runAndGobbleCommand(String command, Runtime r) {
		try {
			Process p2 = r.exec(command);
			new StreamGobbler(p2.getErrorStream(), "ERROR").start();
			new StreamGobbler(p2.getInputStream(), "INPUT").start();
			p2.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
