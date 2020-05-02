package beans;

public class Lien {
	private int idLien;
	private String nomLien;
	private String contenuLien;
	private String idMail; 
	private Mail mail;
	public Lien() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getIdLien() {
		return idLien;
	}
	public void setIdLien(int idLien) {
		this.idLien = idLien;
	}
	public String getNomLien() {
		return nomLien;
	}
	public void setNomLien(String nomLien) {
		this.nomLien = nomLien;
	}
	public String getContenuLien() {
		return contenuLien;
	}
	public void setContenuLien(String contenuLien) {
		this.contenuLien = contenuLien;
	}
	public Mail getMail() {
		return mail;
	}
	public void setMail(Mail mail) {
		this.mail = mail;
	}
	public String getIdMail() {
		return idMail;
	}
	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}
	@Override
	public String toString() {
		return "Lien [idLien=" + idLien + ", nomLien=" + nomLien + ", contenuLien=" + contenuLien + ", idMail=" + idMail
				+ ", mail=" + mail + "]";
	}
	
}
