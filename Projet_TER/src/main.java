import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;

import beans.Personne;
import dao.MailDao;
import dao.MailDestinataireDao;
import dao.PersonneDAO;

public class main {

	private static final String LIEN_FICHIER = "/home/etudiant/M1/S2/TER/Projet/TER_M1/Projet_TER/Data/president/";  
	
	
	public static void main(String[] args) throws MessagingException, IOException {
	//	PersonneDAO personneDao;
	//	MailDao mailDao;
	//	MailDestinataireDao malDestinataireDao;
	//	dao.DaoFactory daoFactory = dao.DaoFactory.getInstance();
	//	personneDao = daoFactory.getPersonneDao();
	//	mailDao = daoFactory.getMailDao();
	//	malDestinataireDao = daoFactory.getMailDestinataireDao();
		
		
		parseur test = new parseur(LIEN_FICHIER+30);
		
		System.out.println("************************MessageID********************");
		System.out.println(test.GetMessageId()); 	
		System.out.println("************************expéditeur********************");		
		test.getExpediteur();
		System.out.println("************************Destinataire********************");		
		ArrayList<Personne>  destinatire = test.getDestinataire(Message.RecipientType.TO);
		System.out.println("************************en copie********************");
		//ArrayList<Personne>  destinatireCC = test.getDestinataire(Message.RecipientType.CC);
		System.out.println("************************subject********************");
		System.out.println(test.GetSubject());
		System.out.println("************************BODY********************");
		System.out.println(test.GetMailContenu()); 
		test.getPieceJointe();
		System.out.println("************************Signature********************");
		System.out.println(test.getSignature());
		
		System.out.println("************************fin Signature********************");
		//MailList a= parseur.ParseMail(LIEN_FICHIER+10); 
		//System.out.println(a.toString());
		
		//ArrayList<MailList> listeMail = new ArrayList<MailList>(); 
		
		//for(int i = 1; i<32; ++i) {
		//	listeMail.add(ParseMail(LIEN_FICHIER+i)); 
		//}
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
