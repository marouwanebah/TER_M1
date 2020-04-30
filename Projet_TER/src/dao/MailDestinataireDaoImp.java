package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Email;
import beans.Mail;
import beans.MailDestinataire;
import beans.Personne;

public class MailDestinataireDaoImp implements MailDestinataireDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_destinataire (id_mail, email_email)"
			+ " VALUES(?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT id_mail, email_email FROM td_destinataire WHERE id_mail=? and email_email=?";
	
	public MailDestinataireDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterDestinataire(MailDestinataire mailDestinataire) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, mailDestinataire.getMail().getIdMail());
            preparedStatement.setString(2, mailDestinataire.getEmail().getEmail());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public MailDestinataire getMailDestinataire(Mail mail, Email email) {
		MailDestinataire mailDestinataire = null;
		Connection connexion = null;
		MailDao mailDao = daoFactory.getMailDao();
		EmailDao emailDao  = daoFactory.getEmailDao();
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, mail.getIdMail());
			preparedStatement.setString(2, email.getEmail());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				mailDestinataire = new MailDestinataire();
				mailDestinataire.setMail(mailDao.getMail(rs.getString("id_mail")));
				mailDestinataire.setEmail(emailDao.getEmail(rs.getString("email_email")));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mailDestinataire;
	}

}
