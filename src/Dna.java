import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class Dna {
	
private String linearSequence, alleleName, qualtMeasure;
private static Map<Dna, String> map1;
private static Map<Dna, String> map2;
private static Dna pos;
private static Dna neg;


	public Dna(String linearSequence, String alleleName, String qualtMeasure){
		this.linearSequence = linearSequence;
		this.alleleName = alleleName;
		this.qualtMeasure = qualtMeasure;
	}
	
	// getter method
	public String getSequence() {
		return linearSequence;
	}
	
	// parses the excel sheet into strings
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
	
	//+++++++++++++++++++++++++ GUI methods ++++++++++++++++++++++++++++++++++++++++
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
	
	
	//prompts the user to enter the files containing peptides
	public static void prompt() {
		Component frame = null;
		JOptionPane.showMessageDialog(frame,
			    "Please enter the file containing 'positive' peptides, \n" + 
			    "followed by the file containing the 'negative' peptides.",
			    "Hark!",
			    JOptionPane.PLAIN_MESSAGE);
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

  /*bk, bw, ca are the columns in the excel file to be read
    if linear Sequence is in both files, and the AlleleName is the same, just count
	how many times it occurs. */
	public static void getFiles() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		
		if(returnValue == JFileChooser.APPROVE_OPTION){
			try {
				Workbook workbook = new HSSFWorkbook(new FileInputStream(fileChooser.getSelectedFile()));
				Sheet sheet = workbook.getSheetAt(0);
				map1 = new HashMap<Dna, String>();
				
				//iterating through every row, only the cells in columns A, H, and J are read
				for(Row row: sheet){
					Cell linearSequence = row.getCell(63);
					String parseSequence =  parseCellValue(workbook, linearSequence);
					
					Cell alleleName = row.getCell(74);
					String parseAllele = parseCellValue(workbook, alleleName);
					
					Cell qualtMeasure = row.getCell(78);
					String parseQuality = parseCellValue(workbook, qualtMeasure);
					
					//creating a new Dna object which takes in the parsed cell value
					//and is then added to the map
					pos = new Dna(parseSequence, parseAllele, parseQuality);
					map1.put(pos, parseSequence);
					
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e){
				e.printStackTrace();
			}
		
		JFileChooser fileChooser2 = new JFileChooser();
		int returnValue2 = fileChooser2.showOpenDialog(null);
		
		if(returnValue2 == JFileChooser.APPROVE_OPTION){
			try {
				Workbook workbook = new HSSFWorkbook(new FileInputStream(fileChooser2.getSelectedFile()));
				Sheet sheet = workbook.getSheetAt(0);
				map2 = new LinkedHashMap<Dna, String>();
				//iterating through every row, only the cells in columns A, H, and J are read
				for(Row row: sheet){
					Cell linearSequence = row.getCell(63);
					String parseSequence =  parseCellValue(workbook, linearSequence);
					
					Cell alleleName = row.getCell(74);
					String parseAllele = parseCellValue(workbook, alleleName);
					
					Cell qualtMeasure = row.getCell(78);
					String parseQuality = parseCellValue(workbook, qualtMeasure);
					
					
					neg = new Dna(parseSequence, parseAllele, parseQuality);
					
					map2.put(neg, parseSequence);
					
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
			
			/* here we compare the size of the maps in order to make sure that commonKeys
			 * is larger than other map, thereby truly checking every element */
			if(map1.size() > map2.size()){
				Set<Dna>commonKeys = new HashSet<Dna>(map1.keySet());
				commonKeys.retainAll(map2.keySet());
				System.out.println(commonKeys.size() + " common peptides were found.");
			}
			else {
				Set<Dna>commonKeys = new HashSet<Dna>(map2.keySet());
				commonKeys.retainAll(map1.keySet());
				System.out.println(commonKeys.size()  + " common peptides were found.");
			}
			
		}
		
	}	
	
	//PrinterWriter out = new PrintWriter("filename.txt");
	//out.println(text);
	//out.close();
	
/*= = = = = = = = = = = important overridden methods and toString = = = = = = = = = = = = = = = = = = = = = = */
	
	public String toString(){
		
		return linearSequence + "\t" + alleleName + "\t\t" + qualtMeasure;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alleleName == null) ? 0 : alleleName.hashCode());
		result = prime * result
				+ ((linearSequence == null) ? 0 : linearSequence.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dna other = (Dna) obj;
		if (alleleName == null) {
			if (other.alleleName != null)
				return false;
		} else if (!alleleName.equals(other.alleleName))
			return false;
		if (linearSequence == null) {
			if (other.linearSequence != null)
				return false;
		} else if (!linearSequence.equals(other.linearSequence))
			return false;
		return true;
	}
/*= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = */
}
	
