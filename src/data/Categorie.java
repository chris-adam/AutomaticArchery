package data;

import java.io.Serializable;

public class Categorie implements Serializable
{
	private String categorie;
	private String typeBlason;
	private int distance;
	
	public Categorie(String categorie, String typeBlason, int distance){
		this.categorie = categorie;
		this.typeBlason = typeBlason;
		this.distance = distance;
	}

	public String getCategorie() {return this.categorie;}
	public String getTypeBlason() {return this.typeBlason;}
	public int getDistance() {return this.distance;}
	
	public String toString(){
		return this.categorie + ", " + this.typeBlason + ", " + String.valueOf(this.distance);
	}
}
