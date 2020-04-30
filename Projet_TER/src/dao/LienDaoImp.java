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
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, lien.getNomLien());
            preparedStatement.setString(1, lien.getContenuLien());
            preparedStatement.setString(1, lien.getMail().getIdMail());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

}
