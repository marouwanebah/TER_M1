package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import beans.PieceJointe;

public class PieceJointeDaoImpl implements PieceJointeDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_piece_jointe (nom_piece_jointe, contenu_piece_jointe, id_mail, code_type_piece_jointe)"
			+ " VALUES(?,?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT nom_piece_jointe, contenu_piece_jointe, id_mail, code_type_piece_jointe FROM td_piece_jointe WHERE nom_piece_jointe=?";
	
	public PieceJointeDaoImpl(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterPieceJointe(PieceJointe pieceJointe) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, pieceJointe.getNomPieceJointe());
            preparedStatement.setString(1, pieceJointe.getContenuJointe());
            preparedStatement.setString(1, pieceJointe.getMail().getIdMail());
            preparedStatement.setString(1, pieceJointe.getTypePieceJointe().getCodeTypePieceJointe());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

}
