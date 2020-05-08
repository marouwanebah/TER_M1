package dao;

import java.sql.Connection;

import beans.Lien;

public interface LienDao {
	public void ajouterLien(Lien lien, Connection connexion);
}
