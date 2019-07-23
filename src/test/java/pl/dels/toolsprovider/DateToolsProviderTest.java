package pl.dels.toolsprovider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.List;

import org.junit.jupiter.api.Test;

public class DateToolsProviderTest {
	
	@Test
	void getMondayOfTheWeekShouldReturnDateOfMondayInCurrentlyWeek() {
		
		//given
		LocalDate today = LocalDate.now();
		LocalDate monday = today;

		while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
			monday = monday.minusDays(1);
		}
		
		//when
		LocalDate mondayToTest = DateToolsProvider.getMondayOfTheWeek();
				
		//then
		assertEquals(mondayToTest, monday);
	}
	
	@Test
	void getMondayOfTheWeekShouldReturnDateOfSundayInCurrentlyWeek() {
		
		//given
		LocalDate today = LocalDate.now();
		LocalDate sunday = today;

		while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
			sunday = sunday.plusDays(1);
		}
		
		//when
		LocalDate sundayToTest = DateToolsProvider.getSundayOfTheWeek();
				
		//then
		assertEquals(sundayToTest, sunday);
	}
	
	@Test
	void getDatesBetweenShouldReturnListWithDates() {
		
		//given
		//when
		List<String> listWithDates = DateToolsProvider.getDatesBetween();
		
		//then
		assertThat(listWithDates, hasSize(7));
		assertEquals(listWithDates.get(0), String.valueOf(DateToolsProvider.getMondayOfTheWeek()));
		assertEquals(listWithDates.get(6), String.valueOf(DateToolsProvider.getSundayOfTheWeek()));
	}
}
