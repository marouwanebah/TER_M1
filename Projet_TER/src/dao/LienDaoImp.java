package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import beans.Lien;

public class LienDaoImp implements LienDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_lien (nom_lien, contenu_lien, id_mail)"
			+ " VALUES(?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT nom_lien, contenu_lien, id_mail FROM td_lien WHERE nom_lien=?";
	
	public LienDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterLien(Lien lien) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, lien.getNomLien());
            preparedStatement.setString(2, lien.getContenuLien());
            preparedStatement.setString(3, lien.getIdMail());
            
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

}
