package pl.dels.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import pl.dels.database.dao.ChartActivityDao;
import pl.dels.model.ChartActivity;

class ChartActivityServiceTest {

	/*@Test
	void getProcessedChartActivitiesFromFirebird() throws ClassNotFoundException, IOException {

		//given
		List<ChartActivity> createdChartActivityList = createChartActivities();

		ChartActivityDao activityDao = mock(ChartActivityDao.class);

		ChartActivityService activityService = new ChartActivityService(activityDao);

		String mondayOfTheWeek = "2019-08-05";
		String sundayOfTheWeek = "2019-08-09";

		given(activityDao.getAllActivities(mondayOfTheWeek, sundayOfTheWeek)).willReturn(createdChartActivityList);

		//when
		List<ChartActivity> returnedChartActivityList = activityService.getProcessedChartActivitiesFromFirebird();

		//then
		verify(activityDao, times(1)).getAllActivities(mondayOfTheWeek, sundayOfTheWeek);
		assertThat(returnedChartActivityList, hasSize(6));
		assertThat(returnedChartActivityList.get(0).getWorkOrder(), equalTo("ZRXXXX"));
		assertNotEquals(createdChartActivityList, returnedChartActivityList);
	}
*/
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

		return Arrays.asList(activity1, activity2, activity3, activity4, activity5, activity6, activity7, activity8,
				activity9, activity10);
	}
}
