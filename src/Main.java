import controler.Controler;
import model.Model;
import vue.Vue;

public class Main
{
	public static void main(String[] args){
		Model model = new Model();
		model.loadCompets();
		Controler controler = new Controler(model);
		Vue vue = new Vue(controler);
		model.addObserver(vue);
		vue.setVisible(true);
	}
}

// TODO problème de gestion des cibles mixtes dans l'assignation des cibles, taille des blasons informatiquement pas gérée
// TODO empêcher les cibles avec deux tireurs de même club

// TODO sortir les feuilles de marques sur excel

// TODO inter-équipe