package pl.dels.database.dao;

import java.io.IOException;

import java.util.List;

import pl.dels.model.ChartActivity;

public interface ChartActivityDao {

	List<ChartActivity> getAllActivities(String startDate, String stopDate) throws ClassNotFoundException, IOException;
}
