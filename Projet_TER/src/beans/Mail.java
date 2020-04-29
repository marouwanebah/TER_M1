package beans;


public class Mail {
	private String idMail;
	private Personne expediteurPrincipal;
	private String sujetMail;
	private String dateEnvoiMail;
	private String contenuMail; 
	private Mail mailPère;
	public Mail() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getIdMail() {
		return idMail;
	}
	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}
	public String getDateEnvoiMail() {
		return dateEnvoiMail;
	}
	public void setDateEnvoiMail(String dateEnvoiMail) {
		this.dateEnvoiMail = dateEnvoiMail;
	}
	public String getContenuMail() {
		return contenuMail;
	}
	public void setContenuMail(String contenuMail) {
		this.contenuMail = contenuMail;
	}
	public String getSujetMail() {
		return sujetMail;
	}
	public void setSujetMail(String sujetMail) {
		this.sujetMail = sujetMail;
	}
	public Personne getExpediteur() {
		return expediteurPrincipal;
	}
	public void setExpediteur(Personne expediteur) {
		this.expediteurPrincipal = expediteur;
	}
	public Mail getMailPère() {
		return mailPère;
	}
	public void setMailPère(Mail mailPère) {
		this.mailPère = mailPère;
	}
	
}
