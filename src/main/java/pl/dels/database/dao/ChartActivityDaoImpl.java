package pl.dels.database.dao;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import pl.dels.database.util.ConnectionProvider;
import pl.dels.model.ChartActivity;

@Repository
public class ChartActivityDaoImpl implements ChartActivityDao {

	private final int TIME_DIVIDER = 3600;

	@Override
	public List<ChartActivity> getAllActivities(String startDate, String stopDate)
			throws ClassNotFoundException, IOException {

		String getAllChartActivities = "SELECT DISTINCT ZR, CZAS_TRWANIA \r\n"
										+ "from TS_KRONOS_WYKONANIA_CZYNNOSCI \r\n"
										+ "WHERE (CZYNNOSC_SYMBOL = 'Pisanie programu AOI' AND (DATA_OD BETWEEN '" + startDate + "' AND '"+ stopDate + "') AND GRUPA_ZASOBOW = 'SMT') \r\n"
										+ "OR    (CZYNNOSC_SYMBOL = 'Poprawa programu AOI' AND (DATA_OD BETWEEN '" + startDate + "' AND '"+ stopDate + "') AND GRUPA_ZASOBOW = 'SMT')";

		List<ChartActivity> chartActivityList = new ArrayList<>();

		try (Connection connection = ConnectionProvider.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(getAllChartActivities);) {

			while (resultSet.next()) {

				chartActivityList.add(createChartActivityFromRs(resultSet));
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
