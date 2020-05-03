package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Pays;

public class PaysDaoImp implements PaysDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_pays (nom_pays)"
			+ " VALUES(?)";
	private static final String SQL_SELECT_ONLY = "SELECT nom_pays FROM td_pays WHERE nom_pays=?";
	
	public PaysDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterPays(Pays pays) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, pays.getNomPays());
            
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
	public Pays getPays(String nom) {
		Pays pays = null;
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, nom);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				pays = new Pays();
				pays.setNomPays(rs.getString("nom_pays"));
			}
			preparedStatement.close();
			connexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pays;
	}

}
