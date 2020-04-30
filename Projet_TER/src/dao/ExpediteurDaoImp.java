package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Email;
import beans.Expediteur;
import beans.Mail;
import beans.MailDestinataire;

public class ExpediteurDaoImp implements ExpediteurDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_expediteur (id_mail, email_email)"
			+ " VALUES(?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT id_mail, email_email FROM td_expediteur WHERE id_mail=? and email_email=?";
	
	public ExpediteurDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterExpediteur(Expediteur expediteur) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, expediteur.getMail().getIdMail());
            preparedStatement.setString(2, expediteur.getEmail().getEmail());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public Expediteur getExpediteur(Mail mail, Email email) {
		Expediteur expediteur = null;
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
				expediteur = new Expediteur();
				expediteur.setMail(mailDao.getMail(rs.getString("id_mail")));
				expediteur.setEmail(emailDao.getEmail(rs.getString("email_email")));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return expediteur;
	}

}
