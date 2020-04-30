package dao;

import beans.Pays;

public interface PaysDao {
	public void ajouterPays(Pays pays);
	public Pays getPays(String nom);
}
