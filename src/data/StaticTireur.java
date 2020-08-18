package data;

import java.io.Serializable;
import java.util.ArrayList;

public class StaticTireur implements Serializable
{
	private ArrayList<String> customTitle = new ArrayList<String>();
	private ArrayList<Categorie> listCategories = new ArrayList<Categorie>();
	private ArrayList<Integer> listPelotons = new ArrayList<Integer>();
	private int nbrInstances = 0;
	
	public StaticTireur() {}

	ArrayList<String> getCustomTitle() {return this.customTitle;}
	ArrayList<Categorie> getListCategories() {
		return this.listCategories;
	}
	ArrayList<Integer> getListPelotons() {return this.listPelotons;}
	int getNbrInstances() {return this.nbrInstances;}
	
	public void setStaticVariables(){
		this.customTitle = Tireur.getCustomTitle();
		this.listCategories = Tireur.getListCategories();
		this.listPelotons = Tireur.getListPelotons();
		this.nbrInstances = Tireur.getNbrInstances();
	}
}
