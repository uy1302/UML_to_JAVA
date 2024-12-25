package postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	private String url = "jdbc:postgresql://localhost/postgres";
	private String user = "postgres";
	private String password = "13022005";
	
	private void connect() {
		try(Connection connection = DriverManager.getConnection(url,user,password);){
			if (connection != null) {
				System.out.println("Connected to PostGreSQL successfully");
			}else {
				System.out.println("Failed to connect");
			}
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT VERSION()");
			if (resultSet.next()) {
				System.out.println(resultSet.getString(1));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args ) {
		DBUtils sqlConnect = new DBUtils();
		sqlConnect.connect();
	}
}
