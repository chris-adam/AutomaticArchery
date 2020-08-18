package data;

import java.io.Serializable;
import java.util.ArrayList;

public class Competition implements Serializable
{
	private ArrayList<Categorie> listCategories = new ArrayList<Categorie>();
	private String name = new String();

	public Competition(String name){
		this.name = name;
	}
	
	public String getName() {return this.name;}
	public void setName(String name) {this.name = name;}
	public ArrayList<Categorie> getListCategories() {return this.listCategories;}
	
	public void addCategorie(Categorie cat) {this.listCategories.add(cat);}
	public void clearCats() {this.listCategories.clear();}
	public void removeCat(Categorie cat) {this.listCategories.remove(cat);}
}
