package beans;

import java.util.Date;

public class PersonneFonction {
	private Personne personne;
	private Fonction fonction;
	private Date dateDebutFonction;
	private Date dateFinFonction;
	public PersonneFonction() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Personne getPersonne() {
		return personne;
	}
	public void setPersonne(Personne personne) {
		this.personne = personne;
	}
	public Fonction getFonction() {
		return fonction;
	}
	public void setFonction(Fonction fonction) {
		this.fonction = fonction;
	}
	public Date getDateDebutFonction() {
		return dateDebutFonction;
	}
	public void setDateDebutFonction(Date dateDebutFonction) {
		this.dateDebutFonction = dateDebutFonction;
	}
	public Date getDateFinFonction() {
		return dateFinFonction;
	}
	public void setDateFinFonction(Date dateFinFonction) {
		this.dateFinFonction = dateFinFonction;
	}
	
	
}
