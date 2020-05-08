package dao;

import java.sql.Connection;

import beans.Institution;

public interface InstitutionDao {
	public void ajouterInstitution(Institution institution, Connection connexion);
	public Institution getInstitution(String nomInstitution, Connection connexion);
}
