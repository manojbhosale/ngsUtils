package com.psl.automation.panels;

import hsutils.BAMParser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BarcodeMetricsPanel {
	
	JButton metrics;
	JButton browse;
	JTextField pathField;
	JPanel selection;
	File bamPath;
	Future<String[]> resultsFuture;
	JPanel fileConfigPanel = new JPanel();
	JPanel metricsButtonPanel;
	JTextField percentField;
	JTextField averageField;
	JTextField medianField;
	
	public void BrowseButton(){
		browse = new JButton("Browse");
		//file2.setAlignmentY(Component.LEFT_ALIGNMENT);
		browse.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				String[] extensions = {"BAM"};
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("BAM",extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(fileConfigPanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					bamPath = jfile.getSelectedFile();
					pathField.setText(bamPath.getAbsolutePath());
					//This is where a real application would open the file.
					//log.append("Opening: " + file.getName() + "." + newline);
				} else {
					//log.append("Open command cancelled by user." + newline);
				}

			}
		});

		selection.add(browse);

	}
	
	public JPanel createFileConfigPanel(){
		/*JPanel refFasta = new JPanel();
		//refFasta.setLayout(new BoxLayout(refFasta,BoxLayout.PAGE_AXIS));
		fileConfigPanel.setLayout(new BoxLayout(fileConfigPanel,BoxLayout.PAGE_AXIS));
		JLabel ref = new JLabel("Genome Reference");
		ImageIcon img = new ImageIcon("C:\\Users\\manojkumar_bhosale\\Desktop\\download.png");
		JButton browse = new JButton(img);
		browse.setBounds(10,10, 50, 20);
		JTextField jtf = new JTextField();
		jtf.setPreferredSize(new Dimension(300,30));
		jtf.setEditable(false);
		
		//ImageIcon img = ImageIcon("C:\\Users\\manojkumar_bhosale\\Desktop\\download.png");
		JPanel refDbsnp = new JPanel();
		JLabel dbsnp = new JLabel("dbSNP");
		JButton browseDbsnp = new JButton("Browse");
		JTextField jtfDbsnp = new JTextField();
		jtfDbsnp.setPreferredSize(new Dimension(300,30));
		jtfDbsnp.setEditable(false);
		browseDbsnp.setMnemonic(KeyEvent.VK_B);

		JPanel refDesign = new JPanel();
		JLabel design = new JLabel("Design");
		JButton browseDesign = new JButton("Browse");
		JTextField jtfDesign = new JTextField();
		jtfDesign.setPreferredSize(new Dimension(300,30));
		jtfDesign.setEditable(false);
		browseDesign.setMnemonic(KeyEvent.VK_R);



		refFasta.add(ref);
		refFasta.add(browse);
		refFasta.add(jtf);

		refDbsnp.add(dbsnp);
		refDbsnp.add(browseDbsnp);
		refDbsnp.add(jtfDbsnp);

		refDesign.add(design);
		refDesign.add(browseDesign);
		refDesign.add(jtfDesign);

		fileConfigPanel.add(refFasta);
		fileConfigPanel.add(refDbsnp);
		fileConfigPanel.add(refDesign);*/
		fileConfigPanel.setLayout(new GridLayout(4,2));
		
		JPanel bamSelect = new JPanel();
		bamSelect.setBorder(BorderFactory.createTitledBorder("Select HS BAM file"));
		selection = new JPanel();
		JPanel results = new JPanel();
		results.setBorder(BorderFactory.createTitledBorder("Results"));
		bamSelect.setLayout(new BoxLayout(bamSelect,BoxLayout.PAGE_AXIS));
		results.setLayout(new BoxLayout(results,BoxLayout.PAGE_AXIS));
		selection.setLayout(new FlowLayout(FlowLayout.LEFT));
		//selection.setSize(300, 10);
		JLabel ref = new JLabel("BAM File");
		ImageIcon img = new ImageIcon("C:\\Users\\manojkumar_bhosale\\Desktop\\download.png");
		browse = new JButton("Select BAM");
		pathField = new JTextField();
		pathField.setPreferredSize(new Dimension(350,30));
		pathField.setEditable(false);
		//jtf.setSize(100, 10);
		
		JLabel percent = new JLabel("Percent duplicates removed ");
		JLabel mean = new JLabel("Average amplification level ");
		JLabel median = new JLabel("Median amplification level ");
		
		BrowseButton();
		selection.add(pathField);
		//selection.add(bamSelect);
		bamSelect.add(selection);
		
		GridLayout gl1 = new GridLayout(3,2);
		results.setLayout(gl1);
		
		percentField = new JTextField();
		percentField.setPreferredSize(new Dimension(300,20));
		percentField.setEditable(false);
		averageField = new JTextField();
		averageField.setPreferredSize(new Dimension(300,20));
		averageField.setEditable(false);
		medianField = new JTextField();
		medianField.setPreferredSize(new Dimension(300,20));
		medianField.setEditable(false);
		
		results.add(percent);
		results.add(percentField);
		results.add(mean);
		results.add(averageField);
		results.add(median);
		results.add(medianField);
		
		metricsButtonPanel = new JPanel();
		fileConfigPanel.add(bamSelect);
		//FlowLayout flmetrics = new FlowLayout();
		metricsButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		CalculateHsMetrics();
		fileConfigPanel.add(metricsButtonPanel);
		fileConfigPanel.add(results);
		return fileConfigPanel;
		
	}

	
	public void CalculateHsMetrics(){
		metrics = new JButton("Calculate");
		

		metrics.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try{
					if(bamPath != null){
						ExecutorService pool = Executors.newFixedThreadPool(3);
						
						BAMParser bp = new BAMParser(bamPath);
						
						
						/*Thread t = new Thread(bp);
						t.start();
						
						t.join();
*/						//results = getMetrics(bamPath);
						resultsFuture = pool.submit(bp);
						
						String results[]  = resultsFuture.get();
						/*if(dtm.getRowCount() != 0){
							dtm.removeRow(0);
						}*/
						
						percentField.setText(results[0]);
						averageField.setText(results[1]);
						medianField.setText(results[2]);
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				
				/*for(int i = 0; i < dtm.getRowCount();i++){
					for(int j = 3; j < dtm.getColumnCount(); j++){
						
						if(Integer.valueOf((String)dtm.getValueAt(i, j)) > 0){
							
							
						}
						
					}
				}*/

			}
		});
		
		
		metricsButtonPanel.add(metrics);

	}


}
