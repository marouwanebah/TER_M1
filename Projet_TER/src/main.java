
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.mail.MessagingException;

import beans.Email;
import beans.Expediteur;
import beans.Fonction;
import beans.Institution;
import beans.Lien;
import beans.ListeDiffusion;
import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;
import beans.PieceJointe;
import dao.EmailDao;
import dao.ExpediteurDao;
import dao.FonctionDao;
import dao.InstitutionDao;
import dao.LienDao;
import dao.ListeDiffusionDao;
import dao.MailDao;
import dao.MailDestinataireDao;
import dao.PersonneDAO;
import dao.PieceJointeDao;

public class main {
	private static final dao.DaoFactory daoFactory = dao.DaoFactory.getInstance();
	//private static final String DOSSIER_PRINCIPAL = "/home/diallo/Documents/projetTER/corpus/president_2010/president_2010/";    
	private static final String DOSSIER_PRINCIPAL = "/home/etudiant/M1/S2/TER/president_2010/president_2010/";
	
	private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/president_2010/president_2010/president_2010-10/";
		
	public static void insertBD(MailList aa, Connection connexion){
		EmailDao emailDao;
		InstitutionDao institutionDao;
		PersonneDAO personneDao;
		FonctionDao fonctionDao;
		MailDao mailDao;
		PieceJointeDao pieceJointeDao;
		LienDao lienDao;
		ExpediteurDao expediteurDao;
		MailDestinataireDao malDestinataireDao;
		ListeDiffusionDao listeDiffusionDao;
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
		listeDiffusionDao = daoFactory.getListeDiffusionDao();
			//System.out.println(aa.getFrom().getNomPersonne() +" "+ aa.getFrom().getPrenomPersonne()+ " " +aa.getFrom().getEmailPersonne());
		
			
			//insert institution expediteur principal
			String institutionExpPrinc;
			if(aa.getFrom().getInstitutionPersonne() != null) {
				institutionExpPrinc = aa.getFrom().getInstitutionPersonne().getNomInstitution();
				Institution institutionPrinc = new Institution();
				institutionPrinc.setNomInstitution(institutionExpPrinc);
				if(institutionDao.getInstitution(institutionExpPrinc, connexion) == null)
					institutionDao.ajouterInstitution(institutionPrinc, connexion);
			}
		//insert email principal
			Email email = new Email();
			email.setEmail(aa.getFrom().getEmailPersonne());
			email.setInstitution(aa.getFrom().getInstitutionPersonne().getNomInstitution());
			if(emailDao.getEmail(email.getEmail(), connexion) == null) {
				emailDao.ajouterEmail(email, connexion);
			}
			//insert personne principale
			if(aa.getFrom().getNomPersonne() != "" && aa.getFrom().getPrenomPersonne() != "") {
				if(personneDao.getPersonne(email.getEmail(), connexion) == null) {
					personneDao.ajouterPersonne(aa.getFrom(), connexion);
				}
			}
			//insert liste Diffusion
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
				if(fonctionDao.getFonction(vemail, connexion) == null)
				fonctionDao.ajouterFonction(fonction, connexion);
			}
			if(aa.getFrom().getEmailPersonne().matches("(.*)list(.*)")) {
				ListeDiffusion liste = new ListeDiffusion();
				liste.setEmailListeDiffusion(email.getEmail());
				if(listeDiffusionDao.getListe(email.getEmail(), connexion) == null)
					listeDiffusionDao.ajouterListe(liste, connexion);
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
			mail.setTypeMail(aa.getTypeemail());
			mail.setContenuMailPropre(aa.getContenuePropre());
			mail.setSignatureMail(aa.getSignature());
			if(mailDao.getMail(mail.getIdMail(), connexion) == null)
				mailDao.ajouterMail(mail, connexion);
			//insert piece jointe
			for(PieceJointe pieceJointe : aa.getAttachments()) {
				pieceJointeDao.ajouterPieceJointe(pieceJointe, connexion);
			}
			//insert lien
			for(Lien lien : aa.getLiens()) {
				lienDao.ajouterLien(lien, connexion);
			}
			//insert Expediteur principal
			if(expediteurDao.getExpediteur(mail, email, connexion) == null) {
				Expediteur expediteur = new Expediteur();
				expediteur.setMail(mail);
				expediteur.setEmail(email);
				expediteurDao.ajouterExpediteur(expediteur, connexion);
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
						if(institutionDao.getInstitution(institutionExpcc, connexion) == null)
							institutionDao.ajouterInstitution(institutioncc, connexion);
					}
					Email emailcc = new Email();
					emailcc.setEmail(pers.getEmailPersonne());
					emailcc.setInstitution(pers.getInstitutionPersonne().getNomInstitution());
					//insert email
					if(emailDao.getEmail(emailcc.getEmail(), connexion) == null) {
						emailDao.ajouterEmail(emailcc, connexion);
					}
					//insert personne
					if(pers.getNomPersonne() != "" && pers.getPrenomPersonne() != "") {
						if(personneDao.getPersonne(emailcc.getEmail(), connexion) == null) {
							personneDao.ajouterPersonne(pers, connexion);
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
						if(fonctionDao.getFonction(vemail, connexion) == null)
						fonctionDao.ajouterFonction(fonction, connexion);
					}
					//insert liste Diffusion
					if(pers.getEmailPersonne().matches("(.*)list(.*)")) {
						ListeDiffusion liste = new ListeDiffusion();
						liste.setEmailListeDiffusion(emailcc.getEmail());
						if(listeDiffusionDao.getListe(emailcc.getEmail(), connexion) == null)
							listeDiffusionDao.ajouterListe(liste, connexion);
					}
					//insert exp cc
					if(expediteurDao.getExpediteur(mail, emailcc, connexion) == null) {
						Expediteur expediteur = new Expediteur();
						expediteur.setMail(mail);
						expediteur.setEmail(emailcc);
						expediteurDao.ajouterExpediteur(expediteur, connexion);
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
					if(institutionDao.getInstitution(institutionvDest, connexion) == null)
						institutionDao.ajouterInstitution(institutionDest, connexion);
				}
				Email emaildest = new Email();
				emaildest.setEmail(person.getEmailPersonne());
				emaildest.setInstitution(person.getInstitutionPersonne().getNomInstitution());
				//insert email
				if(emailDao.getEmail(emaildest.getEmail(), connexion) == null) {
					emailDao.ajouterEmail(emaildest, connexion);
				}
				//insert personne
				if(person.getNomPersonne() != "" && person.getPrenomPersonne() != "") {
					if(personneDao.getPersonne(emaildest.getEmail(), connexion) == null) {
						personneDao.ajouterPersonne(person, connexion);
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
					if(fonctionDao.getFonction(vemail, connexion) == null)
						fonctionDao.ajouterFonction(fonction, connexion);
				}
				//insert liste Diffusion
				if(person.getEmailPersonne().matches("(.*)list(.*)")) {
					ListeDiffusion liste = new ListeDiffusion();
					liste.setEmailListeDiffusion(emaildest.getEmail());
					if(listeDiffusionDao.getListe(emaildest.getEmail(), connexion) == null)
						listeDiffusionDao.ajouterListe(liste, connexion);
				}
				//insert dest
				if(malDestinataireDao.getMailDestinataire(mail, emaildest, connexion) == null) {
					MailDestinataire destinataire = new MailDestinataire();
					destinataire.setMail(mail);
					destinataire.setEmail(emaildest);
					malDestinataireDao.ajouterDestinataire(destinataire, connexion);
				}
			}
			}
		
		
	}
	
	public static void main(String[] args) throws MessagingException, IOException {
		
		
		 	parseur test;
	        MailList a = new MailList();
	        Connection connexion = null;
	        int i = 0;
	        //ArrayList<MailList> listeMail = new ArrayList<MailList>();
	       //332  2 18 21  24 42 47 63 65 						49	
	       //test = new parseur(LIEN_FICHIER+112);
	       //test.getMailTest();
	       
	        
	        File[] files = new File(DOSSIER_PRINCIPAL).listFiles();
	        System.out.println("============Debut Insertion==============");
	        try {
				connexion = daoFactory.getConnection();
				for (File file : files) {
		            if (file.isDirectory()) {
		                for(File fileInsideFolder : file.listFiles()) {
		                  //  System.out.println(fileInsideFolder.getAbsolutePath());
		                    test = new parseur(fileInsideFolder.getAbsolutePath());
		                    a= test.mailToObject();
		                   // System.out.println(i++);
		                    insertBD(a, connexion);
		                   
		                }
		            }
		        }
				System.out.println(i);
		        System.out.println("============Fin Insertion==============");
			} catch (SQLException ex) {
	        	try{
	                if(connexion != null)
	                	connexion.rollback();
	            }catch(SQLException e){
	                System.out.println(e.getMessage());
	            }
	        	System.out.println(ex.getMessage());
	        } finally {
	        	 try {
	                 if(connexion != null) connexion.close();
	                 
	             } catch (SQLException e) {
	                 System.out.println(e.getMessage());
	             }
	        }
	    

	        /* */
	}
}