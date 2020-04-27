package dao;

import beans.Personne;

public interface PersonneDAO {
	public void ajouterPersonne(Personne personne);
	public Personne getPersonne(String email);
}
