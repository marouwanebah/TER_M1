package dao;

import java.sql.Connection;

import beans.ListeDiffusion;

public interface ListeDiffusionDao {
	public void ajouterListe(ListeDiffusion liste, Connection connexion);
	public ListeDiffusion getListe(String emailListe, Connection connexion);
}
