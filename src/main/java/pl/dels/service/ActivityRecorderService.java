package pl.dels.service;

import java.sql.Timestamp;

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

	@Autowired
	public void setActivityRepository(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void addActivityToDatabase(String machineNumber, String workOrder, String side, String activityType, String comments,
			Timestamp startDateTime, Timestamp stopDateTime, int downtime, String nameOfLoggedUser) {
		
		Activity activity = new Activity(machineNumber,workOrder, side, activityType, comments, startDateTime, stopDateTime, downtime);

		User user = userRepository.findByUsername(nameOfLoggedUser);
		activity.setUser(user);
		activityRepository.save(activity);
	}
}
