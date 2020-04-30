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
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, pays.getNomPays());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pays;
	}

}
