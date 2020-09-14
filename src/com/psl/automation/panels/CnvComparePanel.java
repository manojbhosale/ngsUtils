package com.psl.automation.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import bedUtils.BedUtils;
import cnv_compare.CnvCompare;

public class CnvComparePanel {

	JPanel vcfComparePanel = new JPanel();
	// JComboBox modeCombo = new JComboBox();
	JButton file1;
	JButton bed;
	JButton folder;
	JButton compare;

	JTextField cnvOneField;
	JTextField cnvTwoField;
	JTextField folderField;

	File cnvOnePath;
	File cnvTwoPath;
	File folderPath;
	
	JPanel selection = new JPanel();
	JPanel fileSelection = new JPanel();
	JPanel oneComparison = new JPanel();
	JPanel twoComparison = new JPanel();
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel pathPanel = new JPanel();
	JPanel comparePanel = new JPanel();

	//CompareUtils vcfCompareUtil = new CompareUtils();
	//ComparisonResult result;

	File lastPath;
	
	/*
	JTable jt = new JTable() {

		
		 * DefaultTableCellRenderer colortext=new DefaultTableCellRenderer(); {
		 * colortext.setForeground(Color.RED); }
		 * 
		 * @Override public TableCellRenderer getCellRenderer(int arg0, int
		 * arg1) { return colortext; }
		 
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
			"NewDeletions" }, 0);*/

	public JPanel createCnvComparePanel() {
		vcfComparePanel.setLayout(new BoxLayout(vcfComparePanel,
				BoxLayout.PAGE_AXIS));

		fileSelection.setLayout(new BoxLayout(fileSelection,
				BoxLayout.PAGE_AXIS));
		
		
		oneComparison
				.setBorder(BorderFactory.createTitledBorder("File Selection"));
		oneComparison.setLayout(new BoxLayout(oneComparison,
				BoxLayout.PAGE_AXIS));
		selection.setLayout(new FlowLayout(FlowLayout.LEFT));
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
		vcfOne.setText("Select CNV segments File1:");
		vcfOne.add(Box.createVerticalGlue());
		JLabel bedlabel = new JLabel();
		bedlabel.setText("Select CNV segments File2:");
		bedlabel.add(Box.createVerticalGlue());
		JLabel folderLabel = new JLabel();
		folderLabel.setText("Output Folder:");
		folderLabel.add(Box.createVerticalGlue());
		
		// textPanel.add(modeLabel);
				textPanel.add(Box.createVerticalStrut(15));
				textPanel.add(vcfOne);
				textPanel.add(Box.createVerticalStrut(15));
				textPanel.add(bedlabel);
				textPanel.add(Box.createVerticalStrut(15));
				textPanel.add(folderLabel);
				// textPanel.add(Box.createRigidArea(new Dimension(10,25)));

				// buttonPanel.add(modeCombo);
				buttonPanel.add(Box.createVerticalStrut(25));
				FileOneButton();
				buttonPanel.add(Box.createVerticalStrut(15));
				FileTwoButton();
				buttonPanel.add(Box.createVerticalStrut(10));
				Folder();
				buttonPanel.add(Box.createVerticalStrut(10));
				Compare();
				
				cnvOneField = new JTextField();
				cnvOneField.setEditable(false);
				cnvOneField.setPreferredSize(new Dimension(450,30));

				cnvTwoField = new JTextField();
				cnvTwoField.setPreferredSize(new Dimension(450,30));
				cnvTwoField.setEditable(false);
				
				folderField = new JTextField();
				folderField.setPreferredSize(new Dimension(450,30));
				folderField.setEditable(false);
				
				pathPanel.add(Box.createVerticalStrut(15));
				pathPanel.add(cnvOneField);
				pathPanel.add(Box.createVerticalStrut(15));
				pathPanel.add(cnvTwoField);
				pathPanel.add(Box.createVerticalStrut(15));
				pathPanel.add(folderField);
				
				//jt.setModel(dtm);

				// jt.setBounds(30,40,200,300);
				//JScrollPane jsp = new JScrollPane(jt);
				
				selection.add(textPanel);
				selection.add(buttonPanel);
				selection.add(pathPanel);
				oneComparison.add(selection);
				//oneComparison.add(buttonPanel);
				//oneComparison.add(pathPanel);

				/*twoComparison.add(mapping);
				FileMapButton();
				twoComparison.add(mapFileField);*/

				fileSelection.add(oneComparison);
				//fileSelection.add(twoComparison);
				fileSelection.add(comparePanel);

				vcfComparePanel.add(fileSelection);
				//vcfComparePanel.add(jsp);

				return vcfComparePanel;
	}
	
	public void FileOneButton() {
		file1 = new JButton("Browse");
		// file1.setAlignmentY(Component.LEFT_ALIGNMENT);
		file1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				jfile.setCurrentDirectory(lastPath);
				
				String[] extensions = { "TXT","BED" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"TXT,BED", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(vcfComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					cnvOnePath = jfile.getSelectedFile();
					cnvOneField.setText(cnvOnePath.getAbsolutePath());
					lastPath = new File(cnvOnePath.getAbsolutePath());
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
		bed = new JButton("Browse");
		// file2.setAlignmentY(Component.LEFT_ALIGNMENT);
		bed.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				jfile.setCurrentDirectory(lastPath);

				String[] extensions = { "TXT","BED" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"TXT,BED", extensions);
				jfile.setFileFilter(extensionFilter);
				int returnValue = jfile.showOpenDialog(vcfComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					cnvTwoPath = jfile.getSelectedFile();
					cnvTwoField.setText(cnvTwoPath.getAbsolutePath());
					lastPath = new File(cnvTwoPath.getAbsolutePath());
					// This is where a real application would open the file.
					// log.append("Opening: " + file.getName() + "." + newline);
				} else {
					// log.append("Open command cancelled by user." + newline);
				}

			}
		});

		buttonPanel.add(bed);

	}
	
	public void Compare() {
		compare = new JButton("Intersect");

		compare.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
					if (cnvOnePath != null && cnvTwoPath != null) {
						CnvCompare.compareCnvFilesToFolder(cnvOnePath,
								cnvTwoPath,folderPath.toPath());
						
					} 



			}
		});
		
		comparePanel.add(compare);

}

	public void Folder() {
		folder = new JButton("Browse");
		// file2.setAlignmentY(Component.LEFT_ALIGNMENT);
		folder.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser jfile = new JFileChooser();
				jfile.setCurrentDirectory(lastPath);
				jfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				/*String[] extensions = { "txt" };
				FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(
						"txt", extensions);
				jfile.setFileFilter(extensionFilter);
*/				int returnValue = jfile.showOpenDialog(vcfComparePanel);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					folderPath = jfile.getSelectedFile();
					folderField.setText(folderPath.getAbsolutePath());
					lastPath = new File(folderPath.getAbsolutePath());

					// This is where a real application would open the file.
					// log.append("Opening: " + file.getName() + "." + newline);
				} else {
					// log.append("Open command cancelled by user." + newline);
				}

			}
		});

		buttonPanel.add(folder);

	}
	
	
	

	
}
