package pl.dels.toolsprovider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

public class TimeToolsProviderTest {
	
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
}
