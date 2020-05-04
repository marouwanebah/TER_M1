package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Fonction;

public class FonctionDaoImp implements FonctionDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO tc_fonction (code_fonction, libelle_fonction, email_email)"
			+ " VALUES(?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT code_fonction, libelle_fonction, email_email FROM tc_fonction WHERE email_email=?";
	
	public FonctionDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterFonction(Fonction fonction) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, fonction.getCodeFonction());
            preparedStatement.setString(2, fonction.getLibelleFonction());
            preparedStatement.setString(3, fonction.getEmail().getEmail());
            
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
	public Fonction getFonction(String email) {
		Fonction fonction = null;
		Connection connexion = null;
		EmailDao emailDao  = daoFactory.getEmailDao();
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				fonction = new Fonction();
				fonction.setCodeFonction(rs.getString("code_fonction"));
				fonction.setLibelleFonction(rs.getString("libelle_fonction"));
				fonction.setEmail(emailDao.getEmail(rs.getString("email_email")));
			}
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fonction;
	}

}
