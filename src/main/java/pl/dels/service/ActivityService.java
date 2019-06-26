package pl.dels.service;

import java.io.IOException;
import java.sql.Timestamp;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import pl.dels.model.User;
import pl.dels.model.enums.MachineNumber;
import pl.dels.model.enums.Side;
import pl.dels.toolsprovider.DateToolsProvider;
import pl.dels.database.dao.ActivityDao;
import pl.dels.database.dao.ActivityDaoImpl;
import pl.dels.database.repository.ActivityRepository;
import pl.dels.database.repository.UserRepository;
import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private UserRepository userRepository;

	public Activity saveActivityInDatabase(MachineNumber machineNumber, String workOrder, Side side, String activityType,
			String comments, Timestamp startDateTime, Timestamp stopDateTime, double downtime, String nameOfLoggedUser) {

		Activity activity = Activity.builder()
				.machineNumber(machineNumber)
				.workOrder(workOrder)
				.side(side)
				.activityType(activityType)
				.comments(comments)
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(downtime)
				.build();
				
		User user = userRepository.findByUsername(nameOfLoggedUser);
		activity.setUser(user);
		
		return activityRepository.save(activity);		
	}

	public List<Activity> getAllActivitiesFromMySql(Comparator<Activity> comparator) {
		
		List<Activity> activities = activityRepository.findAll();
		
		if (comparator != null && activities != null) {
			activities.sort(comparator);
		}
		
		return activities;
	}
	
	public List<ChartActivity> getFilteredActivitiesFromMySql() {
		
		List<Activity> aoiActivities = getAllActivitiesFromMySql((wo1, wo2) -> wo2.getWorkOrder().compareTo(wo1.getWorkOrder()));
		
		List<String> dateBetween = DateToolsProvider.getDatesBetween();
	
		System.out.println();
		System.out.println("Daty które mają być sprawdzane docelowo");
		
		dateBetween.forEach(System.out::println);
				
		//TEMP
		String mondayOfTheWeek = "2019-06-18";
		String sundayOfTheWeek = "2019-06-20";	

		//POPRAWIĆ FILTROWANIE ABY BYŁO POMIEDZY TYGODNIE
		/*List<Activity> filteredActivityList = aoiActivities.stream()
		.filter(activity -> dateBetween.stream().anyMatch(date -> date.equals(String.valueOf(activity.getStartDateTime()).substring(0, 10))))
		.collect(Collectors.toList());*/
		
		List<ChartActivity> chartActivityAoiList = aoiActivities.stream()
													.filter(activity -> String.valueOf(activity.getStartDateTime()).substring(0, 10).equals(sundayOfTheWeek))
													.map(activity -> new ChartActivity(activity.getWorkOrder(),aoiActivities.stream()
															.filter(activityWO -> activityWO.getWorkOrder().equals(activity.getWorkOrder()))
															.mapToDouble(activityWO -> activityWO.getDowntime()).sum()))
													.sorted((activity1, activity2) -> activity1.getWorkOrder().compareTo(activity2.getWorkOrder()))
													.distinct()
													.collect(Collectors.toList());
		//Weryfikacja czynności
		System.out.println();
		System.out.println("Czynności AR:");

		chartActivityAoiList.forEach(System.out::println);
		
		return chartActivityAoiList;
	}
	
	public List<ChartActivity> getFilteredActivitiesFromFirebird() throws ClassNotFoundException, IOException {
				
		//TEMP
		String mondayOfTheWeek = "2019-06-18";
		String sundayOfTheWeek = "2019-06-26";	
		
		ActivityDao activityDao = new ActivityDaoImpl();

		List<ChartActivity> kronosActivities = activityDao.getAllActivities(mondayOfTheWeek, sundayOfTheWeek);

		List<ChartActivity> chartActivityKronosList = kronosActivities.stream()
														.map(activity -> new ChartActivity(activity.getWorkOrder(),kronosActivities.stream()
																.filter(activityWO -> activityWO.getWorkOrder().equals(activity.getWorkOrder()))
																.mapToDouble(activityWO -> activityWO.getDowntime()).sum()))
														.sorted((activityWO1, activityWO2) -> activityWO1.getWorkOrder().compareTo(activityWO2.getWorkOrder()))
														.distinct()
														.collect(Collectors.toList());
		//Weryfikacja czynności	
		System.out.println();
		System.out.println("Czynności KRONOS:");

		chartActivityKronosList.forEach(System.out::println);
		
		return chartActivityKronosList;
	}

	public Activity createTempActivity(MachineNumber machineNumber, String workOrder, Side side, String activityType) {

		Activity activity = Activity.builder()
				.machineNumber(machineNumber)
				.workOrder(workOrder)
				.side(side)
				.activityType(activityType)
				.build();
		
		return activity;
	}
}
