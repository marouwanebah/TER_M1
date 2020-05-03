
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;

import beans.Email;
import beans.Expediteur;
import beans.Fonction;
import beans.Institution;
import beans.Lien;
import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;
import beans.PieceJointe;
import dao.EmailDao;
import dao.ExpediteurDao;
import dao.FonctionDao;
import dao.InstitutionDao;
import dao.LienDao;
import dao.MailDao;
import dao.MailDestinataireDao;
import dao.PersonneDAO;
import dao.PieceJointeDao;

public class main {

	private static final String LIEN_FICHIER = "/home/diallo/Documents/projetTER/corpus/president_2010/president_2010/president_2010-06/";  
	//private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/president_2010/president_2010/president_2010-07/";  

	
	public static void main(String[] args) throws MessagingException, IOException {
		EmailDao emailDao;
		InstitutionDao institutionDao;
		PersonneDAO personneDao;
		FonctionDao fonctionDao;
		MailDao mailDao;
		PieceJointeDao pieceJointeDao;
		LienDao lienDao;
		ExpediteurDao expediteurDao;
		MailDestinataireDao malDestinataireDao;
		dao.DaoFactory daoFactory = dao.DaoFactory.getInstance();
		emailDao = daoFactory.getEmailDao();
		institutionDao = daoFactory.getInstitutionDao();
		personneDao = daoFactory.getPersonneDao();
		fonctionDao = daoFactory.getFonctionDao();
		mailDao = daoFactory.getMailDao();
		pieceJointeDao = daoFactory.getPieceJointeDao();
		lienDao = daoFactory.getLienDao();
		expediteurDao = daoFactory.getExpediteurDao();
		malDestinataireDao = daoFactory.getMailDestinataireDao();
		
		parseur test;
		/*test.getMailTest();
		test.getLiens();*/

		
		//MailList tes = test.mailToObject(); 
		//System.out.println(tes.toString());
		//System.out.println(tes.getBody());
		//System.out.println(tes.getFrom().getNomPersonne());
		//System.out.println(tes.getFrom().getPrenomPersonne());
		//System.out.println("attachement"+tes.getAttachments().get(0).getContenuJointe());
		//tous est gerer sauf les liens 
		MailList a;
		//System.out.println(a.getFrom().getEmailPersonne());
		
		
		//System.out.println(a.toString());
		
		ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		for(int i = 1; i<32; ++i) {
			test = new parseur(LIEN_FICHIER+i);
			a= test.mailToObject();
			//System.out.println(a.getFrom().getEmailPersonne());
			listeMail.add(a); 
		}
		
		for(MailList aa : listeMail) {
			//System.out.println(aa.getFrom().getNomPersonne() +" "+ aa.getFrom().getPrenomPersonne()+ " " +aa.getFrom().getEmailPersonne());
			System.out.println(aa.getFrom().getEmailPersonne());
			//insert email principal
			Email email = new Email();
			email.setEmail(aa.getFrom().getEmailPersonne());
			email.setSignature(aa.getSignature());
			if(emailDao.getEmail(email.getEmail()) == null) {
				emailDao.ajouterEmail(email);
			}
			//insert institution expediteur principal
			String institutionExpPrinc;
			if(aa.getFrom().getInstitutionPersonne() != null) {
				institutionExpPrinc = aa.getFrom().getInstitutionPersonne().getNomInstitution();
				Institution institutionPrinc = new Institution();
				institutionPrinc.setNomInstitution(institutionExpPrinc);
				if(institutionDao.getInstitution(institutionExpPrinc) == null)
					institutionDao.ajouterInstitution(institutionPrinc);
			}
			//insert personne principale
			if(aa.getFrom().getNomPersonne() != "" && aa.getFrom().getPrenomPersonne() != "") {
				if(personneDao.getPersonne(email.getEmail()) == null) {
					personneDao.ajouterPersonne(aa.getFrom());
				}
			}
			//insert fonction principale
			else {
				String vemail = email.getEmail(); 
				String code = vemail.substring(0, 4);
				String[] tabLibelle = vemail.split("@");
				String libelle = tabLibelle[0];
				Fonction fonction = new Fonction();
				fonction.setCodeFonction(code);
				fonction.setLibelleFonction(libelle);
				fonction.setEmail(email);
				if(fonctionDao.getFonction(vemail) == null)
				fonctionDao.ajouterFonction(fonction);
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
			//insert piece jointe
			for(PieceJointe pieceJointe : aa.getAttachments()) {
				pieceJointeDao.ajouterPieceJointe(pieceJointe);
			}
			//insert lien
			for(Lien lien : aa.getLiens()) {
				lienDao.ajouterLien(lien);
			}
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
					//insert institution expediteur cc
					String institutionExpcc;
					if(pers.getInstitutionPersonne() != null) {
						institutionExpcc = pers.getInstitutionPersonne().getNomInstitution();
						Institution institutioncc = new Institution();
						institutioncc.setNomInstitution(institutionExpcc);
						if(institutionDao.getInstitution(institutionExpcc) == null)
							institutionDao.ajouterInstitution(institutioncc);
					}
					//insert personne
					if(pers.getNomPersonne() != "" && pers.getPrenomPersonne() != "") {
						if(personneDao.getPersonne(emailcc.getEmail()) == null) {
							personneDao.ajouterPersonne(pers);
						}
					}
					//insert fonction cc
					else {
						String vemail = emailcc.getEmail(); 
						String code = vemail.substring(0, 4);
						String[] tabLibelle = vemail.split("@");
						String libelle = tabLibelle[0];
						Fonction fonction = new Fonction();
						fonction.setCodeFonction(code);
						fonction.setLibelleFonction(libelle);
						fonction.setEmail(emailcc);
						if(fonctionDao.getFonction(vemail) == null)
						fonctionDao.ajouterFonction(fonction);
					}
					//insert exp cc
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
				//insert institution destinataire
				String institutionvDest;
				if(person.getInstitutionPersonne() != null) {
					institutionvDest = person.getInstitutionPersonne().getNomInstitution();
					Institution institutionDest = new Institution();
					institutionDest.setNomInstitution(institutionvDest);
					if(institutionDao.getInstitution(institutionvDest) == null)
						institutionDao.ajouterInstitution(institutionDest);
				}
				//insert personne
				if(person.getNomPersonne() != "" && person.getPrenomPersonne() != "") {
					if(personneDao.getPersonne(emaildest.getEmail()) == null) {
						personneDao.ajouterPersonne(person);
					}
				}
				//insert fonction dest
				else {
					String vemail = emaildest.getEmail(); 
					String code = vemail.substring(0, 4);
					String[] tabLibelle = vemail.split("@");
					String libelle = tabLibelle[0];
					Fonction fonction = new Fonction();
					fonction.setCodeFonction(code);
					fonction.setLibelleFonction(libelle);
					fonction.setEmail(emaildest);
					if(fonctionDao.getFonction(vemail) == null)
						fonctionDao.ajouterFonction(fonction);
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
