package dao;

import java.sql.Connection;

import beans.Email;
import beans.Expediteur;
import beans.Mail;

public interface ExpediteurDao {
	public void ajouterExpediteur(Expediteur expediteur, Connection connexion);
	public Expediteur getExpediteur(Mail mail, Email email, Connection connexion);
}
