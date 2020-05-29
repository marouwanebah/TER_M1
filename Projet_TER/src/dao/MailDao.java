package dao;

import java.sql.Connection;
import java.util.List;

import beans.Mail;

public interface MailDao {
	public void ajouterMail(Mail mail, Connection connexion);
	public Mail getMail(String idMail, Connection connexion);
	public List<Mail> listerMails(Connection connexion);
	public void modifierMail(Mail mail, Connection connexion);
}
