package database_migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*

Réalisé par Anass Daoudi GI4 2016/2017 ENSA Marrakech
 
*/

public class ConnexionDB {
	private Connection connection;
	private Statement st;
	private final String mysqlDriver = "com.mysql.jdbc.Driver";
	private final String oracleDriver = "oracle.jdbc.driver.OracleDriver";
	

	public ConnexionDB(String dbServer, String dbName, String userName, String pass, int port) {
		String serverDriver = null, serverURLDB = null;
		String mysqlURLDB = "jdbc:mysql://localhost:" + port + "/" + dbName;
		String oracleURLDB = "jdbc:oracle:thin:@localhost:" + port + ":" + dbName;
		
		if (dbServer.equals("mysql")) {
			serverDriver = mysqlDriver;
			serverURLDB = mysqlURLDB;
		} else if (dbServer.equals("oracle")) {
			serverDriver = oracleDriver;
			serverURLDB = oracleURLDB;
		}
		try {
			Class.forName(serverDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			connection = DriverManager.getConnection(serverURLDB, userName, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			st = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Statement getStatement() {
		return st;
	}

	public Connection getConnection() {
		return connection;
	}
}
