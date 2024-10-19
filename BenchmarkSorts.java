//Jake Shoemake
//4/1/24
//CMSC 451
//Project 1

import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;

//This is the main program where the sorting methods begin
public class BenchmarkSorts{
	static JButton selection, generate;
	static JLabel status;
	static JFileChooser fileChooser;
	static JTable table;
	static DefaultTableModel model;
	static Object[][] data;
	static JTextArea field;
	static final DecimalFormat twoPt = new DecimalFormat("0.00");
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Benchmark Report");
		TitledBorder benchmarkTitle = BorderFactory.createTitledBorder("File Selection");
		JPanel benchmarker = new JPanel();
		benchmarker.setLayout(null);
		benchmarker.setBorder(benchmarkTitle);
		benchmarker.setBounds(0, 0, 545, 90);
		
		TitledBorder reportTitle = BorderFactory.createTitledBorder("Report Table");
		JPanel report = new JPanel();
		report.setLayout(null);
		report.setBorder(reportTitle);
		report.setBounds(0, 90, 545, 280);
		
		//Panel 
		//Benchmark section
		status = new JLabel("Generate benchmark reports or display them here.");
		status.setBounds(10, 20, 545, 30);
		
		selection = new JButton("Select File");
		selection.setBounds(10, 50, 100, 30);
		selection.addActionListener(new displayReport());
		
		generate = new JButton("Generate New Reports");
		generate.setBounds(125, 50, 170, 30);
		generate.addActionListener(new generateReport());		
		benchmarker.add(status); benchmarker.add(generate);
		benchmarker.add(selection);
		
		//Report section
		//The table
		field = new JTextArea();
		field.setBounds(10, 20, 525, 250);
		field.setEditable(false);
		data = new Object[12][5];
		report.add(field);
		
		//Frame + panel operations
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(560, 410);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(benchmarker);
		frame.add(report);
	}
	
	public static class generateReport implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			status.setText("Generating reports...");
			generate.setEnabled(false); selection.setEnabled(false);
			Random rand = new Random();
			String shellRun = "", mergeRun = "";
			
			for(int i = 1; i <= 12; i++) {
				//Creates the randomized array
				int shuffle[] = new int[i * 100];
				shellRun = shellRun + (i * 100);
				mergeRun = mergeRun + (i * 100);
				
				for(int r = 0; r < 40; r++) {
					//Makes data in randomized array
					for(int j = 0; j < (i * 100); j++) {
						shuffle[j] = rand.nextInt(100);
					}
					
					//Performs the benchmark
					//Merge Sort
					MergeSort merger = new MergeSort(shuffle);
					merger.startSort();
					merger.endSort();
					mergeRun = mergeRun + " " + merger.getCount() + " " + merger.getTime();
					
					//Shell Sort
					ShellSort sheller = new ShellSort(shuffle);
					sheller.startSort();
					sheller.endSort();
					shellRun = shellRun + " " + sheller.getCount() + " " + sheller.getTime();
				}
				
				if(i <= 12) {
					mergeRun = mergeRun + "\n";
					shellRun = shellRun + "\n";
				}
				
			}
			//Appends the results into each file
			try {
				FileWriter mergeWriter = new FileWriter("mergeReport.txt");
				mergeWriter.write(mergeRun);
				mergeWriter.close();
			} catch (IOException m) {
				m.printStackTrace();
			}
			try {
				FileWriter shellWriter = new FileWriter("shellReport.txt");
				shellWriter.write(shellRun);
				shellWriter.close();
			} catch (IOException s) {
				s.printStackTrace();
			}
			
			generate.setEnabled(true); selection.setEnabled(true);
			status.setText("New Benchmark Reports successfully made!");
		}
	}
	
	public static class displayReport implements ActionListener {
		public void actionPerformed(ActionEvent f) {
			try {
				displayer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//Displays the results of a given file into a table
	private static void displayer() throws Exception{
		fileChooser = new JFileChooser();
		int response = fileChooser.showOpenDialog(null); //Select file to 
		if(response == JFileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
			Scanner scan = new Scanner(file);
			StringTokenizer tokenizer;
			String information = "";
			int tokenNum, arrayNum = 1;
			double count = 0, time = 0; //Average count
			
			double[] countCoef;
			double[] timeCoef;
			
			field.setText("Size\tAvg Count\tCoef Count\tAvg Time\tCoef Time\n");
			
			//Parses each line of the report
			while(scan.hasNextLine()) {
				countCoef = new double[40];
				timeCoef = new double[40];
				count = 0; time = 0;
				
				tokenNum = 0;
				int countNum = 0, timeNum = 0;
				information = scan.nextLine();
				tokenizer = new StringTokenizer(information, " ");
				
				while(tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if(tokenNum == 0) {
						data[arrayNum - 1][0] = arrayNum * 100;
					} else {
						switch(tokenNum % 2) {
						case(1):
							count = count + Double.valueOf(token);
							countCoef[countNum] = Double.valueOf(token);
							countNum++;
							break;
						case(0):
							time = time + Double.valueOf(token);
							timeCoef[timeNum] = Double.valueOf(token);
							timeNum++;
							break;
						}
					}
					tokenNum++;
				}
				//Critical count
				String critAvg = twoPt.format(count / countCoef.length);
				data[arrayNum - 1][1] = critAvg;
				String critCoef = twoPt.format(coefficience(countCoef));
				data[arrayNum - 1][2] = critCoef + "%";
				
				//
				String timeAvg = twoPt.format(time / timeCoef.length);
				data[arrayNum - 1][3] = timeAvg;
				String nanoCoef = twoPt.format(coefficience(timeCoef));
				data[arrayNum - 1][4] = nanoCoef + "%";
				
				arrayNum++;
			}
			
			String results;
			for(int i = 0; i < 12; i++) {
				results = "";
				for(int j = 0; j < 5; j++) {
					results = results + data[i][j] + "\t";
				}
				results = results + "\n";
				field.append(results);
			}
		}
		
	}
	
	private static double coefficience(double[] array) {
		double mean = mean(array);
		double standard = standardDeviation(array, mean(array));
		return (standard/ mean) * 100;
	}
	
	private static double mean(double[] array) {
		double mean = 0;
		for(int i = 0; i < array.length; i++) {
			mean += array[i];
		}
		return (double) (mean / array.length);
	}
	
	private static double standardDeviation(double[] array, double mean) {
		double sum = 0;
		for(int i = 0; i < array.length; i++) {
			sum += (double) Math.pow(array[i] - mean, 2);
		}
		double sd = (double) Math.sqrt(sum / (array.length - 1));
		return sd;
	}
}
