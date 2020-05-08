package dao;

import java.sql.Connection;

import beans.Mail;

public interface MailDao {
	public void ajouterMail(Mail mail, Connection connexion);
	public Mail getMail(String idMail, Connection connexion);
}
