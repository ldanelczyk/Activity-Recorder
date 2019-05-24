package pl.dels.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import pl.dels.model.Activity;
import pl.dels.repository.ActivityRepository;
import pl.dels.repository.UserRepository;

class ActivityServiceTest {

	private Timestamp startDateTime = new Timestamp(new Date().getTime());

	private Timestamp stopDateTime = new Timestamp(new Date().getTime());
	
	@Test
	void saveActivityInDatabase() {
		
		//given
		Activity createdActivity = createActivity();
		
		ActivityRepository activityRepository = mock(ActivityRepository.class);
		UserRepository userRepository = mock(UserRepository.class);

		ActivityService activityService = new ActivityService(activityRepository, userRepository);
	
		given(activityRepository.save(any(Activity.class))).willReturn(createdActivity);
		
		//when
		Activity returnedActivity = activityService.saveActivityInDatabase("AOI2", "ZRXXYY", "BOT", "Poprawa programu AOI", "Example Comment2", startDateTime, stopDateTime, 0.001, null);

		ArgumentCaptor<Activity> activityArgument = ArgumentCaptor.forClass(Activity.class);
	
		//then
		verify(activityRepository, times(1)).save(activityArgument.capture());
		verifyNoMoreInteractions(activityRepository);
		assertEquals(createdActivity, returnedActivity);
		assertEquals(createdActivity, activityArgument.getAllValues().get(0));
	}
	
	@Test
	void getAllActivitiesSortedWithComparator() {

		//given
		List<Activity> listWithActivities = createActivities();
		
		ActivityRepository activityRepository = mock(ActivityRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		
		ActivityService activityService = new ActivityService(activityRepository, userRepository);
		
		given(activityRepository.findAll()).willReturn(listWithActivities);
		
		//when
		List<Activity> activityList = activityService.getAllActivities((a1, a2) -> a1.getWorkOrder().compareTo(a2.getWorkOrder()));
		
		//then
		verify(activityRepository, times(1)).findAll();
		assertThat(activityList, hasSize(3));
		assertThat(activityList.get(0).getWorkOrder(), equalTo("ZRXXXX"));
	}
	
	@Test
	void createdTempActivityShouldBeNotNull() {
		
		//given
		ActivityService activityService = new ActivityService();
		
		//when
		Activity tempActivity = activityService.createTempActivity("AOI1", "ZRXXXX", "TOP", "Pisanie programu AOI");
		
		//then
		assertNotNull(tempActivity);
	}
	
	@Test
	void downtimeCounterShouldReturnDowntimeBetweenTwoDates() throws InterruptedException {
		
		//given
		ActivityService activityService = new ActivityService();
		
		Timestamp startDateTime = new Timestamp(2019, 5, 23, 21, 00, 10, 0);
		
		Timestamp stopDateTime = new Timestamp(2019, 5, 23, 21, 30, 10, 0);
		
		//when
		double downtime = activityService.downtimeCounter(startDateTime, stopDateTime);
		
		//then
		assertThat(downtime, equalTo(0.5));
	}
	
	private Activity createActivity() {
		
		Activity activity = Activity.builder()
				.machineNumber("AOI2")
				.workOrder("ZRXXYY")
				.side("BOT")
				.activityType("Poprawa programu AOI")
				.comments("Example Comment2")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(0.001)
				.build();
		
		return activity;
	}
	
	private List<Activity> createActivities() {
		
		Activity activity1 = Activity.builder()
				.machineNumber("AOI1")
				.workOrder("ZRXXXX")
				.side("TOP")
				.activityType("Poprawa programu AOI")
				.comments("Example Comment4")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(0.015)
				.build();
		
		Activity activity2 = Activity.builder()
				.machineNumber("AOI2")
				.workOrder("ZRXXYY")
				.side("BOT")
				.activityType("Poprawa programu AOI")
				.comments("Example Comment3")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(2.005)
				.build();
		
		Activity activity3 = Activity.builder()
				.machineNumber("AOI1")
				.workOrder("ZRYYYY")
				.side("BOT")
				.activityType("Pisanie programu AOI")
				.comments("Example Comment5")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(10.008)
				.build();
		
		return Arrays.asList(activity1, activity2, activity3);
	}
}
