package beans;

public class Personne {
	private String emailPersonne;
	private String nomPersonne;
	private String prenomPersonne;
	private String rolePersonne;

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
	public String getRolePersonne() {
		return rolePersonne;
	}
	public void setRolePersonne(String rolePersonne) {
		this.rolePersonne = rolePersonne;
	}

	
}
