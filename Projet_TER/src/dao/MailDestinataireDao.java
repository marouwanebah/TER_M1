package dao;

import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;

public interface MailDestinataireDao {
	public void ajouterDestinataire(MailDestinataire mailDestinataire);
	public MailDestinataire getMailDestinataire(Mail mail, Personne personne);
}
