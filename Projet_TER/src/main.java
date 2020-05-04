
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
	private static final dao.DaoFactory daoFactory = dao.DaoFactory.getInstance();
	//private static final String LIEN_FICHIER = "/home/diallo/Documents/projetTER/corpus/president_2010/president_2010/president_2010-06/";  
	//private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/president_2010/president_2010/president_2010-07/";  
	private static final String DOSSIER_PRINCIPAL = "/home/etudiant/M1/S2/TER/president_2010/president_2010/"; 
	private static final String DOSSIER_1 = "president_2010-06/"; 
	private static final String DOSSIER_2 = "president_2010-07/"; 
	private static final String DOSSIER_3 = "president_2010-08/"; 
	private static final String DOSSIER_4 = "president_2010-09/"; 
	private static final String DOSSIER_5 = "president_2010-10/"; 
	private static final String DOSSIER_6 = "president_2010-11/"; 
	private static final String DOSSIER_7 = "president_2010-12/"; 
	
	
	public static void insertBD(MailList aa){
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
	
	public static void main(String[] args) throws MessagingException, IOException {
		
		
		parseur test;
		MailList a = new MailList();
		ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		
		//test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_3+5);
		//test.getMailTest();
	
		//Dossier1 32 fichiers 
		for(int i = 1; i<=32; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_1+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		//Dossier2 288 fichiers 
		for(int i = 1; i<=288; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_2+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		//Dossier3 137 fichiers 
		for(int i = 1; i<=137; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_3+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		//Dossier4 547 fichiers 
		for(int i = 1; i<=547; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_4+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		//Dossier5 458 fichiers 
		for(int i = 1; i<=458; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_5+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		//Dossier6 529 fichiers 
		for(int i = 1; i<=529; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_6+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		//Dossier7 380 fichiers 
		for(int i = 1; i<=380; ++i) {
			System.out.println(i);
			test = new parseur(DOSSIER_PRINCIPAL+DOSSIER_7+i);
			a= test.mailToObject();
			//test.getMailTest();	
			listeMail.add(a); 
		}
		
		
		
		
		
		for(MailList aa : listeMail) {
			insertBD(aa);
		}
		/**/
	

	}
}