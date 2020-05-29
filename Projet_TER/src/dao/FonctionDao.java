package dao;

import java.sql.Connection;

import beans.Fonction;

public interface FonctionDao {
	public void ajouterFonction(Fonction fonction, Connection connexion);
	public Fonction getFonction(String email, Connection connexion);
}
