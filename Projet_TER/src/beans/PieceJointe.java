package beans;

public class PieceJointe {
	private String numPieceJointe;
	private String nomPieceJointe;
	private String contenuJointe;
	private Mail mail;  // j'ai transfromé l'atribut Mail en juste l'ID du mail
	private TypePieceJointe typePieceJointe;
	public PieceJointe() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNumPieceJointe() {
		return numPieceJointe;
	}
	public void setNumPieceJointe(String numPieceJointe) {
		this.numPieceJointe = numPieceJointe;
	}
	public String getNomPieceJointe() {
		return nomPieceJointe;
	}
	public void setNomPieceJointe(String nomPieceJointe) {
		this.nomPieceJointe = nomPieceJointe;
	}
	public String getContenuJointe() {
		return contenuJointe;
	}
	public void setContenuJointe(String contenuJointe) {
		this.contenuJointe = contenuJointe;
	}
	public Mail getMail() {
		return mail;
	}
	public void setMail(Mail mail) {
		this.mail = mail;
	}
	
	public TypePieceJointe getTypePieceJointe() {
		return typePieceJointe;
	}
	public void setTypePieceJointe(TypePieceJointe typePieceJointe) {
		this.typePieceJointe = typePieceJointe;
	}
	
	
	
	
}
