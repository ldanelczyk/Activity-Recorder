package pl.dels.service;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Comparator;
import java.util.List;

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

@NoArgsConstructor
@AllArgsConstructor
@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private UserRepository userRepository;
	
	private final double DIVIDER_OF_SECONDS = 1000000000;

	private final double DIVIDER_OF_HOUR = 3600;

	private final double ROUND_CONSTANCE = 1000;

	public Activity saveActivityInDatabase(MachineNumber machineNumber, String workOrder, Side side, String activityType,
			String comments, Timestamp startDateTime, Timestamp stopDateTime, double downtime,
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

	public List<Activity> getAllActivities(Comparator<Activity> comparator) {
		
		List<Activity> activities = activityRepository.findAll();
		
		if (comparator != null && activities != null) {
			activities.sort(comparator);
		}
		return activities;
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

	public double downtimeCounter(Timestamp startDateTime, Timestamp stopDateTime) {

		LocalDateTime localStartDateTime = startDateTime.toLocalDateTime();

		LocalDateTime localStopDateTime = stopDateTime.toLocalDateTime();

		long differenceBetweenDateInNanoseconds = ChronoUnit.NANOS.between(localStartDateTime, localStopDateTime);

		double downtime = differenceBetweenDateInNanoseconds / DIVIDER_OF_SECONDS / DIVIDER_OF_HOUR;

		double downtimeInHour = Math.floor(downtime * ROUND_CONSTANCE) / ROUND_CONSTANCE;

		return downtimeInHour;
	}
}
