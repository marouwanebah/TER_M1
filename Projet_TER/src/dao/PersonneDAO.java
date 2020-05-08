package dao;

import java.sql.Connection;

import beans.Personne;

public interface PersonneDAO {
	public void ajouterPersonne(Personne personne, Connection connexion);
	public Personne getPersonne(String email, Connection connexion);
}
