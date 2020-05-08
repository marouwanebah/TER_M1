package dao;

import java.sql.Connection;

import beans.Email;

public interface EmailDao {
	public void ajouterEmail(Email email, Connection connexion );
	public Email getEmail(String vemail, Connection connexion);
}
