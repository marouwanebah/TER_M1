package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.Pays;
import beans.Ville;

public class VilleDaoImp implements VilleDao {
	private DaoFactory daoFactory;
	private static final String SQL_INSERT = "INSERT INTO tc_ville (code_postal, nom_ville, nom_pays)"
			+ " VALUES(?,?,?)";
	private static final String SQL_SELECT_ONLY = "SELECT code_postal, nom_ville, nom_pays FROM tc_ville WHERE code_postal=?";
	
	public VilleDaoImp(DaoFactory daoFactory ) {
		super();
		this.daoFactory = daoFactory;
	}
	@Override
	public void ajouterVille(Ville ville) {
		Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = connexion.prepareStatement(SQL_INSERT);

            preparedStatement.setInt(1, ville.getCodePostal());
            preparedStatement.setString(2, ville.getNomVille());
            preparedStatement.setString(3, ville.getPays().getNomPays());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public Ville getVille(int codePostal) {
		Ville ville = null;
		Connection connexion = null;
        PreparedStatement preparedStatement = null;
		try {
			connexion = daoFactory.getConnection();
			preparedStatement = connexion.prepareStatement
					(SQL_SELECT_ONLY);
			preparedStatement.setInt(1, codePostal);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
				ville = new Ville();
				Pays pays = new Pays();
				ville.setCodePostal(rs.getInt("code_postal"));
				ville.setNomVille(rs.getString("nom_ville"));
				pays.setNomPays(rs.getString("nom_pays"));
				ville.setPays(pays);
			}
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ville;
	}

}
