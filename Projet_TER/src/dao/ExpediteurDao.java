package dao;

import beans.Email;
import beans.Expediteur;
import beans.Mail;

public interface ExpediteurDao {
	public void ajouterExpediteur(Expediteur expediteur);
	public Expediteur getExpediteur(Mail mail, Email email);
}
