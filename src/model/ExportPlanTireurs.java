package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Tireur;

public class ExportPlanTireurs{

    private static String[] columns = {"Nom", "Prénom", "Matricule", "Catégorie", "Distance", "Cible"};
    private ArrayList<Tireur> listTireurs =  new ArrayList<Tireur>();
    private String peloton, FileName;
    
    public ExportPlanTireurs(ArrayList<Tireur> listTireurs, String peloton, String FileName){
    	for(Tireur tireur : listTireurs)
    		if(peloton.equals(String.valueOf(tireur.getPeloton())))
    			this.listTireurs.add(tireur);
		
		// On retrie dans l'ordre des cibles et blasons
		Collections.sort(this.listTireurs, new Comparator<Tireur>() {
		    public int compare(Tireur t1, Tireur t2) {
		    	return t1.getNom().compareToIgnoreCase(t2.getNom()) != 0 ? t1.getNom().compareToIgnoreCase(t2.getNom()) :
		    		t1.getPrenom().compareToIgnoreCase(t2.getPrenom());
		    }
		});
		
    	this.peloton = peloton;
    	this.FileName = FileName;
    }

    public void export() throws IOException, InvalidFormatException {
        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Plan des tireurs");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);

        // Create a Row for the peloton
        Row pelotonRow = sheet.createRow(0);
        Cell pelotonCell = pelotonRow.createCell(0);
        pelotonCell.setCellValue("Peloton: " + this.peloton);
        pelotonCell.setCellStyle(headerCellStyle);


        // Create cells for column titles
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        Row headerRow = sheet.createRow(1);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // CellStyle for the main spreadsheet 
        CellStyle mainCellStyle = workbook.createCellStyle();
        mainCellStyle.setBorderBottom(BorderStyle.THIN);
        mainCellStyle.setBorderTop(BorderStyle.THIN);
        mainCellStyle.setBorderRight(BorderStyle.THIN);
        mainCellStyle.setBorderLeft(BorderStyle.THIN);
        mainCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create Other rows and cells with employees data
        for(int rowNum = 0 ; rowNum < this.listTireurs.size() ; rowNum++) {
            Row row = sheet.createRow(rowNum + 2);
            

            for(int i = 0; i < columns.length; i++) {
            	Cell cell = row.createCell(i);
            	cell.setCellStyle(mainCellStyle);
            	
            	switch(i) {
            		case 0:
            			cell.setCellValue(this.listTireurs.get(rowNum).getNom());
            			break;
            		case 1:
            			cell.setCellValue(this.listTireurs.get(rowNum).getPrenom());
            			break;
            		case 2:
            			cell.setCellValue(this.listTireurs.get(rowNum).getMatricule());
            			break;
            		case 3:
            			cell.setCellValue(this.listTireurs.get(rowNum).getCategorie());
            			break;
            		case 4:
            			cell.setCellValue(this.listTireurs.get(rowNum).getDistance() + "m");
            			break;
            		case 5:
            			cell.setCellValue(this.listTireurs.get(rowNum).getCible()+this.listTireurs.get(rowNum).getBlason());
            			break;
            	}
            }
        }

		// Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(this.FileName + "_plan-tireurs_peloton-"+this.peloton+".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
        
        JOptionPane.showMessageDialog(null, "Plan des tireurs terminé", "Terminé", JOptionPane.INFORMATION_MESSAGE);
    }
}