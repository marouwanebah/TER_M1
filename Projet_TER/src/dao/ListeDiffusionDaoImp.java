package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.ListeDiffusion;
import beans.Mail;

public class ListeDiffusionDaoImp implements ListeDiffusionDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_liste_diffusion (email_liste_diffusion)"
			+ " VALUES(?)";
	private static final String SQL_SELECT_ONLY = "SELECT email_liste_diffusion FROM  td_liste_diffusion WHERE email_liste_diffusion=?";
	
	public ListeDiffusionDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterListe(ListeDiffusion liste, Connection connexion) {
		//Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            //connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, liste.getEmailListeDiffusion());
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
                // if(connexion != null) connexion.close();
                 
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
        }
		
	}

	@Override
	public ListeDiffusion getListe(String emailListe, Connection connexion) {
		ListeDiffusion liste = null;
		//Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			//connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, emailListe);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				liste = new ListeDiffusion();
				liste.setEmailListeDiffusion(rs.getString("email_liste_diffusion"));
			}
			preparedStatement.close();
			//connexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return liste;
	}

}
