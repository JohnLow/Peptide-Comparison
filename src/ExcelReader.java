

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ExcelReader {
	
	
	/*public static Vehicle searchForVehicleID(ArrayList<Vehicle> Vehicles)
	{
	String vehicleID = JOptionPane.showInputDialog(null, "Enter a vehicle ID:");
	
	for (Vehicle ArrayData : Vehicles)
	{
	if (ArrayData.getVehicleID().equalsIgnoreCase(vehicleID))
	{
	return ArrayData;
	}
	}
	
	return null;
	}*/
	public static void main (String[] args){
		
		Dna.chooseYourDestiny();
		Dna.chooseFiles();
		
		
	}
	
}