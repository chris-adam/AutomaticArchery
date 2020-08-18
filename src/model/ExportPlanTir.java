package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Tireur;

public class ExportPlanTir{

    private static String[] columns = {"Cible", "Blason", "Distance"};
    private ArrayList<Tireur> listTireurs =  new ArrayList<Tireur>();
    private ListCibles listCibles;
    private String peloton, FileName;
    
    public ExportPlanTir(ArrayList<Tireur> listTireurs, String peloton, String FileName){
    	for(Tireur tireur : listTireurs)
    		if(peloton.equals(String.valueOf(tireur.getPeloton())))
    			this.listTireurs.add(tireur);
		
		// On retrie dans l'ordre des cibles et blasons
		Collections.sort(this.listTireurs, new Comparator<Tireur>() {
		    public int compare(Tireur t1, Tireur t2) {
		    	if (t1.getCible() < t2.getCible())
		    		return -1;
		    	else if (t1.getCible() > t2.getCible())
		    		return 1;
		    	else if (t1.getBlason().compareTo(t2.getBlason()) < 0)
		    		return -1;
		    	else if (t1.getBlason().compareTo(t2.getBlason()) > 0)
		    		return 1;
		    	else
		    		return 0;
		    }
		});
		
    	this.peloton = peloton;
    	this.FileName = FileName;
    	this.listCibles = new ListCibles();
    }

    public void export() throws IOException, InvalidFormatException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Plan de tir");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row for the peloton
        Row pelotonRow = sheet.createRow(0);
        Cell pelotonCell = pelotonRow.createCell(0);
        pelotonCell.setCellValue("Peloton: " + this.peloton);
        pelotonCell.setCellStyle(headerCellStyle);

        // Create cells for column titles
        Row headerRow = sheet.createRow(1);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with employees data
        for(int rowNum = 0 ; rowNum < this.listCibles.getSize() ; rowNum++) {
            Row row = sheet.createRow(rowNum + 2);

            row.createCell(0).setCellValue(this.listCibles.getNumero(rowNum));

            row.createCell(1).setCellValue(this.listCibles.getBlason(rowNum));

            row.createCell(2).setCellValue(this.listCibles.getDistance(rowNum) + " m");
        }

		// Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(this.FileName + "_plan-tir_peloton-"+this.peloton+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
        
        JOptionPane.showMessageDialog(null, "Plan de tir terminé", "Terminé", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private class ListCibles{
    	private ArrayList<String> listNum = new ArrayList<>();
    	private ArrayList<String> listBlason = new ArrayList<>();
    	private ArrayList<String> listDistance = new ArrayList<>();
    	
    	public ListCibles(){
			ArrayList<String> listBlasonActu = new ArrayList<String>();
			
    		this.listNum.add(String.valueOf(listTireurs.get(0).getCible()));
    		listBlasonActu.add(listTireurs.get(0).getTypeBlason());
    		this.listDistance.add(String.valueOf(listTireurs.get(0).getDistance()));

    		for(Tireur tireur : listTireurs.subList(1, listTireurs.size())){
    			if(!String.valueOf(tireur.getCible()).equals(this.listNum.get(this.listNum.size()-1))) {
    				this.listNum.add(String.valueOf(tireur.getCible()));
					this.listBlason.add(this.getFormattedBlasons(listBlasonActu));
					listBlasonActu.clear();
					this.listDistance.add(String.valueOf(tireur.getDistance()));
    			}
    			listBlasonActu.add(tireur.getTypeBlason());
    		}
    		
    		if(this.listNum.size() > this.listBlason.size()) {
    			if(this.listNum.size() > 1)
    				listBlasonActu.add(listTireurs.get(listTireurs.size()-1).getTypeBlason());
    			this.listBlason.add(this.getFormattedBlasons(listBlasonActu));
    		}
    	}
    	
    	private String getFormattedBlasons(ArrayList<String> listBlasonActu){
    		return listBlasonActu.size() == 1 ? listBlasonActu.get(0) : String.join(" ; ", listBlasonActu);
    	}
    	
    	public String getNumero(int i){
    		return this.listNum.get(i);
    	}
    	
    	public String getBlason(int i){
    		return this.listBlason.get(i);
    	}
    	
    	public String getDistance(int i){
    		return this.listDistance.get(i);
    	}
    	
    	public int getSize(){
    		return this.listNum.size();
    	}
    }
}