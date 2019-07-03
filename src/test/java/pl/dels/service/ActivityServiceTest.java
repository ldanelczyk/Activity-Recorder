package pl.dels.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pl.dels.database.dao.ActivityDao;
import pl.dels.database.dao.ActivityDaoImpl;
import pl.dels.database.repository.ActivityRepository;
import pl.dels.database.repository.UserRepository;
import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;
import pl.dels.model.enums.MachineNumber;
import pl.dels.model.enums.Side;
import pl.dels.toolsprovider.TimeToolsProvider;

class ActivityServiceTest {
	

	private Timestamp startDateTime = Timestamp.valueOf("2019-06-20 10:02:00");

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
	void getProcessedChartActivitiesFromFirebird() throws ClassNotFoundException, IOException {
		
		//given
		List<ChartActivity> createdChartActivityList = createChartActivities();
		
		ActivityDao activityDao = mock(ActivityDao.class);
		
		ActivityService activityService = new ActivityService(activityDao);
		
		String mondayOfTheWeek = "2019-06-18";
		String sundayOfTheWeek = "2019-06-26";
				
		given(activityDao.getAllActivities(mondayOfTheWeek, sundayOfTheWeek)).willReturn(createdChartActivityList);
		
		//when
		List<ChartActivity> returnedChartActivityList = activityService.getProcessedChartActivitiesFromFirebird();
		
		//then
		verify(activityDao, times(1)).getAllActivities(mondayOfTheWeek, sundayOfTheWeek);
		assertThat(returnedChartActivityList, hasSize(6));
		assertThat(returnedChartActivityList.get(0).getWorkOrder(), equalTo("ZRXXXX"));
		assertNotEquals(createdChartActivityList, returnedChartActivityList);		
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
	
	@Test
	void downtimeCounterShouldReturnDowntimeBetweenTwoDates() throws InterruptedException {
		
		//given		
		Timestamp startDateTime = new Timestamp(2019, 5, 23, 21, 00, 10, 0);
		
		Timestamp stopDateTime = new Timestamp(2019, 5, 23, 21, 30, 10, 0);
		
		//when
		double downtime = TimeToolsProvider.downtimeCounter(startDateTime, stopDateTime);
		
		//then
		assertThat(downtime, equalTo(0.5));
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
				.downtime(0.015)
				.build();
		
		Activity activity2 = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRXXYY")
				.side(Side.BOT)
				.activityType("poprawa programu AOI")
				.comments("Example Comment3")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(2.005)
				.build();
		
		Activity activity3 = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRYYYY")
				.side(Side.BOT)
				.activityType("pisanie programu AOI")
				.comments("Example Comment5")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(10.008)
				.build();
		
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
	
	private List<ChartActivity> createChartActivities() {
		
		ChartActivity activity1 = new ChartActivity("ZRXXXX", 1);
		ChartActivity activity2 = new ChartActivity("ZRXXXX", 1);
		ChartActivity activity3 = new ChartActivity("ZRXXYY", 2);
		ChartActivity activity4 = new ChartActivity("ZRXXYY", 2);
		ChartActivity activity5 = new ChartActivity("ZRYYYY", 3);
		ChartActivity activity6 = new ChartActivity("ZRYYYY", 3);
		ChartActivity activity7 = new ChartActivity("ZRYYYX", 4);
		ChartActivity activity8 = new ChartActivity("ZRYYYX", 4);
		ChartActivity activity9 = new ChartActivity("ZRZZZZ", 1);
		ChartActivity activity10 = new ChartActivity("ZRZZYX", 1);
		
		return Arrays.asList(activity1, activity2, activity3, activity4, activity5, activity6, activity7, activity8, activity9, activity10);
	}
}
