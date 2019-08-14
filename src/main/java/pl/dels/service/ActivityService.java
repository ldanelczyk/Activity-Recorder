package pl.dels.service;

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
import pl.dels.database.repository.ActivityRepository;
import pl.dels.database.repository.UserRepository;
import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;

/**
 * @author danelczykl
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private UserRepository userRepository;

	public Activity saveActivityInDatabase(MachineNumber machineNumber, String workOrder, Side side,
			String activityType, String comments, Timestamp startDateTime, Timestamp stopDateTime, double downtime,
			String nameOfLoggedUser) {

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

	public List<ChartActivity> getProcessedChartActivitiesFromMySql() {

		List<Activity> aoiActivities = getAllActivitiesFromMySql((wo1, wo2) -> wo2.getWorkOrder().compareTo(wo1.getWorkOrder()));

		// TEMP
		String sundayOfTheWeek = "2019-08-07";

		// TARGET
		// List<String> dateBetween = DateToolsProvider.getDatesBetween();

		List<ChartActivity> chartActivityAoiList = aoiActivities.stream()
				.filter(activity -> String.valueOf(activity.getStartDateTime()).substring(0, 10).equals(sundayOfTheWeek))
				//.filter(activity -> dateBetween.stream().anyMatch(date -> date.equals(String.valueOf(activity.getStartDateTime()).substring(0, 10))))
				.map(activity -> new ChartActivity(activity.getWorkOrder(),
						aoiActivities.stream()
								.filter(activityWO -> activityWO.getWorkOrder().equals(activity.getWorkOrder()))
								.mapToDouble(activityWO -> activityWO.getDowntime()).sum()))
				.sorted((activity1, activity2) -> activity1.getWorkOrder().compareTo(activity2.getWorkOrder()))
				.distinct()
				.collect(Collectors.toList());

	/*	// Weryfikacja czynności AR
		System.out.println();
		System.out.println("Czynności AR po filtrowaniu:");
		chartActivityAoiList.forEach(System.out::println);*/

		return chartActivityAoiList;
	}

	public Activity createTempActivity(MachineNumber machineNumber, String workOrder, Side side, String activityType) {

		Activity activity = Activity
				.builder()
				.machineNumber(machineNumber)
				.workOrder(workOrder)
				.side(side)
				.activityType(activityType)
				.build();

		return activity;
	}

	public ActivityService(ActivityRepository activityRepository) {
		super();
		this.activityRepository = activityRepository;
	}
}
