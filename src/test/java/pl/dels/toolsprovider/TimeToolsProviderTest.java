package pl.dels.toolsprovider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.Timestamp;

import java.time.LocalTime;

import org.hamcrest.CoreMatchers;

import org.junit.jupiter.api.Test;

public class TimeToolsProviderTest {

	@Test
	void downtimeCounterShouldReturnDowntimeBetweenTwoDates() throws InterruptedException {

		//given
		@SuppressWarnings("deprecation")
		Timestamp startDateTime = new Timestamp(2019, 5, 23, 21, 00, 10, 0);

		@SuppressWarnings("deprecation")
		Timestamp stopDateTime = new Timestamp(2019, 5, 23, 21, 30, 10, 0);

		//when
		double downtime = TimeToolsProvider.downtimeCounter(startDateTime, stopDateTime);

		//then
		assertThat(downtime, equalTo(0.5));
	}

	@Test
	void convertTimeToFileNameShouldReturnChangedName() {

		//given
		LocalTime localTimeToConvert = LocalTime.of(9, 20, 00);

		//when
		String localTimeConverted = TimeToolsProvider.convertTimeToFileName(localTimeToConvert);

		//then
		assertThat(localTimeConverted, CoreMatchers.containsString("_"));
	}
}
