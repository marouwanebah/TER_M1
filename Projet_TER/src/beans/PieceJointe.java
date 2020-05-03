package beans;

import java.io.FileInputStream;

public class PieceJointe {
	private String numPieceJointe;
	private String nomPieceJointe;
	private FileInputStream contenuJointe;
	private Mail mail;  //? 
	private String MailId;
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
	
	public FileInputStream getContenuJointe() {
		return contenuJointe;
	}
	public void setContenuJointe(FileInputStream contenuJointe) {
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
	public String getMailId() {
		return MailId;
	}
	public void setMailId(String mailId) {
		MailId = mailId;
	}
	@Override
	public String toString() {
		return "PieceJointe [numPieceJointe=" + numPieceJointe + ", nomPieceJointe=" + nomPieceJointe
				+ ", contenuJointe=" + "" + ", mail=" + mail + ", MailId=" + MailId + ", typePieceJointe="
				+ typePieceJointe + "]";
	}
	
	
	
	
}
