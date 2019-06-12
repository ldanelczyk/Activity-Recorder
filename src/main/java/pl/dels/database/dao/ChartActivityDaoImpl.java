package pl.dels.database.dao;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import pl.dels.database.util.ConnectionProvider;
import pl.dels.model.ChartActivity;

public class ChartActivityDaoImpl implements ChartActivityDao {

	private final int TIME_DIVIDER = 3600;

	@Override
	public List<ChartActivity> getAllActivities() throws ClassNotFoundException, IOException {

		String getAllChartActivities = "SELECT ZASOB, ZR, CZYNNOSC_SYMBOL, CZAS_TRWANIA \r\n" + "\r\n"
				+ "from TS_KRONOS_WYKONANIA_CZYNNOSCI \r\n" 
				+ "\r\n" + "WHERE ZR = '25261'";

		List<ChartActivity> chartActivityList = new ArrayList<>();

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(getAllChartActivities);) {

			while (resultSet.next()) {

				if (resultSet.getString("CZYNNOSC_SYMBOL").equals("Pisanie programu AOI")
						|| resultSet.getString("CZYNNOSC_SYMBOL").equals("Poprawa programu AOI")) {

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
