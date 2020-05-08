package dao;

import java.sql.Connection;

import beans.PieceJointe;

public interface PieceJointeDao {
	public void ajouterPieceJointe(PieceJointe pieceJointe, Connection connexion);
}
