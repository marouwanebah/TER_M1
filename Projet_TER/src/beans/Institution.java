package beans;

public class Institution {
	private String nomInstitution;
	private Ville ville;
	public Institution() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNomInstitution() {
		return nomInstitution;
	}
	public void setNomInstitution(String nomInstitution) {
		this.nomInstitution = nomInstitution;
	}
	public Ville getVille() {
		return ville;
	}
	public void setVille(Ville ville) {
		this.ville = ville;
	}
	@Override
	public String toString() {
		return "Institution [nomInstitution=" + nomInstitution + "]";
	}
	
	
	
}
