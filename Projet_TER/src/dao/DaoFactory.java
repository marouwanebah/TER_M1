package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private String url;
    private String username;
    private String password;
    
    
    DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {

        }

        DaoFactory instance = new DaoFactory(
        		 "jdbc:mysql://localhost:3306/ter?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "sory", "123456");
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    
    // Récupération du Dao
    /*public UtilisateurDAO getUtilisateurDao() {
        return new UtilisateurDaoImp(this); 
    }
    public PartieDAO getPartieDao() {
        return new PartieDaoImp(this); 
    }*/
    public PersonneDAO getPersonneDao() {
    	return new PersonneDaoImp(this);
    }
    public MailDao getMailDao() {
    	return new MailDaoImp(this);
    }
    public MailDestinataireDao getMailDestinataireDao() {
    	return new MailDestinataireDaoImp(this);
    }
    public PaysDao getPaysDao() {
    	return new PaysDaoImp(this);
    }
    public EmailDao getEmailDao() {
    	return new EmailDaoImp(this);
    }
    public ExpediteurDao getExpediteurDao() {
    	return new ExpediteurDaoImp(this);
    }
    public PieceJointeDao getPieceJointeDao() {
    	return new PieceJointeDaoImpl(this);
    }
    public LienDao getLienDao() {
    	return new LienDaoImp(this);
    }
    public FonctionDao getFonctionDao() {
    	return new FonctionDaoImp(this);
    }
    public InstitutionDao getInstitutionDao() {
    	return new InstitutionDaoImp(this);
    }
    
}
