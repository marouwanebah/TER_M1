package dao;

import beans.Email;

public interface EmailDao {
	public void ajouterEmail(Email email);
	public Email getEmail(String vemail);
}
