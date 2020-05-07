
import java.io.File;
import java.io.IOException;

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
	//private static final String DOSSIER_PRINCIPAL = "/home/diallo/Documents/projetTER/corpus/president_2010/president_2010/";    
	private static final String DOSSIER_PRINCIPAL = "/home/etudiant/M1/S2/TER/president_2010/president_2010/"; 
	
		
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
		//insert institution expediteur principal
			String institutionExpPrinc;
			if(aa.getFrom().getInstitutionPersonne() != null) {
				institutionExpPrinc = aa.getFrom().getInstitutionPersonne().getNomInstitution();
				Institution institutionPrinc = new Institution();
				institutionPrinc.setNomInstitution(institutionExpPrinc);
				if(institutionDao.getInstitution(institutionExpPrinc) == null)
					institutionDao.ajouterInstitution(institutionPrinc);
			}
		//insert email principal
			Email email = new Email();
			email.setEmail(aa.getFrom().getEmailPersonne());
			email.setSignature(aa.getSignature());
			email.setInstitution(aa.getFrom().getInstitutionPersonne().getNomInstitution());
			if(emailDao.getEmail(email.getEmail()) == null) {
				emailDao.ajouterEmail(email);
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
				String[] tabLibelle = vemail.split("@");
				String code = tabLibelle[0];
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
					//insert institution expediteur cc
					String institutionExpcc;
					if(pers.getInstitutionPersonne() != null) {
						institutionExpcc = pers.getInstitutionPersonne().getNomInstitution();
						Institution institutioncc = new Institution();
						institutioncc.setNomInstitution(institutionExpcc);
						if(institutionDao.getInstitution(institutionExpcc) == null)
							institutionDao.ajouterInstitution(institutioncc);
					}
					Email emailcc = new Email();
					emailcc.setEmail(pers.getEmailPersonne());
					emailcc.setInstitution(pers.getInstitutionPersonne().getNomInstitution());
					//insert email
					if(emailDao.getEmail(emailcc.getEmail()) == null) {
						emailDao.ajouterEmail(emailcc);
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
						String[] tabLibelle = vemail.split("@");
						String code = tabLibelle[0];
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
				//insert institution destinataire
				String institutionvDest;
				if(person.getInstitutionPersonne() != null) {
					institutionvDest = person.getInstitutionPersonne().getNomInstitution();
					Institution institutionDest = new Institution();
					institutionDest.setNomInstitution(institutionvDest);
					if(institutionDao.getInstitution(institutionvDest) == null)
						institutionDao.ajouterInstitution(institutionDest);
				}
				Email emaildest = new Email();
				emaildest.setEmail(person.getEmailPersonne());
				emaildest.setInstitution(person.getInstitutionPersonne().getNomInstitution());
				//insert email
				if(emailDao.getEmail(emaildest.getEmail()) == null) {
					emailDao.ajouterEmail(emaildest);
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
					String[] tabLibelle = vemail.split("@");
					String code = tabLibelle[0];
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
		//ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		test = new parseur(DOSSIER_PRINCIPAL+"president_2010-07/"+62);
		test.getMailTest();
		/*
		File[] files = new File(DOSSIER_PRINCIPAL).listFiles();
		System.out.println("============Debut Insertion==============");
		for (File file : files) {
	        if (file.isDirectory()) {
	            for(File fileInsideFolder : file.listFiles()) {
	            	System.out.println(fileInsideFolder.getAbsolutePath());
					test = new parseur(fileInsideFolder.getAbsolutePath());
					a= test.mailToObject();
					insertBD(a);
	            }
	        }
	    }
		System.out.println("============Fin Insertion==============");
		 */
	}
}