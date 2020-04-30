package dao;

import beans.Ville;

public interface VilleDao {
	public void ajouterVille(Ville ville);
	public Ville getVille(int codePostal);
}
