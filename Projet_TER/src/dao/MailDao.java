package dao;

import beans.Mail;

public interface MailDao {
	public void ajouterMail(Mail mail);
	public Mail getMail(String idMail);
}
