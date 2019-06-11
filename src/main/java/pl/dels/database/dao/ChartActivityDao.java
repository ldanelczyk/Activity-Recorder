package pl.dels.database.dao;

import java.util.List;

import pl.dels.model.ChartActivity;

public interface ChartActivityDao {

	List<ChartActivity> getAllActivities() throws ClassNotFoundException;
}
