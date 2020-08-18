package model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.Tireur;

public class ExportClassement{
	
	private Workbook workbook;
	private Sheet sheet;
    private static String[] columns = {"Nom", "Prénom", "Club", "Matricule", "Cat.", "Pts S1", "10 S1", "9 S1", "Pts S2", "10 S2", "9 S2", "Pts", "10", "9"};
    private ArrayList<Tireur> listTireurs;
    private String FileName, currentCat = "";
    private static String[] ordreCats = {"ASP", "NLJ", "NLS", "NLM", "NLV", "NRJ", "NRS", "NRM", "NRV", "CDP", "CDB", "CDJ", "CDC", "CDJ", "CD1", "CD2", 
    		"CDM", "CDV", "CHP", "CHB", "CHC", "CHJ", "CH1", "CH2", "CHM", "CHV", "RDP", "RDB", "RDJ", "RDC", "RDJ", "RD1", "RD2", "RDM", "RDV", "RHP", 
    		"RHB", "RHC", "RHJ", "RH1", "RH2", "RHM", "RHV"};
    
    public ExportClassement(ArrayList<Tireur> listTireurs, String FileName){
    	
        // Create a Workbook
        workbook = new XSSFWorkbook();
        // Create a Sheet
        sheet = workbook.createSheet("Plan de tir");
        
    	this.listTireurs = listTireurs;
		
		// On retrie dans l'ordre des cibles et blasons
		Collections.sort(this.listTireurs, new Comparator<Tireur>() {
		    public int compare(Tireur t1, Tireur t2) {
		    	// Tri selon les catégories
		    	if (Arrays.asList(ordreCats).contains(t1.getCategorie()) && Arrays.asList(ordreCats).contains(t2.getCategorie()))
			    	if (Arrays.asList(ordreCats).indexOf(t1.getCategorie()) < Arrays.asList(ordreCats).indexOf(t2.getCategorie()))
			    		return -1;
			    	else if (Arrays.asList(ordreCats).indexOf(t1.getCategorie()) > Arrays.asList(ordreCats).indexOf(t2.getCategorie()))
			    		return 1;
			    else
			    	if (Arrays.asList(ordreCats).contains(t1.getCategorie()) || Arrays.asList(ordreCats).contains(t2.getCategorie()))
				    	if (!Arrays.asList(ordreCats).contains(t1.getCategorie()))
				    		return -1;
				    	else
				    		return 1;
			    	else
				    	if (t1.getCategorie().compareTo(t2.getCategorie()) < 0)
				    		return -1;
				    	else if (t1.getCategorie().compareTo(t2.getCategorie()) > 0)
				    		return 1;
			    
		    	// Tri selon les points
		    	if (t1.getSerieTotal() < t2.getSerieTotal())
		    		return -1;
		    	else if (t1.getSerieTotal() > t2.getSerieTotal())
		    		return 1;
		    	else if (t1.getSerieaTotal() < t2.getSerieaTotal())
		    		return -1;
		    	else if (t1.getSerieaTotal() > t2.getSerieaTotal())
		    		return 1;
		    	else if (t1.getSeriebTotal() < t2.getSeriebTotal())
		    		return -1;
		    	else if (t1.getSeriebTotal() > t2.getSeriebTotal())
		    		return 1;
		    	else
		    		return 0;
		    }
		});
		
    	this.FileName = FileName;
    }

    
    public void export() throws IOException, InvalidFormatException {

        // Create Other rows and cells with archers data
        int rowNum = 0;
        for(int i = 0 ; i < this.listTireurs.size() ; i++) {
            Row row = sheet.createRow(rowNum);
            if(!this.currentCat.equals(this.listTireurs.get(i).getCategorie())){
            	if(rowNum>0)
            		row = sheet.createRow(++rowNum);
            	
            	this.currentCat = this.listTireurs.get(i).getCategorie();
            	this.createTitles(row);
            	row = sheet.createRow(++rowNum);
            }
            this.createRowTireur(row, i);
            rowNum++;
        }

		// Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(this.FileName + "_classement.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
        
        JOptionPane.showMessageDialog(null, "Classement terminé", "Terminé", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void createRowTireur(Row row, int tireurIndex){

        // CellStyle for the main spreadsheet 
        CellStyle mainCellStyle = workbook.createCellStyle();
        mainCellStyle.setBorderBottom(BorderStyle.THIN);
        mainCellStyle.setBorderTop(BorderStyle.THIN);
        mainCellStyle.setBorderRight(BorderStyle.THIN);
        mainCellStyle.setBorderLeft(BorderStyle.THIN);
		mainCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create Other rows and cells with employees data
        for(int i = 0; i < columns.length; i++) {
        	Cell cell = row.createCell(i);
        	cell.setCellStyle(mainCellStyle);

        	switch(i) {
        		case 0:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getNom());
        			break;
        			// TODO ajouter prénom
        		case 1:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getPrenom());
        			break;
        		case 2:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getClub());
        			break;
        		case 3:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getMatricule());
        			break;
        		case 4:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getCategorie());
        			break;
        		case 5:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerie1points());
        			break;
        		case 6:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerie1a());
        			break;
        		case 7:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerie1b());
        			break;
        		case 8:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerie2points());
        			break;
        		case 9:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerie2a());
        			break;
        		case 10:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerie2b());
        			break;
        		case 11:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerieTotal());
        			break;
        		case 12:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSerieaTotal());
        			break;
        		case 13:
        			cell.setCellValue(this.listTireurs.get(tireurIndex).getSeriebTotal());
        			break;
        	}
        }
    }
    
    
    private void createTitles(Row row){
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create cells for column titles
        for(int i = 0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }
}