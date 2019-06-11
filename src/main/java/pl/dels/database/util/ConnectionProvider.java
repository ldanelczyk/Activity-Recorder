package pl.dels.database.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	
	private static ConnectionProvider instance;
	
	private Connection connection;
	
	private String url;

	private String username;

	private String password;
	
	private final String hostname;
	
	private ConnectionProvider() throws ClassNotFoundException, SQLException {
		
		url = "***";

		username = "***";

		password = "***";
		
		hostname = "***";
		
		Class.forName(hostname);
		
		this.connection = DriverManager.getConnection(url, username, password);
	}
	
	public Connection getConnection() {
        return connection;
}
	
	public static ConnectionProvider getInstance() throws ClassNotFoundException, SQLException {
		
		if(instance==null) {
			
			instance = new ConnectionProvider();
			
		}else if (instance.getConnection().isClosed()){
			
			instance = new ConnectionProvider();	
		}
		return instance;
	}

}
