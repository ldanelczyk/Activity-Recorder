package pl.dels.toolsprovider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;

import pl.dels.database.dao.ActivityDao;
import pl.dels.database.dao.ActivityDaoImpl;
import pl.dels.database.repository.ActivityRepository;
import pl.dels.model.Activity;
import pl.dels.model.ChartActivity;
import pl.dels.model.User;
import pl.dels.model.enums.MachineNumber;
import pl.dels.model.enums.Side;
import pl.dels.service.ActivityService;

class XlsProviderTest {

	@AfterAll
	static void removeTestFile() {

		File testedFile = new File("test.xlsx");
		testedFile.delete();
	}

	@Test
	void generateExcelFileWithAllDataFromDb() throws IOException {

		// given
		ActivityRepository activityRepository = mock(ActivityRepository.class);

		ActivityService activityService = new ActivityService(activityRepository);

		List<Activity> listWithPreparedActivities = createActivities();

		given(activityService.getAllActivitiesFromMySql((a1, a2) -> a1.getWorkOrder().compareTo(a2.getWorkOrder())))
				.willReturn(listWithPreparedActivities);

		XlsProvider xlsProvider = new XlsProvider(activityService);

		// when
		xlsProvider.generateExcelFileWithAllDataFromDb("test.xlsx");

		List<ActivityPoiji> listWithRodeObjects = readDataFromCreatedExcelFile();

		// then
		assertEquals("ZRYYYY", listWithRodeObjects.get(0).getWorkOrder());
		verify(activityRepository, times(1)).findAll();
		assertThat(listWithRodeObjects, hasSize(3));

		for (ActivityPoiji activityPoiji : listWithRodeObjects) {

			assertThat(activityPoiji, notNullValue());
			assertThat(String.valueOf(activityPoiji.getMachineNumber()), containsString("AOI"));
			assertThat(String.valueOf(activityPoiji.getMachineNumber()).length(), lessThan(5));
			assertThat(activityPoiji.getWorkOrder(), containsString("ZR"));
			assertThat(activityPoiji.getWorkOrder().length(), lessThan(8));
			assertThat(String.valueOf(activityPoiji.getSide()), containsString("T"));
			assertThat(String.valueOf(activityPoiji.getSide()).length(), lessThan(4));
			assertThat(activityPoiji.getActivityType(), containsString("programu"));
			assertThat(activityPoiji.getActivityType().length(), lessThan(21));
		}
	}

	/*@Test 
	void generateExcelFileWithChartFromAGivenWeek() throws
	 ClassNotFoundException, IOException {
	  
	  // given 
	  List<Activity> listWithPreparedActivities = createActivities();
	  List<ChartActivity> listWithPreparedMySqlActivities =
	  createChartActivitiesForMySql(); List<ChartActivity>
	  listWithPreparedFireBirdActivities = createChartActivitiesForFireBird();
	  
	  ActivityRepository activityRepository = mock(ActivityRepository.class);
	  ActivityDao activityDao = mock(ActivityDaoImpl.class);
	  
	  ActivityService activityService = new ActivityService(activityRepository,
	  activityDao);
	  
	  given(activityRepository.findAll()).willReturn(listWithPreparedActivities);
	  given(activityService.getProcessedChartActivitiesFromMySql()).willReturn(
	  listWithPreparedMySqlActivities);
	  given(activityService.getProcessedChartActivitiesFromFirebird()).willReturn(
	  listWithPreparedFireBirdActivities);
	 
	  XlsProvider xlsProvider = new XlsProvider(activityService);
	 
	  // when 
	  xlsProvider.generateExcelFileWithChartFromAGivenWeek("test2.xlsx"); 
	  
	}*/

	private List<ActivityPoiji> readDataFromCreatedExcelFile() throws IOException {

		InputStream stream = new FileInputStream(new File("test.xlsx"));

		List<ActivityPoiji> activities = Poiji.fromExcel(stream, PoijiExcelType.XLS, ActivityPoiji.class);

		return activities;
	}

	private List<Activity> createActivities() {

		Timestamp startDateTime = new Timestamp(new Date().getTime());

		Timestamp stopDateTime = new Timestamp(new Date().getTime());

		Activity activity1 = Activity.builder()
				.machineNumber(MachineNumber.AOI1)
				.workOrder("ZRXXXX")
				.side(Side.TOP)
				.activityType("poprawa programu AOI")
				.comments("Example Comment4")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(0.015)
				.user(new User("username", "encodedPassword"))
				.build();

		Activity activity2 = Activity.builder()
				.machineNumber(MachineNumber.AOI2)
				.workOrder("ZRXXYY")
				.side(Side.BOT)
				.activityType("poprawa programu AOI")
				.comments("Example Comment3")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(8.015)
				.user(new User("username", "encodedPassword"))
				.build();

		Activity activity3 = Activity.builder()
				.machineNumber(MachineNumber.AOI1)
				.workOrder("ZRYYYY")
				.side(Side.BOT)
				.activityType("pisanie programu AOI")
				.comments("Example Comment5")
				.startDateTime(startDateTime)
				.stopDateTime(stopDateTime)
				.downtime(10.008)
				.user(new User("username", "encodedPassword"))
				.build();

		return Arrays.asList(activity1, activity2, activity3);
	}

	private List<ChartActivity> createChartActivitiesForMySql() {

		ChartActivity chartActivity1 = new ChartActivity("25234", 1.7);

		ChartActivity chartActivity2 = new ChartActivity("25255", 2);

		ChartActivity chartActivity3 = new ChartActivity("25280", 3);

		return Arrays.asList(chartActivity1, chartActivity2, chartActivity3);
	}

	private List<ChartActivity> createChartActivitiesForFireBird() {

		ChartActivity chartActivity1 = new ChartActivity("25234", 1.9);

		ChartActivity chartActivity2 = new ChartActivity("25255", 2.5);

		ChartActivity chartActivity3 = new ChartActivity("25280", 3.2);

		return Arrays.asList(chartActivity1, chartActivity2, chartActivity3);
	}
}
