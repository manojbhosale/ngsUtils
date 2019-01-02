package com.psl.automation.panels;

import hsutils.BAMParser;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import vcfutils.TsTvStatistics;

public class TsTvMetricsPanel {
	
	JButton browse;
	JButton metrics;
	JPanel fileConfigPanel = new JPanel();
	File vcfPath;
	JTextField pathField;
	
	JPanel selection;
	JPanel metricsButtonPanel;
	
	JTextField tstvratio;
	JTextField numtransitions;
	JTextField numtransversions;
	JComboBox typeCombo;
	String selectedType;
	String[] results;
	
	public void BrowseButton(){
		browse = new JButton("Browse");
		//file2.setAlignmentY(Component.LEFT_ALIGNMENT);
		browse.addActionListener( new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				String[] extensions = {"VCF"};
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("VCF",extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(fileConfigPanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					vcfPath = jfile.getSelectedFile();
					pathField.setText(vcfPath.getAbsolutePath());
					//System.out.println(vcfPath);
					//This is where a real application would open the file.
					//log.append("Opening: " + file.getName() + "." + newline);
				} else {
					//log.append("Open command cancelled by user." + newline);
				}

			}
		});

		selection.add(browse);

	}
	
	public void dropDown(){
		String[] calcTypes = {"Hom","Het","Both","Both(Only Count)"};
		typeCombo = new JComboBox<String>(calcTypes);
		
		typeCombo.setSelectedIndex(1);
		typeCombo.setEditable(false);
		selectedType = "Het";
		typeCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				selectedType = (String)typeCombo.getSelectedItem();
				//System.out.println(selectedType);
			}
		});
		
		selection.add(typeCombo);	
	}
	
	public JPanel createVcfUtilPanel(){
		fileConfigPanel.setLayout(new GridLayout(4,2));
		
		JPanel bamSelect = new JPanel();
		bamSelect.setBorder(BorderFactory.createTitledBorder("Select a VCF file"));
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
		
		JLabel percent = new JLabel("Ts/Tv ratio");
		JLabel mean = new JLabel("#Transitions");
		JLabel median = new JLabel("#Transversions");
		
		BrowseButton();
		dropDown();
		selection.add(pathField);
		//selection.add(bamSelect);
		bamSelect.add(selection);
		
		GridLayout gl1 = new GridLayout(3,2);
		results.setLayout(gl1);
		
		tstvratio = new JTextField();
		tstvratio.setPreferredSize(new Dimension(300,20));
		tstvratio.setEditable(false);
		numtransitions = new JTextField();
		numtransitions.setPreferredSize(new Dimension(300,20));
		numtransitions.setEditable(false);
		numtransversions = new JTextField();
		numtransversions.setPreferredSize(new Dimension(300,20));
		numtransversions.setEditable(false);
		
		results.add(percent);
		results.add(tstvratio);
		results.add(mean);
		results.add(numtransitions);
		results.add(median);
		results.add(numtransversions);
		
		metricsButtonPanel = new JPanel();
		fileConfigPanel.add(bamSelect);
		//FlowLayout flmetrics = new FlowLayout();
		metricsButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		calculateTsTvMetrics();
		fileConfigPanel.add(metricsButtonPanel);
		fileConfigPanel.add(results);
		return fileConfigPanel;
	}

	
	public void calculateTsTvMetrics(){
		metrics = new JButton("Calculate");
		

		metrics.addActionListener( new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try{
					if(vcfPath != null){
						TsTvStatistics tstv = new TsTvStatistics(selectedType.toUpperCase());
						//Thread t = new Thread(tstv);
						//t.start();
						
						//t.join();
						//results = getMetrics(bamPath);
						results = tstv.calculateTsTvRatio(vcfPath);
						/*if(dtm.getRowCount() != 0){
							dtm.removeRow(0);
						}*/
						//System.out.println(results);
						tstvratio.setText(results[0]);
						numtransitions.setText(results[1]);
						numtransversions.setText(results[2]);
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
