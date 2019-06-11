package pl.dels.database.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import pl.dels.model.ChartActivity;

public class ChartActivityDaoImpl implements ChartActivityDao {

	private final int TIME_DIVIDER = 3600;

	private final String URL = "***";

	private final String USERNAME = "***";

	private final String PASSWORD = "***";

	private final String HOSTNAME = "***";

	@Override
	public List<ChartActivity> getAllActivities() {

		List<ChartActivity> chartActivityList = new ArrayList<>();

		String query = "SELECT ZASOB, ZR, CZYNNOSC_SYMBOL, CZAS_TRWANIA \r\n" + "\r\n"
				+ "from TS_KRONOS_WYKONANIA_CZYNNOSCI \r\n" 
				+ "\r\n" + "WHERE ZR = '25261'";
		try {

			Class.forName(HOSTNAME);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);) {

			while (resultSet.next()) {

				if (resultSet.getString("CZYNNOSC_SYMBOL").equals("Pisanie programu AOI")
						|| resultSet.getString("CZYNNOSC_SYMBOL").equals("Pisanie programu AOI")) {

					chartActivityList.add(createChartActivityFromRs(resultSet));
				}
			}

		} catch (

		SQLException e)

		{
			e.printStackTrace();

		}
		return chartActivityList;
	}

	private ChartActivity createChartActivityFromRs(ResultSet rs) throws SQLException {

		ChartActivity chartActivity = new ChartActivity();
		chartActivity.setWorkOrder(rs.getString("ZR"));
		chartActivity.setDowntime(Double.parseDouble(rs.getString("CZAS_TRWANIA")) / TIME_DIVIDER);

		return chartActivity;
	}
}
