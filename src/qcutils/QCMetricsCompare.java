package qcutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QCMetricsCompare {

	public QCCompareResults compareQc(File file1, File file2) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file1));

		String line = "";
		Integer numMatch = 0;
		Integer numUnMatch = 0;
		Integer numNewMetric = 0;
		
		Map<String, String> qcMap = new HashMap<>();

		while ((line = br.readLine()) != null) {

			if (line.startsWith("#"))
				continue;
			// System.out.println(line);

			String[] splited = line.split("\\=");
			// System.out.println(splited[0]);

			qcMap.put(splited[0], splited[1]);

		}
		br.close();

		BufferedReader br1 = new BufferedReader(new FileReader(file2));

		line = "";

		while ((line = br1.readLine()) != null) {

			if (line.startsWith("#"))
				continue;
			// System.out.println(line);

			String[] splited = line.split("\\=");
			// System.out.println(splited[0]);

			if (qcMap.containsKey(splited[0])) {
				if (qcMap.get(splited[0]).equals(splited[1])) {
					numMatch++;
				}else{
					numUnMatch++;
				}
			}else{
				numNewMetric++;
			}
			// qcMap.put(splited[0],splited[1]);

		}
		br1.close();
		
		return new QCCompareResults(file1, file2, numMatch, numUnMatch, numNewMetric);
	}

}
