package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Mail;

public class MailDaoImp implements MailDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_mail (id_mail, date_envoi_mail, contenu_mail, sujet_mail, email_email, id_mail_pere)"
			+ " VALUES(?,?,?,?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT id_mail, date_envoi_mail, contenu_mail, sujet_mail FROM td_mail WHERE id_mail=?";
	
	public MailDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}

	@Override
	public void ajouterMail(Mail mail) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, mail.getIdMail());
            preparedStatement.setString(2, mail.getDateEnvoiMail());
            preparedStatement.setString(3, mail.getContenuMail());
            preparedStatement.setString(4, mail.getSujetMail());
            preparedStatement.setString(5, mail.getExpediteur().getEmail());
            preparedStatement.setString(6, mail.getMailPere().getIdMail());
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected == 1)
            	connexion.commit();
            else
            	connexion.rollback();
            preparedStatement.close();
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
                 if(preparedStatement != null) preparedStatement.close();
                 if(connexion != null) connexion.close();
                 
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
        }
		
	}

	@Override
	public Mail getMail(String idMail) {
		Mail mail = null;
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, idMail);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				mail = new Mail();
				mail.setIdMail(rs.getString("id_mail"));
				mail.setDateEnvoiMail(rs.getString("date_envoi_mail"));
				mail.setContenuMail(rs.getString("contenu_mail"));
				mail.setSujetMail(rs.getString("sujet_mail"));
			}
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mail;
	}

}