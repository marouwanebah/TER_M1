package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Email;

public class EmailDaoImp implements EmailDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_email (email_email, signature_email, nom_institution)"
			+ " VALUES(?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT email_email, signature_email, nom_institution FROM td_email WHERE email_email=?";
	
	public EmailDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterEmail(Email email) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, email.getEmail());
            preparedStatement.setString(2, email.getSignature());
            preparedStatement.setString(3, email.getInstitution());
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
	public Email getEmail(String vemail) {
		Email email = null;
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, vemail);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				email = new Email();
				email.setEmail(rs.getString("email_email"));
				email.setSignature(rs.getString("signature_email"));
				email.setInstitution(rs.getString("signature_email"));
			}
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return email;
	}

}
