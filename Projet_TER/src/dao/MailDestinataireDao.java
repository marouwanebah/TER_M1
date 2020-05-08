package dao;

import java.sql.Connection;

import beans.Email;
import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;

public interface MailDestinataireDao {
	public void ajouterDestinataire(MailDestinataire mailDestinataire, Connection connexion);
	public MailDestinataire getMailDestinataire(Mail mail, Email email, Connection connexion);
}
