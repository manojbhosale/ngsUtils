package com.psl.automation.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import qcutils.QCCompareResults;
import qcutils.QCMetricsCompare;

public class QCComparePanel {

	JPanel qcComparePanel = new JPanel();
	// JComboBox modeCombo = new JComboBox();
	JButton file1;
	JButton file2;
	JButton file3;
	JButton compare;

	JTextField qcOneField;
	JTextField qcTwoField;
	JTextField mapFileField;

	File qcOnePath;
	File qcTwoPath;
	File mapFilePath;

	JPanel fileSelection = new JPanel();
	JPanel oneComparison = new JPanel();;
	JPanel twoComparison = new JPanel();;
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel pathPanel = new JPanel();
	JPanel comparePanel = new JPanel();

	QCMetricsCompare qcCompareUtil  = new QCMetricsCompare();
	QCCompareResults result;

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

	DefaultTableModel dtm = new DefaultTableModel(new String[] { "QC1",
			"QC2", "Match", "MisMatch", "Newmetrics" }, 0);

	public JPanel createCompareQcPanel() {
		qcComparePanel.setLayout(new BoxLayout(qcComparePanel,
				BoxLayout.PAGE_AXIS));

		fileSelection.setLayout(new BoxLayout(fileSelection,
				BoxLayout.PAGE_AXIS));
		oneComparison
				.setBorder(BorderFactory.createTitledBorder("Single file"));
		oneComparison.setLayout(new BoxLayout(oneComparison,
				BoxLayout.LINE_AXIS));
		twoComparison
				.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
		vcfOne.setText("Select QC 1:");
		vcfOne.add(Box.createVerticalGlue());
		JLabel vcfTwo = new JLabel();
		vcfTwo.setText("Select QC 2:");
		vcfTwo.add(Box.createVerticalGlue());
		JLabel mapping = new JLabel();
		mapping.setText("Select QC Mapping file:");
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

		qcOneField = new JTextField();
		qcOneField.setEditable(false);
		qcOneField.setSize(100, 10);

		qcTwoField = new JTextField();
		qcTwoField.setSize(100, 10);
		qcTwoField.setEditable(false);

		mapFileField = new JTextField();
		mapFileField.setSize(100, 10);
		mapFileField.setEditable(false);

		pathPanel.add(Box.createVerticalStrut(15));
		pathPanel.add(qcOneField);
		pathPanel.add(Box.createVerticalStrut(15));
		pathPanel.add(qcTwoField);
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

		qcComparePanel.add(fileSelection);
		qcComparePanel.add(jsp);

		return qcComparePanel;
	}

	public void FileOneButton() {
		file1 = new JButton("Browse");
		// file1.setAlignmentY(Component.LEFT_ALIGNMENT);
		file1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				String[] extensions = { "properties" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"properties", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(qcComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					qcOnePath = jfile.getSelectedFile();
					qcOneField.setText(qcOnePath.getAbsolutePath());
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
				String[] extensions = { "properties" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"properties", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(qcComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					qcTwoPath = jfile.getSelectedFile();
					qcTwoField.setText(qcTwoPath.getAbsolutePath());
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
				String[] extensions = { "txt" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"txt", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(qcComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					mapFilePath = jfile.getSelectedFile();
					mapFileField.setText(mapFilePath.getAbsolutePath());
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
					if (qcOnePath != null && qcTwoPath != null) {
						result = qcCompareUtil.compareQc(qcOnePath,
								qcTwoPath);
						/*
						 * if(dtm.getRowCount() != 0){ dtm.removeRow(0); }
						 */
						dtm.addRow(new Object[] { result.getFile1(),
								result.getFile2(), result.getNumMatch(),
								result.getNumUnMatch(), result.getNumNewMetric()});

					} else if (mapFilePath.exists()) {
						BufferedReader br = new BufferedReader(new FileReader(
								mapFilePath));
						String line = "";
						while ((line = br.readLine()) != null) {
							line.trim();
							String[] paths = line.split("\t");
							result = qcCompareUtil.compareQc(new File(
									paths[0]), new File(paths[1]));
							System.out.println(result);
							/*
							 * if(dtm.getRowCount() != 0){ dtm.removeRow(0); }
							 */
							dtm.addRow(new Object[] { result.getFile1(),
									result.getFile2(), result.getNumMatch(),
									result.getNumUnMatch(), result.getNumNewMetric()});
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
