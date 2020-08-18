package observer;

import java.util.ArrayList;

import data.Tireur;

public interface Observer
{
	public void update(ArrayList<Tireur> listTireurs, String frameTitle);
}
