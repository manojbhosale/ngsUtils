package com.psl.automation.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import vcfutils.CompareUtils;
import vcfutils.ComparisonResult;

public class CompareVcfPanel {

	JPanel vcfComparePanel = new JPanel();
	// JComboBox modeCombo = new JComboBox();
	JButton file1;
	JButton file2;
	JButton file3;
	JButton compare;

	JTextField vcfOneField;
	JTextField vcfTwoField;
	JTextField mapFileField;

	File vcfOnePath;
	File vcfTwoPath;
	File mapFilePath;

	JPanel fileSelection = new JPanel();
	JPanel oneComparison = new JPanel();
	JPanel twoComparison = new JPanel();
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel pathPanel = new JPanel();
	JPanel comparePanel = new JPanel();

	CompareUtils vcfCompareUtil = new CompareUtils();
	ComparisonResult result;

	File lastPath;
	
	JTable jt = new JTable() {

		/*
		 * DefaultTableCellRenderer colortext=new DefaultTableCellRenderer(); {
		 * colortext.setForeground(Color.RED); }
		 * 
		 * @Override public TableCellRenderer getCellRenderer(int arg0, int
		 * arg1) { return colortext; }
		 */
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int col) {
			Component comp = super.prepareRenderer(renderer, row, col);

			if (col < 3) {

				comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}

			Object value = getModel().getValueAt(row, col);
			if (value.equals(0)) {
				comp.setBackground(Color.green);
			} else {
				comp.setBackground(Color.red);
			}
			return comp;
		}

		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};

	DefaultTableModel dtm = new DefaultTableModel(new String[] { "Vcf1",
			"Vcf2", "Common", "New", "Missed", "MissedSNPs", "NewSNPs",
			"MissedInsertions", "NewInsertions", "MissedDeletions",
			"NewDeletions" }, 0);

	public JPanel createCompareVcfPanel() {
		vcfComparePanel.setLayout(new BoxLayout(vcfComparePanel,
				BoxLayout.PAGE_AXIS));

		fileSelection.setLayout(new BoxLayout(fileSelection,
				BoxLayout.PAGE_AXIS));
		oneComparison
				.setBorder(BorderFactory.createTitledBorder("Single file"));
		oneComparison.setLayout(new BoxLayout(oneComparison,
				BoxLayout.LINE_AXIS));
		//twoComparison
		//	.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		twoComparison.setBorder(BorderFactory
				.createTitledBorder("Multiple files"));
		twoComparison.setLayout(new BoxLayout(twoComparison,
				BoxLayout.LINE_AXIS));
		// fileSelection.setLayout(new GridLayout());

		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.PAGE_AXIS));
		textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// textPanel.setBackground(Color.CYAN);

		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		// buttonPanel.setBackground(Color.GREEN);

		pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.PAGE_AXIS));
		pathPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// pathPanel.setBackground(Color.YELLOW);

		/*
		 * JLabel modeLabel = new JLabel();
		 * modeLabel.add(Box.createVerticalGlue());
		 * modeLabel.setText("Select Mode: ");
		 * 
		 * modeCombo.addItem("One to One"); //modeCombo.addItem("One to Many");
		 * modeCombo.addItem("Many to Many");
		 */

		JLabel vcfOne = new JLabel();
		vcfOne.setText("Select VCF 1:");
		vcfOne.add(Box.createVerticalGlue());
		JLabel vcfTwo = new JLabel();
		vcfTwo.setText("Select VCF 2:");
		vcfTwo.add(Box.createVerticalGlue());
		JLabel mapping = new JLabel();
		mapping.setText("Select Mapping file:");
		mapping.add(Box.createVerticalGlue());

		// textPanel.add(modeLabel);
		textPanel.add(Box.createVerticalStrut(15));
		textPanel.add(vcfOne);
		textPanel.add(Box.createVerticalStrut(15));
		textPanel.add(vcfTwo);
		// textPanel.add(Box.createRigidArea(new Dimension(10,25)));

		// buttonPanel.add(modeCombo);
		buttonPanel.add(Box.createVerticalStrut(25));
		FileOneButton();
		buttonPanel.add(Box.createVerticalStrut(15));
		FileTwoButton();
		buttonPanel.add(Box.createVerticalStrut(10));
		Compare();

		vcfOneField = new JTextField();
		vcfOneField.setEditable(false);
		vcfOneField.setSize(100, 10);

		vcfTwoField = new JTextField();
		vcfTwoField.setSize(100, 10);
		vcfTwoField.setEditable(false);

		mapFileField = new JTextField();
		mapFileField.setSize(100, 10);
		mapFileField.setEditable(false);

		pathPanel.add(Box.createVerticalStrut(15));
		pathPanel.add(vcfOneField);
		pathPanel.add(Box.createVerticalStrut(15));
		pathPanel.add(vcfTwoField);
		// pathPanel.add(Box.createRigidArea(new Dimension(5,25)));

		// String data[][]={ {"","","","",""}};
		// String column[]={"Vcf1","Vcf2","Common","Missed","New"};

		jt.setModel(dtm);

		// jt.setBounds(30,40,200,300);
		JScrollPane jsp = new JScrollPane(jt);

		oneComparison.add(textPanel);
		oneComparison.add(buttonPanel);
		oneComparison.add(pathPanel);

		twoComparison.add(mapping);
		FileMapButton();
		twoComparison.add(mapFileField);

		fileSelection.add(oneComparison);
		fileSelection.add(twoComparison);
		fileSelection.add(comparePanel);

		vcfComparePanel.add(fileSelection);
		vcfComparePanel.add(jsp);

		return vcfComparePanel;
	}

	public void FileOneButton() {
		file1 = new JButton("Browse");
		// file1.setAlignmentY(Component.LEFT_ALIGNMENT);
		file1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				jfile.setCurrentDirectory(lastPath);
				
				String[] extensions = { "VCF" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"VCF", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(vcfComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					vcfOnePath = jfile.getSelectedFile();
					vcfOneField.setText(vcfOnePath.getAbsolutePath());
					lastPath = new File(vcfOnePath.getAbsolutePath());
					// This is where a real application would open the file.
					// log.append("Opening: " + file.getName() + "." + newline);
				} else {
					// log.append("Open command cancelled by user." + newline);
				}

			}
		});

		buttonPanel.add(file1);

	}

	public void FileTwoButton() {
		file2 = new JButton("Browse");
		// file2.setAlignmentY(Component.LEFT_ALIGNMENT);
		file2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				jfile.setCurrentDirectory(lastPath);

				String[] extensions = { "VCF" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"VCF", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(vcfComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					vcfTwoPath = jfile.getSelectedFile();
					vcfTwoField.setText(vcfTwoPath.getAbsolutePath());
					lastPath = new File(vcfTwoPath.getAbsolutePath());
					// This is where a real application would open the file.
					// log.append("Opening: " + file.getName() + "." + newline);
				} else {
					// log.append("Open command cancelled by user." + newline);
				}

			}
		});

		buttonPanel.add(file2);

	}

	public void FileMapButton() {
		file3 = new JButton("Browse");
		// file2.setAlignmentY(Component.LEFT_ALIGNMENT);
		file3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				jfile.setCurrentDirectory(lastPath);
				String[] extensions = { "txt" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"txt", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(vcfComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					mapFilePath = jfile.getSelectedFile();
					mapFileField.setText(mapFilePath.getAbsolutePath());
					lastPath = new File(mapFilePath.getAbsolutePath());

					// This is where a real application would open the file.
					// log.append("Opening: " + file.getName() + "." + newline);
				} else {
					// log.append("Open command cancelled by user." + newline);
				}

			}
		});

		twoComparison.add(file3);

	}

	public void Compare() {
		compare = new JButton("Compare");

		compare.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					if (vcfOnePath != null && vcfTwoPath != null) {
						result = vcfCompareUtil.compareVcfs(vcfOnePath,
								vcfTwoPath);
						/*
						 * if(dtm.getRowCount() != 0){ dtm.removeRow(0); }
						 */
						dtm.addRow(new Object[] { result.getFile1(),
								result.getFile2(), result.getCommonCalls(),
								result.getNewCalls(), result.getMissedCalls(),
								result.getMissedSnps(), result.getNewSnps(),
								result.getMissedInsertions(),
								result.getNewInsertions(),
								result.getMissedDeletions(),
								result.getNewDeletions() });

					} else if (mapFilePath.exists()) {
						BufferedReader br = new BufferedReader(new FileReader(
								mapFilePath));
						String line = "";
						while ((line = br.readLine()) != null) {
							line.trim();
							String[] paths = line.split("\t");
							result = vcfCompareUtil.compareVcfs(new File(
									paths[0]), new File(paths[1]));
							System.out.println(result);
							/*
							 * if(dtm.getRowCount() != 0){ dtm.removeRow(0); }
							 */
							dtm.addRow(new Object[] { result.getFile1(),
									result.getFile2(), result.getCommonCalls(),
									result.getNewCalls(), result.getMissedCalls(),
									result.getMissedSnps(), result.getNewSnps(),
									result.getMissedInsertions(),
									result.getNewInsertions(),
									result.getMissedDeletions(),
									result.getNewDeletions() });
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				/*
				 * for(int i = 0; i < dtm.getRowCount();i++){ for(int j = 3; j <
				 * dtm.getColumnCount(); j++){
				 * 
				 * if(Integer.valueOf((String)dtm.getValueAt(i, j)) > 0){
				 * 
				 * 
				 * }
				 * 
				 * } }
				 */

			}
		});

		comparePanel.add(compare);

	}
}
