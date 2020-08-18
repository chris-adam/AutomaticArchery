package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import data.Categorie;
import data.Competition;
import data.StaticTireur;
import data.Tireur;
import observer.Observable;
import observer.Observer;

public class Model implements Observable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private ArrayList<Tireur> listTireurs = new ArrayList<Tireur>();
	private transient ArrayList<Competition> listCompetitions = new ArrayList<Competition>();
	private StaticTireur staticTireur = new StaticTireur();
	private transient int peloton = 0;
	private int ABCD = 4;
	private String fileName;
	private String competitionName;
	
	public Model(){
		this.updatePelotonList();
	}
	
	public void ajouterTireur(Tireur tireur){
		this.listTireurs.add(tireur);
		this.updatePelotonList();
	}
	
	public void supprimerTireur(int ID){
		Tireur tireur = this.getTireurAtID(ID);
		if(tireur != null){
			this.listTireurs.remove(tireur);
			this.updatePelotonList();
		}
	}
	
	public void supprimerTireurs() {
		this.listTireurs = new ArrayList<Tireur>();
		this.notifyObserver();
	}
	
	public void addCompetition(Competition newCompet){
		Boolean isIn = false;
		for(Competition compet : this.listCompetitions)
			if(compet.getName().equals(newCompet.getName()))
				isIn = true;
		if(!isIn)
			this.listCompetitions.add(newCompet);
		else
			JOptionPane.showMessageDialog(null, "Ce nom existe déjà", "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	public Boolean addCategorie(Categorie cat){
			
		Boolean added = false;
		int i = 0;
		while(i < this.listCompetitions.size() && !added){
			if(this.listCompetitions.get(i).getName().equals(this.competitionName)){
				ArrayList<String> listCategorieNames = new ArrayList<String>();
				for(Categorie catTemp : this.listCompetitions.get(i).getListCategories())
					listCategorieNames.add(catTemp.getCategorie());
				
				if(!listCategorieNames.contains(cat.getCategorie())) {
					this.listCompetitions.get(i).addCategorie(cat);
					Tireur.setListCategories(this.listCompetitions.get(i).getListCategories());
					added = true;
				}
			}
			i++;
		}
		
		if(added) this.notifyObserver();
		return added; // renvoie true si la catégorie a bien été ajoutée
	}
	
	public void selectCompetition(){
		ArrayList<String> listCompetitionNames = new ArrayList<String>();
		for(Competition compet : this.listCompetitions)
			listCompetitionNames.add(compet.getName());
		
		if(listCompetitionNames.size() > 0) { 
			Object competitionNameTemp = JOptionPane.showInputDialog(null, "Nom du type de compétition", "Sélection du type de compétition", 
					JOptionPane.PLAIN_MESSAGE, null, listCompetitionNames.toArray(), listCompetitionNames.get(0));
			if(competitionNameTemp != null) {
				this.competitionName = competitionNameTemp.toString();
				this.setListCategories();
			}
		}
		else
			JOptionPane.showMessageDialog(null, "Aucun type de compétition sauvegardé", "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	public void supprimerCompetition(){
		ArrayList<String> listCompetitionNames = new ArrayList<String>();
		for(Competition compet : this.listCompetitions)
			listCompetitionNames.add(compet.getName());
		
		if(listCompetitionNames.size() > 0) {
			try {
				String competitionASupprimer = JOptionPane.showInputDialog(null, "Nom du type de compétition", "Suppression d'un type de compétition", 
						JOptionPane.PLAIN_MESSAGE, null, listCompetitionNames.toArray(), listCompetitionNames.get(0)).toString();
				
				int i = 0;
				while(i < this.listCompetitions.size()) {
					if(this.listCompetitions.get(i).getName().equals(competitionASupprimer))
						this.listCompetitions.remove(i--);
					i++;
				}
				
				if(competitionASupprimer.equals(this.competitionName))
					this.competitionName = "";
			} catch(NullPointerException e) {}
		}
		else
			JOptionPane.showMessageDialog(null, "Aucun type de compétition sauvegardé", "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	public void supprimerCategorie(){
		ArrayList<String> listCompetitionNames = new ArrayList<String>();
		for(Competition compet : this.listCompetitions)
			listCompetitionNames.add(compet.getName());
		
		try{
			ArrayList<String> listCategorieNames = new ArrayList<String>();
			for(Categorie cat : this.listCompetitions.get(listCompetitionNames.indexOf(this.competitionName)).getListCategories())
				listCategorieNames.add(cat.getCategorie());
			
			if(listCategorieNames.size() > 0) {
				try {
					String categorieASupprimer = JOptionPane.showInputDialog(null, "Nom de la catégorie", "Suppression d'une catégorie", JOptionPane.PLAIN_MESSAGE, 
							null, listCategorieNames.toArray(), listCategorieNames.get(0)).toString();
					
					for(Categorie cat : this.listCompetitions.get(listCompetitionNames.indexOf(this.competitionName)).getListCategories())
						if(cat.getCategorie().equals(categorieASupprimer))
							this.listCompetitions.get(listCompetitionNames.indexOf(this.competitionName)).removeCat(cat);
					
					this.notifyObserver();
				} catch(NullPointerException e) {}
			}
			else
				JOptionPane.showMessageDialog(null, "Aucune catégorie pour ce type de compétition", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch(ArrayIndexOutOfBoundsException e) {JOptionPane.showMessageDialog(null, "Aucun type de compétition sélectionné", "Erreur", JOptionPane.ERROR_MESSAGE);}
	}
	
	private void setListCategories(){
		ArrayList<Categorie> listCat = new ArrayList<Categorie>();
		Boolean found = false;
		int i = 0;
		while(i < this.listCompetitions.size() && !found){
			if(this.listCompetitions.get(i).getName().equals(this.competitionName)){
				listCat = this.listCompetitions.get(i).getListCategories();
				found = true;
			}
			i++;
		}
		Tireur.setListCategories(listCat);
		this.notifyObserver();
	}
	
	public void addCol(String title){
		Tireur.addCustomTitle(title);
		for(Tireur tireur : this.listTireurs)
			tireur.setCustomRow(Tireur.getCustomTitle().size()-1, "");
		this.notifyObserver();
	}
	
	public void supprimerColonne(String title){
		int index = Tireur.getCustomTitle().indexOf(title);
		Tireur.removeCustomTitle(title);
		for(Tireur tireur : this.listTireurs)
			tireur.getCustomRow().remove(index);
		this.notifyObserver();
	}
	
	public void setPelotonFilter(int peloton){
		this.peloton = peloton;
		this.updatePelotonList();
	}
	
	public void updatePelotonList(){
		Tireur.emptyListPelotons();
		Tireur.addPeloton(0);
		for(Tireur tireur : this.listTireurs){
			Boolean bool = true;
			for(Object peloton : Tireur.getPelotonCombo())
				if(tireur.getPeloton() == (Integer)peloton) {
					bool = false;
					break;
				}
			if(bool)
				Tireur.addPeloton(tireur.getPeloton());
		}
		this.notifyObserver();
	}
	
	private Tireur getTireurAtID(int ID){
		for(Tireur tireur : this.listTireurs)
			if(tireur.getID() == ID)
				return tireur;
		return null;
	}
	
	public void setPlanCible(){
		String[] numBlason = {"A", "C", "B", "D"};
		int numCible = 1;
		int countBlason = 0;
		
		// On trie les tireurs par ordre de peloton, distance et type de blason
		Collections.sort(this.listTireurs, new Comparator<Tireur>() {
		    public int compare(Tireur t1, Tireur t2) {
		    	if (t1.getPeloton() < t2.getPeloton())
		    		return -1;
		    	else if (t1.getPeloton() > t2.getPeloton())
		    		return 1;
		    	else if (t1.getDistance() < t2.getDistance())
		    		return -1;
		    	else if (t1.getDistance() > t2.getDistance())
		    		return 1;
		    	else if (t1.getTypeBlason().compareTo(t2.getTypeBlason()) < 0)
		    		return -1;
		    	else if (t1.getTypeBlason().compareTo(t2.getTypeBlason()) > 0)
		    		return 1;
		    	else
		    		return 0;
		    }
		});

		// On affecte les cibles et blasons selon les pelotons, distances et types de blason
		for(int i = 0 ; i < this.listTireurs.size() ; i++){
			if(i > 0){
				if(this.listTireurs.get(i-1).getPeloton() != this.listTireurs.get(i).getPeloton()) {
					numCible = 1;
					countBlason = 0;
				}
				else if(this.listTireurs.get(i-1).getDistance() != this.listTireurs.get(i).getDistance() || countBlason == this.ABCD) {
					numCible = this.listTireurs.get(i-1).getCible()+1;
					countBlason = 0;
				}
				else if(!this.listTireurs.get(i-1).getTypeBlason().equals(this.listTireurs.get(i).getTypeBlason())){
//					if(this.ABCD == 3 || countBlason > 2 || Tireur.getListTypeBlason().indexOf(this.listTireurs.get(i-1).getTypeBlason()) > 2){
//						numCible = this.listTireurs.get(i-1).getCible()+1;
//						countBlason = 0;
//					}
//					else if(countBlason <= 2 && Tireur.getListTypeBlason().indexOf(this.listTireurs.get(i-1).getTypeBlason()) <= 2)
//						countBlason = 2;
					numCible = this.listTireurs.get(i-1).getCible()+1;
					countBlason = 0;
				}
					
			}
			
			if(this.peloton == 0 || this.peloton == this.listTireurs.get(i).getPeloton()){
				this.listTireurs.get(i).setCible(numCible);
				this.listTireurs.get(i).setBlason(numBlason[countBlason]);
				countBlason++;
			}
		}
		
		// On retrie dans l'ordre des blasons
		Collections.sort(this.listTireurs, new Comparator<Tireur>() {
		    public int compare(Tireur t1, Tireur t2) {
		    	if (t1.getPeloton() < t2.getPeloton())
		    		return -1;
		    	else if (t1.getPeloton() > t2.getPeloton())
		    		return 1;
		    	else if (t1.getCible() < t2.getCible())
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
		
		this.notifyObserver();
	}
	public void setABCD(int ABCD) {this.ABCD = ABCD;}
	public int getABCD() {return this.ABCD;}
	public String getCompetitionName() {return this.competitionName;}
	
	public void setPlanBlasons(){
		Competition compet = null;
		for(Competition competTemp : this.listCompetitions)
			if(competTemp.getName().equals(this.competitionName))
				compet = competTemp;
		
		for(Tireur tireur : this.listTireurs)
			for(Categorie cat : compet.getListCategories())
				if(tireur.getCategorie().equals(cat.getCategorie())) {
					tireur.setDistance(cat.getDistance());
					tireur.setTypeBlason(cat.getTypeBlason());
				}
		
		this.notifyObserver();
	}
	
	public void sauvegarder(String nomFichier){
		this.staticTireur.setStaticVariables();
		if(this.fileName == null || !this.fileName.equals(nomFichier))
			this.setFileName(nomFichier);

		
	    ObjectOutputStream oos = null;
	    try {
		    oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("save\\"+nomFichier+".txt"))));
		    oos.writeObject(this);
	    } catch (IOException e) {e.printStackTrace();}
	    finally {
	    	try {
	    		if(oos != null) oos.close();
	    	} catch (IOException e) {e.printStackTrace();}
	    }

	    oos = null;
	    try {
	    	oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("competition\\competition.txt"))));
	    	oos.writeObject(this.listCompetitions);
	    } catch (IOException e) {e.printStackTrace();}
	    finally {
	    	try {
	    		if(oos != null) oos.close();
	    	} catch (IOException e) {e.printStackTrace();}
	    }
	}
	
	public void ouvrir(String nomFichier){
		// load the save
		ObjectInputStream ois = null;
	    try {
    		ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("save\\" + nomFichier + ".txt"))));
		    try {
		    	try{
			    	Model modelTemp = (Model)ois.readObject();
			    	for(Tireur tireur : modelTemp.getListTireurs())
			    		this.listTireurs.add(tireur);
			    	this.setStaticTireur(modelTemp.getStaticTireur());
			    	Tireur.loadStaticVariables(this.staticTireur);
			    	this.ABCD = modelTemp.getABCD();
			    	this.competitionName = modelTemp.getCompetitionName();
		    	} catch(InvalidClassException e) {JOptionPane.showMessageDialog(null, "Fichier corrompu: "+nomFichier+".txt", "Erreur", JOptionPane.ERROR_MESSAGE);}
			} catch (ClassNotFoundException e) {e.printStackTrace();}
	    } catch (FileNotFoundException e) {e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    finally {
	    	try {
	    		if(ois != null) ois.close();
	    	} catch (IOException e) {e.printStackTrace();}
	    }
	    
	    this.setPelotonFilter(0);
	    this.setListCategories();
		this.setFileName(nomFichier);
	}
	
	public void loadCompets(){
	    // load the compets data
		ObjectInputStream ois = null;
	    try {
    		ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File("competition\\competition.txt"))));
		    try {
		    	this.listCompetitions = (ArrayList<Competition>)ois.readObject();
	    	} catch (InvalidClassException e) {JOptionPane.showMessageDialog(null, "Fichier corrompu: competition.txt", "Erreur", JOptionPane.ERROR_MESSAGE);
		    } catch (ClassNotFoundException e) {e.printStackTrace();}
	    } catch (FileNotFoundException e) {e.printStackTrace();
	    } catch (IOException e) {e.printStackTrace();}
	    finally {
	    	try {
	    		if(ois != null) ois.close();
	    	} catch (IOException e) {e.printStackTrace();}
	    }
	}
	
	private void setFileName(String fileName){
		this.fileName = fileName;
		this.notifyObserver();
	}
	
	public void supprimer(String nomFichier){
		File file = new File("save\\" + nomFichier + ".txt");
		file.delete();
	}
	
	public ArrayList<Tireur> getListTireurs() {return this.listTireurs;}
	private void setStaticTireur(StaticTireur sTireur) {this.staticTireur = sTireur;}
	public StaticTireur getStaticTireur() {return this.staticTireur;}
	
	//Implémentation du pattern observer
	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	public void notifyObserver() {
		if(this.fileName == null)
			;//this.sauvegarder("autosave");  // TODO
		else
			this.sauvegarder(this.fileName);
		
		ArrayList<Tireur> listTireursTemp = new ArrayList<Tireur>();
		for(Tireur tireur : this.listTireurs){
			if(this.peloton <= 0 || tireur.getPeloton() == this.peloton)
				listTireursTemp.add(tireur);
		}
		
		String frameTitle = new String((this.fileName != null ? " - " + this.fileName : "") + (this.competitionName != null ? " - " + this.competitionName : ""));
		
		for(Observer obs : listObserver)
			obs.update(listTireursTemp, frameTitle);
	}

	public void removeObserver() {
		listObserver = new ArrayList<Observer>();
	}
	
	public void getPlanTir(String peloton){
		ExportPlanTir ept = new ExportPlanTir(this.listTireurs, peloton, this.fileName);
		try{
			ept.export();
		} catch(IOException e) {e.printStackTrace();
		} catch(InvalidFormatException e) {e.printStackTrace();}
	}
	
	public void getClassement(){
		ExportClassement ec = new ExportClassement(this.listTireurs, this.fileName);
		try{
			ec.export();
		} catch(IOException e) {e.printStackTrace();
		} catch(InvalidFormatException e) {e.printStackTrace();}
	}
	
	public void getPlanTireurs(String peloton){
		ExportPlanTireurs ept = new ExportPlanTireurs(this.listTireurs, peloton, this.fileName);
		try{
			ept.export();
		} catch(IOException e) {e.printStackTrace();
		} catch(InvalidFormatException e) {e.printStackTrace();}
	}
}
