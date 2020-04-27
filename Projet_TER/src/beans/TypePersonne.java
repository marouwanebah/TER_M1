package beans;

public class TypePersonne {
	private String codePersonne;
	private String libellePersonne;
	public TypePersonne() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TypePersonne(String codePersonne, String libellePersonne) {
		super();
		this.codePersonne = codePersonne;
		this.libellePersonne = libellePersonne;
	}
	public String getCodePersonne() {
		return codePersonne;
	}
	public void setCodePersonne(String codePersonne) {
		this.codePersonne = codePersonne;
	}
	public String getLibellePersonne() {
		return libellePersonne;
	}
	public void setLibellePersonne(String libellePersonne) {
		this.libellePersonne = libellePersonne;
	}
	

}
