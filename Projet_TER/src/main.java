
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;

import beans.Email;
import beans.Expediteur;
import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;
import dao.EmailDao;
import dao.ExpediteurDao;
import dao.MailDao;
import dao.MailDestinataireDao;
import dao.PersonneDAO;

public class main {

	//private static final String LIEN_FICHIER = "/home/diallo/Documents/projetTER/corpus/president_2010/president_2010/president_2010-06/";  
	private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/president_2010/president_2010/president_2010-07/";  

	
	public static void main(String[] args) throws MessagingException, IOException {
		EmailDao emailDao;
		PersonneDAO personneDao;
		MailDao mailDao;
		ExpediteurDao expediteurDao;
		MailDestinataireDao malDestinataireDao;
		dao.DaoFactory daoFactory = dao.DaoFactory.getInstance();
		emailDao = daoFactory.getEmailDao();
		personneDao = daoFactory.getPersonneDao();
		mailDao = daoFactory.getMailDao();
		expediteurDao = daoFactory.getExpediteurDao();
		malDestinataireDao = daoFactory.getMailDestinataireDao();
		
		
		parseur test = new parseur(LIEN_FICHIER+20);
		test.getMailTest();
		test.getLiens();

		
		//MailList tes = test.mailToObject(); 
		//System.out.println(tes.toString());
		//System.out.println(tes.getBody());
		//System.out.println(tes.getFrom().getNomPersonne());
		//System.out.println(tes.getFrom().getPrenomPersonne());
		//System.out.println("attachement"+tes.getAttachments().get(0).getContenuJointe());
		//tous est gerer sauf les liens 
		//MailList a;
		//System.out.println(a.getFrom().getEmailPersonne());
		
		
		//System.out.println(a.toString());
		/*
		ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		for(int i = 1; i<32; ++i) {
			test = new parseur(LIEN_FICHIER+i);
			a= test.mailToObject();
			//System.out.println(a.getFrom().getEmailPersonne());
			listeMail.add(a); 
		}
		
		for(MailList aa : listeMail) {
			System.out.println(aa.getFrom().getNomPersonne() +" "+ aa.getFrom().getPrenomPersonne()+ " " +aa.getFrom().getEmailPersonne());
			//insert email
			Email email = new Email();
			email.setEmail(aa.getFrom().getEmailPersonne());
			email.setSignature(aa.getSignature());
			if(emailDao.getEmail(email.getEmail()) == null) {
				emailDao.ajouterEmail(email);
			}
			//insert personne
			if(aa.getFrom().getNomPersonne() != null && aa.getFrom().getPrenomPersonne() != null) {
				if(personneDao.getPersonne(email.getEmail()) == null) {
					personneDao.ajouterPersonne(aa.getFrom());
				}
			}
			//insert Mail
			Mail mail = new Mail();
			Mail mailPere = new Mail();
			mail.setIdMail(aa.getIdMail());
			mail.setContenuMail(aa.getBody());
			mail.setSujetMail(aa.getSujet());
			mail.setDateEnvoiMail(aa.getDate());
			mail.setExpediteur(email);
			mail.setMailPere(mailPere);
			if(mailDao.getMail(mail.getIdMail()) == null)
				mailDao.ajouterMail(mail);
			//insert Expediteur principal
			if(expediteurDao.getExpediteur(mail, email) == null) {
				Expediteur expediteur = new Expediteur();
				expediteur.setMail(mail);
				expediteur.setEmail(email);
				expediteurDao.ajouterExpediteur(expediteur);
			}
			//insert Expediteur cc
			if(aa.getDestinataireEnCopie() != null) {
				for(Personne pers : aa.getDestinataireEnCopie()) {
					Email emailcc = new Email();
					emailcc.setEmail(pers.getEmailPersonne());
					//insert email
					if(emailDao.getEmail(emailcc.getEmail()) == null) {
						emailDao.ajouterEmail(emailcc);
					}
					//insert personne
					if(pers.getNomPersonne() != null && pers.getPrenomPersonne() != null) {
						if(personneDao.getPersonne(emailcc.getEmail()) == null) {
							personneDao.ajouterPersonne(pers);
						}
					}
					if(expediteurDao.getExpediteur(mail, emailcc) == null) {
						Expediteur expediteur = new Expediteur();
						expediteur.setMail(mail);
						expediteur.setEmail(emailcc);
						expediteurDao.ajouterExpediteur(expediteur);
					}
				}
			}
			//insert Destinataire
			if(aa.getDestinataire() != null) {
			MailDestinataire mailDestinataire = new MailDestinataire();
			mailDestinataire.setMail(mail);
			for(Personne person : aa.getDestinataire()) {
				Email emaildest = new Email();
				emaildest.setEmail(person.getEmailPersonne());
				//insert email
				if(emailDao.getEmail(emaildest.getEmail()) == null) {
					emailDao.ajouterEmail(emaildest);
				}
				//insert personne
				if(person.getNomPersonne() != null && person.getPrenomPersonne() != null) {
					if(personneDao.getPersonne(emaildest.getEmail()) == null) {
						personneDao.ajouterPersonne(person);
					}
				}
				//insert dest
				if(malDestinataireDao.getMailDestinataire(mail, emaildest) == null) {
					MailDestinataire destinataire = new MailDestinataire();
					destinataire.setMail(mail);
					destinataire.setEmail(emaildest);
					malDestinataireDao.ajouterDestinataire(destinataire);
				}
			}
			}
		}
		
		/*
					System.out.println("--------------"+"message numero : "+(i)+"-------------------");
			System.out.println(a.toString());
			System.out.println("-------------"+" fin message numero : "+(i)+"--------------");
			++i;
		
		
		
		for(MailList a : listeMail) {

			//insert Personne
			TypePersonne typePersonne = new TypePersonne();
			typePersonne.setCodePersonne("PH");
			typePersonne.setLibellePersonne("Physique");
			Personne personne= new Personne();
			personne.setEmailPersonne(a.getFrom());
			personne.setNomPersonne("test");
			personne.setPrenomPersonne("test");
			personne.setRolePersonne("test");
			personne.setTypePersonne(typePersonne);
			if(personneDao.getPersonne(personne.getEmailPersonne())== null )
				personneDao.ajouterPersonne(personne);
			//insert Mail
			Mail mail = new Mail();
			Mail mailPere = new Mail();
			mail.setIdMail(a.getIdMail());
			mail.setContenuMail(a.getBody());
			mail.setSujetMail(a.getSujet());
			mail.setDateEnvoiMail(a.getDate());
			mail.setExpediteur(personne);
			mail.setMailPère(mailPere);
			if(mailDao.getMail(mail.getIdMail()) == null)
				mailDao.ajouterMail(mail);
			//insert Destinataire
			MailDestinataire mailDestinataire = new MailDestinataire();
			mailDestinataire.setMail(mail);
			for(String dest : a.getDestinataire()) {
				personne = personneDao.getPersonne(dest);
				if(personne == null ) {
					Personne destinat= new Personne();
					destinat.setEmailPersonne(dest);
					destinat.setNomPersonne("test");
					destinat.setPrenomPersonne("test");
					destinat.setRolePersonne("test");
					destinat.setTypePersonne(typePersonne);
					personneDao.ajouterPersonne(destinat);
					personne = destinat;
				}
				mailDestinataire.setPersonne(personne);
				if(malDestinataireDao.getMailDestinataire(mail, personne) == null)
					malDestinataireDao.ajouterDestinataire(mailDestinataire);
			}

		}
		
	*/
	}

	

}
