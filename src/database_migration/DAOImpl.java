package database_migration;

import java.sql.SQLException;

/*

Réalisé par Anass Daoudi GI4 2016/2017 ENSA Marrakech
 
*/

public class DAOImpl implements DAO {
	public void createTable(ConnexionDB c, String tableName, String columnsDescription) {
		String request = "create table " + tableName + "( " + columnsDescription + " )";
		try {
			c.getStatement().executeUpdate(request);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertRecord(ConnexionDB c, String tableName, String recordDescription) {
		String request = "insert into " + tableName + " values ( " + recordDescription + " )";
		try {
			c.getStatement().executeUpdate(request);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
