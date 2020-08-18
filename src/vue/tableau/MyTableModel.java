package vue.tableau;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import data.Tireur;

// ---------- Modèle du tableau

public class MyTableModel extends AbstractTableModel {
	private ArrayList<Tireur> data = new ArrayList<Tireur>();

	public int getColumnCount() {return Tireur.title.length+Tireur.getCustomTitle().size();}
	public int getRowCount() {return this.data.size();}
	public String getColumnName(int col) {
		if(col < 0)
			return null;
		else if(col < Tireur.title.length)
			return Tireur.title[col];
		else
			return Tireur.getCustomTitle().get(col - Tireur.title.length);
	}
	
	public Object getValueAt(int row, int col) {
		try{
			switch(getColumnName(col)){
				case "Nom": return this.data.get(row).getNom();
				case "Prénom": return this.data.get(row).getPrenom();
				case "Club": return this.data.get(row).getClub();
				case "Matricule": return this.data.get(row).getMatricule();
				case "Catégorie": return this.data.get(row).getCategorie();
				case "Peloton": return this.data.get(row).getPeloton();
				case "Distance": return Integer.toString(this.data.get(row).getDistance()) + " m";
				case "Cible": return this.data.get(row).getCible();
				case "N° blason": return this.data.get(row).getBlason();
				case "Type de blason": return this.data.get(row).getTypeBlason();
				case "Statut": return this.data.get(row).getStatut();
				case "Série 1": return this.data.get(row).getSerie1points();
				case "S1 X/10": return this.data.get(row).getSerie1a();
				case "S1 10/9": return this.data.get(row).getSerie1b();
				case "Série 2": return this.data.get(row).getSerie2points();
				case "S2 X/10": return this.data.get(row).getSerie2a();
				case "S2 10/9": return this.data.get(row).getSerie2b();
				case "Total": return this.data.get(row).getSerieTotal();
				case "Tot X/10": return this.data.get(row).getSerieaTotal();
				case "Tot 10/9": return this.data.get(row).getSeriebTotal();
				default:
					if(col < Tireur.title.length + Tireur.getCustomTitle().size())
							return this.data.get(row).getCustomRow().get(col - Tireur.title.length);
			}
		} catch (IndexOutOfBoundsException e) {return null;}
		return null;
	}
	
	public int getRowID(int row){
		return this.data.get(row).getID();
	}
	
	public void setValueAt(Object value, int row, int col){
		try{
			switch(getColumnName(col)){
				case "Nom": this.data.get(row).setNom(value.toString()); break;
				case "Prénom": this.data.get(row).setPrenom(value.toString()); break;
				case "Club": this.data.get(row).setClub(value.toString().toUpperCase()); break;
				case "Matricule": this.data.get(row).setMatricule(value.toString().toUpperCase()); break;
				case "Catégorie": this.data.get(row).setCategorie(value.toString().toUpperCase()); break;
				case "Peloton": this.data.get(row).setPeloton(Integer.valueOf(value.toString())); break;
				case "Distance": this.data.get(row).setDistance(Integer.valueOf(value.toString())); break;
				case "Cible": this.data.get(row).setCible(Integer.valueOf(value.toString())); break;
				case "N° blason": this.data.get(row).setBlason(value.toString().substring(0, 1).toUpperCase()); break;
				case "Type de blason": this.data.get(row).setTypeBlason(value.toString()); break;
				case "Statut": this.data.get(row).setStatut(value.toString()); break;
				case "Série 1": this.data.get(row).setSerie1points(Integer.valueOf(value.toString())); break;
				case "S1 X/10": this.data.get(row).setSerie1a(Integer.valueOf(value.toString())); break;
				case "S1 10/9": this.data.get(row).setSerie1b(Integer.valueOf(value.toString())); break;
				case "Série 2": this.data.get(row).setSerie2points(Integer.valueOf(value.toString())); break;
				case "S2 X/10": this.data.get(row).setSerie2a(Integer.valueOf(value.toString())); break;
				case "S2 10/9": this.data.get(row).setSerie2b(Integer.valueOf(value.toString())); break;
				default:
					try{
						if(col < Tireur.title.length + Tireur.getCustomTitle().size())
							this.data.get(row).setCustomRow(col - Tireur.title.length, value.toString());
					} catch(ArrayIndexOutOfBoundsException e) {}
			}
			this.fireTableCellUpdated(row,  col);
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "La valeur doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Class<?> getColumnClass(int col){
		if(this.data.size() > 0)
			return this.getValueAt(0, col).getClass();
		else
			return Object.class;
	}
	
	public boolean isCellEditable(int row, int col){
		return true;
	}
	
	public void update(ArrayList<Tireur> data){
		this.data = new ArrayList<Tireur>();
		for(Tireur tireur : data)
			this.data.add(tireur);
		this.fireTableStructureChanged();
	}
}
