package beans;

public class Institution {
	private int idInstitution;
	private String nomInstitution;
	private Ville ville;
	public Institution() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getIdInstitution() {
		return idInstitution;
	}
	public void setIdInstitution(int idInstitution) {
		this.idInstitution = idInstitution;
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
