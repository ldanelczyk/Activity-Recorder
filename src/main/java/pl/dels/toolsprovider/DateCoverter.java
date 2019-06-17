package pl.dels.toolsprovider;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateCoverter {

	public static LocalDate getMondayOfTheWeek() {

		LocalDate today = LocalDate.now();
		LocalDate monday = today;

		while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
			
			monday = monday.minusDays(1);
		}

		return monday;
	}

	public static LocalDate getSundayOfTheWeek() {

		LocalDate today = LocalDate.now();
		LocalDate sunday = today;

		while(sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
			
			sunday = sunday.plusDays(1);
		}

		return sunday;
	}

	public static List<String> getDatesBetween() {

		LocalDate startDate = getMondayOfTheWeek();
		
		return IntStream.iterate(0, i -> i + 1)
				.limit(7)
				.mapToObj(i -> startDate.plusDays(i))
				.map(i -> String.valueOf(i))
				.collect(Collectors.toList());
	}
}
