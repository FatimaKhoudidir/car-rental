package application;

import java.sql.*;

public class ConnectionMysql {
	public Connection cnx = null;
	
	public static Connection connectToDB(){
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/location","root","kamal");
			System.out.println("connection reussi.");
			return cnx;
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Erreur de connection a la base de donnees.");
			e.printStackTrace();
			return null;
		}
		
	}

}
