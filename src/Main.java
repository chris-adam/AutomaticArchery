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

// TODO probl�me de gestion des cibles mixtes dans l'assignation des cibles, taille des blasons informatiquement pas g�r�e
// TODO emp�cher les cibles avec deux tireurs de m�me club

// TODO sortir les feuilles de marques sur excel

// TODO inter-�quipe