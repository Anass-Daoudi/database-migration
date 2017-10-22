package database_migration;

/*

Réalisé par Anass Daoudi GI4 2016/2017 ENSA Marrakech
 
*/

public interface DAO {
	public abstract void createTable(ConnexionDB c, String tableName, String columnsDescription);

	public abstract void insertRecord(ConnexionDB c, String tableName, String recordDescription);
}
