package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Tireur implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String prenom = "";
	private String nom = "";
	private String club = "";
	private String categorie = "";
	private String matricule = "";
	private String typeBlason = "";
	private String statut = "---";
	private String blason = "";
	public static final String[] listStatuts = {"---", "Absent", "Disqualifié", "Abandon", "Autre"};
	private int ID = 0;
	private int distance = 0;
	private int serie1points = 0;
	private int serie1a = 0;
	private int serie1b = 0;
	private int serie2points = 0;
	private int serie2a = 0;
	private int serie2b = 0;
	private int peloton = 0;
	private int cible = 0;

	public static final String[] title = {"Nom", "Prénom", "Club", "Matricule", "Catégorie", "Peloton", "Distance", "Cible", "N° blason", "Type de blason", "Statut", 
						"Série 1", "S1 X/10", "S1 10/9", "Série 2", "S2 X/10", "S2 10/9", "Total", "Tot X/10", "Tot 10/9"};
	private static ArrayList<String> customTitle = new ArrayList<String>();
	private ArrayList<String> customRow = new ArrayList<String>();
	
	private static int nbrInstances = 0;
	
	private static ArrayList<Integer> listPelotons = new ArrayList<Integer>();
	private static ArrayList<Categorie> listCategories = new ArrayList<Categorie>();
	//public static final ArrayList<String> listTypeBlason = new ArrayList<String>(Arrays.asList("Trispot", "40 cm", "60 cm", "80 cm réduit", "80 cm", "122 cm"));
	
	public Tireur(String nom, String prenom, String club, String categorie, String matricule, int peloton){
		this.nom = nom;
		this.prenom = prenom;
		this.club = club;
		this.categorie = categorie;
		this.matricule = matricule;
		this.peloton = peloton;
		this.setID(++Tireur.nbrInstances);
		this.setTypeBlasonCat();
		this.setDistanceCat();
		for(int i = 0 ; i < Tireur.customTitle.size() ; i++)
			this.customRow.add("");
	}

	public void setPrenom(String prenom) {this.prenom = prenom;}
	public String getNom() {return this.nom;}
	public void setNom(String nom) {this.nom = nom;}
	public String getPrenom() {return this.prenom;}
	public void setClub(String club) {this.club = club;}
	public String getClub() {return this.club;}
	public void setCategorie(String categorie) {this.categorie = categorie;}
	public String getCategorie() {return this.categorie;}
	public void setMatricule(String matricule) {this.matricule = matricule;}
	public String getMatricule() {return this.matricule;}
	public void setTypeBlason(String typeBlason) {this.typeBlason = typeBlason;}
	public String getTypeBlason() {return this.typeBlason;}
	public void setStatut(String statut) {this.statut = statut;}
	public String getStatut() {return this.statut;}
	private void setID(int ID) {this.ID = ID;}
	public int getID() {return this.ID;}
	public void setDistance(int distance) {this.distance = distance;}
	public int getDistance() {return this.distance;}
	public void setSerie1points(int serie1points) {this.serie1points = serie1points;}
	public int getSerie1points() {return this.serie1points;}
	public void setSerie1a(int serie1a) {this.serie1a = serie1a;}
	public int getSerie1a() {return this.serie1a;}
	public void setSerie1b(int serie1b) {this.serie1b = serie1b;}
	public int getSerie1b() {return this.serie1b;}
	public void setSerie2points(int serie2points) {this.serie2points = serie2points;}
	public int getSerie2points() {return this.serie2points;}
	public void setSerie2a(int serie2a) {this.serie2a = serie2a;}
	public int getSerie2a() {return this.serie2a;}
	public void setSerie2b(int serie2b) {this.serie2b = serie2b;}
	public int getSerie2b() {return this.serie2b;}
	public void setPeloton(int peloton) {this.peloton = peloton;}
	public int getPeloton() {return this.peloton;}
	public void setCible(int cible) {this.cible = cible;}
	public int getCible() {return this.cible;}
	public void setBlason(String blason) {this.blason = blason;}
	public String getBlason() {return this.blason;}
	public int getSerieTotal() {return this.serie1points+this.serie2points;}
	public int getSerieaTotal() {return this.serie1a+this.serie2a;}
	public int getSeriebTotal() {return this.serie1b+this.serie2b;}
	public static void setListCategories(ArrayList<Categorie> listCat) {Tireur.listCategories = listCat;}
	public static Object[] getCatCombo(){
		ArrayList<String> listCatNames = new ArrayList<String>();
		for(Categorie cat : Tireur.listCategories)
			listCatNames.add(cat.getCategorie());
		return listCatNames.toArray();
	}
	
	public static Object[] getDistCombo(){
		ArrayList<Integer> listDist = new ArrayList<Integer>();
		for(Categorie cat : Tireur.listCategories)
			if(!listDist.contains(cat.getDistance()))
				listDist.add(cat.getDistance());
		
		return listDist.toArray();
	}
	
	public static ArrayList<String> getListTypeBlason(){
		ArrayList<String> listTypeBlason = new ArrayList<String>();
		for(Categorie cat : Tireur.listCategories)
			if(!listTypeBlason.contains(cat.getTypeBlason()))
				listTypeBlason.add(cat.getTypeBlason());
		Collections.sort(listTypeBlason);
		return listTypeBlason;
	}

	public static ArrayList<String> getCustomTitle() {return (ArrayList<String>)Tireur.customTitle.clone();}
	public static void addCustomTitle(String title) {Tireur.customTitle.add(title);}
	public static void removeCustomTitle(String title) {Tireur.customTitle.remove(title);}
	
	public ArrayList<String> getCustomRow() {return this.customRow;}
	public void setCustomRow(int index, String elem) {
		if(this.customRow.size() <= index)
			this.customRow.add(elem);
		else
			this.customRow.set(index, elem);
	}
	
	public static void emptyListPelotons() {Tireur.listPelotons = new ArrayList<Integer>();}
	public static void addPeloton(int peloton) {Tireur.listPelotons.add(peloton);}
	public static Object[] getPelotonCombo() {
		Collections.sort(Tireur.listPelotons);
		return Tireur.listPelotons.toArray();
	}

	private void setTypeBlasonCat() {
		for(Categorie cat : Tireur.listCategories)
			if(this.categorie == cat.getCategorie())
				this.typeBlason = cat.getTypeBlason();
	}
	
	private void setDistanceCat() {
		for(Categorie cat : Tireur.listCategories)
			if(this.categorie == cat.getCategorie())
				this.distance = cat.getDistance();
	}
	
	public static void loadStaticVariables(StaticTireur sTireur){
		Tireur.customTitle = sTireur.getCustomTitle();
		Tireur.listCategories = sTireur.getListCategories();
		Tireur.listPelotons = sTireur.getListPelotons();
		Tireur.nbrInstances = sTireur.getNbrInstances();
	}
	static ArrayList<Categorie> getListCategories() {return Tireur.listCategories;}
	static ArrayList<Integer> getListPelotons() {return Tireur.listPelotons;}
	static int getNbrInstances() {return Tireur.nbrInstances;}
}
