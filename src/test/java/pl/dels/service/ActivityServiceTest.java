package pl.dels.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;

import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pl.dels.database.repository.ActivityRepository;
import pl.dels.database.repository.UserRepository;
import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;
import pl.dels.model.enums.MachineNumber;
import pl.dels.model.enums.Side;

class ActivityServiceTest {

	private Timestamp startDateTime = Timestamp.valueOf("2019-08-07 10:02:00");

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
		Activity returnedActivity = activityService.saveActivityInDatabase(MachineNumber.AOI2, "ZRXXYY", Side.BOT, "poprawa programu AOI", "Example Comment2", startDateTime, stopDateTime, 0.001, null);

		ArgumentCaptor<Activity> activityArgument = ArgumentCaptor.forClass(Activity.class);

		//then
		verify(activityRepository, times(1)).save(activityArgument.capture());
		verifyNoMoreInteractions(activityRepository);
		assertEquals(createdActivity, returnedActivity);
		assertEquals(createdActivity, activityArgument.getAllValues().get(0));
	}

	@Test
	void getAllActivitiesFromMySql() {

		//given
		List<Activity> createdActivityList = createActivities();

		ActivityRepository activityRepository = mock(ActivityRepository.class);

		ActivityService activityService = new ActivityService(activityRepository);

		given(activityRepository.findAll()).willReturn(createdActivityList);

		//when
		List<Activity> returnedActivityList = activityService.getAllActivitiesFromMySql((a1, a2) -> a1.getWorkOrder().compareTo(a2.getWorkOrder()));

		//then
		verify(activityRepository, times(1)).findAll();
		assertThat(returnedActivityList, hasSize(4));
		assertThat(returnedActivityList.get(0).getWorkOrder(), equalTo("ZRXXXX"));
		assertEquals(createdActivityList, returnedActivityList);
	}

	@Test
	void getProcessedChartActivitiesFromMySql() {

		//given
		List<Activity> createdActivityList = createActivities();

		ActivityRepository activityRepository = mock(ActivityRepository.class);

		ActivityService activityService = new ActivityService(activityRepository);

		given(activityRepository.findAll()).willReturn(createdActivityList);

		//when
		List<ChartActivity> returnedChartActivityList = activityService.getProcessedChartActivitiesFromMySql();

		//then
		verify(activityRepository, times(1)).findAll();
		assertThat(returnedChartActivityList, hasSize(3));
		assertThat(returnedChartActivityList.get(0).getWorkOrder(), equalTo("ZRXXXX"));
		assertNotEquals(createdActivityList, returnedChartActivityList);
	}

	@Test
	void createdTempActivityShouldBeNotNull() {

		//given
		ActivityService activityService = new ActivityService();

		//when
		Activity tempActivity = activityService.createTempActivity(MachineNumber.AOI1, "ZRXXXX", Side.TOP, "poprawa programu AOI");

		//then
		assertNotNull(tempActivity);
	}

	private Activity createActivity() {

		Activity activity = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRXXYY")
				.side(Side.BOT)
				.activityType("poprawa programu AOI")
				.comments("Example Comment2")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(0.001)
				.build();

		return activity;
	}

	private List<Activity> createActivities() {

		Activity activity1 = Activity.builder()
				.machineNumber(MachineNumber.AOI1)
				.workOrder("ZRXXXX")
				.side(Side.TOP)
				.activityType("poprawa programu AOI")
				.comments("Example Comment4")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(0.015).build();

		Activity activity2 = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRXXYY")
				.side(Side.BOT)
				.activityType("poprawa programu AOI")
				.comments("Example Comment3")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(2.005).build();

		Activity activity3 = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRYYYY")
				.side(Side.BOT)
				.activityType("pisanie programu AOI")
				.comments("Example Comment5")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(10.008).build();

		Activity activity4 = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRYYYX")
				.side(Side.BOT)
				.activityType("pisanie programu AOI")
				.comments("Example Comment1")
				.startDateTime(Timestamp.valueOf("2019-06-15 10:02:00"))
				.stopDateTime(stopDateTime)
				.downtime(10.008)
				.build();

		return Arrays.asList(activity1, activity2, activity3, activity4);
	}
}
