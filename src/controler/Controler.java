package controler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.Categorie;
import data.Competition;
import data.Tireur;
import model.Model;

public class Controler
{
	private Model model;
	private String nomSauvegarde = null;
	
	public Controler(Model model){
		this.model = model;
	}
	
	public Boolean ajouterTireur(ArrayList<JComponent> champs){
		try{
			Tireur tireur = new Tireur(((JTextField)champs.get(0)).getText().substring(0, 1).toUpperCase() + ((JTextField)champs.get(0)).getText().substring(1), 
									   ((JTextField)champs.get(1)).getText().substring(0, 1).toUpperCase() + ((JTextField)champs.get(1)).getText().substring(1), 
									   ((JTextField)champs.get(2)).getText().toUpperCase(), ((JComboBox<?>)champs.get(3)).getSelectedItem().toString().toUpperCase(), 
									   ((JTextField)champs.get(4)).getText().toUpperCase(), Integer.valueOf(((JTextField)champs.get(5)).getText().substring(0, 1)));
			model.ajouterTireur(tireur);
			return true;
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "<NumberFormatException>\nErreur d'encodage du nouveau tireur.\nLe peloton doit être un numéro.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch(NullPointerException e) {
			JOptionPane.showMessageDialog(null, "<NullPointerException>\nErreur d'encodage du nouveau tireur.\nRenseignez tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public void addCol(String title){
		this.model.addCol(title);
	}
	
	public void supprimerTireur(int ID) {
		if(JOptionPane.YES_OPTION == 
		JOptionPane.showConfirmDialog(null, "Supprimer le tireur ?", "Suppresion d'un tireur", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
			this.model.supprimerTireur(ID);
	}
	
	public void ajouterCompetition(){
		String compet = JOptionPane.showInputDialog(null, "Nom du type de compétition : ", "Créer un nouveau type de compétition", JOptionPane.PLAIN_MESSAGE);
		if(compet != null)
			this.model.addCompetition(new Competition(compet));
	}
	
	public void ajouterCategorie(){
		AjouterCategoriePrompt catPrompt = new AjouterCategoriePrompt(null, "Ajouter une nouvelle catégorie", true);
		Categorie cat = catPrompt.getCategorie();
		if(cat != null && !this.model.addCategorie(cat))
			JOptionPane.showMessageDialog(null, "La catégorie n'a pas pu être ajoutée.\n"
					+ "Vérifiez si: \n- Un type de compétition a été sélectionné \n- La catégorie existe déjà", "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	public void selectCompetition(){
		// Dans ce cas, le JOptionPane sera généré dans le modèle
		this.model.selectCompetition();
	}
	
	public void supprimerCompetition(){
		// Dans ce cas, le JOptionPane sera généré dans le modèle
		this.model.supprimerCompetition();
	}

	public void supprimerCategorie(){
	// Dans ce cas, le JOptionPane sera généré dans le modèle
		this.model.supprimerCategorie();
	}
	
	public void setPelotonFilter(int peloton) {
		this.model.setPelotonFilter(peloton);
	}
	
	public void supprimerColonne(String title) {
		if(JOptionPane.YES_OPTION == 
		JOptionPane.showConfirmDialog(null, "Supprimer la colonne ?", "Suppresion d'une colonne personnalisée", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
			this.model.supprimerColonne(title);
	}
	
	public int nouveau(){
		File file;
		String nomSauvegardeTemp;
		int rep = JOptionPane.OK_OPTION;
		
    	do{
    		nomSauvegardeTemp = JOptionPane.showInputDialog(null, "Nom du fichier:", "Nouveau", JOptionPane.PLAIN_MESSAGE);
        	file = new File("save\\" + nomSauvegardeTemp + ".txt");
        	if(file.exists())
        		rep = JOptionPane.showConfirmDialog(null, "Un fichier du même nom existe déjà.\nVoulez-vous l'écraser ?", "Erreur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    	} while(file.exists() && rep != JOptionPane.OK_OPTION);
    	
    	if(nomSauvegardeTemp != null && rep == JOptionPane.OK_OPTION) {
    		this.nomSauvegarde = nomSauvegardeTemp;
    		this.model.sauvegarder(this.nomSauvegarde);
    		return JOptionPane.OK_OPTION;
    	}
    	else
    		return JOptionPane.CANCEL_OPTION;
	}
	
	public void sauvegarder(Boolean newSave){
		if(newSave || this.nomSauvegarde == null)
			this.nouveau();
		else
			this.model.sauvegarder(this.nomSauvegarde);
	}
	
	public void ouvrir(){
		File file = new File("save");
		File[] listFiles = file.listFiles();
		ArrayList<Object> list = new ArrayList<Object>();
		for(File fileTemp : listFiles)
			list.add(fileTemp.getName().substring(0, fileTemp.getName().length()-4));
		
		if(list.size() < 1)
			JOptionPane.showMessageDialog(null, "Aucun fichier à charger", "Erreur", JOptionPane.ERROR_MESSAGE);
		else {
			String nomACharger = (String)JOptionPane.showInputDialog(null, "Nom du fichier:", "Charger", JOptionPane.PLAIN_MESSAGE, null, list.toArray(), list.get(0));
			if(nomACharger != null){
				this.nomSauvegarde = nomACharger;
				this.supprimerTireurs();
				this.model.ouvrir(this.nomSauvegarde);
			}
		}
	}
	
	public void supprimer(){
		String nomASupprimer;
		File file = new File("save");
		File[] listFiles = file.listFiles();
		ArrayList<Object> list = new ArrayList<Object>();
		for(File fileTemp : listFiles)
			list.add(fileTemp.getName().substring(0, fileTemp.getName().length()-4));
		
		if(list.size() < 1)
			JOptionPane.showMessageDialog(null, "Aucun fichier à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
		else {
			nomASupprimer = (String)JOptionPane.showInputDialog(null, "Nom de la compétition:", "Supprimer", JOptionPane.PLAIN_MESSAGE, null, list.toArray(), list.get(0));
			if(this.nomSauvegarde.equals(nomASupprimer))
				this.supprimerTireurs();
			this.model.supprimer(nomASupprimer);
		}
	}
	
	public void supprimerTireurs() {this.model.supprimerTireurs();}
	
	public void setPlanCible() {
		if(JOptionPane.YES_OPTION == 
		JOptionPane.showConfirmDialog(null, "Cette action supplante les éventuelles modifications manuelles.", "Assigner les cibles", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
			this.model.setPlanCible();
	}
	
	public void setPlanBlasons() {
		if(JOptionPane.YES_OPTION == 
		JOptionPane.showConfirmDialog(null, "Cette action supplante les éventuelles modifications manuelles.", "Assigner les blasons", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
			this.model.setPlanBlasons();
	}
	
	public void setABCD(String type) {this.model.setABCD(type.length());}
	
	public void getPlanCible(){
		try {
			this.model.getPlanTir(String.valueOf(JOptionPane.showInputDialog(null, "Choix du peloton:", "Plan de tir", JOptionPane.PLAIN_MESSAGE, null, 
					Arrays.copyOfRange(Tireur.getPelotonCombo(), 1, Tireur.getPelotonCombo().length), Tireur.getPelotonCombo()[1])));
		} catch(ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "Données insuffisantes pour l'export en Excel", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void getClassement(){
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, "Générer le classement ? \nAttention, cette action peut écraser le dernier classement généré",
				"Générer le classement", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE))
			this.model.getClassement();
	}
	
	public void getPlanTireurs(){
		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, "Générer le plan des tireurs ? "
				+ "\nAttention, cette action peut écraser le dernier plan généré",
				"Générer le plan des tireurs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE))

			try {
				this.model.getPlanTireurs(String.valueOf(JOptionPane.showInputDialog(null, "Choix du peloton:", "Plan de tir", JOptionPane.PLAIN_MESSAGE, null, 
						Arrays.copyOfRange(Tireur.getPelotonCombo(), 1, Tireur.getPelotonCombo().length), Tireur.getPelotonCombo()[1])));
			} catch(ArrayIndexOutOfBoundsException e) {
				JOptionPane.showMessageDialog(null, "Données insuffisantes pour l'export en Excel", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
	}
}
