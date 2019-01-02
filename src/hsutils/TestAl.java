package hsutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestAl {

	public static void main(String[] args) {
		
		//String record = "al:Z:[[*,RG:Z:1,AS:i:0,XI:i:1,rd:Z:[K00336:31:HF35MBBXX:1:2115:2514:47594]], [*,RG:Z:1,AS:i:0,XI:i:1,rd:Z:[K00336:31:HF35MBBXX:1:1123:30756:46838]], [*,RG:Z:1,AS:i:0,XI:i:1,rd:Z:[K00336:33:HG5G5BBXX:6:1221:17513:26283]]";
		
		//Object al = record.getAttribute("al");
		
		/*Object al = "Z:[[*,RG:Z:1,AS:i:0,XI:i:1,rd:Z:[K00336:31:HF35MBBXX:1:2115:2514:47594]], [*,RG:Z:1,AS:i:0,XI:i:1,rd:Z:[K00336:31:HF35MBBXX:1:1123:30756:46838]], [*,RG:Z:1,AS:i:0,XI:i:1,rd:Z:[K00336:33:HG5G5BBXX:6:1221:17513:26283]]";
		
		if(null != al && al instanceof String){
			String alStr = (String) al;
			
			Pattern p = Pattern.compile("XI\\:i\\:(\\d+)");
			Matcher m = p.matcher(alStr);
			
			while(m.find()){
				System.out.println(m.group(1));
			}
			
			
		}
		*/
		
		BAMParser bp = new BAMParser();
		
		Integer[] a = new Integer[]{0,0,0,5,4};
		
		System.out.println(bp.getMedianAmplificationLevel(a));
		
	}
	
}
