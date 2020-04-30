package beans;

public class Personne {
	private int idPersonne;
	private String emailPersonne;
	private String nomPersonne;
	private String prenomPersonne;
	private Institution institutionPersonne;
	public Personne() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Personne(String emailPersonne, String nomPersonne, String prenomPersonne) {
		super();
		this.emailPersonne = emailPersonne;
		this.nomPersonne = nomPersonne;
		this.prenomPersonne = prenomPersonne;
	}

	
	public int getIdPersonne() {
		return idPersonne;
	}
	public void setIdPersonne(int idPersonne) {
		this.idPersonne = idPersonne;
	}
	public String getEmailPersonne() {
		return emailPersonne;
	}
	public void setEmailPersonne(String emailPersonne) {
		this.emailPersonne = emailPersonne;
	}
	public String getNomPersonne() {
		return nomPersonne;
	}
	public void setNomPersonne(String nomPersonne) {
		this.nomPersonne = nomPersonne;
	}
	public String getPrenomPersonne() {
		return prenomPersonne;
	}
	public void setPrenomPersonne(String prenomPersonne) {
		this.prenomPersonne = prenomPersonne;
	}
	public Institution getInstitutionPersonne() {
		return institutionPersonne;
	}
	public void setInstitutionPersonne(Institution institutionPersonne) {
		this.institutionPersonne = institutionPersonne;
	}
	
	
}
