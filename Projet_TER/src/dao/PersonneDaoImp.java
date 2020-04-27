package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.Personne;
import dao.DaoFactory;

public class PersonneDaoImp implements PersonneDAO {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO td_personne (email_personne, nom_personne, prenom_personne, role_personne, code_type_personne)"
			+ " VALUES(?,?,?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT email_personne, nom_personne, prenom_personne, role_personne FROM td_personne WHERE email_personne=?";
	
	public PersonneDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	
	@Override
	public void ajouterPersonne(Personne personne) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setString(1, personne.getEmailPersonne());
            preparedStatement.setString(2, personne.getNomPersonne());
            preparedStatement.setString(3, personne.getPrenomPersonne());
            preparedStatement.setString(4, personne.getRolePersonne());
            preparedStatement.setString(5, personne.getTypePersonne().getCodePersonne());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public Personne getPersonne(String email) {
		Personne personne = null;
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				personne = new Personne();
				personne.setEmailPersonne(rs.getString("email_personne"));
				personne.setNomPersonne(rs.getString("nom_personne"));
				personne.setPrenomPersonne(rs.getString("prenom_personne"));
				personne.setRolePersonne(rs.getString("role_personne"));
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return personne;
	}
	
}
