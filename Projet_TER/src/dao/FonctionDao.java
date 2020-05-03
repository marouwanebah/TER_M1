package dao;

import beans.Fonction;

public interface FonctionDao {
	public void ajouterFonction(Fonction fonction);
	public Fonction getFonction(String email);
}
