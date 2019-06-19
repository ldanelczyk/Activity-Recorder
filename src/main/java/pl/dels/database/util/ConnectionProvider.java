package pl.dels.database.util;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.List;

import org.apache.commons.io.FileUtils;

public class ConnectionProvider {

	private static ConnectionProvider instance;

	private Connection connection;

	private String url;

	private String username;

	private String password;

	private String hostname;

	private ConnectionProvider() throws ClassNotFoundException, SQLException, IOException {

		List<String> lines = FileUtils.readLines(new File("C:\\Users\\danelczykl\\Desktop\\db_data.txt"));

		url = lines.get(0);

		username = lines.get(1);

		password = lines.get(2);

		hostname = lines.get(3);

		Class.forName(hostname);

		this.connection = DriverManager.getConnection(url, username, password);
	}

	public Connection getConnection() {
		return connection;
	}

	public static ConnectionProvider getInstance() throws ClassNotFoundException, SQLException, IOException {

		if (instance == null) {

			instance = new ConnectionProvider();

		} else if (instance.getConnection().isClosed()) {

			instance = new ConnectionProvider();
		}

		return instance;
	}
}
