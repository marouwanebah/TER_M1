package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Institution;

public class InstitutionDaoImp implements InstitutionDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_institution (nom_institution)"
			+ " VALUES(?)";
	private static final String SQL_SELECT_ONLY = "SELECT nom_institution FROM td_institution WHERE nom_institution=?";
	
	public InstitutionDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterInstitution(Institution institution, Connection connexion) {
		//Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
           // connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, institution.getNomInstitution());
            
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
                 //if(connexion != null) connexion.close();
                 
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
        }
		
	}

	@Override
	public Institution getInstitution(String nomInstitution, Connection connexion) {
		Institution institution = null;
		//Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			//connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, nomInstitution);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				institution = new Institution();
				institution.setNomInstitution(rs.getString("nom_institution"));
				
			}
			preparedStatement.close();
			//connexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return institution;
	}

}
