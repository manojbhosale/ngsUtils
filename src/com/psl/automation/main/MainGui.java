package com.psl.automation.main;

import hsutils.BamSorter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.apache.log4j.Logger;

import com.psl.automation.panels.BarcodeMetricsPanel;
import com.psl.automation.panels.BedUtilitiesPanel;
import com.psl.automation.panels.CompareVcfPanel;
import com.psl.automation.panels.QCComparePanel;
import com.psl.automation.panels.TsTvMetricsPanel;
import com.psl.automation.panels.VcfBedIntersectionPanel;

public class MainGui implements ActionListener,Runnable{
	//Log4j logger
	private static Logger log4jLog = Logger.getLogger(MainGui.class.getName());

	JTextField path;
	static int numClicks = 0;
	JPanel gatkPanel = new JPanel();
	JButton jb = new JButton("Browse");
	JButton sort = new JButton("Sort");
	JComboBox designtype = new JComboBox();
	
	JTextArea log;
	File file = null;
	JTabbedPane jtp = new JTabbedPane();
	

	public void run(){

		JFrame mainFrame = new JFrame("MAutomaton");
		URL file = 
				getClass().getClassLoader().getResource("50Pix.png");
		ImageIcon img = new ImageIcon(file);
		System.out.println(file);
		mainFrame.setIconImage(img.getImage());
		mainFrame.setSize(500, 300);
		mainFrame.setDefaultCloseOperation(mainFrame.EXIT_ON_CLOSE);
		//JPanel gatkPanel = new JPanel();

		
		//path.setMargin(new Insets(2, 2, 2, 2));
		//jtp.addTab("GATK", gatkPanel);
		CompareVcfPanel cvp = new CompareVcfPanel();
		BarcodeMetricsPanel bmp = new BarcodeMetricsPanel();
		TsTvMetricsPanel tstvp = new TsTvMetricsPanel();
		QCComparePanel qcp = new QCComparePanel();
		VcfBedIntersectionPanel vbedp = new VcfBedIntersectionPanel();
		BedUtilitiesPanel bup = new BedUtilitiesPanel();
		jtp.addTab("VCF comparator", cvp.createCompareVcfPanel());
		jtp.addTab("HS Util", bmp.createFileConfigPanel());
		jtp.addTab("VCF Util", tstvp.createVcfUtilPanel());
		jtp.addTab("QC compare", qcp.createCompareQcPanel());
		jtp.addTab("VCF BED intersect", vbedp.createIntersectVcfPanel());
		jtp.addTab("BED intersect", bup.createIntersectBedPanel());
		
		jtp.addChangeListener(new TabSelected());
		
		//jtp.setBackgroundAt(jtp.getSelectedIndex(), Color.CYAN);
		//set tab name font
	      Font boldFont = new Font("Courier new", Font.BOLD, 18);        

		jtp.setForeground(Color.DARK_GRAY);
		jtp.setFont(boldFont);
		//jtp.setForeground(Color.RED);
		jtp.setUI(new BasicTabbedPaneUI() {
			   @Override
			   protected void installDefaults() {
			       super.installDefaults();
			       highlight = Color.pink;
			       lightHighlight = Color.green;
			       shadow = Color.red;
			       darkShadow = Color.cyan;
			       focus = Color.red;
			       
			   }
			});

//		/createFileConfigPanel();
		//createSortPanel();
		mainFrame.add(jtp);
		mainFrame.setSize(1000, 500);
		mainFrame.setVisible(true);
		

	}
	
	 private class TabSelected implements ChangeListener {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	            int index = jtp.getSelectedIndex();
	            for(int i = 0; i < 6;i++) {
	            	if(i==index) {
	            		jtp.setBackgroundAt(index, Color.getHSBColor(34, 0.24f, 0.97f));
	            		//Color.ge
	            	}else {
	            		jtp.setBackgroundAt(i,Color.getHSBColor(336, 0.3f, 0.75f));
	            	}
	            }
	            
	            

	        }

	    }


	public void createSortPanel(){
		path = new JTextField();
		path.setSize(5, 20);
		log = new JTextArea(5,20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		JLabel jl = new JLabel();
		JLabel action = new JLabel();
		JLabel chosenPath = new JLabel();
		JLabel stdOutput = new JLabel();

		//String[] designs = {"SureSelect", "HaloPlex"}; 
		designtype.addItem("SureSelect");
		designtype.addItem("HaloPlex");
		designtype.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("Design is of "+designtype.getSelectedItem()+"type !!");
				if(designtype.getSelectedItem().equals("HaloPlex")){
					sort.setEnabled(false);
				}else{
					sort.setEnabled(true);
				}
			}
		});

		action.setText("Action: ");
		jb.addActionListener(this);
		sort.addActionListener(this);
		jb.setAlignmentX(jb.LEFT_ALIGNMENT);
		sort.setAlignmentX(jb.LEFT_ALIGNMENT);
		sort.setMnemonic(KeyEvent.VK_S);


		jl.setText("Choose File: ");
		jl.setToolTipText("Browse to the file and select it for sorting.");
		chosenPath.setText("Chosen File:");
		stdOutput.setText("Execution Log: ");
		gatkPanel.setLayout(new BoxLayout(gatkPanel, BoxLayout.PAGE_AXIS));

		gatkPanel.add(jl);
		gatkPanel.add(jb);
		gatkPanel.add(designtype);
		gatkPanel.add(action);
		gatkPanel.add(sort);
		gatkPanel.add(chosenPath);
		gatkPanel.add(path);
		gatkPanel.add(stdOutput);
		gatkPanel.add(logScrollPane);


	}

	


	public static void main(String args[]){
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}

		MainGui mg = new MainGui();
		Thread t = new Thread(mg);
		t.start();

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		numClicks++;
		//text.setText("Button Clicked " + numClicks + " times");

		//System.out.println(arg0.getSource() == sort);
		if(arg0.getSource() == jb){
			JFileChooser jfile = new JFileChooser();
			String[] extensions = {"BAM","SAM"};
			FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("BAM, SAM",extensions);
			jfile.setFileFilter(extensionFilter);
			int returnValue = jfile.showOpenDialog(gatkPanel);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				file = jfile.getSelectedFile();
				path.setText(file.getAbsolutePath());
				//This is where a real application would open the file.
				//log.append("Opening: " + file.getName() + "." + newline);
			} else {
				//log.append("Open command cancelled by user." + newline);
			}
		}
		String sortedName = file.getName();
		String replaced = sortedName.replaceAll(".bam", ".sorted.bam");

		//System.out.println(replaced);

		if(arg0.getSource() == sort && file != null){

			BamSorter bs  = new BamSorter(file, replaced, log4jLog);
			bs.start();

			log.append("Manoj");
			int errorFlag = 0;
			if(BamSorter.sortError.equals("")){
				log.append("File sorted successfully !!\n");
			}else{
				log.append("File sorting failed !!\n"+BamSorter.sortError+"\n\n"+"Please check error log for further information !!");
			}

		}else{



		}

	}
	
	
}
