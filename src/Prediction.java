import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//find if a sequence is in all 5 sheets, and if it is, return the sequence and value contained in 2nd column 
// in all 5 sheets.  then down the line find out if it is within 4/5 sheets
public class Prediction { 
	
	private String linearSequence, numValue;
	private static Map<Prediction, String> map,map2,map3,map4,map5;
	private static List<String> list,list2,list3,list4,list5;
	private static Set<List> set,set2,set3,set4,set5;
	private static List<Cell> cell;
	
	public Prediction(String sequence, String value) {
		this.linearSequence = sequence;
		this.numValue = value;
	}
	

	/*= = = = = = = = = = = important overridden methods and toString = = = = = = = = = = = = = = = = = */
	@Override
	public String toString() {
		return "( " + linearSequence + "  " + numValue + " )" + "\n";
	}
	/*= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = */

	
		/* parseCellValue is the same method as in Dna.java. It was included in Prediction.java because 
		 * when attempting to call on the method Eclipse thought making the method 'default' or 
		 * "static String parse...." would be correct.  I wasn't sure if this was appropriate so a copy 
		 * was made. */
	
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
	
	public static void prediction() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showOpenDialog(null);
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			try {
				InputStream imp = new FileInputStream(fileChooser.getSelectedFile());
				Workbook workbook = null;
				try {
					workbook = WorkbookFactory.create(imp);
				} catch (InvalidFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//get all 5 sheets and place values of column 0 and 1 into applicable collections
				Sheet sheet = workbook.getSheetAt(0);
				map = new LinkedHashMap<Prediction, String>();
				list = new ArrayList<String>();
				for(Row row: sheet) {
					Cell sequence = row.getCell(0);
					Cell value = row.getCell(1);
					
					String parseSequence =  parseCellValue(workbook, sequence);
					String parseValue = parseCellValue(workbook, value);
					
					Prediction prediction = new Prediction(parseSequence,parseValue);
					map.put(prediction, parseSequence);
					list.add(parseSequence);
				}
				
			    Sheet sheet1 = workbook.getSheetAt(1);
				list2 = new ArrayList<String>();
				for(Row row: sheet1) {
					Cell sequence = row.getCell(0);
					String parseSequence =  parseCellValue(workbook, sequence);
					
					Cell value = row.getCell(1);
					String parseValue = parseCellValue(workbook, value);
					
					Prediction prediction = new Prediction(parseSequence, parseValue);
					map.put(prediction, parseSequence);
					list2.add(parseSequence);	
				}
				
				Sheet sheet2 = workbook.getSheetAt(2);
				list3 = new ArrayList<String>();
				for(Row row: sheet2) {
					Cell sequence = row.getCell(0);
					String parseSequence =  parseCellValue(workbook, sequence);
					
					Cell value = row.getCell(1);
					String parseValue = parseCellValue(workbook, value);
					
					Prediction prediction = new Prediction(parseSequence, parseValue);
					map.put(prediction, parseSequence);
					list3.add(parseSequence);
				}
				
				Sheet sheet3 = workbook.getSheetAt(3);
				list4 = new ArrayList<String>();
				for(Row row: sheet3) {
					Cell sequence = row.getCell(0);
					String parseSequence =  parseCellValue(workbook, sequence);
					
					Cell value = row.getCell(1);
					String parseValue = parseCellValue(workbook, value);
					
					Prediction prediction = new Prediction(parseSequence, parseValue);
					map.put(prediction, parseSequence);
					list4.add(parseSequence);
				}
				
				Sheet sheet4 = workbook.getSheetAt(4);
				list5 = new ArrayList<String>();
				for(Row row: sheet4) {
					Cell sequence = row.getCell(0);
					String parseSequence =  parseCellValue(workbook, sequence);
					
					Cell value = row.getCell(1);
					String parseValue = parseCellValue(workbook, value);
					
					Prediction prediction = new Prediction(parseSequence, parseValue);
					map.put(prediction, parseSequence);
					list5.add(parseSequence);
				}
				/* Here every string in the five separate lists are put into a single, large list.
				 * Then, every list is compared against the large list to see if the current index
				 * of big (large list) is contained within the five lists.  If so, increment the 
				 * counter, and display values accordingly.  At the end, the values are put back
				 * into sets so as to eliminate duplicates. */
				List<String> big = new ArrayList();
				big.addAll(list);
				big.addAll(list2);
				big.addAll(list3);
				big.addAll(list4);
				big.addAll(list5);
			
				int counter;
				Set hasFour = new HashSet();
				Set hasFive = new HashSet();
				for(int k = 0; k < big.size(); k++) {
					counter = 0;
					
						if(list.contains(big.get(k))) {
							counter++;	
						}
						if(list2.contains(big.get(k))){
							counter++;	
						}
						if(list3.contains(big.get(k))) {
							counter++;	
						}
						if(list4.contains(big.get(k))) {
							counter++;	
						}
						if(list5.contains(big.get(k))) {
							counter++;	
						}
						
						if(counter ==4 ) {
							for(Map.Entry entry: map.entrySet()) {
								String value = (String) entry.getValue();
								if(value != null){
									if(value.equalsIgnoreCase(big.get(k))) {
										hasFour.add(entry.getKey());		
									}
								}
							}
						}
						if(counter == 5) {
							for(Map.Entry entry: map.entrySet()) {
								String value = (String) entry.getValue();
								if(value != null){
									if(value.equalsIgnoreCase(big.get(k))) {
										hasFive.add(entry.getKey());		
									}
								}	
							}
						}
				}
				
				System.out.println("======================================================");
				System.out.println("These sequences appeared in four of the five sheets: \n" 
							+ hasFour.toString().replace(",", "").replace("[", "").replace("]", ""));
				System.out.println("======================================================\n\n\n");
				
				
				System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				System.out.println("These sequences appeared in every sheet: \n" 
							+ hasFive.toString().replace(",", "").replace("[", "").replace("]", ""));
				System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				
			}
			
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
}

