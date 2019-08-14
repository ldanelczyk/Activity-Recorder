package pl.dels.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import pl.dels.database.dao.ChartActivityDao;
import pl.dels.model.ChartActivity;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class ChartActivityService {

	@Autowired
	private ChartActivityDao activityDao;
	
	@Autowired
	private ActivityService activityService;

	public List<ChartActivity> getProcessedChartActivitiesFromFirebird() throws ClassNotFoundException, IOException {

		// TEMP
		String mondayOfTheWeek = "2019-08-05";
		String sundayOfTheWeek = "2019-08-09";

		// TARGET
		//String mondayOfTheWeek = DateToolsProvider.getMondayOfTheWeek(); 
		//String sundayOfTheWeek = DateToolsProvider.getSundayOfTheWeek();

		List<ChartActivity> kronosActivities = activityDao.getAllActivities(mondayOfTheWeek, sundayOfTheWeek);
		
		List<ChartActivity> aoiActivities = activityService.getProcessedChartActivitiesFromMySql();

		List<ChartActivity> chartActivityKronosList = kronosActivities.stream()
				.filter(kronosActivity -> aoiActivities.stream().anyMatch(aoiActivity -> aoiActivity.getWorkOrder().equals(kronosActivity.getWorkOrder())))
				.map(activity -> new ChartActivity(activity.getWorkOrder(),
						kronosActivities.stream()
								.filter(activityWO -> activityWO.getWorkOrder().equals(activity.getWorkOrder()))
								.mapToDouble(activityWO -> activityWO.getDowntime()).sum()))
				.sorted((activityWO1, activityWO2) -> activityWO1.getWorkOrder().compareTo(activityWO2.getWorkOrder()))
				.distinct()
				.collect(Collectors.toList());
		
		return chartActivityKronosList;
	}
}
