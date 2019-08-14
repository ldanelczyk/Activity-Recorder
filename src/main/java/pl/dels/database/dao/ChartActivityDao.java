package pl.dels.database.dao;

import java.io.IOException;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.dels.model.ChartActivity;

@Repository
public interface ChartActivityDao {

	List<ChartActivity> getAllActivities(String startDate, String stopDate) throws ClassNotFoundException, IOException;
}
