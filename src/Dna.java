import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class Dna implements Comparable<Dna>{
	
private String linSeq;
private String allele;
private String qualt;
private static List<Dna> list1;
private static List<Dna> list2;
private Dna posArray;
private Dna negArray;

	public Dna(String linearSequence, String alleleName, String qualtMeasure){
		linSeq = linearSequence;
		allele = alleleName;
		qualt = qualtMeasure;
	}
	
	public String getSequence() {
		return linSeq;
	}
	
	private static String parseCellValue(Workbook workBook, Cell cell) {      
	    FormulaEvaluator evaluator = workBook.getCreationHelper().createFormulaEvaluator();
	    String cellValue = null;              
	    if (cell != null) {
	        switch (cell.getCellType()) {
	            case Cell.CELL_TYPE_STRING:
	                cellValue = cell.getRichStringCellValue().getString();
	                break;
	            case Cell.CELL_TYPE_NUMERIC:
	                if (DateUtil.isCellDateFormatted(cell)) {
	                    cellValue = cell.getDateCellValue().toString();
	                } else {
	                    cellValue = new Double(cell.getNumericCellValue()).toString();
	                }
	                break;
	            case Cell.CELL_TYPE_BOOLEAN:
	                cellValue = new Boolean(cell.getBooleanCellValue()).toString();
	                break;
	            case Cell.CELL_TYPE_FORMULA:
	                cellValue = evaluator.evaluate(cell).formatAsString();
	                break;
	        }                  
	    }
	    return cellValue;
	}
	
	public static void chooseYourDestiny() {
		Component frame = null;
		Object[] options = {"Comparison",
        "Prediction"};
		int n = JOptionPane.showOptionDialog(frame,
		"Which type of analysis would you \n" + "like to perform?",
		"Hark!",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,     //do not use a custom Icon
		options,  //the titles of buttons
		options[0]); //default button title
	}
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//prompts the user to enter the file containing positive peptides
	public static void positiveMessage() {
		Component frame = null;
		JOptionPane.showMessageDialog(frame,
			    "Please enter the file containing 'negative' peptides.",
			    "Hark!",
			    JOptionPane.PLAIN_MESSAGE);
	}
	
	//prompts the user to enter the file containing negative peptides
	public static void negativeMessage() {
		Component frame = null;
		JOptionPane.showMessageDialog(frame,
			    "Please enter the file containing 'positive' peptides.",
			    "Hark!",
			    JOptionPane.PLAIN_MESSAGE);
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	
	//if linear Sequence is in both files, and the AlleleName is the same, just count
	//how many times it occurs.
	public static void chooseFiles() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		
		if(returnValue == JFileChooser.APPROVE_OPTION){
			try {
				Workbook workbook = new HSSFWorkbook(new FileInputStream(fileChooser.getSelectedFile()));
				
				Sheet sheet = workbook.getSheetAt(0);
				List<Dna> list1 = new ArrayList<Dna>();
				
				//iterating through every row, only the cells in columns A, H, and J are read
				for(Row row: sheet){
					Cell linearSequence = row.getCell(63);
					String parseSequence =  parseCellValue(workbook, linearSequence);
					
					Cell alleleName = row.getCell(74);
					String parseAllele = parseCellValue(workbook, alleleName);
					
					Cell qualtMeasure = row.getCell(78);
					String parseQuality = parseCellValue(workbook, qualtMeasure);
					
					//creating a new Dna object which takes in the parsed cell value
					//and is then added to the List
					Dna posArray = new Dna(parseSequence, parseAllele, parseQuality);
					list1.add(posArray);
					
					
				}
				Collections.sort(list1);
				System.out.println(list1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		JFileChooser fileChooser2 = new JFileChooser();
		int returnValue2 = fileChooser2.showOpenDialog(null);
		//bk, bw, ca
		//if linear Sequence is in both files, and the AlleleName is the same, just count
		//how many times it occurs.
		if(returnValue2 == JFileChooser.APPROVE_OPTION){
			try {
				Workbook workbook = new HSSFWorkbook(new FileInputStream(fileChooser2.getSelectedFile()));
				
				Sheet sheet = workbook.getSheetAt(0);
				List<Dna> list2 = new ArrayList<Dna>();
				
				//iterating through every row, only the cells in columns A, H, and J are read
				for(Row row: sheet){
					Cell linearSequence = row.getCell(63);
					String parseSequence =  parseCellValue(workbook, linearSequence);
					
					Cell alleleName = row.getCell(74);
					String parseAllele = parseCellValue(workbook, alleleName);
					
					Cell qualtMeasure = row.getCell(78);
					String parseQuality = parseCellValue(workbook, qualtMeasure);
					
					//creating a new ArrayList which takes in the parsed cell value
					Dna negArray = new Dna(parseSequence, parseAllele, parseQuality);
					list2.add(negArray);
					
				}
				System.out.println(list2);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		int count = 0;
		try {
			for(Dna pos: list1){
				for(Dna neg: list2) {
					if(pos.compareTo(neg) == 0) {
						count ++;
					}
				}
			}
		}
		catch(NullPointerException e) {
		}
		System.out.println(count);
	}
	
	//PrinterWriter out = new PrintWriter("filename.txt");
	//out.println(text);
	//out.close();
	/*public static void negativeFile() {
		JFileChooser fileChooser2 = new JFileChooser();
		int returnValue2 = fileChooser2.showOpenDialog(null);
		//bk, bw, ca
		//if linear Sequence is in both files, and the AlleleName is the same, just count
		//how many times it occurs.
		if(returnValue2 == JFileChooser.APPROVE_OPTION){
			try {
				Workbook workbook = new HSSFWorkbook(new FileInputStream(fileChooser2.getSelectedFile()));
				
				Sheet sheet = workbook.getSheetAt(0);
				List<Dna> Dna = new ArrayList<Dna>();
				
				//iterating through every row, only the cells in columns A, H, and J are read
				for(Row row: sheet){
					Cell linearSequence = row.getCell(63);
					String parseSequence =  parseCellValue(workbook, linearSequence);
					
					Cell alleleName = row.getCell(74);
					String parseAllele = parseCellValue(workbook, alleleName);
					
					Cell qualtMeasure = row.getCell(78);
					String parseQuality = parseCellValue(workbook, qualtMeasure);
					
					//creating a new ArrayList which takes in the parsed cell value
					Dna negArray = new Dna(parseSequence, parseAllele, parseQuality);
					Dna.add(negArray);
					
					Collections.sort(Dna);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	} */
	
	public String toString(){
		String thing = new String(linSeq + " " + allele + ", " + qualt + "\n");
		return thing;
	}
	
	@Override
	public int compareTo(Dna posOrNeg) {
	
		return posOrNeg.getSequence().compareTo(this.linSeq);
	}
	
}
	
