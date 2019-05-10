package pl.dels.service;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.dels.model.User;
import pl.dels.model.Activity;
import pl.dels.repository.ActivityRepository;
import pl.dels.repository.UserRepository;

@Service
public class ActivityRecorderService {

	private ActivityRepository activityRepository;

	private UserRepository userRepository;

	private Timestamp startDateTime;

	private final double DIVIDER_OF_SECONDS = 1000000000;

	private final double DIVIDER_OF_HOUR = 3600;

	private final double ROUND_CONSTANCE = 1000;

	@Autowired
	public void setActivityRepository(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void saveActivityInDatabase(String machineNumber, String workOrder, String side, String activityType,
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
		activityRepository.save(activity);
	}

	public List<Activity> getAllActivities(Comparator<Activity> comparator) {
		List<Activity> activities = activityRepository.findAll();
		if (comparator != null && activities != null) {
			activities.sort(comparator);
		}
		return activities;
	}

	public Activity createTempActivity(String machineNumber, String workOrder, String side, String activityType) {

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

	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime() {
		this.startDateTime = new Timestamp(new Date().getTime());
	}
}
