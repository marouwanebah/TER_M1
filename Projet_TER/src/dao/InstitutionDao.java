package dao;

import beans.Institution;

public interface InstitutionDao {
	public void ajouterInstitution(Institution institution);
	public Institution getInstitution(String nomInstitution);
}
