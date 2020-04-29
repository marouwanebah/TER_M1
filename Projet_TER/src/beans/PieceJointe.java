package beans;

public class PieceJointe {
	private String numPieceJointe;
	private String nomPieceJointe;
	private String contenuJointe;
	private String mailID;  // j'ai transfromé l'atribut Mail en juste l'ID du mail 
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
	/*
	public Mail getMail() {
		return mail;
	}
	public void setMail(Mail mail) {
		this.mail = mail;
	}
	*/
	public String getMailID() {
		return mailID;
	}
	public void setMailID(String mailID) {
		this.mailID = mailID;
	}
	
	
	
}
