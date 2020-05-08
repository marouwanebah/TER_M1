package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import beans.PieceJointe;

public class PieceJointeDaoImpl implements PieceJointeDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_piece_jointe (nom_piece_jointe, contenu_piece_jointe, id_mail)"
			+ " VALUES(?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT nom_piece_jointe, contenu_piece_jointe, id_mail, code_type_piece_jointe FROM td_piece_jointe WHERE nom_piece_jointe=?";
	
	public PieceJointeDaoImpl(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterPieceJointe(PieceJointe pieceJointe, Connection connexion) {
		//Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
           // connexion = daoFactory.getConnection();
            connexion.setAutoCommit(false);
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, pieceJointe.getNomPieceJointe());
            preparedStatement.setBinaryStream(2, pieceJointe.getContenuJointe());
            preparedStatement.setString(3, pieceJointe.getMailId());
            
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

}
